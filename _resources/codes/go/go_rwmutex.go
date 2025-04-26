package main

import (
	"fmt"
	"sync"
	"time"
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
	for i := 0; i < 1000; i++ {
		go add(10)
	}

	for i := 0; i < 10; i++ {
		// 结果中存在相同数字，说明有同时读情况发生
		go fmt.Println(read())
	}

	time.Sleep(2 * time.Second)
}
