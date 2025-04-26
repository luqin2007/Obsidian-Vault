# 1.9
## K2 编译器

JVM 的 K2 编译器进入 Beta 阶段。现在 K2 编译器支持多平台了。

> [!Warning] 即使指定了 `languageVersion=2.0`，Android kapt 插件仍会自动修改成 1.9

指定 languageVersion 为 2.0，设置以下编译参数即可

```shell
./gradle assemble -Pkotlin.experimental.tryK2=true
```
## 以下新特性转正

- [[Kotlin 1.6#Duration API]] 已完全稳定
- [[Kotlin 1.7#..< 创建前闭后开区间]]
- [[Kotlin 1.8#1.8.20#数据对象]]
- [[Kotlin 1.8#1.8.20#取消内联类次构造函数次数]]
- [[Kotlin 1.8#1.8.20#枚举类 values() 函数替代]]
## @JvmDefault 禁用

现在 `@JvmDefault` 注解将会引发异常，应当使用 `-Xjvm-default`，`JvmDefaultWithoutCompatibility` 和 `JvmDefaultWithCompatibility` 控制
## 通用正则

```kotlin
fun main() {
    val regex = """\b(?<city>[A-Za-z\s]+),\s(?<state>[A-Z]{2}):\s(?<areaCode>[0-9]{3})\b""".toRegex()
    val input = "Coordinates: Austin, TX: 123"

    val match = regex.find(input)!!
    println(match.groups["city"]?.value)
    // Austin
    println(match.groups["state"]?.value)
    // TX
    println(match.groups["areaCode"]?.value)
    // 123
}
```
## 16 进制解析

```kotlin
val macAddress = "001b638445e6".hexToByteArray()

// Use HexFormat{} builder to separate the hexadecimal string by colons
println(macAddress.toHexString(HexFormat { bytes.byteSeparator = ":" }))
// "00:1b:63:84:45:e6"

// Use HexFormat{} builder to:
// * Make the hexadecimal string uppercase
// * Group the bytes in pairs
// * Separate by periods
val threeGroupFormat = HexFormat { upperCase = true; bytes.bytesPerGroup = 2; bytes.groupSeparator = "." }

println(macAddress.toHexString(threeGroupFormat))
// "001B.6384.45E6"

// Use HexFormat{} builder to parse a hexadecimal that has prefix: "0x".
println("0x3a".hexToInt(HexFormat { number.prefix = "0x" })) // "58"
```
## Kotlin/Native

- [Preview of custom memory allocator](https://book.kotlincn.net/text/whatsnew19.html#preview-of-custom-memory-allocator)
- [Objective-C or Swift object deallocation hook on the main thread](https://book.kotlincn.net/text/whatsnew19.html#objective-c-or-swift-object-deallocation-hook-on-the-main-thread)
- [No object initialization when accessing constant values in Kotlin/Native](https://book.kotlincn.net/text/whatsnew19.html#no-object-initialization-when-accessing-constant-values-in-kotlinnative)
- [Ability to configure standalone mode for iOS simulator tests](https://book.kotlincn.net/text/whatsnew19.html#ability-to-configure-standalone-mode-for-ios-simulator-tests-in-kotlinnative)
- [Library linkage in Kotlin/Native](https://book.kotlincn.net/text/whatsnew19.html#library-linkage-in-kotlinnative)
- Kotlin/Native 标准库更新
- `@Volatile` 支持更多平台
## Kotlin Multiplatform

- [Changes to Android target support](https://book.kotlincn.net/text/whatsnew19.html#changes-to-android-target-support)
- [New Android source set layout enabled by default](https://book.kotlincn.net/text/whatsnew19.html#new-android-source-set-layout-enabled-by-default)
- [Preview of the Gradle configuration cache in multiplatform projects](https://book.kotlincn.net/text/whatsnew19.html#preview-of-the-gradle-configuration-cache)
## Kotlin/Wasm

[Kotlin 1.9.0 · Kotlin 官方文档 中文版 (kotlincn.net)](https://book.kotlincn.net/text/whatsnew19.html)
## Kotlin/JS

- [Removal of the old Kotlin/JS compiler](https://book.kotlincn.net/text/whatsnew19.html#removal-of-the-old-kotlinjs-compiler)
- [Deprecation of the Kotlin/JS Gradle plugin](https://book.kotlincn.net/text/whatsnew19.html#deprecation-of-the-kotlinjs-gradle-plugin)
- [Deprecation of external enum](https://book.kotlincn.net/text/whatsnew19.html#deprecation-of-external-enum)
- [Experimental support for ES6 classes and modules](https://book.kotlincn.net/text/whatsnew19.html#experimental-support-for-es6-classes-and-modules)
- [Changed default destination of JS production distribution](https://book.kotlincn.net/text/whatsnew19.html#changed-default-destination-of-js-production-distribution)
- [Extract org.w3c declarations from stdlib-js](https://book.kotlincn.net/text/whatsnew19.html#extract-orgw3c-declarations-from-stdlib-js)
## Gradle

- [Removed classpath property](https://book.kotlincn.net/text/whatsnew19.html#removed-classpath-property)
- [New Gradle compiler options](https://book.kotlincn.net/text/whatsnew19.html#new-compiler-options)
- [Project-level compiler options for Kotlin/JVM](https://book.kotlincn.net/text/whatsnew19.html#project-level-compiler-options-for-kotlinjvm)
- [Compiler option for Kotlin/Native module name](https://book.kotlincn.net/text/whatsnew19.html#compiler-option-for-kotlinnative-module-name)
- [Separate compiler plugins for official Kotlin libraries](https://book.kotlincn.net/text/whatsnew19.html#separate-compiler-plugins-for-official-kotlin-libraries)
- [Incremented minimum supported version](https://book.kotlincn.net/text/whatsnew19.html#incremented-minimum-supported-version)
- [kapt doesn't cause eager task creation](https://book.kotlincn.net/text/whatsnew19.html#kapt-doesnt-cause-eager-task-creation-in-gradle)
- [Programmatic configuration of the JVM target validation mode](https://book.kotlincn.net/text/whatsnew19.html#programmatic-configuration-of-the-jvm-target-validation-mode)
# 1.9.20