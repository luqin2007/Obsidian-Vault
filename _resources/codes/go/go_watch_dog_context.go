package main

import (
	"context"
	"fmt"
	"sync"
	"time"
)

func main() {
	wg := sync.WaitGroup{}
	ctx, stop := context.WithCancel(context.Background())

	wg.Add(1)
	go func() {
		defer wg.Done()
		watchDog(ctx, "【监控狗 1】")
	}()

	// 控制协程 5s 后退出
	time.Sleep(5 * time.Second)
	stop() // 停止
	wg.Wait()
}

func watchDog(ctx context.Context, name string) {
	for {
		select {
		case <-ctx.Done():
			fmt.Println(name, "收到停止信号")
			return
		default:
			fmt.Println(name, "正在监控...")
		}
		// 每秒检查一次
		time.Sleep(time.Second)
	}
}
