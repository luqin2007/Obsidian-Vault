Applicative functor 是实现了 `ap` 方法的 `pointed functor`。该接口实现的是让不同 `functor` 相互 `apply`（即 `map`） 的功能。

```java
// pointed functor
interface PointedFunctor<T> {
    <R> Applicative<R> map(Function<T, R> f);
    static <T> Applicative<T>(T value);
    T getValue();
}
// applicative functor
interface Applicative<T> extends PointedFunctor<T> {
    // ap 函数
    default <R, I> PointedFunctor<R> ap(PointedFunctor<I> other) {
        // 要求 T 本身是一个函数 Function<I, R>
        return other.map((Function<I, R>) value());
    }
    // static 版本的 ap，带有类型校验
    static <T, R> PointedFunctor<R> ap(
                      Applicative<Function<T, R>> f, 
                      PointedFunctor<T> v) {
        return v.map(f.value());
    }
}
```

这种写法有点像前序的运算符。下面例子中使用伪代码，F 是一个 `applicative functor`

```
add :: int -> int -> int
int a, b, sum

sum = F.of(add)      // F(int -> int -> int)
       .ap(F.of(a))  // F(int -> int)
       .ap(F.of(b))  // int
```

`map` 等价于 `of/ap`

```
f :: int -> int
int x

// 等价
F.of(x).map(f) == F.of(f).ap(F.of(x))
```
# 运算律

* 同一律：对 functor 应用 id 函数不会改变值

  ```
  A: functor
  v: A v

  A.of(id).ap(v) == v
  ```
* 同态：不管是把所有的计算都放在容器里还是先在外面进行计算然后再放到容器里，结果相同

  ```
  A.of(f).ap(A.of(x)) == A.of(f(x))
  ```
* 互换：让函数在 `ap` 的左边还是右边发生 lift 是无关紧要的

  ```
  v: x -> r
  x: value

  v.ap(A.of(x)) == A.of(f -> f(x)).ap(v)
  ```
* 组合：标准的函数组合适用于容器内部的函数调用

  ```
  A.of(compose).ap(u).ap(v).ap(w) = u.ap(v.ap(w))
  ```
# lift

lift：一个函数在调用的时候，如果被 `map` 包裹了，那么它就会从一个非 functor 函数转换为一个 functor 函数。这个过程称为 `lift`

可以通过 pointfree 的方式调用 applicative functor，其中 App 是一个 applicative functor

```java
App<R> lift(Function<T, R> f, App<T> v) {
    return v.map(f);
}
App<R> lift2(BiFunction<T1, T2, R> f, App<T1> v1, App<T2> v2) {
    return v1.map(f).ap(v2);
}
App<R> lift3(Function3<T1, T2, T3, R> f, App<T1> v1, App<T2> v2, App<T3> v3) {
    return v1.map(f).ap(v2).ap(v3);
}
// ... 可以有无数个
```
