# 1.4.0

## SAM 转换

Single Abstract Method，单抽象方法转换，当 Java 类或接口只有一个未实现方法时，可通过 lambda 转换

```kotlin
fun interface IntPredicate {  
    fun accept(i: Int): Boolean  
}  
  
val isEvent = IntPredicate { it % 2 == 0 }
```

## 异常 API

- `Throwable.stackTraceToString()`：将异常信息和异常栈生成到 String
- `Throwable.addSuppressed()`，`Throwable.suppressedExceptions`：
- `@Throws`：注解，标记当编译成 Java 或 Native 时应当检查的异常

## 数组与集合

- `setOfNonNull()`：创建一个 Set，忽略其中的 `null`
- `shuffled()`：生成乱序迭代
- `*Indexed()`：带有该后缀的函数版本，传入的 lambda 第一个参数包含索引
- `*OrNull()`：当集合空或无结果时返回 `null`；不带该后缀的版本则会抛出异常
- `runningFold()`

- `ByteArray.decodeToString()`，`String.encodeToByteArray()`
- `CharArray.concatToString()`，`String.toCharArray()`

- `ArrayDeque` 双端序列，在两端常数时间插入删除
	- `addFirst()`，`addLast()`，`removeFirst()`，`removeLast()`
	- `first()`，`last()`

## 字符串

- StringBuilder：`set()`，`setRange()`，`deleteAt()`，`deleteRange()`，`appendRange()`，`insertRange()`
- Appendable：`appendLine()` 替代 `appendln()`

## 位操作

- `countOneBits()`，`countLeadingZeroBits()`，`countTrailingZeroBits()`
- `takeHightestOneBit()`，`takeLowestOneBit()`
- `rotateLeft()`，`rotateRight()`

## 委托

- 新增 `PropertyDelegateProvider` 接口用于创建委托提供器
- `ReadWriteProperty` 继承自 `ReadOnlyProperty`

## 其他

- 可混用具名参数与位置参数传参
- 允许拖尾 `,`：在传参，数组等允许多个值的地方，可在末尾保留一个 `,`
- lambda 表达式传入函数引用时，支持识别（忽略）参数默认值
- lambda 表达式传入函数引用时，支持可变参数
- `when` 中支持使用 `continue` 和 `break` 控制外部循环
- 面向库作者的显式 API 模式
- 新的 KotlinJava，KotlinJs，KotlinNative 编译器
- `KType.javaType` 可以获取对应的 `java.lang.reflect.Type`

# 1.4.20

## Path

[[Kotlin 1.5#1.5.0#Path API]]

## 其他

- 已支持 Java 15
- 使用 `invokedynamic` 完成字符串连接
- `String.replace()` 性能提升
- Kotlin Android：`kotlin-parcelize` 生成器插件与 `@Parcelize` 注解

## Kotlin 多平台

### KotlinJS

`commonWebpackConfig` 配置 `webpack`，`customField` 配置 `package.json`，选择性 `yarn` 依赖解析

### KotlinNative

逃逸分析，性能提升，XCode 12 库支持等

# 1.4.30

## 区域无关大小写转换

[[Kotlin 1.5#1.5.0#区域无关大小写转换]]

## 字符码值与数位转换

[[Kotlin 1.5#1.5.0#字符码值与数位转换]]

## 记录类

[[Kotlin 1.5#1.5.0#记录类]]

## 密封类

[[Kotlin 1.5#1.5.0#密封类#密封接口]]
[[Kotlin 1.5#1.5.0#密封类#包范围的密封类层次]]

## 其他

- 密封类可以在包内的任意文件中
- JVM IR 编译器 Beta
- KotlinNative 支持 XCode 12.2 库
- KotlinJS 顶层属性延迟初始化
- `@Serializable` 序列化支持内联类和无符号类型
