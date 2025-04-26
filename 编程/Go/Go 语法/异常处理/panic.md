`panic` 是 Go 的内置函数，接收 `{go}interface{}` 类型参数，用于发生严重错误时使程序中断执行

> [!note] `{go}interface{}` 为空接口，表示可以接收任意类型参数

# recover

`panic` 中断的程序可以通过 `recover()` 恢复并获取其原因。

> [!note] 由于程序中断后，只有 `defer` 函数才会触发，因此 `recover` 需要配合 `refer` 使用

```go
package main

import "fmt"

func killMe() {
	println("Here 1")
	panic("Kill the Program")
}

func main() {
	defer func() {
		if p := recover(); p != nil {
			println("Here 2")
			fmt.Println(p)
		}
	}()

	killMe()
}
```