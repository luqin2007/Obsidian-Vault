#cpp20 

编译器查找无限定域的函数名字时，除了当前[[../命名空间|命名空间]]域以外，也会把函数参数类型所处的命名空间加入查找的范围。

编译器 ADT 规则支持在参数命名空间中查找带显示指定模板实参的函数模板。

```cpp
int h = 0;
void g() {};
namespace N {
    struct A{};
    template<class T> void f(T) {};
    template<class T> void g(T) {};
    template<class T> void h(T) {};
}

int main() {
    // 成功
    f<N::A>(N::A());
    // 成功
    g<N::A>(N::A());
    // 失败：存在变量 h，可被解析成 h <N::A > (N::A())，存在歧义
    h<N::A>(N::A());
    return 0;
}
```
