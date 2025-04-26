# 1.6.0

## when 语句警告提示

对于密封类、枚举和 Boolean 类型，若 `when` 语句没有覆盖所有类型和值，编译器会有异常或警告提示

## suspend 超类型

允许将 `suspend` 修饰的函数类型作为超类
- 超类中不能同时具有 `suspend` 函数类型与普通函数类型
- 只能有一个 `suspend` 函数类型

```kotlin
class MyClass: suspend () -> Unit {  
    override suspend fun invoke() {  
        TODO("Not yet implemented")  
    }  
}
```

## suspend 转化

```kotlin
fun getSuspending(suspending: suspend () -> Unit) {}

fun suspending() {}

fun foo(regular: () -> Unit) {
    getSuspending {}              // ok
    getSuspending(::suspending)   // ok
    getSuspending(regular)        // ok
}
```

## 注解类型实例化

允许在任意位置调用注解类构造函数实例化注解类型对象

```kotlin
annotation class AnnoClass(val info: String)  
  
fun foo() {  
    val obj = AnnoClass("abc")  
}
```

## 允许泛型注解

允许 `AnnotationTarget.TYPE_PARAMETER` 修饰的注解修饰泛型参数

```kotlin
@Target(AnnotationTarget.TYPE_PARAMETER)
annotation class BoxContent

class Box<@BoxContent T>
```

## 可重复注解

使用 `@Repeatable` 修饰的注解类允许重复注解，会自动创建对应的 `Container` 嵌套注解类

```kotlin
// 自动创建 @RepeatableAnnotation.Container 容器类
@Repeatable  
annotation class RepeatableAnnotation(val info: String)  
  
@RepeatableAnnotation("A")  
@RepeatableAnnotation("B")  
@RepeatableAnnotation("C")  
class AClass
```

重复注解可通过 `KAnnotatedElement.findAnnotations()` 方法反射获得。

若要创建自定义容器，通过 `@JvmRepeatable` 定义

```kotlin
@JvmRepeatable(Tags::class)
annotation class Tag(val name: String)

annotation class Tags(val value: Array<Tag>)
```

## typeOf

通过 `typeOf()` 可以获取任意类型的 KClass 对象

```kotlin
inline fun <reified T> renderType(): String {
    val type = typeOf<T>()
    return type.toString()
}

fun main() {
    val fromExplicitType = typeOf<Int>()
    val fromReifiedType = renderType<List<Int>>()
}
```

## Duration API

- 已部分稳定
- 内部数据使用 `Long` 替代 `Double`
- 增加 `Duration.seconds(Int)` 等一系列构建方法，`Int.seconds` 等扩展方法仍保留，但位于 `Duration` 中

- `toComponents()` 接受的函数第一个值类型为 `long`
- `DurationUnit` 不再是 `java.util.concurrent.TimeUnit` 的别名

### toString() 输出变更

- 秒及以上单位，若可以用更大的单位表示，换算成更大的单位
- 秒及以上单位，若带有小数，将小数换算成更小的单位
- 秒以下单位，如 `ms`，`us`，`ns`，可以带有小数且不再进行舍入
- 带有负号时，如果输出多节，带有括号，否则不带，如 `-(1h 30min)` 和 `-12m`

| 方法                                    | 原输出 | 现输出     |
| --------------------------------------- | ------ | ---------- |
| Duration.days(45).toString()            | 45.0d  | 45d        |
| Duration.days(1.5).toString()           | 36.0h  | 1d 12h     |
| Duration.minutes(1230).toString()       | 20.5h  | 20h 30m    |
| Duration.minutes(2415).toString()       | 40.3h  | 1d 16h 15m |
| Duration.minutes(920).toString()        | 920m   | 15h 20m    |
| Duration.seconds(1.546).toString()      | 1.55s  | 1.546s     |
| Duration.milliseconds(25.12).toString() | 25.1ms | 25.12ms    | 

### 从 String 解析 Duration

- `Duration.parse(String)`：形如 `Duration.toString()`，`Duration.toIsoString()` 输出的格式
	- `Duration.parseOrNull()`
- `Duration.parseIsoString()`：形如 `Duration.toIsoString()` 输出的格式

## Regex 拆分序列

相当于一个延迟计算的根据给定正则的 `split()`
- `Regex.splitToSequence()`
- `String.splitToSequence()`

## 其他

- 新的 `readln()` 与 `readlnOrNull` 函数取代 `readLine()`
- `buildMap()`，`buildList()`，`buildSet()` 等集合构建器已稳定 
- 整形对象的 `rotateRight()` 和 `rotateLeft()` 方法可实现整型循环移位运算
- 可以在任意系统上编译 Windows 程序
- LLVM 与链接器更新
- klib 链接失败的详细错误信息

# 1.6.20

## 上下文接收者原型
#实验性功能 

实验性功能，通过 `-Xcontext-receivers` 开启

Kotlin 不再限制只有一个接收者。通过 `context` 可以为函数、属性甚至是类声明上下文，声明后调用者需要（隐式）通过 `implicit` 确定上下文

```kotlin
import java.util.logging.Logger  
  
interface LoggingContext {  
    val log: Logger  
}  
  
context(LoggingContext)  
fun start() {  
    // do something  
    // 'this' is LoggingContext}  
  
fun test(ctx: LoggingContext) {  
    with(ctx) {  
        start()  
    }  
}
```

## 绝对不可空类型

[[Kotlin 1.7#1.7.0#绝对不可空类型]]

## 函数式接口构造函数可调用引用

[[Kotlin 1.7#1.7.0#函数式接口构造函数可调用引用]]

## JVM 单个模块并行编译
#实验性功能 

通过并行编译，可将编译效率提升 15% 左右，通过 `-Xbackend-threads n` 参数开启，n 可选为：
- 数字：并行线程个数
- 0：根据 CPU 核心数判断

注意：
- 不兼容 kapt
- 需要更多内存

## @JvmDefaultWithCompatibility 注解

向接口类添加 `@JvmDefaultWithCompatibility`，并与 `-Xjvm-default=all` 参数共同使用时，可为接口中所有非抽象成员添加 JVM 的 `default` 方法

## KotlinNative

- 新版内存管理器的更新
- 新版内存管理器中清除阶段的并发实现
- 注解类的实例化
- 与 Swift async/await 互操作：返回 Void 而不是 KotlinUnit
- 使用 libbacktrace 实现更佳的堆栈跟踪
- 支持独立的 Android 可执行文件
- 性能提升
- 改进了 cinterop 模块导入过程中的错误处理
- 对 Xcode 13 库的支持

## KotlinJS

- 开发版二进制文件的增量编译（IR）
- 时顶层属性默认惰性初始化（IR）
- 模块默认采用分开的 JS 文件（IR）
- Char 类优化（IR）
- 导出改进（IR 与旧版后端）
- 异步测试的 @AfterTest 保证
- Kotlin/JS Gradle 项目持久化 yarn.lock
- 默认使用 --ignore-scripts 安装 npm 依赖
