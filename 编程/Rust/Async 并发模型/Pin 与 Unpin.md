# Pin 与 Unpin

实现了 `Unpin`  表示该结构在内存中可以安全移动，`Pin`  表示该结构在内存中不能任意移动

> [!note]
>  `Unpin`  是一个 Trait，`Pin`  是一个结构体

`Pin`  结构体内包含一个指针。如果 `Pin`  内指针指向的数据实现了 `!Unpin` ，`Pin`  保证内部实现了的指针对应的数据不会被任意移动。

> [!note]
> 可以使用 `_marker: PhantomPinned`  表示实现 `!Pin` 

`Pin`  多用于自引用类型

```rust
struct Test {
    a: String,
    b: *const String,
}

impl Test {
    fn new(text: &str) {
        Test { a: String::from(text), b: std::ptr::null(), }
    }

    fn init(&mut self) {
        let self_ref: *const String = &self.a;
        self.b = self_ref;
    }

    fn a(&self) -> &str { &self.a }
    fn b(&self) -> &String {unsafe{&*(self.b)}}
}
```

在没有 `Pin`  的情况下交换：

`````col
````col-md
flexGrow=1
===
![[image-20240428202121-as6wj51.png]]
````
````col-md
flexGrow=1
===
```rust
fn main() {
    let mut test1 = Test::new::("test1");
    // a="test1", b="test1"
    test1.init();
    let mut test2 = Test::new::("test2");
    // a="test2", b="test2"
    test2.init();

    // 交换
    std::mem::swap(&mut test1, &mut test2);
  
    // test1: a="test2", b="test1"
    // test2: a="test1", b="test2"
}
```
````
`````

实现 `Pin` ：

```rust
struct Test {
    a: String,
    b: *const String,
    _marker: PhantomPined,
}

impl Test {
    fn new(text: &str) {
        Test { a: String::from(text), 
               b: std::ptr::null(), 
               // !Unpin 标记
               _marker: PhantomPined,  }
    }

    fn init(self: Pin<&mut Self>) {
        let self_ref: *const String = &self.a;
        let this = unsafe { self.get_unchecked_mut() };
        this.b = self_ptr;
    }

    fn a(self: Pin<&Self>) -> &str { &self.get_ref().a }
    fn b(self: Pin<&Self>) -> &String { unsafe{ &*(self.b) } }
}
```
```rust
fn main() {
    let mut test1 = Test::new::("test1");
    // a="test1", b="test1"
    test1.init();
    let mut test2 = Test::new::("test2");
    // a="test2", b="test2"
    test2.init();

    // 错误：`PhantomPined` cannot be unpinned
    std::mem::swap(&mut test1, &mut test2);
}

```
