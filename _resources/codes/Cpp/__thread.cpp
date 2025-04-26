#include <windows.h>
#include <cstdio>

// 根据编译器选择修饰
#ifdef __GNUC__                       // gcc
__thread char name[20];
#elif _MSC_VER                        // vs
__declspec(thread) char name[20];
#endif

// 线程执行的任务方法
DWORD WINAPI Fun(LPVOID lpParam) {
    sprintf(name, "Thread %lu", GetCurrentThreadId());
    for (int i = 0; i < 10; ++i) {
        // 通过 TlsGetValue 获取值
        printf("Thread %s: %d\n", name, i);
    }
    return 0L;
}

int main() {
    // 创建两个线程，运行并等待结束
    HANDLE hThread1 = CreateThread(NULL, 0, Fun, NULL, 0, NULL);
    HANDLE hThread2 = CreateThread(NULL, 0, Fun, NULL, 0, NULL);
    WaitForSingleObject(hThread1, INFINITE);
    WaitForSingleObject(hThread2, INFINITE);
    return 0;
}