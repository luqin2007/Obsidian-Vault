#include <iostream>

using namespace std;

void b(int &&b) {
    cout << "b&&=" << b << endl;
}

void b(int &b) {
    cout << "b&=" << b << endl;
}

void a(int &&a) {
    cout << "a&&=" << a << endl;
    b(std::move(a)); // 这里用了 move 语义
}

void a(int &a) {
    cout << "a&=" << a << endl;
    b(a);
}

int main() {
    int v = 10;
    // a&=10
    // b&=10
    a(v);
    // a&&=10
    // b&&=10
    a(10);

    return 0;
}