#include <string>
#include <iostream>
#include <mutex>
#include <thread>

using namespace std;

int main() {
    mutex m;

    auto func = [&](const string& name, int k) {
        m.lock();
        cout << "Running: " << name << " ";
        for (int i = 0; i < k; ++i) {
            cout << i << " ";
        }
        cout << endl;
        m.unlock();
    };

    thread threads[5];
    for (int i = 0; i < 5; ++i) {
        string name {"Thread "};
        name.append(to_string(i)).push_back('-');
        threads[i] = thread(func, name, 10);
    }

/*
线程依次有序执行，共享资源（m）限制线程执行

Running: Thread 0- 0 1 2 3 4 5 6 7 8 9
Running: Thread 1- 0 1 2 3 4 5 6 7 8 9
Running: Thread 2- 0 1 2 3 4 5 6 7 8 9
Running: Thread 3- 0 1 2 3 4 5 6 7 8 9
Running: Thread 4- 0 1 2 3 4 5 6 7 8 9
*/
    for (auto &item: threads) {
        item.join();
    }
}