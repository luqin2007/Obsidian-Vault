#include <thread>
#include <iostream>
#include <synchapi.h>

using namespace std;

void Test() {
    jthread th;
    {
        th = jthread([](const stop_token st) {
            while (!st.stop_requested()) {
                // 没有收到中断请求，则执行
                cout << "1";
                Sleep(500);
            }
        });
    }
    Sleep(10 * 1000);
    // 外部发起中断请求
    auto ret = th.request_stop();
}

int main(int argc, char* argv[]) {
    Test();
    cout << endl;
    return 0;
}