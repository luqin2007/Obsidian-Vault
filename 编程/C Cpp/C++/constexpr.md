#cpp11

> [!note] C++11 之前的编译时常量声明
> * `const`：可能是运行时常量
> * `#define`：普通的查找替换，若原本不是常量则宏定义也不是常量

`constexpr` 关键字表示**变量**、**方法**为编译时常量，可以在编译时计算。

```cpp
constexpr int get_index0() { return 0; }
constexpr int get_index1() { return 1; }

int main() {
    int argc = 0;
    switch (argc) {
        case get_index0(): ;
        case get_index1(): ;
    }
    return 0;
}
```

对于变量，要求表达式每一部分必须都是编译时常量表达式

对于函数，要求如下：

* 有返回值，函数不能是 `void` 的

```cpp
// invalid return type 'void' of 'constexpr' function 'constexpr void foo()'
constexpr void foo() {
}
```

> [!success] 在 GCC 上，这是个 warning 而非 error

* 函数只有一条 `return` 语句

```cpp
// body of 'constexpr' function 'constexpr int abs2(int)' not a return-statement
constexpr int abs2(int x) {
    if (x > 0) {
        return x;
    } else {
        return -x;
    }
}
```
  
* 使用前必须实现，不能只有声明

```cpp
// 'constexpr int max_unsigned_char()' used before its definition
constexpr int max_unsigned_char();
enum {
    // enumerator value for 'max_uchar' is not an integer constant
    max_uchar = max_unsigned_char();
};
constexpr int max_unsigned_char() {
    return UCHAR_MAX;
}
```
  
* 必须是纯函数：参数相同时返回值必须相同

* 除了参数，其他各部分必须是编译时常量表达式

````tabs
tab: ++ 运算符
```cpp
// expression '++ x' is not a constant expression
constexpr int next(int x) {
    return ++x;
}
```
<br/>

> [!success] 在 GCC 上，这是个 warning 而非 error

tab: 普通函数
```cpp
int g() {
    return 42;
}

// call to non-'constexpr' function 'int g()'
// err: g() 不是一个常量表达式
constexpr int f() {
    return g();
}
```

tab: 循环结构
```cpp
// body of 'constexpr' function 'constexpr int sum(int)' not a return-statement
constexpr int sum(int x) {
    int result = 0;
    while (x > 0) {
        result += x--;
    }
    return result;
}
```
````
  
* 类的成员中的 `constexpr` 函数无论是否被 `const` 修饰，都是 `const` 的

#cpp14

* 函数体允许声明变量，但必须初始化，且不能是 `static`，`thread_local` 的
* 允许出现 `if`，`switch`，但不能有 `goto`
* 允许出现 `for`，`while`，`do-while`
* 允许修改生命周期与常量表达式相同的对象
* 函数返回值可以为 `void`
* `constexpr` 声明的成员函数不再自动具有 `const` 属性

#cpp20

* 允许使用平凡类的默认初始化
* 允许修改联合体类型的有效成员

> [!success] 该条规则在 GCC 和 MSVC 均可以 C++17 版本编译通过

* 允许出现 `try-catch` 结构，但不能使用 `throw` 抛出异常，`catch` 块永不执行