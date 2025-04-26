#include <iostream>

using namespace std;

int add(int a, int b) {
    return a + b;
}

int mul(int a, int b) {
    return a * b;
}

int calc(int a, int b, int (*operation)(int, int)) {
    return operation(a, b);
}

int main() {
    // 3 + 5 = 8
    cout << "3 + 5 = " << calc(3, 5, add) << endl;
    // 3 * 5 = 15
    cout << "3 * 5 = " << calc(3, 5, mul) << endl;
    return 0;
}