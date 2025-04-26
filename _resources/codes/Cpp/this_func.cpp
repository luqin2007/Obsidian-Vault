#include<iostream>

using namespace std;

int main() {
    auto fact = [](this auto self, int n) -> int {
        return n ? n * self(n - 1) : 1;
    }

    cout << fact(5) << end;
    return 0;
}