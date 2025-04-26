package main

import (
	"fmt"
	"unsafe"
)

type person struct {
	Name string
	Age  int
}

func main() {
	p := new(person)
	// 第一个字段可以直接转换
	pName := (*string)(unsafe.Pointer(p))
	*pName = "Alice"
	// 其他字段需要计算偏移量
	off := unsafe.Offsetof(p.Age)
	up := uintptr(unsafe.Pointer(p))
	pAge := (*int)(unsafe.Pointer(off + up))
	*pAge = 20

	fmt.Println(*p)
}
