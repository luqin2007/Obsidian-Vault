```go title:main.go
package main

import "fmt"

func main() {
    fmt.Println("Hello World!")
}
```

- 使用 `//` 、`/**/` 注释
- 行末可以省略 `;`

---

- [[变量]]
- [[数据类型/数据类型|数据类型]]
- [[控制结构]]
- [[函数]]
- [[异常处理/异常处理|异常处理]]
- [[运行时反射]]
- [[json]]
- [[unsafe]]
- [[泛型]]

---

- 并发：基于协程
	- [[并发/协程与管道|协程与管道]]
	- [[并发/同步原语|同步原语]]
	- [[并发/并发控制|并发控制]]
	- [[并发/并发模式|并发模式]]
	- [[并发/字节池|字节池]]
- 工程管理
	- [[工程管理/测试|测试]]
	- [[工程管理/代码优化|代码优化]]
	- [[工程管理/模块化|模块化]]

# 编译与运行

使用 `go run main.go` 可以直接运行 `main.go` 文件

使用 `go build main.go` 可以将 `main.go` 编译打包成可执行文件

使用 `go install main.go` 可以将 `main.go` 编译并安装到 `%GOBIN%` 环境变量指向的目录中

通过设置 `GOOS` 和 `GOARCH` 分别为目标系统和平台，可以方便的进行跨平台编译

# 参考

```cardlink
url: https://book.douban.com/subject/36171041/
title: "打开Go语言之门"
description: "通过不断思考和整理，作者编写了这本更具系统性也更通俗易懂的Go语言入门和进阶书籍，以帮助学习者少走弯路，比其他人更快一步提升职业竞争力。本书共包含 五 个部分：第1部分：快速入门Go 语言。作者挑选..."
host: book.douban.com
image: https://img1.doubanio.com/view/subject/l/public/s34363389.jpg
```
