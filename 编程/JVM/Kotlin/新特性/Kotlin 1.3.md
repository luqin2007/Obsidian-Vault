# 协程

# 契约
#实验性功能 

让一个函数能够以编译器理解的方式显式描述其行为，类似 `String?::isNullOrEmpty()` 都是由此实现的，包括
- 通过声明函数调用的结果与所传参数值之间的关系来改进智能转换分析

```kotlin
@OptIn(ExperimentalContracts::class)  
fun require(condition: Boolean) {  
    // 当函数正常返回时，编译器可知 condition 为 true  
    contract { returns() implies condition }  
  
    if (!condition) throw IllegalArgumentException()  
}  
  
fun foo(s: Any) {  
    require(s is String)  
    // 此处 s 可被智能转换成 String
}
```

- 在存在高阶函数的情况下改进变量初始化的分析

```kotlin
@OptIn(ExperimentalContracts::class)  
fun synchoronize(block: () -> Unit) {  
    // block 方法在该方法内调用且仅调用一次  
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }  
}  
  
fun foo(s: Any) {  
    val x: Int  
    // 块只调用一次，因此不会提示重复初始化  
    synchoronize {   
        x = 100  
    }  
}
```

# 内联类

[[Kotlin 1.5#1.5.0#内联类]]

# 无符号类型

[[Kotlin 1.5#1.5.0#无符号类型]]

# @JvmDefault

用于标注接口中的函数默认实现，生成 JVM 平台的 `default` 方法，会导致无法兼容 Java7 及之前版本

*由于后期新版本 Kotlin 默认目标版本变更为 1.8，未来该注解被弃用*

# 其他

- 允许无参 `main` 函数：`void main() { ... }`
- 元函数增加到 `Function42`
- 注解类允许包含的内嵌类、接口、对象、伴生对象
- 可用 `@JvmStatic` 和 `@JvmField` 注解接口伴生对象的成员，能够将其提升成类 `static` 成员
- `kotlin.random.Random` 平台统一随机数生成器
- `isNullOrEmpty`，`orEmpty`，`ifEmpty`，`ifBlank` 已添加到集合，映射及对象数组
- `array.copyInto` 方法用于快速复制数组
- `key.associateWith { value }` 可用于根据键创建 `Map`
- `KClass.sealedSubclasses` 可通过反射获取密封类所有直接子类型
