package main

import (
	"fmt"
	"time"
)

func main() {
	result := make(chan string)

	go func() {
		// 模拟长时间网络操作
		time.Sleep(10 * time.Second)
		result <- "服务端结果"
	}()

	// 超时 5s 结束
	select {
	case v := <-result:
		fmt.Println(v)
	case <-time.After(5 * time.Second):
		fmt.Println("Timeout")
	}
}
