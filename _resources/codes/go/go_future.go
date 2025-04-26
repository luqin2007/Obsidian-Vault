package main

import (
	"fmt"
	"math/rand"
	"time"
)

func main() {
	// channel 相当于 future
	chVegetable := washVegetables()
	time.Sleep(time.Second)
	chWater := boilWater()
	// 等待任务完成
	vegetables := <-chVegetable
	water := <-chWater
	fmt.Println("可以做菜了", vegetables, water)
}

func washVegetables() <-chan string {
	out := make(chan string)
	fmt.Println("洗菜...")
	go func() {
		defer close(out)
		// do
		r := rand.New(rand.NewSource(time.Now().UnixNano()))
		for r.Int31()%10 != 0 {
			time.Sleep(1 * time.Second)
		}
		out <- "洗好的蔬菜"
		fmt.Println("洗完菜了...")
	}()
	return out
}

func boilWater() <-chan string {
	out := make(chan string)
	fmt.Println("烧水...")
	go func() {
		defer close(out)
		// do
		r := rand.New(rand.NewSource(time.Now().UnixNano()))
		for r.Int31()%10 != 0 {
			time.Sleep(1 * time.Second)
		}
		out <- "烧开的水"
		fmt.Println("烧完水了...")
	}()
	return out
}
