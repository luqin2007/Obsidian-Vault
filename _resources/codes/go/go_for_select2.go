package main

import (
	"sync"
	"time"
)

func main() {
	wg := sync.WaitGroup{}
	wg.Add(2)

	done := make(chan bool)
	result := make(chan int)

	// 工作协程
	go func() {
		defer wg.Done()
		for _, s := range []int{} {
			select {
			case done <- true:
				return
			case result <- s: // 将数据传递给其他 channel
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

func watchDog(done chan bool) {
	// 5s 后退出
	time.Sleep(5 * time.Second)
	done <- true
}
