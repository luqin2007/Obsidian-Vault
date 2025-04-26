# 1.7.0

## 委托实现内联类的内联值

现已允许内联类使用委托

```kotlin
@JvmInline
value class BarWrapper(val bar: Bar): Bar by bar
```

## 下划线用于泛型

当有多个泛型时，若有一个或几个泛型变量需要指定，其余可以用 `_` 代替让编译器自动推断

```kotlin
abstract class SomeClass<T> {
    abstract fun execute(): T
}

class SomeClassImpl1: SomeClass<String>() {
    override fun execute() = "Test"
}

class SomeClassImpl2: SomeClass<Int>() {
    override fun execute() = 42
}

inline fun <reified S: SomeClass<T>, T> run(): T {
    return S::class.constructors.first().call().execute()
}

fun main() {
    val a = run<SomeClassImpl1, _>() // run<SomeClassImpl1, String>
    val b = run<SomeClassImpl2, _>() // run<SomeClassImpl2, Int>
}
```

## 绝对不可空类型

使用 `&` 可以定义绝对不可空类型，用于泛型中将可能为可空类型的泛型类型转换为不可空类型，`&` 之前为一个可空类型，之后为 `Any`：

```kotlin
// 当 T 为可空类型 M? 时，T & Any 为不可空类型 M
fun <T> elvisLike(x: T, y: T & Any): T & Any = x ?: y
```

## 函数式接口构造函数可调用引用

通过用 `fun` 修饰只有一个抽象函数的 `interface`，类似以下：

```kotlin
fun interface AnInterface {
    fun sayHello()
}
```

其相当于

```kotlin
interface AnInterface {
    fun sayHello()
}

fun AnInterface(block: () -> Unit): AnInterface = object : AnInterface {
    override fun sayHello() = block()
}
```

例如：

```kotlin
fun interface Printer {
    fun print()
}

fun foo() {
    // 通过类似构造的方法创建接口对象
    val p = Printer { 
        println("Hello")
    }

	// 或者 调用函数
    addPrinter {
        println("HHello")
    }
}

fun addPrinter(p: Printer) {
    // do something
}


```

## Regex 特定位置匹配

- `Boolean Regex.matchAt()`
- `Regex.matchesAt()`

```kotlin
fun main() {
    val releaseText = "Kotlin 1.7.0 is on its way!"
    // regular expression: one digit, dot, one digit, dot, one or more digits
    val versionRegex = "\\d[.]\\d[.]\\d+".toRegex()

    println(versionRegex.matchesAt(releaseText, 0)) // "false"
    println(versionRegex.matchesAt(releaseText, 7)) // "true"

    println(versionRegex.matchAt(releaseText, 0)) // "null"
    println(versionRegex.matchAt(releaseText, 7)?.value) // "1.7.0"
}
```

## 反射访问注解

```kotlin
import kotlin.reflect.KAnnotatedElement

@Repeatable
annotation class Tag(val name: String)

@Tag("First Tag")
@Tag("Second Tag")
fun taggedFunction() {
    println("I'm a tagged function!")
}

fun main() {
    val x = ::taggedFunction
    val foo = x as KAnnotatedElement
    println(foo.findAnnotations<Tag>()) // [@Tag(name=First Tag), @Tag(name=Second Tag)]
}
```

## 深递归函数

使用 `DeepRecursiveFunction` 创建深递归函数，可定义一个将堆栈保存在堆上的函数，以便于深入的递归计算，以免触发 StackOverflowError

```kotlin
import kotlin.math.max  
  
class Tree(val left: Tree?, val right: Tree?)  
  
val depth = DeepRecursiveFunction<Tree?, Int> {  
    return@DeepRecursiveFunction if (it == null)
        0
    else
        // 使用 callRecursive 相当于递归调用
        max(callRecursive(it.left), callRecursive(it.right)) + 1  
}
```

## 内联类时间源标记

`TimeSource.Monotonic` 的 `markNow()`，`elapsedNow()`，`measureTime()`，`measureTimedValue()` 等函数返回的 `TimeMark` 已被替换成内联类

```kotlin
@OptIn(ExperimentalTime::class)
fun main() {
    val mark = TimeSource.Monotonic.markNow() // Returned `TimeMark` is inline class
    val elapsedDuration = mark.elapsedNow()
}
```

## Optional 扩展

[[Kotlin 1.8#1.8.0#Optional 扩展]]

## 其他

- 集合 `max()`，`min()` 等返回不可空类型，新增 `*OrNull` 版本相关函数
- 不再需要 `-opt-in=kotlin.RequiresOptIn` 抑制不稳定功能 warn 提示
- 新增 `-Xjdk-release` 参数相当于 Java 编译时使用 `--release`
- 移除编译目标版本 1.6

## Gradle

- 增量编译的新方式
- 用于跟踪编译器性能的新版构建报告
- Gradle 与 Android Gradle 插件最低支持版本的变更
- 支持 Gradle 插件变体
- Kotlin Gradle 插件 API 的更新
- 通过插件 API 提供 sam-with-receiver 插件
- 编译任务的变更
- kapt 中每个注解处理器生成文件的新版统计信息
- 弃用 kotlin.compiler.execution.strategy 系统属性
- 删除弃用的选项、方法与插件

## Kotlin 多平台

- 正则命名捕获组已支持 JS 和 Native 平台

### KotlinNative

- 新版内存管理器的性能改进
- 与 JVM 及 JS IR 后端统一编译器插件 ABI
- 支持独立的 Android 可执行文件
- 与 Swift async/await 互操作：返回 Void 而不是 KotlinUnit
- 通过 Objective-C 桥接禁止未声明的异常
- 改进了 CocoaPods 集成
- 覆盖了 Kotlin/Native 编译器下载 URL

### NativeJS

- 新版 IR 后端的性能改进
- 使用 IR 时的成员名缩短
- 通过 IR 后端的 polyfill 支持旧版浏览器
- 从 js 表达式动态加载 JavaScript 模块
- 为 JavaScript 测试运行器指定环境变量

# 1.7.20

## ..< 创建前闭后开区间
#实验性功能 

新增 `..<` 运算符用于创建前闭后开区间，类似 `until`，可用于 `when` 等，返回值实现了 `OpenEndRange<T>` 和 `ClosedRange<T>`

```kotlin
@OptIn(ExperimentalStdlibApi::class)
fun foo(value: Double) {
    when (value) {
        in 0.00 ..< 0.25 -> // ...
        in 0.25 ..< 0.50 -> // ...
        in 0.50 ..< 0.75 -> // ...
        in 0.75 ..< 1.00 -> // ...
    }
}
```

启用：
- 增加 `@OptIn(ExperimentalStdlibApi::class)` 注解或 `-opt-in=kotlin.ExperimentalStdlibApi` 编译选项
- 开启 `-language-version 1.8` 编译选项

## 数据对象
#实验性功能 

使用 `data object` 创建数据对象，相对于普通对象有更好的 `toString()` 输出，需要开启 `-language-version 1.9` 编译选项

```groovy
compileKotlin {
    kotlinOptions.languageVersion = '1.9'
}
```

```kotlin
package org.example
object MyObject
data object MyDataObject

fun main() {
    println(MyObject) // org.example.MyObject@1f32e575
    println(MyDataObject) // MyDataObject
}
```

## 泛型内联类
#实验性功能 

允许内联类使用泛型，此时接收端接收的类型将被编译成泛型上界或 `Any` 或 `Any?`

```kotlin
inline class Box1<T>(val value: T)

inline class Box2<T: Comparable<T>>(val value: T)

// public static final void foo1_uCLU58Q(@NotNull Object value)
fun foo1(value: Box1<String>) {}

// public static final void foo2_6DXwiX0(@NotNull Comparable value)
fun foo2(value: Box2<String>) {}
```

## 更多代理

- 对象类，需要实现 `getValue` 运算符

```kotlin
import kotlin.reflect.KProperty

object NamedObject {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = property.name
}

val a: String by NamedObject
```

- 实现 `ReadOnlyProperty` 及其子类的常量

```kotlin
import kotlin.properties.ReadOnlyProperty

val impl = ReadOnlyProperty<Any?, String> { _, property -> property.name }

val s: String by impl
```

- 常量表达式，包括任意常量表达式，枚举，`this` 或 `null`，但返回对象需要实现 `getValue` 运算符

```kotlin
import kotlin.reflect.KProperty

class A {
    operator fun getValue(thisRef: Any?, property: KProperty<*>) = property.name
    
    val a by this
}
```

## 其他

- 支持 Kotlin K2 编译器插件，使用 `-Xuse-k2` 或 `compileKotlin { kotlinOptions.useK2 = truekotlinOptions.useK2 = true }` 开启
- 集合构建器在需要访问集合中的元素时，需要指定泛型类型
- `Path` 类型支持 `walk()` 一类函数及 `FileVisitor`

## KotlinNative

- 新的默认内存管理器
- 自定义 Info.plist 文件
