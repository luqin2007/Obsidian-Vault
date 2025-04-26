函子 `Functor` 是一种实现 `map` 函数的容器。这允许我们在不解开容器的情况下操作容器中的值。

```java
// 容器
interface Container<A> {}
// 函子
interface Functor<A> extends Container<A> {
    <B> Functor<B> map(Function<A, B> f);
}
```

![[Pasted image 20231022233403-20240513174540-hviqjut.png]]

在范畴学中，`functor` 接受一个范畴的对象和态射（`morphism`），然后把它们映射（`map`）到另一个范畴里去。根据定义，这个新范畴一定会有一个单位元（`identity`），也一定能够组合态射；我们无须验证这一点，前面提到的定律保证这些东西会在映射后得到保留。

* `functor` 相当于图中的 F，即容器；`F a` 表示函子中存放有 a 数据
* 态射相当于图中的 `f`；单位元即 `id` 函数

可以把范畴想象成一个有着多个对象的网络，对象之间靠态射连接。那么 `functor` 可以把一个范畴映射到另外一个，而且不会破坏原有的网络。如果一个对象 `a` 属于源范畴 `C`，那么通过 functor `F` 把 `a` 映射到目标范畴 `D` 上之后，就可以使用 `F a` 来指代 `a` 对象。

![[Pasted image 20231022234931-20240513174628-uwni9rh.png]]

‍
