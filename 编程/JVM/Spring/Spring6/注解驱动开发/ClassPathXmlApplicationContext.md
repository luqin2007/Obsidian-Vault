引入 `context` 模式后，通过 `<context:component-scan>` 元素启用类路径扫描功能。

```reference
file: "@/_resources/codes/spring/annotation-classpathxmlapplicationcontext/src/main/resources/applicationContext.xml"
start: 4
end: 8
```

| 属性                 | 说明                       |
| ------------------ | ------------------------ |
| `base-package`     | 指定搜索 `bean` 的包           |
| `resource-pattern` | 被搜索文件的匹配，默认 `**/*.class` |

除了使用 `resource-pattern` 通过扫描文件名进行过滤，还可以通过其子元素 `<context:include-filter>` 和 `<context:exclude-filter>` 对类进行过滤：

```reference
file: "@/_resources/codes/spring/annotation-classpathxmlapplicationcontext/src/main/resources/applicationContext.xml"
start: 8
end: 11
```

`<context:include-filter>` 和 `<context:exclude-filter>` 标签的匹配方式由 `type` 和 `expression` 确定：

|type|expression 类型|说明|
| ------------| ------------------------| ---------------------------------------------|
|annotation|注解的全类名|包含或排除被某个注解修饰的类|
|assignable|类或接口的全类名|包含或排除继承自对应类的类|
|aspectj|AspectJ 表达式|包含或排除全类名匹配给定 AspectJ 表达式的类|
|regex|正则表达式|包含或排除类名匹配给定正则表达式的类|
|custom|实现了 `TypeFilter` 接口的全类名|包含或排除通过某个自定义过滤器的类|
