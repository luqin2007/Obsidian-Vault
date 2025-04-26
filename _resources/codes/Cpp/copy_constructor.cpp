#include <iostream>
#include <string>

using namespace std;

class A {
    string name;
    int times;
public:
    A(const char* name) : name{name}, times{0} {
        cout << "Create A " << name << endl;
    }

    A(const A &another) : name{another.name}, times{another.times + 1} {
        cout << "Copy from " << name << ", copy times " << another.times << " -> " << times << endl;
    }

    void print() {
        cout << "A " << name << "[" << times << "]" << endl;
    }
};

A print_a(A a) {
    a.print();
    return a;
}

int main() {
    cout << "===" << endl;
    A a {"a"};
    cout << "===" << endl;
    A a2 = a;
    cout << "===" << endl;
    a2 = print_a(a2);
    cout << "===" << endl;
    a2.print();
    cout << "===" << endl;
    return 0;
}