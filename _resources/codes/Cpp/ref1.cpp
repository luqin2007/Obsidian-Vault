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
    b(a);
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
    // b&=10 -- 预期应该是 b&&=10
    a(10);
    
    return 0;
}