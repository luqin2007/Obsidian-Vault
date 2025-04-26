#include <iostream>

using namespace std;

int initialize_value() {
    cout << "initialize value" << endl;
    return 10;
}

void fun() {
    static int value { initialize_value() };
    cout << "value=" << (value++) << endl;
}

int main() {
    fun();
    fun();
    fun();
    fun();
    fun();
    return 0;
}