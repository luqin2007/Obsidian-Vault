#include <iostream>

using namespace std;

struct X {
public:
    X() { cout << "X ctor\n"; }
    X(const X &x) { cout << "X copy ctor\n"; }
    ~X() { cout << "X dtor\n"; }
};

X make_x() {
    X x1, x2;
    // 编译器无法确定返回的是 x1 还是 x2
    if (time(nullptr) % 50) {
        return x1;
    } else {
        return x2;
    }
}

int main() {
    cout << "-------------------------------------\n";
    // X ctor
    // X ctor
    // X copy ctor  --> 从此处开始，返回值优化失效
    // X dtor
    // X dtor
    X x = make_x();
    cout << "-------------------------------------\n";
    return 0;
    // X dtor
}