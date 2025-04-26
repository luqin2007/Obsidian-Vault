---
语言: Rust
语法类型: 中级语法
---
`Trait` 类似接口，定义了一系列共同的行为（方法），支持方法的默认实现和 `self`

```rust
pub trait Summary {
    // 行为方法
    fn summarize(&self) -> String;

    // 默认实现
    fn summarize_more(&self) -> String {
        String::from("(Read More...)")
    }

    // 使用 self
    fn print(&self) {
        println!("{}", self.summarize())
    }
}
```

`Trait` 可以继承。使用 `:` 声明继承关系

```rust
trait A: fmt::Display {
    fn fun_a();

    fn outline_print(&self) {
        let output = self.to_string();
        let len = output.len();
        println!("{}", "*".repeat(len + 4));
        println!("*{}*", " ".repeat(len + 2));
        println!("* {} *", output);
        println!("*{}*", " ".repeat(len + 2));
        println!("{}", "*".repeat(len + 4));
    }
}
```

> [!success]
> 对于某些 Rust 内置的 `Trait`，可以在结构体上使用 `#[drive(<特征名>)]` 派生特征的默认实现
> 
> - `Debug` 使用 {:?} 调试输出
> - `PartialEq`，`Eq` 比较两个值是否相等
> - `PartialOrd`，`Ord` 比较两个值的大小关系
> - `Clone`，`Copy` 深拷贝和浅拷贝
> - `Hash` 计算 Hash 值
> - `Default` 产生默认值

使用 `impl <Trait 名> for <结构体名>`  在结构体上实现 Trait

> [!warning] 孤儿规则
> 如果你想要为类型 A 实现特征 T，那么 A 或者 T 至少有一个是在当前作用域中定义的。如果要为其他结构体实现 Trait，可参考 [[newtype 模式]]

```rust
pub struct News {
    pub headline: String,
    pub location: String,
    pub author: String,
    pub content: String,
}

impl Summary for News {
    fn summarize(&self) -> String {
        format!("{} by {}({})", self.headline, self.author, self.location)
    }
}
```

在函数中，使用 `impl <Trait 名>` 作为参数类型表示某参数需要实现某 trait，同时实现多个 trait 使用 `+` 连接

# Trait bound

> [!info] trait bound：使用特征约束 trait bound 提取到泛型，使用 where 后置特征约束（where 约束）

```rust
// 使用特征作为参数类型
pub fn notify(item1: &impl Summary + Display, 
              item2: &impl Summary + Display) -> impl Clone + Display;
// 特征约束
pub fn notify<T: Summary + Display, 
              U: Clone + Display>(item1: &T, item2: &T) -> U;
// 特征后置
pub fn notify<T, U>(item1: &T, item2: &T) -> U 
              where T: Summary + Display, U: Clone + Display;
```

使用 trait bound 可以针对某些特定的泛型类型实现结构体方法

```rust
use std::fmt::Display;
struct Point<T> { x: T, y: T }

impl<T> Point<T> {
    fn new(x: T, y: T) -> Point<T> {
        Point { x, y }
    }
}

impl<T: Display + PartialOrd> Point<T> {
    fn cmp_display(&self) {
        if self.x >= self.y {
            println!("The largest number is {}", self.x);
        } else {
            println!("The largest number is {}", self.y);
        }
    }
}
```

# Box

由于 Rust 的泛型机制（<font color="#9bbb59">单态化</font>），无法在一个方法中返回不同的类型。若想返回不同的结构体，则可以使用 `Box<dyn T>`

```rust
// impl Summary for News
// impl Summary for Tweet
pub fn new_summary<T: Summary>(username: String, 
                               content: String, 
                               is_news: bool) -> T {
    if is_news {
        News { ... }
    } else {
        Tweet { ... }
    }
}

fn main() {
    let s = new_summary("a".to_string(), "b".to_string(), false);
    //  ^ consider giving `s` a type
}
```

```rust
pub fn new_summary(username: String, 
                   content: String, 
                   is_news: bool) -> Box<dyn Summary> {
    if is_news {
        Box::new(News { ... })
    } else {
        Box::new(Tweet { ... })
    }
}

fn main() {
    // Box<dyn Summary>
    let s = new_summary("a".to_string(), "b".to_string(), false);
    // a: b
    s.print();
}
```
