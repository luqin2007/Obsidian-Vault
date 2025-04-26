monad 是一个可以扁平化（flatten）的 pointed functor。该容器允许延迟计算和自动剥离容器，每调用一次 `join()` 脱一层容器，`join()` 方法符合结合律、同一律。

* 结合律：`compose(join, map(join)) == compose(join, join)`

  ![[Pasted image 20231023024435-20240513175229-nlbm3wr.png]]
* 同一律：`compose(join, of) == compose(join, map(of)) == id`

  ![[Pasted image 20231023024525-20240513175235-zwmfj0o.png]]

# flatMap

在 `map` 后紧接 `join` 的操作组合称为 `flatMap`，在其他地方也有叫 `chain`，`bind`，`>>=` 等。通过 `flatMap` 可以直接获取 `map` 和 `ap` 函数

```java
interface Monad<T> {
    Monad<R> flatMap(Function<T, Monad<R>> f);
}

interface App<T> extends Monad<T> {
    default App<R> map(Function<T, R> f) {
        return flatMap(v -> of(f.apply(v)));
    }
    default App<R> ap(App<V> v) {
        return flatMap(f -> v.map((Function<V, R>) f));
    }
}
```
