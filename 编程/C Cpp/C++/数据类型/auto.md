#cpp11 

`auto` 可让编译器自动推断一个变量的类型，称为类型推断：`auto x = expr`

```cpp
auto a = 10;
auto b = a * 3;
```

同时声明多个变量时，遵循自左向右推导规则，取第一个变量的推导类型

```cpp
int n = 5;
// 根据 &n 推断 auto 为 int，故 m 也为 int
auto *pn = &n, m = 10;
// 编译错误：根据 &n 推断 auto 为 int，不符合 m2 类型
auto *pn2 = &n, m2 = 10.0;
```

可用于对函数指针的简化写法

```cpp
int add(int a, int b) {
    return a + b;
}

int main() {  
    auto add_func = add;
    cout << add_func(5, 7) << endl;
    cout << typeid(add_func(5, 7)).name() << endl;
    cout << typeid(add_func).name() << endl;
    return 0;
}
```

> [!danger] 不要在使用初始化列表的同时使用 `=` 运算符赋值给 `auto` 变量，因为赋值的是初始化列表对象

```cpp
int add(int a, int b) {
    return a + b;
}

int main() {  
    int a {5};  
    auto b {5};  
    int c = {5};  
    auto d = {5};  
    cout << "Type of a is " << typeid(a).name() << endl;  
    cout << "Type of b is " << typeid(b).name() << endl;  
    cout << "Type of c is " << typeid(c).name() << endl;  
    cout << "Type of d is " << typeid(d).name() << endl;  
    return 0;
}
```

可用在 `new` 关键字之后，但不常用

```cpp
// 经过两次推导，第一次推导右侧 auto 类型为 int，第二次推导 i 类型为 int*
auto i = new auto(5);
// 经过两次推导，第一次推导右侧 auto 类型为 int，第二次推导 *j 类型为 int
auto *j = new auto(7);
```

可以用结构化绑定，为解构的元组变量统一增加 `const`，`&` 标记等

```cpp
int a[2] = {1, 2};

// x, y: int
auto [x, y] = a;
// m, n: &int
auto& [m, n] = a;
```

`auto` 类型推断的规则如下：

````tabs
tab: `cv` 限定符
若推导类型为值类型（非指针、引用），忽略 `const`，`volatile` 修饰符
<br/>

```cpp
const int i = 5;
// auto 推断为 int 而非 const int
auto j = i;
```

tab: 引用
若推导类型为引用类型，则忽略引用。使用  `auto &` 声明引用类型
<br/>

```cpp
int i = 5;
int &j = i;
// auto 推断为 int 而非 int&
auto m = j;
// 若需要引用，需要这么写
auto &n = j;
```
<br/>

> [!warning] 但若声明为万能引用，则左值被推导为引用

<br/>

```cpp
int i = 3;
// 由于引用折叠，m 的类型为 int&
auto&& m = i;
// n 的类型为 int&&
auto&& n = 3;
```

tab: 函数/数组
推导类型为对应类型的指针
<br/>

```cpp
int add(int a, int b) {
    return a + b;
}

int arr[10];

auto a = add;
auto b = arr;

int main() {
    cout << typeid(add).name() << endl;
    cout << typeid(a).name() << endl;
    cout << typeid(arr).name() << endl;
    cout << typeid(b).name() << endl;
    return 0;
}
```

tab: 初始化列表
- 直接使用，必须使用单元素
- 通过 `=` 连接，推导为 `std::initializer_list<T>` 类型且元素类型必须相同
<br/>

```cpp
// auto 推断为 std::initializer_list<int>
auto x1 = {1, 2};
// auto 推断为 std::initializer_list<int>
auto x2 = {1};
// 编译错误，元素类型必须相同
auto x3 = {'a', 2.0};
// auto 推断为 int，值为 1
auto x4 {1};
// 编译错误，必须使用单元素
auto x5 {1, 2};
```
````
# 对象复制
#cpp23

 auto 可以识别表达式的类型，`auto(t)` 和 `auto{t}` 的形式可以类比为 `T(t)` 和 `T{t}` 的形式。
 
 可以使用 `auto(t)` 和 `auto{t}` 的形式用于复制对象：

```cpp
string s { "abc" };
string t = auto(s);
string u = auto{s};
```
# 推断函数形参和返回值

一种函数模板的简化写法， #cpp14 允许返回值使用 `auto`， #cpp20 允许形参类型使用 `auto`

```cpp
// auto 具体类型为实参类型
void echo(auto value) {
  // do something
}

// 根据 a+b 推断返回值为 int 类型
auto add(int a, int b) {
    return a + b;
}
```
# 推断 λ 表达式形参和返回值
#cpp14 

`lambda` 形参列表及返回值，实现类似模板的效果

```cpp
auto add = [](auto a, auto b) { return a + b; };
// a 类型推导为 int，b 类型推导为 double，r1 推导为 double
auto r1 = add(3, 5.0);
// a 类型推导为 double，b 类型推导为 int，r2 推导为 double
auto r2 = add(3.0, 5);
// a 类型推导为 char，b 类型推导为 char，r3 推导为 char
auto r3 = add('3', '5');

// 返回值推导为 int
auto l = [](int &i) -> auto& { return i; };
```
# 推断非类型模板类型
#cpp17

`auto` 可用于非类型模板的形参推导

```cpp
// 根据传入的实际参数推断 N 类型
template<auto N>
void f() {
    cout << N << endl;
}

f<10>();  
f<10.0>();  
f<'a'>();
```
# 推断类成员声明
#cpp11 #cpp17

`auto` 关键字可以用于声明被 `const` 或 `inline` 修饰的类静态成员变量

```cpp
struct AutoStructure {
    static const auto i = 10;
    static inline auto j = 20;
}
```
# decltype(auto)
#cpp14

`decltype(auto)` 结合使用表示用 `decltype` 的推导规则推导 `auto`。用法与 `auto` 几乎完全相同只是推导规则不同。

> [!warning] 必须单独使用，不能结合指针、引用、`const`、`volatile` 使用

#cpp17 `decltype(auto)` 还可以用于非类型模板的类型。

```cpp
int i = 3;
int &j = i;
int &&foo();

// a1 a2 均为 int
auto a1 = i;
decltype(auto) a2 = i;
// b1 为 int，b2 为 int&
auto b1 = j;
decltype(auto) b2 = j;
// c1 为 int，c2 为 int&&
auto c1 = foo();
decltype(auto) c2 = foo();
// d1 d2 均为 int
auto d1 {3};
decltype(auto) d2 {3};
// e1 为 initializer_list<int>
// e2 无法编译，因为 {3, 5} 不是表达式
auto e1 = {3, 5};
decltype(auto) e2 = {3, 5};
// *f1 为 int*
// f2 无法编译，因为 decltype(auto) 只能独立使用
auto *f1 = &i;
decltype(auto) *f2 = &i;
```

可以代替函数返回类型后置

`````col
````col-md
```cpp
template<class A, class B>
decltype(auto) add2(A a, B b) {
    return a + b;
}
```
````
````col-md
```cpp
template<class A, class B>
auto add1(A a, B b) -> decltype(a + b) {
    return a + b;
}
```
````
`````
