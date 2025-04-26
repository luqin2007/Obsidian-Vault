Async 模型是一种异步操作模型，需要依赖 futures 包

> [!note]
> `async` 与基于共享内存、锁的多线程模型并非二选一，只是一种风格。

在函数前面添加 `async`  修饰，表示该函数是一个异步函数。此时函数的返回值隐式为 Future

> [!success]
> 在一个代码块前加 `async`  表示一个异步表达式，类似闭包，也支持 `async move` 

```rust
async fn downloads() {
    let future_one = download_async("...");
    let future_two = download_async("...");
    // 等待两个任务完成
    join!(future_one, future_two);
}
```

运行时，执行器会通过 `Future::poll()`  轮询任务是否执行完成

```rust
use futures::executor::block_on;

async fn downloads() { ... }

fn main() {
    let future = downloads();
    // 阻塞运行直到任务完成
    block_on(future);
}
```

在 `async`  方法中通过 `.await` ，可以等待另一个异步方法完成。

等待异步任务完成时，线程并非阻塞，而是在异步等待状态，还是可以执行其他 `future`  达到并发效果的

```rust
async fn hello_world() {
    println!("hello world";
}

async fn hello_kitty() {
    hello_world().await;
    println!("hello kitty");
}
```

`join!`  宏可以异步的等待多个任务完成
- 如果多个任务在一个数组中，使用 `futures::future::join_all` 
- `try_join!`  返回 `Result` 

```rust
async fn enjoy_book_and_music() -> (Book, Music) {
    let book = enjoy_book();
    let music = enjoy_music();
    // join_all([book, music])
    join!(book, music)
}
```

`select!`  宏表示当有任意一个任务完成时，执行其处理器

```rust
async fn enjoy_book_and_music() {
    let book = enjoy_book().fuse();
    let music = enjoy_music().fuse();
    pin_mut!(book, music)
    select! {
        () = t1 => println!("enjoy_book ok"),
        () = t2 => println!("enjoy_music ok"),
    }
}
```

- [[Executor 执行器]]
- [[Future 异步任务]]
- [[Pin 与 Unpin]]