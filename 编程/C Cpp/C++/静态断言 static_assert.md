#cpp11 

```cpp
static_assert([constant_expression], [string_literal]);
```

* `[constant_expression]`：一个返回 `bool` 类型的常量表达式
* `[string_literal]`：若为 `false` 则显示的异常提示

该断言用于编译时，不会对运行时产生影响
# C++20 修订
#cpp20

引入单参数的静态断言，即 `msg` 参数可省略。此时，输出的信息即 `expr`，类似下面的定义：

```cpp
#define sinple_static_assert(expr) static_assert(expr, #expr)
```
# 间接实现

在不使用静态断言的情况下，使用其他语法尝试实现静态断言功能

````tabs
tab: 宏定义
利用数组下标不可为负进行测试
<br/>

```cpp
#define STATIC_ASSERT_CONCAT_IMP(x, y) x ## y
#define STATIC_ASSERT_CONCAT(x, y) STATIC_ASSERT_CONCAT_IMP(x, y)
#define STATIC_ASSERT(expr) \
do {                        \
    char STATIC_ASSERT_CONCAT(static_assert_var, __COUNTER__) \
    [(expr) != 0 ? 1 : -1]; \
} while(0)                  \

int main() {
    // do { char static_assert_var[1]; } while(0);
    STATIC_ASSERT(1);
    // do { char static_assert_var[-1]; } while(0);
    // size '-1' of array 'static_assert_var1' is negative
    STATIC_ASSERT(0);
    return 0;
}
```

tab: 模板特化 1
通过实例化一个不存在的模板引发错误，但无法出现在类和结构体的定义中
<br/>

```cpp
template<bool>
struct static_assert_st;
// 只有 true 的模板
template<>
struct static_assert_st<true> {};

#define STATIC_ASSERT(expr) static_assert_st<(expr) != 0>()

int main() {
    STATIC_ASSERT(1);
    // declaration of 'struct static_assert_st<false>'
    STATIC_ASSERT(0);
    return 0;
}
```

tab: 模板特化 2
声明一个变量，可以出现在定义中，但会改变其内存布局
<br/>

```cpp
template<bool>
struct static_assert_st;
template<>
struct static_assert_st<true> {
};

#define STATIC_ASSERT_CONCAT_IMP(x, y) x ## y
#define STATIC_ASSERT_CONCAT(x, y) STATIC_ASSERT_CONCAT_IMP(x, y)
#define STATIC_ASSERT(expr) \
static_assert_st<(expr) != 0> \
STATIC_ASSERT_CONCAT(static_assert_var, __COUNTER__)

int main() {
    STATIC_ASSERT(1);
    // Implicit instantiation of undefined template 'static_assert_st<false>'
    STATIC_ASSERT(0);
    return 0;
}
```
````
