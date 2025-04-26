package main

import (
	"fmt"
)

func main() {
	coms := buy(10)
	phones := build(coms)
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
