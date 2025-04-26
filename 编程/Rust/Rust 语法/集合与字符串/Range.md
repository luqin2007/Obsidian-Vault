---
语言: Rust
语法类型: 中级语法
---
Range 表示一段整数范围，可以使用 `..` 运算符创建，常用于循环和切片。

`````col
````col-md
flexGrow=1
===
```rust
for num in (1..4).rev() {
    // 4 3 2 1
    println!("Value is {}", num);
}
```
````
````col-md
flexGrow=1
===
```rust
let a = [1, 2, 3, 4, 5];
// sa: 2, 3
let sa = &a[1..3];
```
````
`````

取切片时：

`````col
````col-md
flexGrow=1
===
若以容器第一个元素开头，开头可省略

```rust
let s = String::from("hello");
// 等价于 &s[0..2]
let slice = &s[..2];
```
````
````col-md
flexGrow=1
===
若以容器最后一个元素结尾，结尾可省略

```rust
let s = String::from("hello");
// 等价于 &s[1..s.len()]
let slice = &s[1..];
```
````
`````

所以，`&s[..]` 等价于 `&s[0..s.len()]`