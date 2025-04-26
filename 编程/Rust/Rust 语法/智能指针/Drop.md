---
语言: Rust
语法类型: 高级语法
---
> [!note] 使用场景
> 自定义指针

实现 `Drop` 接口后可自定义离开作用域时执行的代码，类似 C++ 的析构函数

```rust
impl<T> Drop for MyBox<T> {

    fn drop(&mut self) {
        // 一个假的释放方法
        println!("{}", "Pointer dropped!");
    }
}

fn main() {
    let s = MyBox(String::from("abc"));
}
// Pointer dropped!
```

无法直接调用 `drop` 方法释放一个数据，使用 `std::mem::drop` 手动释放资源