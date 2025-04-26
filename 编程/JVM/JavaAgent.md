> [!note] Java Agent = bytecode instrumentation
> Instrumentation 通过独立的应用程序代理程序（称为 JavaAgent），检测和协助已运行的 JVM 程序，甚至替换和修改某些类定义。

JavaAgent 是 Instrumentation 的具体实现。

>[!note] 几乎所有 APM 工具都基于 Instrumentation 实现

Instrumentation 基于 JVMTI（JVM Tool Interface），为 JVM 提供的本地编程接口集合，具有很多强大功能，使 Java 具有更强的动态控制、解释能力，提供基于虚拟机层面的 AOT 支持
# Instrumentation

JavaAgent 的类转换（Instrumentation）分为三种，对应类加载的三个不同时机

![[Pasted image 20240806153858.png]]
* static instrumentation：在类还未被加载时修改
* load-time instrumentation：当类正要被加载时，现将字节码文件传递给 Agent 处理
* dynamic instrumentation：类已经被加载进虚拟机甚至正在使用时，允许类被修改
  而 JavaAgent 可以实现 load-time instrumentation 和 dynamic instrumentation
# 启动

程序启动时，使用 `-javaagent` 参数可以加载 JavaAgent，此时对应 `Laod-Time Instrumentation`

```shell
java ... -javaagent:JavaAgent的Jar包位置 [premain方法参数]
```

第二种执行方法是利用 JVM 的 Attach 机制将 JavaAgent 附加到正在运行的程序上，对应 `Dynamic Instrumentation`，该方法使用了 AttachAPI
* Java8：需要依赖 `com.sun` 的 `tools.jar`
* Java9+：位于 `jdk.attach` 模块中，需要 `require jdk.attach`。另外还需要 `java.instrument` 模块和 `java.management` 模块

```java
public static void main(String[] args) throws Exception {
    // 正在执行的程序进程 pid
    String pid = "1234";
    // 要绑定的 JavaAgent jar 包
    String agent = ".../Agent.jar";
    VirtualMachine vm = VirtualMachine.attach(pid);
    vm.loadAgent(agent);
    vm.detach();
}
```
# 入口

JavaAgent 的入口分为两种，对应 `Load-Time Insatrumentation` 和 `Dynamic Insatrumentation`。二者声明仅入口函数名不同，参数相同
* Load-Time Insatrumentation：`premain`
* Dynamic Insatrumentation：`agentmain`

两种入口每种都支持以下两种声明，第一种的优先级更高，以 `premain` 为例：
1. `public static void premain(String args, Instrumentation inst)`
2. `public static void premain(String args)`

`java.lang.instrument.Instrumentation` 实例是核心部分，集中了几乎所有的功能和方法。

打包时，应在 Manifest 中添加以下内容：

``` title:Manifest
# 两种入口函数注册
Premain-Class: premain 方法所在类
Agent-Class: agentmain 方法所在类

# 开启更多功能
Can-Redefine-Classes: true
Can-Retransform-Classes: true
Can-Set-Native-Method-Prefix: true
```

另外，若用到 JVM 自带的 ASM，还需要 `-XDignore.symbol.file` 参数忽略找不到的类
* 原因：javac 默认不直接使用 `rt.jar` 而是使用 `lib/ct.sym` 中的符号表

>[!note] 若一个应用程序 jar 包（带 main 函数）自带 JavaAgent，应在 Manifest 中定义 `Launcher-Agent-Class` 属性，使用 `agentmain` 作为方法名，但起到 `Load-Time Instrumentation` 的效果，且第一个参数字符串总为空
# 转换

JVM 内置了一套 ASM 类库（`jdk.internal.org.objectweb.asm` 可用于辅助修改）

负责转换的类名为 `ClassFileTransformer`，应重写 `transform` 方法，返回 `null` 表示该类不需要被修改。

`addTransformer` 表示注册一个转换器（修改器）。

当使用 `Load-Time` 模式时，虚拟机加载时的所有类都会经过转换器。而使用 `Dynamic` 模式时，使用 `retransformClasses` 可用于标记某个类需要被重新转换，可通过 `Class.forName` 拿到 class 对象。
# 参考

```cardlink
url: https://lsieun.github.io/java-agent/java-agent-01.html
title: "Java Agent 系列一：基础篇"
description: "UP"
host: lsieun.github.io
```
