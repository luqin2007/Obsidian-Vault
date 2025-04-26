---
语言: Rust
语法类型: 高级语法
---
> [!note] 使用场景
> 在非当前包的结构上实现某些 Trait

要在 `Vec<T>` 上实现 `Display` 是不允许的，因为 `Vec<T>` 和 `Display` 都不在当前包中。

若的确要实现，可使用元组结构体封装（因为元组结构体对于 crate 是本地的），然后实现 `Deref`。

```rust
use std::fmt;
use std::fmt::{format, Formatter, write};
use std::ops::Deref;

struct VecWrapper(Vec<String>);

impl fmt::Display for VecWrapper {
    fn fmt(&self, f: &mut Formatter<'_>) -> fmt::Result {
        write!(f, "[{}]", self.0.join(", "))
    }
}

impl Deref for VecWrapper {
    type Target = Vec<String>;

    fn deref(&self) -> &Self::Target {
        &self.0
    }
}

fn main() {
    let vec = vec!["A".to_string(), "B".to_string(), "C".to_string()];
    let d = VecWrapper(vec);
    println!("{}", d);
}
```