- 尽力使同步要求最低，同步很复杂，且会阻塞线程，尝试避免共享状态
- 类的静态成员应该是线程安全的。通常，.Net Framework 的类满足这个要求
- 实例状态不需要是线程安全的。为得到最佳性能，最好在类的外部使用同步功能，且不对类的每个成员使用同步功能
# Lock

```csharp
lock(object)
{
    // synchronized region for obj
}
```
# Interlocked

线程安全的对整型的递增，递减，交换，取值的方法
# Monitor

`lock` 本质是使用 Monitor 实现，与 lock 相比，可添加一个等待被锁定的超时值，不会无限制的等待被锁定，可使用 TryEnter 方法

```csharp
object key = new object();
Monitor.Enter(key);
try
{
    // synchronized region for obj
}
finally
{
    Monitor.Exit(key);
}
```
# SpinLock

使用与 Monitor 基本相同，只是多了 `IsHide()` 和 `IsHeldByCurrentThread()` 即可

> [!warning]
> SpinLock 为结构体，因此应通过 ref 传递
# WaitHandle

抽象类，用于等待不同的信号。等待的句柄也可以由简单的异步委托使用
- 异步委托的 `BeginInvoke` 方法返回一个 `IAsyncResult` 接口对象，使用该对象可以用 `AsyncWaitHandle` 属性访问 `WaitHandle` 基类
- 调用 `WaitOne` 或者超时发生时，线程会等待接收一个与等待句柄相关的信号
- 调用 `EndInvoke` 方法，线程会最终阻塞，直到得到结果为止

```csharp
public delegate int TakesAWhileDelegate(int x, int ms);

// Main
TakesAWhileDelegate d1 = TakesAWhile;
IAsyncResult ar = d1.BeginInvoke(1, 3000, null, null);
while (true)
{
    if (ar.AsyncWaitHandle.WaitOne(50))
    {
        WriteLine("Can get the result now");
        break;
    }
}
int result = d1.EndInvoke(ar);
WriteLine($"Result: {result}");

// int TakesAWhile(int x, int ms)
Task.Delay(ms).Wait();
return 42;

// 控制台输出：
// Can get the result now
// Result: 42
```
# Mutex

互斥锁，类似于 Monitor，只有一个线程能拥有锁定，只有一个线程能获得互斥锁定，访问到受互斥保护的同步代码区域

```csharp
bool createdNew;
// 可定制参数：锁最初是否应由主调线程持有，互斥名，获得互斥是否已存在的信息
// 第三个参数为是否为新建，若返回 false 则表示互斥已经定义
// 互斥可在另一个进程中定义，操作系统可以识别有名称的互斥，可由进程间共享；未命名则不能在进程间共享
Mutex mutex = new Mutex(false, "CSharpMutex", out createdNew);
// WaitOne 获得互斥锁定，称为互斥锁的拥有者
if (mutex.WaitOne())
{
    try
    {
        // synchronized region
    }
    finally
    {
        // 释放互斥锁
        mutex.ReleaseMutex();
    }
}
else 
{
    // some problem happened while waiting
}
```

由于系统能识别有名称的互斥，因此可以禁止应用启动两次

```csharp
bool mutexCreated;
Mutex mutex = new Mutex(false, "SingletonWinAppMutex", out mutexCreated);
if (mutexCreated)
{
    MessageBox.Show("You can only start one instance of the application");
    Application.Current.Shutdown();
}
```
# Semaphore

信号量，类似于互斥，但信号量可以由多个线程使用，是一种计数的互斥锁定，可以设置可同时访问资源的线程数

信号量有两个
- Semaphore：可以命名，使用系统范围内的资源，允许在不同进程之间同步
- SemaphoreSlim：较短等待时间进行了优化的轻型版本

```csharp
static void Main()
{
    int taskCount = 6;
    int semaphoreCount = 3;
    SemaphoreSlim semaphore = new SemaphoreSlim(semaphoreCount, semaphoreCount);
    Task[] tasks = new Task[taskCount];
    for (int i = 0; i < taskCount; i++)
    {
        tasks[i] = Task.Run(() => TaskMain(semaphore));
    }
    Task.WaitAll(tasks);
    WriteLine("All tasks finished");
    ReadLine();
}

public static void TaskMain(SemaphoreSlim semaphore)
{
    bool isCompleted = false;
    while (!isCompleted)
    {
        // 等待锁定信号量
        if (semaphore.Wait(600))
        {
            try
            {
                WriteLine($"Task {Task.CurrentId} locks the semaphore");
                Task.Delay(2000).Wait();
            }
            finally
            {
                WriteLine($"Task {Task.CurrentId} releases the semaphore");
                semaphore.Release();
                isCompleted = true;
            }
        }
        else
        {
            WriteLine($"Timeout for task {Task.CurrentId}; wait again");
        }
    }
}
```
# Events

事件，也是一个系统范围内的资源同步方法
- System.Threading.ManualResetEvent
- System.Threading.ManualResetEventSlim
	- Set()：发送信号
	- Reset()：返回不发信号的状态
	- WaitOne()：等待信号锁
- System.Threading.AutoResetEvent：如果一个线程等待自动重置的事件发信号，当第一个线程等待状态结束时，事件自动变为不发信号状态
- System.Threading.CountdownEvent

```csharp
/*
Task 2 starts calculation
Task 3 starts calculation
Task 4 starts calculation
Task 1 starts calculation
Task 4 is ready
finished task for 3, result: 10
Task 1 is ready
finished task for 0, result: 4
Task 3 is ready
finished task for 2, result: 8
Task 2 is ready
finished task for 1, result: 6
*/
class Program
{
    static void Main()
    {
        const int taskCount = 4;
        
        var mEvents = new ManualResetEventSlim[taskCount];
        var waitHandles = new WaitHandle[taskCount];
        var calcs = new Calculator[taskCount];
        
        for (int i = 0; i < taskCount; i++)
        {
            int i1 = i;
            mEvents[i1] = new ManualResetEventSlim(false);
            waitHandles[i1] = mEvents[i1].WaitHandle;
            calcs[i1] = new Calculator(mEvents[i1]);
            Task.Run(() => calcs[i1].Calculation(i1 + 1, i1 + 3));
        }
        
        for (int i = 0; i < taskCount; i++)
        {
            int index = WaitHandle.WaitAny(waitHandles);
            if (index == WaitHandle.WaitTimeout)
            {
                WriteLine("Timeout!");
            }
            else
            {
                mEvents[index].Reset();
                WriteLine($"finished task for {index}, result: {calcs[index].Result}");
            }
        }
        
        ReadLine();
    }
}

public class Calculator
{
    private ManualResetEventSlim _mEvent;
    public int Result { get; set; }

    public Calculator(ManualResetEventSlim ev)
    {
        _mEvent = ev;
    }
    
    public void Calculation(int x, int y)
    {
        WriteLine($"Task {Task.CurrentId} starts calculation");
        Task.Delay(new Random().Next(3000)).Wait();
        Result = x + y;
        
        // signal the event-completed!
        WriteLine($"Task {Task.CurrentId} is ready");
        _mEvent.Set();
    }
}
```

可用 CountdownEvent 简化

```csharp
/*
Task 3 starts calculation
Task 1 starts calculation
Task 2 starts calculation
Task 4 starts calculation
Task 2 is ready
Task 1 is ready
Task 3 is ready
Task 4 is ready
all finished
task for 0, result 4
task for 1, result 6
task for 2, result 8
task for 3, result 10
*/
class Program
{
    static void Main()
    {
        const int taskCount = 4;
        
        var cEvent = new CountdownEvent(taskCount);
        var calcs = new Calculator[taskCount];
        
        for (int i = 0; i < taskCount; i++)
        {
            calcs[i] = new Calculator(cEvent);
            int i1 = i;
            Task.Run(() => calcs[i1].Calculation(i1 + 1, i1 + 3));
        }
        cEvent.Wait();
        WriteLine("all finished");
        for (int i = 0; i < taskCount; i++)
        {
            WriteLine($"task for {i}, result {calcs[i].Result}");
        }
        
        ReadLine();
    }
}

public class Calculator
{
    private CountdownEvent _mEvent;
    public int Result { get; set; }
    
    public Calculator(CountdownEvent ev)
    {
        _mEvent = ev;
    }
    
    public void Calculation(int x, int y)
    {
        WriteLine($"Task {Task.CurrentId} starts calculation");
        Task.Delay(new Random().Next(3000)).Wait();
        Result = x + y;
        
        // signal the event-completed!
        WriteLine($"Task {Task.CurrentId} is ready");
        _mEvent.Signal();
    }
}
```
# Barrier

屏障：可实现多个任务分支且以后又需要合并工作的情况。激活一个任务，可动态添加其他任务，然后等待所有任务完成

```csharp
var barrier = new Barrier(numberTasks);
// 通知增加一个任务
barrier.AddParticipant();
Task.Run(() => {
    // 每个任务完成后，通知该任务已经达到屏障
    barrier.SignalAndWait();
    // 移除一个任务
    barrier.RemoveParticipant();
});
```
# ReaderWriterLockSlim

使锁定机制允许锁定多个读取器（而非一个写入器）访问一个资源

提供一个锁定功能，如果没有写入器锁定资源，则允许多个读取器访问资源

```csharp
/*
Writer 1 acquired thie lock
Writer 2 waiting for the write lock
current reader count: 0
Writer 2 waiting for the write lock
current reader count: 0
Writer 2 waiting for the write lock
current reader count: 0
Writer 2 waiting for the write lock
current reader count: 0
Writer 2 waiting for the write lock
current reader count: 0
Writer 1 finished
Writer 2 acquired thie lock
Writer 2 finished
reader 4, loop: 0, item: 2
reader 3, loop: 0, item: 2
reader 2, loop: 0, item: 2
reader 1, loop: 0, item: 2
reader 1, loop: 1, item: 3
reader 4, loop: 1, item: 3
reader 3, loop: 1, item: 3
reader 2, loop: 1, item: 3
reader 3, loop: 2, item: 4
reader 4, loop: 2, item: 4
reader 1, loop: 2, item: 4
reader 2, loop: 2, item: 4
reader 1, loop: 3, item: 5
reader 3, loop: 3, item: 5
reader 4, loop: 3, item: 5
reader 2, loop: 3, item: 5
reader 4, loop: 4, item: 6
reader 1, loop: 4, item: 6
reader 3, loop: 4, item: 6
reader 2, loop: 4, item: 6
reader 1, loop: 5, item: 7
reader 4, loop: 5, item: 7
reader 2, loop: 5, item: 7
reader 3, loop: 5, item: 7
*/   
class Program
{
    private static List<int> _items = new List<int>() { 0, 1, 2, 3, 4, 5 };
    private static ReaderWriterLockSlim _rwl = new ReaderWriterLockSlim(LockRecursionPolicy.SupportsRecursion);

    static void Main()
    {
        var taskFactory = new TaskFactory(TaskCreationOptions.LongRunning, TaskContinuationOptions.None);
        var tasks = new Task[]
        {
            taskFactory.StartNew(WriterMethod, 1),
            taskFactory.StartNew(ReaderMethod, 1),
            taskFactory.StartNew(ReaderMethod, 2),
            taskFactory.StartNew(WriterMethod, 2),
            taskFactory.StartNew(ReaderMethod, 3),
            taskFactory.StartNew(ReaderMethod, 4)
        };
        Task.WaitAll(tasks);

        ReadLine();
    }

    public static void ReaderMethod(object reader)
    {
        try
        {
            _rwl.EnterReadLock();
            for (int i = 0; i < _items.Count; i++)
            {
                WriteLine($"reader {reader}, loop: {i}, item: {_items[i]}");
                Task.Delay(40).Wait();
            }
        }
        finally
        {
            _rwl.ExitReadLock();
        }
    }

    public static void WriterMethod(object writer)
    {
        try
        {
            while(!_rwl.TryEnterWriteLock(50))
            {
                WriteLine($"Writer {writer} waiting for the write lock");
                WriteLine($"current reader count: {_rwl.CurrentReadCount}");
            }
            WriteLine($"Writer {writer} acquired thie lock");
            for (int i = 0; i < _items.Count; i++)
            {
                _items[i]++;
                Task.Delay(50).Wait();
            }
            WriteLine($"Writer {writer} finished");
        }
        finally
        {
            _rwl.ExitWriteLock();
        }
    }
}
```

# Timer

计时器
- System.Threading.Timer：普通计时器

```csharp
// Timer(TimerCallback委托, 传入参数, 第一次执行的时间, 多次执行的间隔 只执行一次则传入-1)
var t1 = new Timer(TimeAction, null, TimeSpan.FromSeconds(2), TimeSpan.FromSeconds(3));
// 更改调用时间间隔
t1.Change();
```

- System.Windows.Threading.DispatcherTimer：基于 XAML 的计时器，事件函数在 UI 线程调用

```csharp
private DispatcherTimer _timer = new DispatcherTimer();
_timer.Tick += (sender, e) => { ... };
_timer.Interval = TimeSpan.FromSeconds(10);
_timer.Start();
_timer.Stop();
```
