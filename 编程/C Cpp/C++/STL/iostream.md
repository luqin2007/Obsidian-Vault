STL 负责处理 `C++` 输入输出功能，相当于 `C` 中的 `stdio.h` 头文件。
# cin

`cin`  相当于 `C` 中的 `stdin` 对象，用于从控制台输入数据。

`cin` 是一个 `istream` 对象，重写了 `>>` 运算符，可直接将数据赋值给一个变量的引用

```cpp
#include<iostream>

using namespace std;

int main() {
    int num1, num2;
    cin >> num1 >> num2;
}
```
# cout

`cout` 相当于 `C` 中的 `stdout` 对象，用于将数据输出到控制台

`cout` 是一个 `ostream` 对象，重写了 `<<` 运算符，可将一个数据直接输出到控制台中

```cpp
#include<iostream>

using namespace std;

int main() {
    cout << "Hello " << "World!" << endl;
    const char* lang = "C++";
    cout << "I'm writing a note for " << lang << endl;
}
```

如果需要格式化输出，则可以使用 `iomanip` 头文件提供的控制函数和对象。

传统的 `scanf` 和 `printf` 仍然可以使用，且在数据量很大且没有其他设置的情况下，比 `cin`和 `cout` 快近一倍
# cerr

异常输出
# endl

`std::endl` 可直接输出到 `cout`，表示换行，相当于 `cout << '\n'`
