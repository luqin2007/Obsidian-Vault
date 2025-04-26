# 协程

# 类型别名

允许通过 `typealias` 关键字生成类型别名，类似 C++ `using` 类型别名功能

```kotlin
typealias StrMap = Map<String, String>
```

# lambda 表达式解构

允许在 lambda 表达式中使用解构声明

```kotlin
fun print(map: Map<String, int>) {
    // 这里将 Entry<String, Integer> 解构为 (key, value)
    // 相当于 val (key, value) = entry
    println(map.mapValues { (key, value) -> "$key -> $value"})
}
```

# 委托绑定拦截

可使用 `provideDelegate` 拦截委托属性绑定

```kotlin
import kotlin.properties.ReadOnlyProperty  
import kotlin.reflect.KProperty  
  
class Box<T>(val value: T) {  
  
    operator fun provideDelegate(thisRef: MyObj, property: KProperty<*>): ReadOnlyProperty<MyObj, T> {  
        // do something  
        println("${property.name}")  
        return ReadOnlyProperty { _, _ -> value }  
    }  
}  
  
class MyObj {  
  
    val obj1 by Box("b-obj1")  
  
    val obj2 by Box("b-obj2")  
}
```

# 泛型枚举访问

允许以泛型类型对枚举类型值进行枚举

```kotlin
inline fun <reified T: Enum<T>> printEnum() {  
    println(enumValues<T>().joinToString { it.name })  
}
```

# 迭代器

- `onEach`：相当于 Java 的 `Stream::peek()`
- `takeIf`，`takeUnless`：相当于 Java 的 `Stream::filter` 和反向谓词版本
- `groupingBy`：对集合分组
- `minOf()`，`maxOf()`

# 集合与数组

- `Map.toMap`，`Map.toMutableMap`：复制 `Map`
- `Map.minus(key)`：`-` 运算符用于移除 `Map` 元素
- `Map.getValue(key)`：当包含对应键时，返回对应值；否则，通过 `withDefault` 创建则返回默认值，否则抛出异常
- 类似数组的列表构造：`List<T>(n, (int)->T)`
- 抽象集合基类：`AbstractList`，`AbstractMutableList` 等各种类，包含 `Collection`，`List`，`Set`，`Map` 等多个版本
- 数组函数：`contentEquals`，`contentDeepEquals`，`contentHashCode`，`contentDeepHashCode`，`contentToString`，`contentDeepToString` 等

# Java Script API

通过 `kts` 在 Java 中获取 Kotlin 的脚本执行器

```java
val engine = ScriptEngineManager().getEngineByExtension("kts")!!
engine.eval("val x = 3")
println(engine.eval("x + 2"))  // 输出 5
```

# 其他

- `::` 可用于获取指定对象或类的方法或属性的引用，以前只能用于 lambda 表达式
- 密封类的子类不必要是密封类的嵌套类，只要在同一个文件中即可
- `_` 下划线可用于替换不使用的参数，可用于 lambda 表达式，解构声明等
- `_` 下划线可用于分隔数字节位便于阅读，类似 Java
- `get()` 方法自定义访问器的属性类型可以自动推断
- `inline` 关键字可用于修饰属性访问器（`get()` 函数）
- 局部变量允许使用委托：`val prop by lazy { ... }` 形式
- `@DslMaker` 允许限制 DSL 上下文类型
- `rem` 替代 `mod`
- 其他新函数
	- `String.toInt()`，`String.toIntOrNull()`，`Int.toString()` 等字符串与数字的转换函数
	- `also`：类似于 `apply()`，接受一个对象，做一些动作并返回，但 `also` 不会覆盖 `this`
- 编译
	- 通过 `-java-parameters` 可在字节码中存储参数名
	- 内联 `const val` 常量
	- 闭包变量的包装类不再有 `volatile` 修饰

# KotlinJS

## external

若需要以类型安全的方式在 Kotlin 中访问 JavaScript 实现的类，可使用 `external` 修饰一个 Kotlin 声明。（在 Kotlin 1.0 中，使用了 `@native` 注解。） JS 平台允许对类与属性使用 external 修饰符。

```kotlin
// DOM Node 类
external class Node {
    val firstChild: Node
    fun appendChild(child: Node): Node
    fun removeChild(child: Node): Node
    // ...
}
```

## 导入处理

使用 `@JsModule` 声明导入 JS 模块，使用 `@JsNonModule` 表示作为模块或全局变量导入，使用 `@JsName` 表示作为变量导入时的变量名

```kotlin
external interface JQuery {
    fun toggle(duration: Int = definedExternally): JQuery
    fun click(handler: (Event) -> Unit): JQuery
}

@JsModule("jquery")
@JsNonModule
@JsName("$")
external fun jquery(selector: String): JQuery

fun main() {
    jquery(".toggle-button").click {
        jquery(".toggle-panel").toggle(300)
    }
}
```

