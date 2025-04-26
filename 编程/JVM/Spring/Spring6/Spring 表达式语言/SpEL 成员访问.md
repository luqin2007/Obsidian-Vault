# 成员访问 `.`

SpEL 使用 `.` 访问对象成员和属性等
- 使用 `?.` 表示空安全的成员访问，若原本对象为 `null`，则返回 `null`
- 使用 `new` 通过构造函数创建对象

```reference
file: "@/_resources/codes/spring/spel/src/test/java/ObjectVisitTest.java"
start: 19
end: 33
```

使用 `{}` 可以内联创建列表（`java.util.Collections$UnmodifiableRandomAccessList`），创建的列表可以转换为数组

```reference
file: "@/_resources/codes/spring/spel/src/test/java/ObjectVisitTest.java"
start: 38
end: 41
```

可以给 SpEL 配置一个<span data-type="text" parent-style="color: var(--b3-card-success-color);background-color: var(--b3-card-success-background);">上下文</span>，上下文可以设置一个根对象，这样对根对象的调用可以省略。

# 索引 `[]`

对于 Properties、List、Map 等，可直接使用索引器 `[]` 访问数据

Spring 上下文内置 `systemProperties` 属性获取系统变量和虚拟机参数

```reference
file: "@/_resources/codes/spring/spel/src/test/java/ObjectVisitTest.java"
start: 46
end: 63
```

可以使用 `T(类型)` <span data-type="text" parent-style="color: var(--b3-card-success-color);background-color: var(--b3-card-success-background);">引用</span>一个类型，用于访问静态成员、进行 `instanceof` 运算等
