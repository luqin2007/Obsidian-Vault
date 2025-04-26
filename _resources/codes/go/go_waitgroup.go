package main

import (
	"fmt"
	"sync"
)

var sum = 0

var mutex = sync.RWMutex{}

func add(i int) {
	mutex.Lock()
	sum += i
	mutex.Unlock()
}

func read() int {
	mutex.RLock()
	defer mutex.RUnlock()
	return sum
}

func main() {
	wg := sync.WaitGroup{}

	wg.Add(1000) // 增加 1000 个协程计数
	for i := 0; i < 1000; i++ {
		go func() {
			defer wg.Done() // 减去一个协程计数
			add(10)
		}()
	}

	wg.Add(10)
	for i := 0; i < 10; i++ {
		go func() {
			defer wg.Done()
			fmt.Println(read())
		}()
	}

	// 阻塞等待所有协程完成
	wg.Wait()
}
