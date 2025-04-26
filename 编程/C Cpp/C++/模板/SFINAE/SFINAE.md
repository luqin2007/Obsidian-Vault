#cpp11 

> [!note] SFINAE
> Substitution failed is not an error，替换失败并非错误。在函数模板重载时，若
> -  *模板形参替换为指定的实参* 
> - *由模板实参推导出模板形参* 
> 
> 的过程中出现了失败，则放弃这个重载而不是抛出一个编译错误。

> [!note] 替换失败：在直接上下文中使用模板实参替换形参后，类型或表达式不合法。

[[替换失败]]的情况比较复杂，编译期模板实例化时产生的非编译失败都是替换失败。

> [!error] 编译错误：替换后在非直接上下文中产生副作用导致错误。

SFINAE 允许试图创建无效的模板类型，但不允许试图计算无效的表达。

```cpp
template<int I> void f1(int (&)[24 / (4 + I)]);
template<int I> void f2(int (&)[24 / (4 - I)]);

int main() {
    // 允许
    &f1<4>;
    // 错误：size of array is not an integral constant-expression
    &f2<4>;
    return 0;
}
```

SFINAE 是函数模板可以重载的重要基础。下面例子中 `g<1>()` 先试图生成 `<int* 1>` 失败，之后尝试生成 `<int 1>` 版本成功。

```cpp
template<int* P> int g() { return *P; }
template<int N> int g() { return N; }

int main() {
    int i = g<1>();
    cout << i << endl;
    return 0;
}
```

利用 `type_traits` 头文件提供的 `enable_if` 实现 SFIANE 称为[[约束|约束]]。

用于类时，则可以配合模板特化用于校验和运算，即[[../模板元编程|模板元编程]]

---

```cpp
struct X {};
struct Y {
    Y(X) {}
};

X foo(Y, Y) { return X(); }

template<class T>
auto foo(T t1, T t2) -> decltype(t1 + t2) {
    return t1 + t2;
}

int main() {
    X t1, t2;
    auto t3 = foo(t1, t2);
    cout << typeid(t3).name() << endl;
    return 0;
}
```

`t1`，`t2` 类型为 X，`X + X` 并没有对应的类型，触发 SFINAE，模板不会进行实例化，转而调用 `X foo(Y, Y)`；而 `Y` 可以由 `X` 隐式转换而来，可以正常编译，`t3` 类型为 `X`

```cpp
template<int I>
void foo(char(*)[I % 2 == 0] = nullptr) {
    cout << "I % 2 == 0\n";
}

template<int I>
void foo(char(*)[I % 2 != 0] = nullptr) {
    cout << "I % 2 != 0\n";
}

int main() {
    char a[1];
    // I % 2 != 0
    foo<5>(&a);
    // I % 2 == 0
    foo<4>(&a);
    return 0;
}
```

根据 `I` 值的奇偶性可以引出 `foo` 后面的形参的两种情况：
* `char(*)[0]=nullptr`：传入的数组长度为 1，替换失败
* `char(*)[1]=nullptr`：编译通过

通过这种方法，可以实现编译时的函数选择

```cpp
class SomeObj {
public:
    void Dump2File() const {
        cout << "dump the object to file\n";
    }
};

template<class T>
auto DumpObj(const T &t) -> decltype(((void) t.Dump2File(), void())) {
    t.Dump2File();
}

void DumpObj(...) {
    cout << "object must have function Dump2File()\n";
}

int main() {
    SomeObj v1;
    string v2;
    // dump the object to file
    DumpObj(v1);
    // object must have function Dump2File()
    DumpObj(v2);
    return 0;
}
```

`DumpObj` 方法要求给定对象需要有 `Dump2File` 方法，通过 `decltype` 检查了一次 `Dump2File` 方法，若没有该方法触发替换失败，调用 `void DumpObj(...)` 重载。
