---
语言: Rust
语法类型: 中级语法
---
可以使用 `panic!` 宏生成一个 `panic`，程序遇到 `panic` 时会自动展开，即对方法进行回溯并清理工作。

```rust
use std::env;

fn main() {
    // 需要设置环境变量
    env::set_var("RUST_BACKTRACE", "full");

    panic!("This is a panic!");
}
```

异常信息中只标注了调用 `panic!` 宏的位置，`RUST_BACKTRACE=1` 环境变量可以阅读 `backtrace`