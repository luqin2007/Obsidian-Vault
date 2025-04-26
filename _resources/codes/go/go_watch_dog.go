package main

import (
	"fmt"
	"sync"
	"time"
)

func main() {
	wg := sync.WaitGroup{}
	stopCh := make(chan bool)

	wg.Add(1)
	go func() {
		defer wg.Done()
		watchDog(stopCh, "【看门狗 1】")
	}()

	// 控制协程 5s 后退出
	time.Sleep(5 * time.Second)
	stopCh <- true
	wg.Wait()
}

func watchDog(stopCh chan bool, name string) {
	for {
		select {
		case <-stopCh:
			fmt.Println(name, "收到停止信号")
			return
		default:
			fmt.Println(name, "正在监控...")
		}
		// 每秒检查一次
		time.Sleep(time.Second)
	}
}
