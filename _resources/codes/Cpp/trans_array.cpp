#include <iostream>

using namespace std;

void print(int arr[]) {
    cout << "arr type is " << typeid(arr).name() << endl; // ptr
    cout << "arr size is " << sizeof arr << endl;         // 8
}

int main() {
    int arr[] = {0, 1, 2, 3, 4};
    // arr type is A5_i
    cout << "arr type is " << typeid(arr).name() << endl;
    // arr size is 20
    cout << "arr size is " << sizeof arr << endl;
    cout << "==========================" << endl;
    // arr type is Pi
    // arr size is 8
    print(arr);
    return 0;
}