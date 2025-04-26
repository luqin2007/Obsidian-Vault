如果一个 bean 只是作为某个特定 bean 的依赖使用，可以使用 `<bean>` 标签声明为内部 bean

* 内部 `bean` 只能访问到其所在上下文，**无法访问**外部的其他 bean
* 内部 `bean` 本质是**匿名**的，指定了 `id` 属性也是无效的
* 内部 `bean` **总是** **`prototype`** **范围的**，指定了其他 `scope` 属性也是无效的

```reference
file: "@/_resources/codes/spring/ioc-innerbean/src/main/resources/applicationContext.xml"
start: 11
end: 16
```

内部 `bean` 也同样可以使用 `util` 模式等其他需要 bean 的情况

```reference
file: "@/_resources/codes/spring/ioc-innerbean/src/main/resources/applicationContext.xml"
start: 18
end: 23
```
