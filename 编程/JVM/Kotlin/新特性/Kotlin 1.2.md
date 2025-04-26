# 注解数组字面量

注解中数组现在可以用 `[]` 直接创建，不需要 `arrayOf` 了

```kotlin
@AnAnnonation(values = [1, 2, 3])
```

# lateinit

> `lateinit`：修饰一个 `var` 属性，允许该属性在声明时不初始化，但需要手动保证在使用前初始化完成

- `lateinit` 关键字现在可以修饰顶层属性和局部变量
- `lateinit` 创建的变量增加 `isInitialized` 属性用于判断是否已经初始化

# inline

- 内联函数现允许带有默认参数

# 强制转换类型推断

- 显式类型转换的类型信息现可以用于类型转换

```kotlin
fun <T> get(): T {  
    // do something  
}

val value = get() as String
```

# 智能转换

- 智能转换现允许用于 lambda 表达式中的局部变量，但这些变量仅在 lambda 表达式之前修改
- 当一个变量有安全调用表达式与空检测赋值时，其智能转换现在也可以应用于安全调用接收者：

```kotlin
fun f(s: Any) {
    val firstChar = (s as? CharSequence)?.firstOrNull();
    if (firstChar != null) {
        // do something
        // 这里，s 会被智能转换成 CharSequence
    }
}
```

# this 引用简写

`this::foo` 现在可以简写成 `::foo`，省略掉 `this`

# 具名不定参数传参变更

为了与注解中的数组字面值保持一致，向一个具名参数形式的 vararg 参数传入单个项目的用法 `foo(items = 1)` 已被弃用。使用伸展操作符 `*` 连同相应的数组工厂函数：

```kotlin
fun foo(vararg items: Any) {
    // do something...
}

foo(items = *arrayOf(...))
```

# 其他特性

- 禁止在 `getter` 中修改只读属性的 `field` 字段
- 禁止继承自 `Throwable` 的泛型类的内部类
- 禁用 `try` 块中的赋值语句用于块后的智能转换，以防破坏类型安全与空安全并引发运行时故障
- 移除数据类型的 `copy` 方法

# 标准库

## 模块兼容

为兼容 Java 9 模块系统，
- 引入 `kotlin-stdlib-jdk7`  与 `kotlin-stdlib-jdk8` 取代 `kotlin-stdlib-jre7` 与 `kotlin-stdlib-jre8`，新构件声明在相同包内
- 移除 `kotlin-reflect` 库中 `kotlin.reflect` 包中弃用的声明，可使用 `kotlin.reflect.full` 包

## 可迭代容器

- `Iterable<T>`，`Sequence<T>`，`CharSequence` 扩展了新方法：
	- 缓存或批处理：`chunked`
	- 滑动窗口与滑动均值：`windowed`
	- 处理成对后续项目：`zipWithNext`
- `List`，`MutableList` 扩展了新方法：`fill`，`replaceAll`，`shuffled`

## 数学库

- `kotlin.math`：常见数学运算
- `toBits`，`toRawBits`，`fromBits` 完成浮点到 bit 转换
- `toBigInteger`，`toBigDecimal` 完成大数据转换
- `BigInteger`，`BigDecimal` 支持 `+`，`-`，`*`，`/`，`%`，`++`，`--` 及其对应的中缀函数

## 其他 API

- `kotlin.text.Regex` 已实现 `Serializable`
- 依赖项中有 `kotlin-stdlib-jdk7` 时，在因其他异常而关闭资源期间抛出一个异常，`Closeable.use` 函数会调用 `Throwable.addSuppressed` 
- 对存在对应原生类型的平台类型 `null` 的变量 `x` 调用 `x.equals(null)` 现抛出 NPE，而不是返回 `true`
- 现在对平台类型调用内联扩展函数时会检查是否非空，防止 `null` 逃逸到其它代码中

# KotlinJS

将 Kotlin 原生数组（如 `IntArray`、 `DoubleArray` 等） 翻译为 [JavaScript 类型化数组](https://developer.mozilla.org/zh-CN/docs/Web/JavaScript/Typed_arrays) 的 JS 有类型数组支持之前是选择性加入的功能，现在已默认启用。
