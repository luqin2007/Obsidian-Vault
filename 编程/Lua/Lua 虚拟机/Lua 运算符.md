# 运算符

Lua 共有 25 个运算符，逻辑运算符可以直接映射到 Go 对应运算符
## 算术运算符

其他运算符都可以直接映射到 Go 运算符，比较特殊的有：
- 整除（`//`）向 `-∞` 取整，而不是简单的截断
- 取模（`%`）可通过整除定义（`a%b=a-(a//b)*b`）
- 乘方（`^`）和字符串连接（字符串 `..`）具有右结合性，其余为左结合
## 位运算

其他运算符都可以直接映射到 Go 运算符，比较特殊的有：
- 右移为无符号右移
- 左右移 `-n` 位相当于向反方向移动 `n` 位
# 自动类型转换

运算过程中的类型包括：

- 算术运算符
	- 除法、乘方
		- 操作数为整数时，提升为浮点
		- 操作数为字符串时，尝试解析成浮点
		- 结果为浮点
	- 其他
		- 若所有运算数都是整数，则执行整型运算
		- 若操作数中有浮点，全部提升为浮点数后运算，结果为浮点数
- 位运算符
	- 若操作数为浮点数，但实际是整数（如 10.0），且没有超出 `int64` 范围，转换为整型
	- 若操作数为字符串，先尝试转换为整型，失败则尝试转换为浮点再转换为整型
	- 结果为整型
- 字符串拼接
	- 操作数为数字，则转换为字符串
# LuaState 实现
## 隐式类型转换

主要是整型、浮点、字符串的互相转换

```go title:number/parser.go
// ParseInteger 将字符串转换为整型
func ParseInteger(str string) (int64, bool) {
	i, err := strconv.ParseInt(str, 10, 64)
	return i, err == nil
}

// ParseFloat 将字符串转换为浮点数
func ParseFloat(str string) (float64, bool) {
	f, err := strconv.ParseFloat(str, 64)
	return f, err == nil
}
```

在此基础上，实现以任意 Lua 类型转换为整型、浮点型的方法。至此 `ToIntegerX` 和 `ToNumberX` 也可以用这两个方法重写

```go title:state/lua_value.go
func convertToFloat(val luaValue) (float64, bool) {
	switch x := val.(type) {
	case int64:
		return float64(x), true
	case float64:
		return x, true
	case string:
		return number.ParseFloat(x)
	default:
		return 0, false
	}
}

func convertToInteger(val luaValue) (int64, bool) {
	switch x := val.(type) {
	case int64:
		return x, true
	case float64:
		return number.FloatToInteger(x)
	case string:
		// 尝试直接转换为整型
		if i, ok := number.ParseInteger(x); ok {
			return i, true
		}
		// 尝试通过浮点数转换为整型
		if f, ok := number.ParseFloat(x); ok {
			return number.FloatToInteger(f)
		}

		return 0, false
	default:
		return 0, false
	}
}
```
## 算术运算符、位运算符

实现不能直接映射到 Go 运算的运算函数

```go fold title:number/math.go
// IFloorDiv 整除 向负无穷取整
func IFloorDiv(a, b int64) int64 {
	if a > 0 && b > 0 || a < 0 && b < 0 || a%b == 0 {
		return a / b
	}
	// 向负无穷取整
	return a/b - 1
}

// FFloorDiv 浮点数整除 向负无穷取整
func FFloorDiv(a, b float64) float64 {
	return math.Floor(a / b)
}

// IMod 取余 利用整除实现
func IMod(a, b int64) int64 {
	return a - IFloorDiv(a, b)*b
}

// FMod 浮点数取余 利用整除实现
func FMod(a, b float64) float64 {
	return a - FFloorDiv(a, b)*b
}

// ShiftLeft 按位左移
func ShiftLeft(a, n int64) int64 {
	if n >= 0 {
		return a << uint64(n)
	}
	return ShiftRight(a, -n)
}

// ShiftRight 按位右移
func ShiftRight(a, n int64) int64 {
	if n >= 0 {
		return int64(uint64(a) >> uint64(n))
	}
	return ShiftLeft(a, -n)
}

// FloatToInteger 将浮点数转换为整数
func FloatToInteger(f float64) (int64, bool) {
	i := int64(f)
	return i, float64(i) == f
}
```

将所有运算符都以二元运算符的形式封装，以便后期统一调用

```go title:state/arith.go
var (
	iadd  = func(a, b int64) int64 { return a + b }
	fadd  = func(a, b float64) float64 { return a + b }
	isub  = func(a, b int64) int64 { return a - b }
	fsub  = func(a, b float64) float64 { return a - b }
	imul  = func(a, b int64) int64 { return a * b }
	fmul  = func(a, b float64) float64 { return a * b }
	imod  = number.IMod
	fmod  = number.FMod
	pow   = math.Pow
	div   = func(a, b float64) float64 { return a / b }
	iidiv = number.IFloorDiv
	fidiv = number.FFloorDiv
	band  = func(a, b int64) int64 { return a & b }
	bor   = func(a, b int64) int64 { return a | b }
	bxor  = func(a, b int64) int64 { return a ^ b }
	shl   = number.ShiftLeft
	shr   = number.ShiftRight
	iunm  = func(a, _ int64) int64 { return -a }
	funm  = func(a, _ float64) float64 { return -a }
	bnot  = func(a, _ int64) int64 { return ^a }
)
```

然后以整型、浮点型分类，便于选择方法

```go title:state/api_arith.go
type operator struct {
	metamethod  string
	integerFunc func(int64, int64) int64
	floatFunc   func(float64, float64) float64
}

var operators = []operator{
	{"__add", iadd, fadd},
	{"__sub", isub, fsub},
	{"__mul", imul, fmul},
	{"__mod", imod, fmod},
	{"__pow", nil, pow},
	{"__div", nil, div},
	{"__idiv", iidiv, fidiv},
	{"__band", band, nil},
	{"__bor", bor, nil},
	{"__bxor", bxor, nil},
	{"__shl", shl, nil},
	{"__shr", shr, nil},
	{"__unm", iunm, funm},
	{"__bnot", bnot, nil},
}

func _arith(a, b luaValue, op operator) luaValue {
	if op.floatFunc == nil {
		// 仅整型运算（位运算）
		if x, ok := convertToInteger(a); ok {
			if y, ok := convertToInteger(b); ok {
				return op.integerFunc(x, y)
			}
		}
	} else {
		if op.integerFunc != nil {
			// 先尝试整型运算
			if x, ok := convertToInteger(a); ok {
				if y, ok := convertToInteger(b); ok {
					return op.integerFunc(x, y)
				}
			}
		}
		// 再尝试浮点运算
		if x, ok := convertToFloat(a); ok {
			if y, ok := convertToFloat(b); ok {
				return op.floatFunc(x, y)
			}
		}
	}
	return nil
}
```
