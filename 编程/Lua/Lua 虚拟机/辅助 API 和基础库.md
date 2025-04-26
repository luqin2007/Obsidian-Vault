![[../../../_resources/images/辅助 API 和基础库 2024-12-09 15.36.04.excalidraw]]

基础 API 提供底层的必要功能

辅助 API 称为 `Auxiliary Library`，是对基础 API 的二次封装，以 `luaL_` 开头

Lua 标准库由辅助 API 和其他 LuaAPI 组成，共有 10 个：
- 基础库：直接以全局变量提供
- 数学库 `math`
- 字符串库 `string`
- UTF-8 库 `utf8`
- 表操作库 `table`
- 输入输出库 `IO`
- 操作系统库 `OS`
- 包和模块库 `package`
- 协程库 `coroutine`
- 调试库 `debug`

官方标准库通过 C 实现，将其翻译成 Go 语言即可
# 开启标准库

Lua 标准库是可选的，通过辅助 API 中的 `OpenLibs` 开启全部标准库，通过 `RequireF` 开启单个标准库，`OpenBaseLib` 开启辅助库，位于 `auxlib.go` 中

```go title:state/auxlib.go
func (self *luaState) OpenLibs() {
	libs := map[string]GoFunction{
		"_G":     stdlib.OpenBaseLib,
		"math":   stdlib.OpenMathLib,
		"table":  stdlib.OpenTableLib,
		"string": stdlib.OpenStringLib,
		"utf8":   stdlib.OpenUTF8Lib,
		"os":     stdlib.OpenOSLib,
	}

	for name, fun := range libs {
		self.RequireF(name, fun, true)
		self.Pop(1)
	}
}
```