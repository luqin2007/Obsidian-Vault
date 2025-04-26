package main

import "testing"

func TestFibonacci(t *testing.T) {
	fsMap := map[int]int{
		0: 0,
		1: 1,
		2: 1,
		3: 2,
		4: 3,
		5: 5,
		6: 8,
		7: 13,
		8: 21,
		9: 34,
	}
	for k, v := range fsMap {
		fib := Fibonacci(k)
		if fib == v {
			t.Logf("结果正确：n=%d, resut=%d", k, fib)
		} else {
			t.Errorf("结果错误：n=%d, 错误答案=%d, 正确答案=%d", k, fib, v)
		}
	}
}

func BenchmarkFibonacci(b *testing.B) {
	n := 20

	b.ReportAllocs() // 开启内存统计
	b.StartTimer()   // 开始计时
	for i := 0; i < b.N; i++ {
		Fibonacci(n)
	}
	b.StopTimer() // 结束计时
}

func BenchmarkFibonacciAsync(b *testing.B) {
	n := 20

	b.RunParallel(func(pb *testing.PB) {
		for pb.Next() {
			Fibonacci(n)
		}
	})
}
