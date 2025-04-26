---
语言: Rust
语法类型: 中级语法
---
`Box<T>` 是最简单的智能指针，将值放入堆中，栈上保留指向堆内存的指针。除指向堆内存外该指针无额外性能消耗，也不提供任何额外功能。

`Box` 指针主要应用于：
- 递归类型：编译时未知大小，但实际使用时有确切大小
- 改善拷贝性能：有大量数据且希望不拷贝的情况下转移所有权

```rust
fn main() {
    let b = Box::new(5);
    // b = 5
    println!("b = {}", b);
}
```
# 递归类型

以实现 `cons list` 为例：

> [!note] Cons List
> 来源于 `Lisp`，利用当前值和另一个列表来构建新列表的一种方法。
> 
> 将一个值连接到另一个值的开头，最后一项为 `Nil`  表示结束，有点像链表。

```rust
enum List {
// ^^^^^^ recursive type has infinite size
    Cons(i32, List),
 // ---- recursive without indirection
    Nil
}

fn main() {
    // [1, 2, 3]
    let list = Cons(1, Cons(2, Cons(3, Nil)));
}

// help: insert some indirection (e.g., a `Box`, `Rc`, or `&`) to make `List` representable
```

修改使用 `Box<T>`  实现

```rust
use crate::List::{Cons, Nil};

enum List {
    Cons(i32, Box<List>),
    Nil
}

fn main() {
    // [1, 2, 3]
    let list =
     Cons(1, Box::new(
        Cons(2, Box::new(
            Cons(3, Box::new(Nil))))));
}
```

# 动态分发

使用 `Box<dyn Trait名>` 可以引用一个 Trait 实现，要求对应实现必须是一个 Trait 对象

> [!note] Trait 对象：只关心值是否实现了某 trait 但不关心具体类型
> 
> 当对象安全时可认为是 Trait 对象，可使用 `Box<dyn T>` 
> - 特征不存在返回类型为 `Self` 的方法
> - 特征不存在包含泛型参数的方法

![[image-20240428150907-8fju816.png]]