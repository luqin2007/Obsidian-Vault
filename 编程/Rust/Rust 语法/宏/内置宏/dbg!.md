---
语言: Rust
语法类型: 高级语法
---
> [!note] 使用场景
> 在 `stderr` 中输出调试信息

在一个表达式上使用 `dbg!()`  宏，会在 `stderr` 中输出文件、行号、表达式结果。

```rust
#[derive(Debug)]
struct Rectangle {
    width: u32,
    height: u32
}

fn main() {
    let rect = Rectangle { width: 30, height: 50 };
    dbg!(&rect);
}
```
