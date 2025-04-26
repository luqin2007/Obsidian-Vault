# 闭包

> [!note] 闭包：Closure，按词法作用域捕获了非局部变量的嵌套函数
> 
> 大多数语言使用静态作用域处理非局部变量问题，在编译时确定变量名绑定的是哪个变量。
> 
> Bash、PowerShell 等语言使用动态作用域，变量名绑定哪个变量在运行时确定

> [!tip] 理论上 Lua 中所有函数都属于闭包，`main` 函数从外部捕获了 `_ENV` 变量
## Upvalue

闭包内捕获的非局部变量，可通过 `luac -l -l` 查看

```lua
local u, v, w

function f()
    u = v -- 捕获 u, v
end
```

![[../../../_resources/images/Pasted image 20241127235945.png]]
## 扁平闭包

Flat Closure，需要借助外围函数来获得更外围函数局部变量的闭包

```lua
local u, v, w

local function f()
    -- 捕获 u, v 供 g() 捕获
    local function g()
        u == v -- 捕获 u, v
    end
end
```

![[../../../_resources/images/Pasted image 20241128001551.png]]

![[../../../_resources/images/Pasted image 20241128001618.png]]
## 全局变量

全局变量存于 `_ENV` 表中，若函数引用了全局变量，则隐式捕获了 `_ENV` 变量

```lua
local function f()
    local function g()
        x = y -- 捕获全局变量 x, y
    end
end
```

![[../../../_resources/images/Pasted image 20241128001734.png]]
# 底层结构

在 `closure` 结构体中添加一个 `upvals` 字段即可

```go title:state/closure.go hl:4
type closure struct {
	proto  *binchunk.Prototype
	goFunc api.GoFunction
	upvals []*upvalue
}
```

1. 为 `main` 函数添加 `_ENV`

```go title:state/api_call.go hl:7-11
func (self *luaState) Load(chunk []byte, chunkName string, mode string) int {
	var proto *binchunk.Prototype
	var closure *closure
	// ...
	self.stack.push(closure)

	// 设置 _ENV
	if len(proto.Protos) > 0 {
		env := self.registry.get(api.LUA_RIDX_GLOBALS)
		closure.upvals[0] = &upvalue{&env}
	}
	return 0
}
```

2. 加载函数（闭包）时初始化 `upvalues`

> [!note] `luaStack` 结构添加一个 `openuvs`，即 `OpenUpvalue`，表示开放的 `Upvalue` 集合。
> 开放（Open）状态：Upvalue 捕获的外围函数局部变量仍在栈上，此时直接引用局部函数即可；
> 
> 闭合（Closed）状态：Upvalue 捕获的变量位于其他位置

```go title:state/api_vm.go hl:7-26
func (self *luaState) LoadProto(n int) {
	stack := self.stack
	proto := stack.closure.proto.Protos[n]
	closure := newLuaClosure(proto)
	stack.push(closure)

	for i, uvInfo := range proto.Upvalues {
		idx := int(uvInfo.Idx)
		if uvInfo.Instack == 1 {
			// 是当前函数上下文的局部变量：访问局部变量
			if stack.openuvs == nil {
				stack.openuvs = map[int]*upvalue{}
			}
			if openuv, ok := stack.openuvs[idx]; ok {
				// Open：栈中直接引用
				closure.upvals[i] = openuv
			} else {
				// Closed：存于其他位置
				closure.upvals[i] = &upvalue{&stack.slots[idx]}
				stack.openuvs[idx] = closure.upvals[i]
			}
		} else {
			// 非局部变量：该参数已被外层函数捕获，直接从外部函数获取
			closure.upvals[i] = stack.closure.upvals[idx]
		}
	}
}
```

3. 完善 `LuaStack` 接口
	- `PushGoFunction`：将 Upvalue 存入 Closure
	- `LuaUpvalueIndex(i int) int`：将注册表伪索引转换为 `Upvalue` 索引
	- `get`，`set`，`isValid`：添加伪索引获取 Upvalue 值
# 指令

| 指令         | 类型      | 说明                                                 |
| ---------- | ------- | -------------------------------------------------- |
| `GETUPVAL` | `iABC`  | 把当前闭包索引为 B 的 Upvalue 值存入寄存器 A 中                    |
| `SETUPVAL` | `iABC`  | 将寄存器 A 的值赋值给 B                                     |
| `GETTABUP` | `iABC`  | 取 Upvalue 索引 B 值为表，以寄存器或常量索引 C 的值为键，取值后存入 A        |
| `SETTABUP` | `iABC`  | 取 Upvalue 索引 A 值为表，以寄存器或常量索引 B 的值为键，将寄存器或常量值 C 存入表 |
| `JMP`      | `iAsBx` | 无条件跳转；关闭 Upvalue 值                                 |
伪指令：
````tabs
tab: GETUPVAL
```
R(A) := Upvalue[B]
```

tab: SETUPVAL
```
Upvalue[B] := R(A)
```

tab: GETTABUP
```
key := Kst(C)
table := R(B)
R(A) = table[key]
```

tab: SETTABUP
```
Upvalue[A][Kst(B)] = Kst(C)
```

tab: JMP
```
PC += sBx
close opened upvalues
```
````
