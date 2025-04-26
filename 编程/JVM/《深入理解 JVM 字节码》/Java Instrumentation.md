JDK 1.5 开始引入 `java.lang.instrument`，通过 `Instrumentation` 可以完成注册类文件转换器、获取所有类等，允许对类进行修改，实现 AOP、性能监控等。

```java
public interface Instrumentation {
    // 注入类文件转化器
    // ClassFileTransformer#transform 返回 null 表示不转换
    void addTransformer(ClassFileTransformer transformer, boolean canRetransform);
    // 对已加载的类重新触发类加载
    void retransformClasses(Class<?>... classes) throws UnmodifiableClassException;
    // 当前 JVM 是否允许使用类重新转换
    boolean isRedefineClassesSupported();
    // 获取所有已加载类
    Class[] getAllLoadedClasses();
}
```

Java Instrumentation 有两种注入方式：Agent Jar 包和 Attach API 远程加载 Agent Jar 包
# javaagent 参数

一个加载 `Instrumentation` 的典型使用方式如下：

```shell
java -javaagent:myagent.jar 其它参数...
```
* `myagent.jar`：包含 `Instrumentation Agent` 的 Jar 包

在 `Agent` Jar 包的 `MANIFEST.MF` 文件中需要指定 `Premain-Class` 等入口信息：

``` title:MANIFEST.MF
premain-Class:demo.javaagent.AgentMain
Agent-Class:demo.javaagent.AgentMain
Can-Redefine-Classes: true
Can-Retransform-Classes: true
```

其中，`premain-Class` 和 `Agent-Class` 指向一个包含 `premain` 函数的类。`premain` 将在 `main` 函数调用之前被调用，其签名如下：

```java
public static void premain(String agentArgument, Instrumentation instrumentation) throws Exception;
```
* `agentArgument`：`Agent` 启动参数
* `instrumentation`：`Instrumentation` 对象

![[../../../../_resources/images/Java Instrumentation 2024-08-02 22.21.09.excalidraw]]
# JVM Attach API

从 JDK 6 开始，JVM 支持动态 Attach Agent，可在 JVM 启动后在任意时刻远程加载 Agent Jar 包，底层主要依赖于信号和 Unix 域套接字。

使用 Attach Agent 启动的代理，入口是 `agentmain` 方法，其方法签名如下：

```java
public static void agentmain(String agentArgument, Instrumentation instrumentation) throws Exception;
```

由于跨进程，JVM 通过发起一个独立的 Java 程序，使用 `VirtualMachine.attach` 绑定对应进程。因此还需要一个 main 函数：

```java
public static void main(String[] args) throws Exception {
    VirtualMachine vm = VirtualMachine.attach("进程 id");
    try {
        vm.loadAgent("Agent Jar 包位置")
    } finally {
        vm.detach();
    }
}
```
## 原理

 JVM Attach API 的实现主要基于信号和 Unix 域套接字。

1. Attach 端检查临时文件目录是否存在 `.java_pidXXX` 文件，即 Unix 套接字文件。如果不存在则创建对应文件，发送 `SIGQUIT` 信号给目标 JVM。每 200ms 检查一次目标 JVM 端 Socket 文件是否生成，5s 还未生成则退出
2. 目标 JVM 进程：收到 `SIGQUIT` 信号后，检查 Attach 端 `.java_pidXXX` 是否存在
    * 存在：Attach 操作，启动 Attach Listener 线程，负责 Attach 请求，并创建 JVM 的 `.java_pidXXX` 文件
    * 不存在：不是 Attach 操作，执行默认逻辑打印栈

### 信号

> [!note] 信号
> Signal，事件发生时对进行的通知机制，又称“软件中断”，可以看成一种轻量级进程间通信。

每个信号都有一个名称，以 `SIG` 开头。

| 信号名       | 信号量 | 说明                       |
| --------- | --- | ------------------------ |
| `SIGINT`  | 2   | 键盘中断信号（`Ctrl+C`）         |
| `SIGQUIT` | 3   | 键盘退出信号                   |
| `SIGKILL` | 9   | `sure kill` 信号，应用程序总是被杀死 |
| `SIGTERM` | 15  | 终止信号                     |

JVM 对 `SIGQUIT` 信号的默认行为是打印所有运行的虚拟机线程堆栈信息。

类 Unit 系统可以通过 `kill -3 pid` 发送
### Unix 域套接字

> [!note] Unix 域套接字
> Unix Domain Socket，实现同一主机上进程间的通信

Unix 域套接字与普通套接字（使用 127.0.0.1 回环地址）相比，
* 更高效：不进行协议处理，不需要计算序列号，也不需要发送确认报文
* 更可靠：不会丢失数据，而普通套接字是为不可靠通信设计的
* 可简单修改为普通套接字