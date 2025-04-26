`unsafe` 包用于绕过 Go 内存安全机制，直接对内存读写，可能危险但效率高。

![[../../../_resources/images/unsafe 2024-09-16 23.25.45.excalidraw|50%]]

# `unsafe.Pointer`

一种特殊指针，类似 `void*`，可作为中转完成任意指针类型的转换

```go
i := 10
p := &i
// *int 10
fmt.Println(reflect.TypeOf(p), *p)

up := unsafe.Pointer(p)
fp := (*float64)(up)
// *float64 5e-323
fmt.Println(reflect.TypeOf(fp), *fp)
```

#  `uintptr`

一种指针类型，类似 `unsafe.Pointer`，可以实现指针类型中转，同时也支持指针运算

```reference
file: "@/_resources/codes/go/go_uintptr.go"
start: 18
end: 22
```

#  `unsafe.Sizeof`

`unsafe.Sizeof(v)` 计算值占内存大小