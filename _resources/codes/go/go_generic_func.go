package main

import "fmt"

func myMin[T int | float32 | float64](a, b T) T {
	if a < b {
		return a
	}
	return b
}

func main() {
	fmt.Println(myMin[int](3, 5))
	fmt.Println(myMin[float32](10.3, 5.0))
	fmt.Println(myMin[float64](1.035, 50.0))
}
