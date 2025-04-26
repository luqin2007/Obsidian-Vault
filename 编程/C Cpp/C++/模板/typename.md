模板类型引用通常需要使用 `typename` 标志说明该值为一个类型

```cpp
template<class T> void foo(typename T::type);
```

有两种情况可以例外：
* 指定基类：`class A<T>: T::B`
* 成员初始化：`class A: T::B { A(): T::B() {} };`
# 带模板的类型
#cpp17 

C++17 之前，模板中仅能用 `class` 声明带模板的形参

```cpp
template<typename T> struct A{};
template<template<typename> class T> struct B{};
```

C++17 之后，下面的情况也可以实现了：

```cpp
template<template<typename> typename T> struct B{};
```
# 省略 typename
#cpp20 

当上下文仅可能为类型时，以下情况可省略 `typename`：

* 类型转换

```cpp
// 包括 static_cast, const_cast, reinterpret_cast, dynamic_cast
static_cast<T::B>(p);
```

* 类型别名

```cpp
using R = T::B;
```

* 后置返回值

```cpp
auto g() -> T::B;
```

* 模板形参默认值

```cpp
template<class R = T::B> struct X;
```

* 全局或命名空间中简单声明或函数定义

```cpp
template<class T> T::R f();
```

* 结构体成员类型

```cpp
template<class T>
struct D {
    T::B b;
}
```

* 成员函数或 `lambda` 表达式形参声明

```cpp
struct D {
    T::B f(T::B) { return T::B(); };
}
```
