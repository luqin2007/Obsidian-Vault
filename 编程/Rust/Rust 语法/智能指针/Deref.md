---
语言: Rust
语法类型: 高级语法
---
> [!note] 使用场景
> 自定义指针

通过解引用获取内部数据的引用

```rust
fn main() {
    let a = 5;
    let b = &a;
    let c = Box::new(a);

    assert_eq!(a, 5);
    assert_eq!(a, *b);
    // 智能指针解引用
    assert_eq!(a, *c);
}
```

实现 `Deref` 允许重载解引用运算符 `*`，可以将引用转换为被引用数据

```rust
use std::ops::Deref;  
  
struct MyBox<T>(T);  
  
impl <T> MyBox<T> {  
    fn new(x: T) -> MyBox<T> {  
        MyBox(x)  
    }  
}  
  
impl<T> Deref for MyBox<T> {  
    type Target = T;  
  
    fn deref(&self) -> &Self::Target {  
        &self.0  
    }  
}  
  
fn main() {  
    let x = 5;  
    let y = MyBox(x);  
  
    assert_eq!(5, *y)  
}
```

 `deref`  方法返回一个解引用时返回数据的引用值。实现后可通过 `*`  将指针结构体解引用成指定数据类型 `type Target T;`  定义了关联类型。  

实现了 `Deref` 的结构作为引用参数传递时可隐式转换为对应类型的引用

- `T: Deref<Target=U>`: `&T` -> `&U`
- `T: Deref<Target=U>`: `&mut T` -> `&U`
- `T: DerefMut<Target=U>`: `&mut T` -> `&mut U`

```rust
fn print(s: &str) {
    println!("{}", s);
}

fn main() {
    let s = MyBox(String::from("abc"));
    // &MyBox<String> -> &String
    // &String -> &str
    print(&s);
}
```
