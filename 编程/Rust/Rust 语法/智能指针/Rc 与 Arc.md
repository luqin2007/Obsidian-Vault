---
语言: Rust
语法类型: 高级语法
---
> [!note] 使用场景
> **单线程中**希望在程序多个部分读取，且编译时无法确定哪一部分最后完成的堆内存。

`Rc<T>` 通过引用计数允许使用多个所有权

在存储数据时使用 `Rc.clone(Rc)` 方法为一个数据创建多个 `Rc<T>` 结构并增加引用计数，当最后一个 `Rc<T>` 释放，即引用计数为 0 时数据销毁。

```rust
use std::rc::Rc;

fn main() {
    let a = 123;
    let p1 = Rc::new(a);
    // Counter: 1
    println!("Counter: {}", Rc::strong_count(&p1));
    let p2 = p1.clone();
    // Counter: 2
    println!("Counter: {}", Rc::strong_count(&p1));
    let p3 = p1.clone();
    // Counter: 3
    println!("Counter: {}", Rc::strong_count(&p1));

    use_data(p1);
    // Counter: 2
    println!("Counter: {}", Rc::strong_count(&p3));
    use_data(p2);
    // Counter: 1
    println!("Counter: {}", Rc::strong_count(&p3));
    use_data(p3);
}

fn use_data(data: Rc<i32>) {}
```
# 弱引用

循环引用易造成内存泄漏，可使用 `Rc::downgrade` 获取弱引用 `Weak<T>` 代替，没有所有权关系，不会造成引用循环。

```rust title:Node
use std::cell::RefCell;
use std::rc::{Rc, Weak};

struct Node {
    value: i32,
    // 当 parent 移除时，children.parent 也应释放，反之不行
    // 即 实际上 children 不应持有 parent 的所有权 避免循环引用
    parent: Weak<Node>,
    // RefCell 类似 &mut, 可在运行时修改数据
    children: RefCell<Vec<Rc<Node>>>
}
```

```rust title:main
fn main() {
    let root = Rc::new(Node {
        value: 0,
        // 指向 Option::None
        parent: Weak::new(),
        children: RefCell::new(vec![])
    });

    let leaf = Rc::new(Node {
        value: 1,
        parent: Rc::downgrade(&root),
        children: RefCell::new(vec![])
    });

    root.children.borrow_mut().push(leaf);
}
```

# Arc

`Arc` 即 `Atomic RC`，用于多线程

```rust hl:7,11
#![forbid(unused_imports)] // Do not change this, (or the next) line.
use std::sync::Arc;
use std::thread;

fn main() {
    let numbers: Vec<_> = (0..100u32).collect();
    let shared_numbers = Arc::new(numbers); // TODO
    let mut joinhandles = Vec::new();

    for offset in 0..8 {
        let child_numbers = shared_numbers.clone(); // TODO
        joinhandles.push(thread::spawn(move || {
            let sum: u32 = child_numbers.iter().filter(|&&n| n % 8 == offset).sum();
            println!("Sum of offset {} is {}", offset, sum);
        }));
    }
    for handle in joinhandles.into_iter() {
        handle.join().unwrap();
    }
}
```
