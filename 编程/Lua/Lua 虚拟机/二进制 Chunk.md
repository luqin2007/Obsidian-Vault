Lua 类似 JVM 虚拟机，先将代码编译成二进制载体才能被执行
- Chunk：可以被 Lua 解释器执行的代码，通常是 `.lua` 文件
- 二进制 Chunk：Lua 不直接执行 Chunk，而是先编译成内部结构，称为二进制 Chunk

> [!note] 预编译：将 Chunk 编译成二进制 Chunk 的过程称为预编译
> 该过程可通过 `luac <lua-file>` 完成
> - `-p`：只检查语法错误，不进行编译
> - `-o <luac-file>`：指定输出位置
> - `-s`：不携带调试信息（行号等）
> 
> `luac` 也可以用来反编译：`luac -l <luac-file>`
> - 当使用 `-l -l` 时，会额外输出常量表、局部变量表、`upvalue` 表等信息 

`````col
````col-md
flexGrow=2
===
Lua 入口函数为 `function main()` 函数，当不存在时编译器会自动创建

```lua
print("Hello world!")
```
````
````col-md
flexGrow=1
===
![[../../../_resources/images/Pasted image 20241119194914.png]]
````
`````
>[!attention] Lua 二进制 Chunk 仅为获取更快的加载速度，并非如 JVM 字节码等为跨平台设计
>- 没有标准化，也没有官方文档，以 Lua 源代码实现为准
>- 没有处理大小端问题，直接使用本机大小端生成，加载时与本机大小端不匹配则无法加载
>- 没有考虑不同版本 Lua 间的兼容问题
>- 没有设计的很紧凑，有时二进制 Chunk 比 Lua 源码还大
# 数据类型

二进制 Chunk 中的数据类型大体上分为数字、字符串、列表三种。

> [!note] 官方 Lua 编译器由 C 编写，因此字节码中包含 C 类型
## 数字

| 数据类型        | 对应 C 类型                    | 对应 Go 类型  | 占用空间（字节） |
| ----------- | -------------------------- | --------- | -------- |
| 字节          | `lu_Byte`（`unsigned char`） | `byte`    | 1        |
| C 整型        | `int`                      | `uint32`  | 4        |
| C size_t 类型 | `size_t`                   | `uint64`  | 8        |
| Lua 整型      | `lua_Integer`（`long long`） | `int64`   | 8        |
| Lua 浮点      | `lua_Number`（`double`）     | `float64` | 8        |
## 字符串

字节数组
- 空串：一个字节 `0x00`
- 短字符串（长度小于 254）：1 字节（长度+1）+字节数组
![[../../../_resources/images/二进制 Chunk 2024-11-19 20.07.52.excalidraw]]
- 长字符串（长度大于 253）：1 字节 `0xFF` + `1 * size_t`（长度+1）+字节数组
![[../../../_resources/images/二进制 Chunk 2024-11-19 20.10.11.excalidraw]]
## 列表

一个 `cint` 表示长度，后接连续 `n` 个数据单元
# 结构

## 头部结构
`````col
````col-md
flexGrow=1
===
```go
type header struct {  
    signature       [4]byte  
    version         byte  
    format          byte  
    luacData        [6]byte  
    cintSize        byte  
    sizetSize       byte  
    instructionSize byte  
    luaIntegerSize  byte  
    luaNumberSize   byte  
    luacInt         int64  
    luacNum         float64  
} // 共 30 字节
```
````
````col-md
flexGrow=1
===
- `signature`：魔数，Lua 中称为签名，四个字节为 `(ESC)Lua` 即 `\x1bLua`
- 版本号：$大版本号 * 16 + 小版本号$，不包含发布号
- 格式号 format：与虚拟机本身格式号相匹配，官方虚拟机格式号为 0
- `luacData`：进一步校验，其内容为 `0x19930D0A1A0A`（1993 为 Lua1.0 发布年份，`0x1993` 后加回车、换行、替换、换行）
- `cintSize` 到 `luaNumberSize` 是各类型的长度，与虚拟机不匹配也无法加载
- `luacInt`：`0x5678`，用于虚拟机检查大小端是否匹配
- `luacNum`：`370.5`，检查浮点格式与虚拟机浮点格式是否匹配，默认 IEEE 754
````
`````
## 函数

`````col
````col-md
flexGrow=1
===
```go
type prototype struct { // 带有 - 注释表示非必须，会被 -s 去除
    source string // -
    lineDefined uint32
    lastLineDefined uint32  
    numParams byte
    isVararg byte
    maxStackSize byte
    code []uint32
    constants []interface{}
    upvalues []upvalue
    protos []*prototype
    lineInfo []uint32
    locVars []locVar
    upvalueNames []string
} // 函数原型
```

| 常量 tag | 类型        |
| ------ | --------- |
| `0x00` | `nil`     |
| `0x01` | `boolean` |
| `0x03` | `number`  |
| `0x13` | `integer` |
| `0x04` | 短字符串      |
| `0x14` | 长字符串      |

````
````col-md
flexGrow=1
===
- `source`：函数来源，仅记录在 `main` 函数中，其余为空串
  - `@<file>`：来自于文件
  - `=<...>`：来自于特殊位置，如 `=stdio`
  - `<str>`：来自于字符串
- `lineDefined`，`lastLineDefined`：起止行号，main 函数都是 0
- `numParams`：固定参数个数，自动生成的 `main` 函数为 0
- `isVararg`：是否存在变长参数，`main` 函数为 1
- `maxStackSize`：寄存器数量，Lua 寄存器通过栈（可下标访问）模拟
- `code`：指令表，每条指令 4 字节
- `constants`：常量表，列表
  - `{ tag: byte, data: interface{} }`
- `upvalues`：`Upvalue` 表，2 字节，与闭包有关 
  - `{ instack byte, idx byte }`
- `protos`：子函数原型表
- `lineInfo`：行号表，与指令表中的指令一一对应
- `locVars`：局部变量表，变量名+起止索引
  - `{ varName: string, startPC: uint32, endPC: uint32 }`
- `upvalueNames`：`upvalue` 名列表，主程序包含一个 `_ENV`
````
`````

