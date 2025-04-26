#java9 

# Flow 响应式流

响应式流用于实现非阻塞背压的异步流处理，元素流从发布者传递到订阅者，不经过任何阻塞

![[Pasted image 20240806073711.png]]

* 订阅者（`Subscriber`） 通过调用发布者（`Publisher`） 的 `subscribe()` 方法订阅，订阅成功则触发订阅者的 `onSubscribe()` 方法向订阅者传递 `Subscription` 对象，否则触发 `onError()` 方法结束对话
* 订阅者通过调用 `Subscription.request(N)` 方法向发布者请求若干元素，可多次请求而不关心之前的请求是否已经发布
* 发布者通过触发订阅者的 `onNext(item)` 向订阅者发布元素，直到达到订阅者请求上限。若发布者再无更多元素发布给订阅者，触发订阅者 `onComplete()` 方法并结束对话
* 订阅者可通过 `Subscription.cancel()` 方法取消订阅并结束对话，但若之前的请求未完成则仍会在后续接收到发布者发布的请求
* 若发布者遇到错误，将调用订阅者的 `onError()` 方法并结束会话

```java
CompletableFuture<Void> future;
// 发布者 Flow.Publisher, Java 提供 SubmissionPublisher 实现类
//   executor: 向订阅者提供元素的线程
//   maxBufferCapacity: 给每个订阅者提供的最大缓冲区大小
//   handler: 当发布-订阅会话出现异常并关闭时的处理函数
// 发布方法
//   offer 非阻塞，当 onDrop 返回 false 时移除，返回正数表示预估缓存剩余元素，负数表示发送失败的尝试次数
//   submit 组设，直到订阅者可用于接受元素
try (SubmissionPublisher<Long> publisher = new SubmissionPublisher<>()) {
    System.out.println("Publisher buffer capacity=" + publisher.getMaxBufferCapacity());
  
    System.out.println("- Register subscriber");
    // consume 将给定方法包装为 ConsumerSubscriber 并注册
    // subscribe 直接注册给定 Subscriber
    future = publisher.consume(System.out::println);
    System.out.println("- Publish data");
    LongStream.range(0, 5).forEach(publisher::submit);
}
if (future != null) {
    try {
        // 等待接受并处理数据
        // 由于前面 try 已经关闭了发布者，这里一次性全部处理所有数据并输出
        // 0 1 2 3 4
        future.get();
    } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
    }
}
```

自定义接收者和非阻塞实例

```java
public static void main(String[] args) {
    try (SubmissionPublisher<Long> publisher = new SubmissionPublisher<>(Executors.newFixedThreadPool(5), 5)) {
        // subscribe 直接注册给定 Subscriber
        publisher.subscribe(new CustomSubscriber("A", 1000));
        publisher.subscribe(new CustomSubscriber("B", 2000));
        publisher.subscribe(new CustomSubscriber("C", 3000));
        PublisherThread thread = new PublisherThread(publisher);
        thread.start();
        thread.join();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
```

```java
public class CustomSubscriber implements Flow.Subscriber<Long> {
    String name;
    long blockTime;
    public CustomSubscriber(String name, long blockTime) {
        this.name = name;
        this.blockTime = blockTime;
    }
    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        System.out.println(name + " subscribed " + subscription);
        // 接受所有数据
        subscription.request(Long.MAX_VALUE);
    }
    @Override
    public void onNext(Long item) {
        try {
            System.out.println(name + " receive " + item + " start.");
            Thread.sleep(blockTime);
            System.out.println(name + " receive " + item + " finished.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onError(Throwable throwable) {
        System.out.println(name + " error " + throwable.getMessage());
        throwable.printStackTrace();
    }
    @Override
    public void onComplete() {
        System.out.println(name + " complete.");
    }
}
```

```java
public class PublisherThread extends Thread {
    SubmissionPublisher<Long> publisher;
    Random random = new Random();
    public PublisherThread(SubmissionPublisher<Long> publisher) {
        this.publisher = publisher;
    }
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            long value = random.nextLong();
            boolean submit = random.nextBoolean();
            if (submit) {
                // 使用 submit 阻塞发送
                System.out.println("-Submit value.");
                publisher.submit(value);
            } else {
                // 使用 offer 非阻塞发送
                int subscriber = random.nextInt(5);
                System.out.println("-Offer " + subscriber);
                publisher.offer(value, (s, v) -> {
                    switch (subscriber) {
                        case 0: return "A".equals(((CustomSubscriber) s).name);
                        case 1: return "B".equals(((CustomSubscriber) s).name);
                        case 2: return "C".equals(((CustomSubscriber) s).name);
                        default: return subscriber != 3;
                    }
                });
            }
            long waiting = random.nextLong(1000, 3000);
            try {
                Thread.sleep(waiting);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("-Publish finished.");
    }
}
```

> 注意：该实例未调用 `Flow.Subscription.cancel()` 方法，因此程序在发送完所有数据后并未退出

`Flow.Processor` 既是发送者又是接收者，可用来作为接收数据，过滤并发送的过滤器

‍
