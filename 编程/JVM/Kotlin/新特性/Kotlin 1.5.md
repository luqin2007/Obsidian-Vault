# 1.5.0
## 记录类

使用 `@JvmRecord` 注解数据类可使其使用 Java 记录类

```kotlin
@JvmRecord  
data class User(val name: String, val age: Int)
```

## 密封类

### 密封接口

接口允许使用 `sealed` 修饰，与修饰类类似，编译器在编译时便已知所有继承类

```kotlin
sealed interface Polygon
```

密封类可用于 `when` 分类

```kotlin
fun draw(polygon: Polygon) = when (polygon) {
    is Rectangle -> //...
    is Triangle -> //...
}
```

一个类可以实现多个密封类接口

### 包范围的密封类层次

密封类现在可以与父类在同一个包中，而不必要在同一文件中

## 内联类

有些时候我们需要对某些类型创建包装器，但会造成额外的内存开销和运行时开销，若被包装的数据时原生类型也会使大量编译器优化失效。

Kotlin 引入内联类用于包装类，在生成代码时，使用内联类的位置 Kotlin 会尽可能使用内部包含的元素去替换包装类，在不必要的情况下不会实际创建包装类对象。一般来说，只要将内联类用于另一种类型，他们就会被装箱（生成包装类对象）

内联类使用 `value` 声明，使用 `@JvmInline` 注解，构造函数有且只有一个对象表示内联的对象

```kotlin
@JvmInline  
value class Password(private val s: String)
```

内联类允许有属性和函数，以及 `init` 代码块，但属性不允许有 `field`，不允许有 `lateinit` 和代理属性

```kotlin
@JvmInline  
value class Name(private val s: String) {  

    init {  
        require(s.isNotBlank())  
    }  
    
    val length get() = s.length  
    
    fun greet() {  
        println("Hello $s")  
    }  
}
```

内联类允许实现接口，但不允许继承其他类，且内联类始终都是 `final` 的

涉及到函数重载时，由于内联类会被自动替换为原始类型，会在函数名后增加一个 hash 值；若需要与其他语言（Java 等）共同使用，应使用 `@JvmName` 为其重命名

```kotlin
@JvmInline  
value class Name(private val s: String)  
  
fun call(name: Name) {  
    println("Call name $name")  
}  
  
fun call(name: String) {  
    println("Call string $name")  
}  
  
@JvmName("callName")  
fun call2(name: Name) {  
    println("Call name $name")  
}  
  
fun call2(name: String) {  
    println("Call string $name")  
}
```

反编译生成 Java 代码后为

```java
public static final void call_75PUH38/* $FF was: call-75PUH38*/(@NotNull String name) {  
   Intrinsics.checkNotNullParameter(name, "name");  
   String var1 = "Call name " + Name.toString-impl(name);  
   System.out.println(var1);  
}  

public static final void call(@NotNull String name) {  
   Intrinsics.checkNotNullParameter(name, "name");  
   String var1 = "Call string " + name;  
   System.out.println(var1);  
}  

@JvmName(  
   name = "callName"  
)  
public static final void callName(@NotNull String name) {  
   Intrinsics.checkNotNullParameter(name, "name");  
   String var1 = "Call name " + Name.toString-impl(name);  
   System.out.println(var1);  
}  

public static final void call2(@NotNull String name) {  
   Intrinsics.checkNotNullParameter(name, "name");  
   String var1 = "Call string " + name;  
   System.out.println(var1);  
}
```

从某些方面来说，内联类很像类型别名，但内联类会创建新类型

## 无符号类型

| 类型   | 说明            | 范围          |
| ------ | --------------- | ------------- |
| UByte  | 8 位无符号整数  | $[0, 255]$    |
| UShort | 16 位无符号整数 | $[0, 65535]$  |
| UInt   | 32 位无符号整数 | $[0, 2^{32})$ | 
| ULong  | 64 位无符号整数 | $[0, 2^{64})$ |

无符号整数实质上是内联类，支持大多数有符号整数的运算

| 类型        | 说明        |
| ----------- | ----------- |
| UByteArray  | UByte 数组  |
| UShortArray | UShort 数组 |
| UIntArray   | UInt 数组   |
| ULongArray  | ULong 数组  | 

与此类似的，还有 `UIntRange`，`UIntProgression`，`ULongRange`，`ULongProgression` 等类型，类似原类型一样都没有开装箱开销

无符号整型的字面量使用 `u/U`，`ul/UL` 为后缀，表示 `UInt`，`ULong` 类型

## 区域无关大小写转换

| 旧版本                | 新版本                                     |
| --------------------- | ------------------------------------------ |
| String.toUpperCase()  | String.uppercase()                         |
| String.toLowerCase()  | String.lowercase()                         |
| String.capitalize()   | String.replaceFirstChar { it.uppercase() } |
| String.decapitalize() | String.replaceFirstChar { it.lowercase() } |
| Char.toUpperCase()    | Char.uppercase()                           |
| Char.toLowerCase()    | Char.lowercase()                           |
| Char.toTitleCase()    | Char.titlecaseChar()/Char.titlecase()      | 

## 字符码值与数位转换

- `Char(code: Int)`
- `Char(code: UShort)`
- `Char.code`

- `Char.digitToInt(radix: Int)`
- `Char.digitToIntOrNull(radix: Int)`
- `Int.digitToChar(radix: Int)`

## Path API

`java.nio.file.Path` 的 API 通过 `kotlin.io` 包扩展后类似 `java.io.File`

## Duration API

[[Kotlin 1.6#1.6.0#Duration API]]

## 字符判断

以下用于字符类型判断的函数已对多平台可用
- `Char.isDigit()`，`Char.isLetter()`，`Char.isLetterOrDigit()`
- `Char.isLowerCase()`，`Char.isUpperCase()`，`Char.isTitleCase()`
- `Char.isDefined()`，`Char.isISOControl()`
- `Char.category: CharCategory`

## kotlin-test

### 测试库

- JVM：`kotlin-test-junit`
- JS：`kotlin-test-js`
- `kotlin-test-common`，`kotlin-test-annotations-common`

### KotlinJVM 配置

```groovy
// JUnit 4
kotlin {
    sourceSets {
        commonTest {
            dependencies {
                implementation kotlin("test")
            }
        }
    }
}

// JUnit 5
tasks {
    test {
        useTestNG()
        useJUnitPlatform()
    }
}
```

### 断言更新

- 值类型：`assertIs<T>(value)`
- 数组和其他可迭代内容：`assertContentEquals(iter1, iter2)`
- 对 `Double` 和 `Float` 的 `assertEquals()`，`assertNotEquals` 重载
- 检查集合元素：`assertContains(collection, value)`
- `assertTrue()`，`assertFalse()`，`expect()` 已内联

## 其他

- 默认编译目标为 Java8
- `@JvmDefault` 已弃用
- 稳定的 JVM IR 后端
- SAM 适配器和 lambda 使用 `invokedynamic` 实现

# 1.5.20

## invokedynamic 字符串连接

## JSpecify 可空注解支持

支持 `org.jspecify.nullness` 注解

## Lombok 支持

[[Kotlin 1.8#Lombok @Builder 支持]]

# 1.5.30

## when 语句警告提示

[[Kotlin 1.6#1.6.0#when 语句警告提示]]

## suspend 超类型

[[Kotlin 1.6#1.6.0#suspend 超类型]]

## 隐式实使用验性 API 要求选择性加入
#实验性功能 

Kotlin 允许使用 `@RequiresOptIn` 创建实验性功能标记注解。

对于隐式使用标记有实验性 API 的地方，现在也要求通过 `OptIn` 选择加入

![[Pasted image 20230115105228.png]]

```kotlin
// 实验性注解标记
@RequiresOptIn(message = "This API is experimental.")  
@Retention(AnnotationRetention.BINARY)  
@Target(AnnotationTarget.CLASS)  
annotation class ExpAnno  

// 实验性 API
@ExpAnno  
class ExpType  

// 选择性加入
@OptIn(ExpAnno::class)  
fun create(): ExpType {  
    return ExpType()  
}
```

## 注解类型实例化

[[Kotlin 1.6#1.6.0#注解类型实例化]]

## Duration API

[[Kotlin 1.6#1.6.0#Duration API#toString() 输出变更]]
[[Kotlin 1.6#1.6.0#Duration API#从 String 解析 Duration]]

## Regex

[[Kotlin 1.6#1.6.0#Regex 拆分序列]]
[[Kotlin 1.7#1.7.0#Regex 特定位置匹配]]

## 其他

- 禁止注解属性的 `get()` 和 `field`
- 改进递归泛型的类型推断
- 解除生成器推断中推断类型限制，如 `buildList {}` 中允许使用 `get()`
- 可空类型注解配置支持：`-Xnullability-annotations=@<pkg-name>:<level>`
- Gradle：支持 Java toolchains：以下方法二选一都行
	- `kotlin { jvmToolchain { ... } }` 
	- `java { jvmToolchain { ... } }`
- 使用 `UsesKotlinJavaToolchain` 接口指定 JDK home

```groovy
project.tasks
    .withType(UsesKotlinJavaToolchain.class)
    .configureEach {
        it.kotlinJavaToolchain.jdk.use(
            '/path/to/local/jdk',
            JavaVersion.<LOCAL_JDK_VERSION>
        )
    }
```



## Kotlin 多平台

- Apple silicon 支持
- 改进 CocoaPods Gradle 插件 DSL
- Swift 5.5 async/await 实验性互操作
- 改进对象与伴生对象的 Swift/Objective-C 映射
- 对于 MinGW 目标弃用了链接到 DLL 而未导入库的用法
- 在共享的原生代码中使用自定义 `cinterop` 库
- 对 `XCFrameworks` 的支持
- Android 构件的新版默认发布设置
- JS IR 编译器后端达到 Beta 版
- 为使用 Kotlin/JS IR 后端的应用程序提供更好的调试体验
