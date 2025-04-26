#include <iostream>

using namespace std;

class Array {
private:
    int values[10]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
public:
    int *begin() {
        return values;
    }

    int *end() {
        return values + 10;
    }
};

int main() {
    Array arr;
    // 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
    for (int v : arr) {
        cout << v << ", ";
    }
    cout << endl;
    return 0;
}