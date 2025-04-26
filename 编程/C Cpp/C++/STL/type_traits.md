`type_traits` 可利用模板进行类型检查

```cpp
template<class T>
T average(const vector<T>& data) {
    static_assert(is_arithnetic<T>::value, "Type T must is arithmetic");
    // do something
}
```

* `is_arithnetic<T>`：类型 T 实现了 `+` 等算术运算符
* 类型判断：`is_integral<T>`，`is_signed<T>`，`is_unsigned<T>`，`is_floating_point<T>`，`is_enum<T>` 等
# is_constant_evaluated
#cpp20 

函数 `std::is_constant_evaluated()` 判断当前代码执行环境是在编译期还是运行时，明显的编译期则返回 `true`

```cpp
constexpr double power(double b, int x) {
    if (std::is_constant_evaluated() && x >= 0) {
        double r = 1.0, p = b;
        unsigned u = x;
        while (u) {
            if (u & 1) r *= p;
            u /= 2;
            p *= p;
        }
        return r;
    } else {
        return std::pow(b, x);
    }
}
```

在 C++ 文档中，明显常量求值包括：
* 常量表达式
* `if constexpr`
* `constexpr` 变量初始化
* 立即函数及其调用
* 约束表达式
* 可在常量表达式中使用或具有常量初始化的变量初始化程序
