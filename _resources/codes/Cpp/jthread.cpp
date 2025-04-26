#include <thread>
#include <iostream>
#include <synchapi.h>

using namespace std;

void Test() {
    jthread th;
    {
        th = jthread([]() {
            for (unsigned i = 1; i < 10; ++i) {
                cout << i << " ";
                Sleep(500);
            }
        });
    }
    // 没有使用join也不会崩溃,因为 jthread 的析构函数默认调用join()
}

int main(int argc, char* argv[]) {
    Test();
    return 0;
}