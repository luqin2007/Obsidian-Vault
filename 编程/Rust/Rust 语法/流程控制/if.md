---
语言: Rust
语法类型: 基础语法
---
`if` 语句用于条件判断

```rust
fn condition1() -> bool { true }
fn condition2() -> bool { true }
fn condition3() -> bool { false }

fn main() {
    if condition1() {
        println!("In condition 1");
    } else if condition2() {
        println!("In condition 2");
    } else if condition3() {
        println!("In condition 3");
    } else {
        println!("In else");
    }
}
```

`if` 是一个表达式，因此也可以用于赋值等

```rust
fn condition1() -> bool { true }
fn condition2() -> bool { true }
fn condition3() -> bool { false }

fn main() {
    let str = if condition1() {
        "In condition 1"
    } else if condition2() {
        "In condition 2"
    } else if condition3() {
        "In condition 3"
    } else {
        "In else"
    };

    println!("{}", str);
}
```