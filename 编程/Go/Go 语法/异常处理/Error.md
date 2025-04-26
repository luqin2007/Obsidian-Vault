Go 错误类接口 Error 只包含一个方法 `{go icon}Error() string`。

当创建一个可能有异常的函数时，通常在返回值后额外返回一个 `error` 值即可
- 正常情况下，`error` 值为 `nil`
- 发生异常时，使用 `errors.New` 创建异常值

```go
func div(a, b int) (int, error) {
	if b == 0 {
	    // 异常
		return 0, errors.New("division by zero")
	}
	return a / b, nil
}
```

# 异常嵌套

使用 `fmt.Errorf(message)` 创建嵌套的异常，使用 `%w` 作为异常的占位符

```go
package main

import ( "errors"; "fmt" )

func main() {
	e := errors.New("内部错误")
	w := fmt.Errorf("Wrap 了错误 %w", e)
	fmt.Println(w)
}
```

使用 `errors.Unwrap(e)` 解开嵌套

```go
package main

import ( "errors"; "fmt" )

func main() {
	e := errors.New("内部错误")
	w := fmt.Errorf("Wrap 了错误 %w", e)
	// 解包
	ue := errors.Unwrap(w)
	fmt.Println(ue)
}
```

使用 `errors.Is(err, target)` 判断 `target` 是否是 `err` 错误：
- `err == target` 时返回 `true`
- 在 `err` 的嵌套链中，若存在 `target`，也返回 `true`

使用 `errors.As(error, errorVar)` 判断异常 `error` 的异常链中是否包含某个异常，并将其提取出来

```go
package main

import (
	"errors"
	"fmt"
)

// 自定义异常类
type commonError struct {
	code uint
	msg  string
}

func (e *commonError) Error() string {
	return fmt.Sprintf("Error[%d]: %s", e.code, e.msg)
}

func main() {
	e := &commonError{1, "common error"}
	w := fmt.Errorf("Wrap 了错误 %w", e)
    // 提取异常
	var cm *commonError
	if errors.As(w, &cm) {
		fmt.Printf("CommonError[%d]: %s\n", cm.code, cm.msg)
	}
}
```

# defer

`defer` 函数用于声明给定函数将推迟且保证在函数返回时执行

```go
func ReadFile(path string) ([]byte, error) {
	f, err := os.Open(path)
	if err != nil {
		return nil, err
	}
	// f.Close() 将被推迟到 return 之后执行
	defer f.Close()
	return io.ReadAll(f)
}
```

一个函数可以有多个 `defer`，函数结束时按声明顺序的倒序依次执行

```go
package main

func main() {
	defer println("Defer 1")
	defer println("Defer 2")
	defer println("Defer 3")
	println("==================")
}
```