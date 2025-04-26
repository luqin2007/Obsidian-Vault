使用 `decltype` 关键字可在编译期获取一个表达式的类型

```cpp
int someInt;
decltype(someInt) otherInt = 10;
```

编译器不会真正计算表达式结果，只是提取类型信息，因此值不一定存在

```cpp
#include <vector>

std::vector<A> vec1;
std::vector<B> vec2;
// vec1 和 vec2 为空，但可正确执行
decltype(vec1[0] + vec2[0]) v;
```

上面例子 v 的类型为 `A` 与 `B` 对象相加的类型，如果存在的话（自定义运算符）。

`decltype` 和 `auto` 推断的类型可能不同，主要差异在 `const` 和 `&`

![[Pasted image 20240805100716.png]]

`decltype` 也常用于推断类型模板之间的运算结果。

```cpp
template<class T1, class T2>
void add(T1 a, T2 b) {
    decltype(a + b) r = a + b;
}
```
# 推导规则

* 若被推导表达式是一个未加括号的表达式或类成员 `e`，推断结果为 `e` 的类型 `T`；若 `e` 为一组重载函数，则无法推断

`````col
````col-md
```cpp
int i = 10;
// decltype(i) 推断为 int
decltype(i) i_ret;
```
````
````col-md
![[Pasted image 20240805100809.png]]
````
`````
`````col
````col-md
```cpp
struct STRUCT {
    int x;
    double y;
} *j;
// decltype(j) 推导为 STRUCT*
decltype(j) j_ret;
// decltype(*j) 推导为 STRUCT&
decltype(*j) j__ret;
// decltype(j->y) 推导为 double
decltype(j->y) jx_ret;
```
````
````col-md
![[Pasted image 20240805100851.png]]
````
`````

* 若推导表达式为一个函数或仿函数调用，推断类型为函数返回值，函数并不会被调用

`````col
````col-md
```cpp
const int&& foo();
// decltype(foo()) 推断为 int&&
decltype(foo()) foo_result;

int fun(char);
double fun(int);
char fun(double);
// decltype(fun(3.0)) 推断为 char
decltype(fun(3.0)) fun_d_result;
```
````
````col-md
![[Pasted image 20240805103905.png]]
````
`````

* 若被推导值为类型为 T 的左值，则结果为 `T&`；若被推导值为将亡值，则结果为 `T&&`

```cpp
// i=3 结果左值，decltype(i=3) 推断结果为 int&
decltype(i=3) lv_result;
// (int&&)i 结果为右值，decltype((int&&)i) 推断结果为 int&&
decltype((int&&)i) rv_result;
```
![[Pasted image 20240805103953.png]]

* 其他类型直接返回被推导值的类型（比较奇怪的是函数）

```cpp
void foo();
// foo_type 类型为 void(void)
// typeid(foo_type).name() 为 FvvE
// 这个不知道有什么用
decltype(foo) foo_type;
// p_foo_type 类型为 void(*)(void)
// typeid(p_foo_type).name() 为 PFvvE
// 这个是函数指针
decltype(foo) *p_foo_type;
```

* 推断保留 const，volatile 修饰符

