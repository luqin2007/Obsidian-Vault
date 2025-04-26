#include <iostream>
using namespace std;

struct X {
public:
    X() { cout << "X ctor\n"; }
    X(const X &x) { cout << "X copy ctor\n"; }
    ~X() { cout << "X dtor\n"; }
};

X make_x_rvo() {
    return {};
}

X make_x_nrvo() {
    X x;
    return x;
}

int main() {
    cout << "-------------------------------------\n";
    // X ctor
    X x1 = make_x_rvo();
    cout << "-------------------------------------\n";
    // X ctor
    X x2 = make_x_nrvo();
    cout << "-------------------------------------\n";
    return 0;
    // X dtor
    // X dtor
}