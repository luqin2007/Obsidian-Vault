package main

import (
	"fmt"
	"sync"
)

func main() {
	coms := buy(10)

	phones1 := build(coms)
	phones2 := build(coms)
	phones3 := build(coms)
	phones := merge(phones1, phones2, phones3)

	goods := pack(phones)
	for p := range goods {
		fmt.Println(p)
	}
}

// 采购工序
func buy(n int) <-chan string {
	out := make(chan string)
	go func() {
		defer close(out)
		for i := 0; i < n; i++ {
			out <- fmt.Sprint("配件", i)
		}
	}()
	return out
}

// 组装工序
func build(in <-chan string) <-chan string {
	out := make(chan string)
	go func() {
		defer close(out)
		for v := range in {
			out <- fmt.Sprintf("组装( %s )", v)
		}
	}()
	return out
}

// 打包工序
func pack(in <-chan string) <-chan string {
	out := make(chan string)
	go func() {
		defer close(out)
		for v := range in {
			out <- fmt.Sprintf("打包( %s )", v)
		}
	}()
	return out
}

func merge(in ...<-chan string) <-chan string {
	var wg sync.WaitGroup
	out := make(chan string)
	// 合并协程
	p := func(in <-chan string) {
		defer wg.Done()
		for v := range in {
			out <- v
		}
	}
	// 开启合并协程
	wg.Add(len(in))
	for _, i := range in {
		go p(i)
	}
	// 等待关闭 channel
	go func() {
		wg.Wait()
		close(out)
	}()

	return out
}
