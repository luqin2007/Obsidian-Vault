>[!note] 生命周期接口：定义一个或多个由 Spring 容器调用的回调方法，在 `bean` 生命周期内适当的时候被调用

> [!note] 方法注入：Spring 生成一个继承自指定类型的子类对象，并覆盖（或实现）其对应方法

> [!tip] Spring 使用 `GCLIB` 执行 `bean` 的子类化

* 使用生命周期接口： [[ApplicationContextAware 接口]]
* 使用方法注入： `bean` 模式的 [[lookup-method 标签|<lookup-method>]] 和 [[replaced-method 标签|<replaced-method>]] 元素

‍
