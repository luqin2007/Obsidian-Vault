执行器负责管理和调用 `Future` 。

一般来说，执行器会轮询一次所有的 `Future` ，调用他们的 `poll()`  函数，之后等待任务调用 `waker()`  方法唤醒轮询器，轮询器才继续调用 `poll()` 。

下面使用消息队列实现一个简单的 `Executor` ：

1. 创建任务结构体 `Task` 。任务实现 `ArcWake`  使自身成为一个 `Waker` 

```rust
struct Task {
    future: Mutex<Option<BoxFuture<'static, ()>>>,
    task_sender: SyncSender<Arc<Task>>,
}

impl ArcWake for Task {
    fn wake_by_ref(arc_self: &Arc<Self>) {
        let cloned = arc_self.clone();
        arc_self.task_sender
            .send(cloned)
            .expect("任务队列已满");
    }
}
```

2. 创建一个 `Executor`  和 `Spawner` 。执行器负责从消息通道中拉取事件。任务就绪时从消息通道中取出任务并执行。

```rust
struct Executor {
    ready_queue: Receiver<Arc<Task>>,
}

impl Executor {
    fn run(&self) {
        while let Ok(task) = self.ready_queue.recv() {
            let mut future_slot = task.future.lock().unwrap();
            if let Some(mut future) = future_slot.take() { 
                let waker = waker_ref(&task);
                let context = &mut Context::from_waker(&*waker);
                if future.as_mut().poll(context).is_pending() { 
                    *future_slot = Some(future)
                }
            }
        }
    }
}
```

```rust
#[derive(Clone)]
struct Spawner {
    task_sender: SyncSender<Arc<Task>>,
}

impl Spawner {
    // 发送一个任务到任务队列
    fn spawn(&self, future: impl Future<Output = ()> + 'static + Send) {
        let future = future.boxed();
        let task = Arc::new(Task {
            future: Mutex::new(Some(future)),
            task_sender: self.task_sender.clone(),
        });
        self.task_sender.send(task).expect("任务队列已满");
    }
}
```

```rust
fn new_executor_and_spawner() -> (Executor, Spawner) {
    const MAX_QUEUE_TASKS: usize = 10_000;
    let (task_sender, ready_queue) = sync_channel(MAX_QUEUE_TASKS);
    (Executor { ready_queue }, Spawner { task_sender })
}
```

3. 在 `main`  中执行

```rust
fn main() {
    let (executor, spawner) = new_executor_and_spawner();
    spawner.spawn(async {
        println!("howdy!");
        TimeFuture::new(Duration::from_secs(2));
        println!("done!")
    });

    // 停止 Spawner，告诉 Executor 不再有新任务
    drop(spawner);

    executor.run();
}
```

‍
