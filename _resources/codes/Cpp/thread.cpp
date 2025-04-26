#include <thread>
#include <iostream>

using namespace std;

int main() {
    thread t([] {
        for (int i = 0; i < 10; ++i) {
            cout << " t: " << i << endl;
        }
    });
    if (t.joinable()) {
        t.detach();
    }

    thread tt([](int k) {
        for (int i = 0; i < k; ++i) {
            cout << "tt: " << i << endl;
        }
    }, 20);
    if (t.joinable()) {
        t.join();
    }
    return 0;
}