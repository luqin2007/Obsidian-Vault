`Future`  代表一个能产出值的异步计算，产生后需要通过执行器 `executor`  执行

> [!note]
> Rust 实现的 Future 实现 `Pin` ，表示数据指向的可变引用的内存地址是不变的

一个简单的 Future 实现计时功能如下：

1. `SharedState`  用于记录任务状态，`completed`  表示任务是否完成

```rust
struct SharedState {
    completed: bool,
    waker: Option<Waker>
}
```

2. `Future`  实现类中持有一个 `shared_state`  以便检查

> [!danger]
> 当执行器是多线程执行器时，应使用 `futures::lock`  替代 `Mutex`  防止线程池死锁

```rust
pub struct TimeFuture {
    shared_state: Arc<Mutex<SharedState>>
}
```
 
3. 实现 `new`  方法，新开一个线程作为计时器，并在必要的时候调用 `Waker`  唤醒任务

> [!note]
> 这里给出的实例中，只需要调用一次任务即可完成。实际情况下可能要多次调用线程中的代码才能完成

```rust
impl TimeFuture {
    pub fn new(duration: Duration) -> TimeFuture {
        let shared_state = Arc::new(Mutex::new(SharedState {
            completed: false,
            waker: None,
        }));
        let thread_state = shared_state.clone();
        thread::spawn(move || {
            // 通过 sleep 计时
            thread::sleep(duration);
            let mut shared_state = thread_state.lock().unwrap();
            shared_state.completed = true;
            // 如果必要的话调用 waker
            if let Some(waker) = shared_state.waker.take() {
                waker.wake();
            }
        });

        TimeFuture { shared_state }
    }
}
```

4. 实现 `poll`  方法，检查 `SharedState`  判断任务是否完成，未完成时向 `SharedState`  提供 `Waker` 

```rust
impl Future for TimeFuture {
    type Output = ();

    fn poll(self: Pin<&mut Self>, cx: &mut Context<'_>) -> Poll<Self::Output> {
        let mut shared_state = self.shared_state.lock().unwrap();
        if shared_state.completed {
            Poll::Ready(())
        } else {
            shared_state.waker = Some(cx.waker().clone());
            Poll::Pending
        }
    }
}
```

‍
