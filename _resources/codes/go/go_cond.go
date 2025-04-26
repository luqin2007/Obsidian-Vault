package main

import (
	"fmt"
	"sync"
)

var sum = 0

var mutex = sync.Mutex{}

func add(i int) {
	mutex.Lock()
	sum += i
	mutex.Unlock()
}

func main() {
	wg1 := sync.WaitGroup{} // 防止 main 协程提前退出
	wg2 := sync.WaitGroup{} // 统计工作协程准备完成
	cond := sync.NewCond(&sync.Mutex{})
	worker := 10

	wg1.Add(worker)
	wg2.Add(worker)
	for i := 0; i < worker; i++ {
		go func(id int) {
			// 准备
			func() {
				defer wg2.Done()
				fmt.Println("ready", id)
			}()
			defer wg1.Done()
			// 等待开始的工作
			cond.L.Lock()
			cond.Wait()
			fmt.Println("start", id)
			add(10)
			cond.L.Unlock() // 工作结束
			fmt.Println("end", id)
		}(i)
	}

	wg1.Add(1)
	go func() {
		defer wg1.Done()
		// 等待所有工作协程准备完成
		wg2.Wait()
		// 启动所有阻塞的工作协程
		cond.Broadcast()
	}()

	// 阻塞等待所有协程完成
	wg1.Wait()
	fmt.Println("sum", sum)
}
