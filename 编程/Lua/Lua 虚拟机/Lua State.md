Lua 是一个可嵌入的脚本语言，其他程序嵌入 Lua 链接库就可以使用 Lua  API 获取脚本执行能力。

Lua API 有一百多个函数，包含 `lua_` 开头的基本函数和 `luaL_` 开头的辅助函数，辅助 API 完全通过基本函数实现。相当的 Lua API 都是在操作虚拟栈。

> [!note] 虚拟栈
> Lua 3.1 引入 `lua_State`，4.0 起 `lua_State` 结构走向前台，方便用户切换多个解释器实例，使用 `lua_newstate()` 创建
> 
> `lua_State` 可以看做一个不纯粹的栈（或者说是一个寄存器），支持通过索引访问其中的元素

![[../../../_resources/images/Lua 嵌入 API 2024-11-20 23.37.10.excalidraw]]

# Lua 栈

Lua 栈是宿主语言与 Lua 语言沟通的桥梁，是 Lua State 封装的最基础状态
## 数据类型

Lua 是动态数据类型，变量本身不携带类型信息，变量值携带类型信息。

```lua
local a, b, c
a = 3.14          -- number
b = a             -- number
c = false         -- boolean
c = { 1, 2, 3 }   -- table
c = "hello"       -- string
```

在 Lua 语言中，数据分为 8 种类型，使用 `type(v)` 可以查看。其中 5 种可以直接映射到 Go 语言类型

> [!note] Lua 5.3 `number` 分为浮点数和整型，但没有在语言层面体现，而是用于虚拟机优化。

| Lua 类型     | Go 类型             |
| ---------- | ----------------- |
| `nil`      | `nil`             |
| `boolean`  | `bool`            |
| `number`   | `int64`，`float64` |
| `string`   | `string`          |
| `table`    | 自定义结构体 `luaTable` |
| `function` | 自定义结构体 `closure`  |
| `thread`   |                   |
| `userdata` |                   |
为每个类型创建对应的常量，由于 Lua 栈按索引存取值，额外增加一个无效类型 `LUA_TNONE`（-1）

```go title:api/consts.go
// 值类型
const (
	LUA_TNONE = iota - 1
	LUA_TNIL
	LUA_TBOOLEAN
	LUA_TLIGHTUSERDATA
	LUA_TNUMBER
	LUA_TSTRING
	LUA_TTABLE
	LUA_TFUNCTION
	LUA_TUSERDATA
	LUA_TTHREAD
)
```
## 栈索引

Lua 栈索引有以下特点：
- 绝对索引：索引从 1 开始，表示栈底
- 相对索引：索引可以为负数，-1 表示栈顶
- 设栈容量为 `n`，栈顶索引为 `top`，可接受索引为 `[1, n]`，有效索引为 `[1, top]`
	- 写入值时必须提供有效索引
	- 读取值时可以提供可接受索引，无效的可接受索引返回 `nil`
![[../../../_resources/images/Lua State 2024-11-21 00.19.41.excalidraw|80%]]
# LuaState

LuaState 接口主要包括以下几类函数：
- LuaStack 栈操作函数，包括辅助插入函数
- LuaStack 索引访问函数

```go fold title:api/lua_state.go
type LuaState interface {

	/* 基础栈操作 */

	// GetTop 获取栈顶索引
	GetTop() int
	// AbsIndex 将相对索引转换为绝对索引
	AbsIndex(index int) int
	// CheckStack 确保 Lua 栈可以存入 n 个元素，若不能存入则扩展栈空间
	CheckStack(n int) bool
	// Pop 弹出栈中 n 个元素
	Pop(n int)
	// Copy 将 from 位置的元素复制到 to
	Copy(from, to int)
	// PushValue 将 index 处的元素复制并压入栈顶
	PushValue(index int)
	// Replace 将栈顶值弹出并写入 index 位置
	Replace(index int)
	// Insert 将栈顶值弹出并插入到 index 位置，其他值依次后移
	Insert(index int)
	// Remove 移除 index 位置元素
	Remove(index int)
	// Rotate 将 [index, top] 区间的值向栈顶方向旋转 n 次，n < 0 则逆向旋转
	Rotate(index, n int)
	// SetTop 设置栈顶索引
	SetTop(index int)

	/* 索引访问函数 */

	// TypeName 获取类型名
	TypeName(tp LuaType) string
	// Type 获取给定 index 值的类型，若下标错返回 LUA_TNONE
	Type(index int) LuaType
	// IsNone 判断给定 index 值是否为 LUA_TNONE
	IsNone(index int) bool
	// IsNil 判断给定 index 值是否为 Nil
	IsNil(index int) bool
	// IsNoneOrNil 判断指定 index 是否有值
	IsNoneOrNil(index int) bool
	// IsBoolean 判断指定 index 是否为布尔，或可被转换为布尔
	IsBoolean(index int) bool
	// IsInteger 判断指定 index 是否为整型，或可被转换为整型
	IsInteger(index int) bool
	// IsNumber 判断给定 index 是否为数字，或可被转换为数字
	IsNumber(index int) bool
	// IsString 判断给定 index 是否为字符串，或可被转换为字符串
	IsString(index int) bool
	// ToBoolean 将给定 index 值转换为布尔值
	ToBoolean(index int) bool
	// ToInteger 将给定 index 值转换为整型
	ToInteger(index int) int64
	// ToIntegerX 将给定 index 值转换为整型，返回转换是否成功
	ToIntegerX(index int) (int64, bool)
	// ToNumber 将给定 index 值转换为数字
	ToNumber(index int) float64
	// ToNumberX 将给定 index 值转换为数字，返回转换是否成功
	ToNumberX(index int) (float64, bool)
	// ToString 将给定 index 值转换为字符串
	ToString(index int) string
	// ToStringX 将给定 index 值转换为数字，返回转换是否成功
	ToStringX(index int) (string, bool)

	/* 入栈函数 */

	// PushNil 将一个 nil 入栈
	PushNil()
	// PushBoolean 将一个 bool 入栈
	PushBoolean(b bool)
	// PushInteger 将一个整型入栈
	PushInteger(n int64)
	// PushNumber 将一个浮点数字入栈
	PushNumber(n float64)
	// PushString 将一个字符串入栈
	PushString(s string)
	// PushGoFunction 将一个 Go 函数入栈
	PushGoFunction(f GoFunction, n int)
	// PushGlobalTable 将全局变量表推入栈顶
	PushGlobalTable()
}
```
## Rotate

Rotate 将 `[index, top]` 区间的值向栈顶方向旋转 n 次，n < 0 则逆向旋转。`Insert`，`Remove` 都可以使用该函数实现

![[../../../_resources/images/Lua State 2024-11-21 01.38.58.excalidraw|80%]]
Lua 内部通过对 `stack` 的三次 `reverse` 操作实现

![[../../../_resources/images/Lua State 2024-11-21 01.50.24.excalidraw|80%]]
具体实现如下：

```go title:state/api_stack.go
func (self *luaState) Rotate(index, n int) {
	t := self.stack.top - 1
	p := self.stack.absIndex(index) - 1
	var m int

	if n >= 0 {
		m = t - n
	} else {
		m = p - n - 1
	}

	self.stack.reverse(p, m)
	self.stack.reverse(m+1, t)
	self.stack.reverse(p, t)
}
```
## SetTop

设置栈顶索引。当给定值小于现有栈顶时，相当于执行 n 次 `pop`；反之，相当于执行 n 次 `push(nil)`

> [!note] Pop(n) 方法可以认为是 SetTop(-n-1)
## IsXxx

判断某个位置是否为某种类型。涉及到三种判断方法：
- `None`，`Nil`：直接判断 `Type(index)`
- `String`：同样判断 `Type(index)`，涉及到类型转换可以是 `LUA_TSTRING` 或 `LUA_TNUMBER`
- `Integer`，`Number`：获取值后判断其类型
## ToXxx

获取 index 位置的对应类型值。大多数包含 `X` 后缀的函数可以判断类型是否匹配

>[!error] `ToString`，`ToStringX` 支持数字类型，但会将对应类型更新成字符串

```go title:state/api_access.go hl:13
func (self *luaState) ToString(index int) string {
	n, _ := self.ToStringX(index)
	return n
}

func (self *luaState) ToStringX(index int) (string, bool) {
	switch x := self.stack.get(index).(type) {
	case string:
		return x, true
	case int64, float64:
		s := fmt.Sprintf("%v", x)
		// !!! 这里会更新 stack 值
		self.stack.set(index, s)
		return s, true
	default:
		return "", false
	}
}
```