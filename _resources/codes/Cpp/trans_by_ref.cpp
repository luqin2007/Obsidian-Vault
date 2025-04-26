#include<iostream>

using namespace std;

static int ID {0};

class A {
public:

    A() { cout << "create A" << ", id=" << id << endl; }
    A(const A &a) { cout << "create A by A&" << ", id=" << id << endl; }
    ~A() { cout << "remove A" << ", id=" << id << endl; }

    int value {};
    int id {ID++};
};

void set_value(A &a, int value);

int main() {
    // create A, id=0
    A a;
    // Value before set_value is 0, id=0
    cout << "Value before set_value is " << a.value << ", id=" << a.id << endl;
    // Value in set_value is 20, id=0
    set_value(a, 20);
    // Value after set_value is 20, id=0
    cout << "Value after set_value is " << a.value << ", id=" << a.id << endl;
    // remove A, id=0
    return 0;
}

void set_value(A &a, int value) {
    a.value = value;
    cout << "Value in set_value is " << a.value << ", id=" << a.id << endl;
}