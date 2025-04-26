package main

import (
	"fmt"
	"sync"
	"time"
)

func main() {
	wg := sync.WaitGroup{}
	wg.Add(2)

	done := make(chan bool)

	// 工作协程
	go func() {
		defer wg.Done()
		for {
			select {
			case <-done:
				return // 退出任务
			default:
				work() // 实际工作
			}
		}
	}()

	// 监控协程
	go func() {
		defer wg.Done()
		watchDog(done)
	}()

	wg.Wait()
}

func work() {
	fmt.Println("Working...")
	time.Sleep(1 * time.Second)
}

func watchDog(done chan bool) {
	// 5s 后退出
	time.Sleep(5 * time.Second)
	done <- true
}
