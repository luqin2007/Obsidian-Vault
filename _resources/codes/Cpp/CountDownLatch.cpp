#include <iostream>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <atomic>

using namespace std;

int main() {
    atomic_int count {10};

    mutex m;
    condition_variable cv;

    thread awaiting = thread([&] () {
        unique_lock<mutex> lock(m);
        while (count > 0) {
            // 每 5ms 检查一次
            cv.wait_for(lock, chrono::milliseconds(5));
        }
        cout << "Latch opened!" << endl;
    });

    thread count_down = thread([&] () {
        while (count > 0) {
            // 每 1s 计数一次
            this_thread::sleep_for(chrono::seconds(1));
            count--;
            cout << "Count = " << count << endl;
        }
        cout << "CountDown finished!" << endl;
    });

    awaiting.join();
    count_down.join();
}