二选一的容器，分为 `Left`、`Right` 两个子类，类似 c 的 `union`。

`Either` 通常用于消除 `try-catch` 的副作用。`Right` 类似于 `Identity`，而 `Left` 多表示异常信息。

此外，`Either` 还可以表示逻辑或、`coproduct` 等概念。

‍
