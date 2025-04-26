package main

import (
	"fmt"
	"sync"
	"time"
)

var sum = 0

var mutex = sync.Mutex{}

func add(i int) {
	mutex.Lock()
	sum += i
	mutex.Unlock()
}

func main() {
	r_sum := 0

	for i := 0; i < 1000; i++ {
		go add(10)
		r_sum += 10
	}

	time.Sleep(2 * time.Second)
	fmt.Println(r_sum, sum)
}
