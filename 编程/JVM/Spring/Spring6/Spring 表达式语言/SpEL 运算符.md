# 关系运算符

除一般的关系运算符外，SpEL 还支持 `instanceof` 和 `matches`

* 支持字符串的大小比较，任何值总是大于 `null` 的

```reference
file: "@/_resources/codes/spring/spel/src/test/java/OperationTest.java"
start: 25
end: 29
```

* 支持 `instanceof` 运算符进行类型检查

```reference
file: "@/_resources/codes/spring/spel/src/test/java/OperationTest.java"
start: 34
end: 38
```

* 支持 `matches` 运算符进行正则匹配

```reference
file: "@/_resources/codes/spring/spel/src/test/java/OperationTest.java"
start: 43
end: 47
```

# 逻辑运算符

逻辑运算符使用 `&&`，`||`，`!`，也可以使用 `and`，`or`

`!` 运算符优先级低于 `&&`、`||`

# 算术运算符

* `+` 适用于数字、字符串、日期，`-` 适用于数字、日期，其余算术运算符适用于数字
* 支持取模 `%`、幂运算 `^`

# 赋值运算符

`=` 赋值运算符，也可以使用 `Expression` 对象的 `setValue` 方法

# 类型运算符

使用 `T(类名)` 可以获取指定类型的 `Class` 对象，也可以用来访问类静态成员

`String` 可以直接用，其他类需要使用全类名

```reference
file: "@/_resources/codes/spring/spel/src/test/java/OperationTest.java"
start: 52
end: 57
```

类型定位通过上下文 `TypeLocator` 实现，`StandardEvaluationContext` 内置 `StandardTypeLocator` 解析类。

# 三元运算符

SpEL 支持 `?:` 运算符和 `Elvis` 运算符

* `displayName = name != null ? name : 'Unknown'`
* `displayName = name ?: 'Unknown'`

# 上下文查找

使用 `#名称` 可以查找使用 `setVariable` 注册的变量和使用 `registerFunction` 注册的方法。

```reference
file: "@/_resources/codes/spring/spel/src/test/java/OperationTest.java"
start: 62
end: 75
```

`#root` 表示对根对象的引用

```reference
file: "@/_resources/codes/spring/spel/src/test/java/OperationTest.java"
start: 80
end: 83
```

`#this` 表示对评估对象的引用，用于表示迭代访问的上下文

```reference
file: "@/_resources/codes/spring/spel/src/test/java/OperationTest.java"
start: 88
end: 100
```

`@bean` 表示从在上下文中的 `BeanResolver` 查找 bean 类，通常为 SpringApplication 容器。

# 容器迭代

对于可迭代容器，SpEL 提供三种语法对其进行访问，类似于 Java 的 `Stream`：

> [!note] SpEL 迭代中对象引用到 `#this`，此时可以省略 `#this` 直接访问其成员

* 选择器 `selection`：`?[表达式]`、`^[表达式]`、`$[表达式]`
    * `?[表达式]` 相当于 `filter`
    * `^[表达式]` 相当于 `findFirst().filter`
    * `$[表达式]` 相当于 `findLast().filter`
* 映射器 `projection`：`![表达式]` 相当于 `map`
