# 1.8.0

## Lombok @Builder 支持

支持在 Kotlin 与 Java 代码都有的模块中调用 Java 的 Lombok 所生成代码

## Optional 扩展

- `getOrNull()`，`getOrDefault()`，`getOrElse()`
- `toList()`，`toSet()`，`toCollection()`，`asSequence()`

## 可加减的 TimeMark 
#实验性功能 

## 目录递归操作
#实验性功能 

`Path` 支持对目录的递归操作：
- `copyToRecursively()`
- `deleteRecursively()`

## 其他

- `Math.cbrt()` 立方根
- `toDuratonUnit()`，`toTimeUnit()` 可用于与 `TimeUnit` 的转换
- `-Xno-new-java-annotation-targets` 参数用于避免生成 `TYPE_USE` 及 `TYPE_PARAMETER` 注解目标
- `-Xdebug` 参数用于禁用编译器优化
- 删除旧版后端
- 新版 Android 源代码集布局

## KotlinNative

- 支持 Xcode 14.1
- 改进了 Objective-C/Swift 互操作性
- CocoaPods Gradle 插件中默认动态 framework

## KotlinJS

- 稳定版 JS IR 编译器后端
- 报告 yarn.lock 已更新的新设置
- 通过 Gradle 属性添加浏览器测试目标
- 向项目添加 CSS 支持的新方式

## Gradle

- 将 Kotlin 编译器选项暴露为 Gradle 惰性属性
- 提高最低支持版本
- 禁用 Kotlin 守护程序回退策略的能力
- 在传递依赖项中使用最新的 kotlin-stdlib 版本
- 强制检查相关 Kotlin 与 Java 编译任务的 JVM 目标兼容等价性
- 解决 Kotlin Gradle 插件的传递依赖项
- 弃用与删除
# 1.8.20
## K2 编译器

Kotlin K2 编译器已进入 Alpha 测试。可以通过指定语言版本为 2.0 开启。在 `build.groovy` 中设置：

```groovy
kotlin {
    sourceSets.all {
        languageSettings {
            languageVersion="2.0"
        }
    }
}
```

或者，通过编译选项开启：`-language-version 2.0`
## 枚举类 values() 函数替代

实验性特性，使用 `entries` 替代 `values()` 可以提高性能，需要通过以下方法开启：

```groovy
tasks
  .withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask.class)
  .configureEach {
    compilerOptions.languageVersion =
      org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9
  }
```
## 数据对象

实验性特性，使用 `data object` 声明数据对象，创建一个单例的 `data class`，常用于密封类和密封接口
- `toString()` 方法：输出类名
- `equal()` 与 `hashCode()` 方法：同一个数据对象的所有实例（理论上只有一个，但不排除 Java 反序列化等方法产生多个的情况）相等且 hash 相同
- 无 `copy` 与 `componentN` 函数

```kotlin
scaled interface ReadResult

data class Number(val number: Int): ReadResult
data class Text(val text: String): ReadResult
data object EndOfFile: ReadResult
```

需要通过以下方法开启：

```groovy
tasks
  .withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask.class)
  .configureEach {
    compilerOptions.languageVersion =
      org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9
  }
```
## 取消内联类次构造函数次数

实验性特性，`@JvmInline value class` 类允许有次级构造且必须调用 `this` 主构造函数

```kotlin
@JvmInline
value class Person(private val name: String) {

    // 次级构造
    constructor(firstName: String, lastName: String)
    : this("$lastName $firstName") {
        // do something...
    }
}
```

需要通过以下方法开启：

```groovy
tasks
  .withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask.class)
  .configureEach {
    compilerOptions.languageVersion =
      org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9
  }
```
## 新增 Wasm 平台支持

实验性支持 Kotlin/Wasm 平台，支持 WebAssembly 二进制编译，启用方法：

```kotlin
// build.gradle.kts
plugins {
    kotlin("multiplatform") version "1.8.20-RC2"
}

kotlin {
    wasm {
        binaries.executable()
        browser {}
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val wasmMain by getting
        val wasmTest by getting
    }
}
```

另外，浏览器也要开启 WebAssembly 支持
- Chrome 109：`--js-flags=--experimental-wasm-gc`
- Chrome 110+：`chrome://flags/#enable-webassembly-garbage-collection` 开启该特性并重启
- Firefox 109+：`about:config` 开启 `javascript.options.wasm_function_references` 与 `javascript.options.wasm_gc` 特性后重启
- Edge 109+：`--js-flags=--experimental-wasm-gc`
## Java 合成属性

实验性功能：Java 中的属性，在 Kotlin 中也可以使用 `::属性名` 引用

```java
public class Persion {
    public String getName() { ... }
    public String getAge() { ... }
}
```

```kotlin
val persons: List<Person> = listOf(...)

persons.sortedBy(Person::age) ...
```

需要通过以下方法开启：

```groovy
tasks
  .withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask.class)
  .configureEach {
    compilerOptions.languageVersion =
      org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9
  }
```
## 标准库

- `use` 支持 `AutoCloseable` 接口。实验性功能，开启方法（二选一）：
	- `@OptIn(ExperimentalStdlibApi::class)`
	- `-opt-in=kotlin.ExperimentalStdlibApi`
- 支持 Base64 编码，包括 `Base64.Default`，`Base64.UrlSage`。实验性功能，开启方法（二选一）：
	- `@OptIn(ExperimentalStdlibApi::class)`
	- `-opt-in=kotlin.ExperimentalStdlibApi`
- Kotlin/Native 平台支持 `@Volatile`，其语义与 Java `volatile` 相同。实验性功能，需要通过以下方法开启：

```groovy
tasks
  .withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask.class)
  .configureEach {
    compilerOptions.languageVersion =
      org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9
  }
```

- 修复 KT-46211 正则栈溢出错误
- 禁止伴随对象使用 `@Serializer` 接口
	- 若必须使用，在外部对象（设为 T ）使用 `@Serializable(T.Companion::class)`
## Kotlin/Native

- [Kotlin/Native 目标更新](https://book.kotlincn.net/text/whatsnew1820.html#kotlinnative-%E7%9B%AE%E6%A0%87%E6%9B%B4%E6%96%B0)
- [弃用旧版内存管理器](https://book.kotlincn.net/text/whatsnew1820.html#%E5%BC%83%E7%94%A8%E6%97%A7%E7%89%88%E5%86%85%E5%AD%98%E7%AE%A1%E7%90%86%E5%99%A8)
- [使用 @import 指令支持 Objective-C 头文件](https://book.kotlincn.net/text/whatsnew1820.html#%E4%BD%BF%E7%94%A8-import-%E6%8C%87%E4%BB%A4%E6%94%AF%E6%8C%81-objective-c-%E5%A4%B4%E6%96%87%E4%BB%B6)
- [Cocoapods Gradle 插件支持仅链接模式](https://book.kotlincn.net/text/whatsnew1820.html#cocoapods-gradle-%E6%8F%92%E4%BB%B6%E6%94%AF%E6%8C%81%E4%BB%85%E9%93%BE%E6%8E%A5%E6%A8%A1%E5%BC%8F)
- [将 Objective-C 扩展作为 UIKit 的类成员导入](https://book.kotlincn.net/text/whatsnew1820.html#%E5%B0%86-objective-c-%E6%89%A9%E5%B1%95%E4%BD%9C%E4%B8%BA-uikit-%E7%9A%84%E7%B1%BB%E6%88%90%E5%91%98%E5%AF%BC%E5%85%A5)
- [重新实现编译器中的编译器缓存管理](https://book.kotlincn.net/text/whatsnew1820.html#%E9%87%8D%E6%96%B0%E5%AE%9E%E7%8E%B0%E7%BC%96%E8%AF%91%E5%99%A8%E4%B8%AD%E7%9A%84%E7%BC%96%E8%AF%91%E5%99%A8%E7%BC%93%E5%AD%98%E7%AE%A1%E7%90%86)
- [Cocoapods Gradle 插件弃用 `useLibraries()`](https://book.kotlincn.net/text/whatsnew1820.html#cocoapods-gradle-%E6%8F%92%E4%BB%B6%E5%BC%83%E7%94%A8-uselibraries)
## Kotlin 多平台

- [设置源代码集分层结构的新方式](https://book.kotlincn.net/text/whatsnew1820.html#%E6%BA%90%E4%BB%A3%E7%A0%81%E9%9B%86%E5%88%86%E5%B1%82%E7%BB%93%E6%9E%84%E7%9A%84%E6%96%B0%E6%96%B9%E5%BC%8F)
- [Kotlin 多平台支持 Gradle 复合构建预览版](https://book.kotlincn.net/text/whatsnew1820.html#kotlin-%E5%A4%9A%E5%B9%B3%E5%8F%B0%E6%94%AF%E6%8C%81-gradle-%E5%A4%8D%E5%90%88%E6%9E%84%E5%BB%BA%E9%A2%84%E8%A7%88%E7%89%88)
- [改进了 Xcode 中 Gradle 错误的输出](https://book.kotlincn.net/text/whatsnew1820.html#%E6%94%B9%E8%BF%9B%E4%BA%86-xcode-%E4%B8%AD-gradle-%E9%94%99%E8%AF%AF%E7%9A%84%E8%BE%93%E5%87%BA)
## Kotlin/JavaScript

- [删除了 Gradle plugin 中的 Dukat 集成](https://book.kotlincn.net/text/whatsnew1820.html#%E5%88%A0%E9%99%A4%E4%BA%86-gradle-plugin-%E4%B8%AD%E7%9A%84-dukat-%E9%9B%86%E6%88%90)
- [源代码映射中的 Kotlin 变量与函数名称](https://book.kotlincn.net/text/whatsnew1820.html#%E6%BA%90%E4%BB%A3%E7%A0%81%E6%98%A0%E5%B0%84%E4%B8%AD%E7%9A%84-kotlin-%E5%8F%98%E9%87%8F%E4%B8%8E%E5%87%BD%E6%95%B0%E5%90%8D%E7%A7%B0)
- [选择加入 TypeScript 定义文件的生成](https://book.kotlincn.net/text/whatsnew1820.html#%E9%80%89%E6%8B%A9%E5%8A%A0%E5%85%A5-typescript-%E5%AE%9A%E4%B9%89%E6%96%87%E4%BB%B6%E7%9A%84%E7%94%9F%E6%88%90)
## Gradle

- [新的 Gradle 插件版本对齐](https://book.kotlincn.net/text/whatsnew1820.html#%E6%96%B0%E7%9A%84-gradle-%E6%8F%92%E4%BB%B6%E7%89%88%E6%9C%AC%E5%AF%B9%E9%BD%90)
- [Gradle 中默认新版 JVM 增量编译](https://book.kotlincn.net/text/whatsnew1820.html#gradle-%E4%B8%AD%E9%BB%98%E8%AE%A4%E6%96%B0%E7%89%88-jvm-%E5%A2%9E%E9%87%8F%E7%BC%96%E8%AF%91)
- [精确备份编译任务的输出](https://book.kotlincn.net/text/whatsnew1820.html#%E7%B2%BE%E7%A1%AE%E5%A4%87%E4%BB%BD%E7%BC%96%E8%AF%91%E4%BB%BB%E5%8A%A1%E7%9A%84%E8%BE%93%E5%87%BA)
- [为所有 Gradle 版本惰性创建 Kotlin/JVM 任务](https://book.kotlincn.net/text/whatsnew1820.html#%E4%B8%BA%E6%89%80%E6%9C%89-gradle-%E7%89%88%E6%9C%AC%E6%83%B0%E6%80%A7%E5%88%9B%E5%BB%BA-kotlinjvm-%E4%BB%BB%E5%8A%A1)
- [编译任务的 destinationDirectory 支持非默认位置](https://book.kotlincn.net/text/whatsnew1820.html#%E7%BC%96%E8%AF%91%E4%BB%BB%E5%8A%A1%E7%9A%84-destinationdirectory-%E6%94%AF%E6%8C%81%E9%9D%9E%E9%BB%98%E8%AE%A4%E4%BD%8D%E7%BD%AE)
- [能够选择退出向 HTTP 统计服务报告编译器参数](https://book.kotlincn.net/text/whatsnew1820.html#%E8%83%BD%E5%A4%9F%E9%80%89%E6%8B%A9%E9%80%80%E5%87%BA%E5%90%91-http-%E7%BB%9F%E8%AE%A1%E6%9C%8D%E5%8A%A1%E6%8A%A5%E5%91%8A%E7%BC%96%E8%AF%91%E5%99%A8%E5%8F%82%E6%95%B0)