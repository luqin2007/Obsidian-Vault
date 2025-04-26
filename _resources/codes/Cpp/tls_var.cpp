#include <windows.h>
#include <cstdio>

DWORD tlsThreadName;

// 线程执行的任务方法
DWORD WINAPI Fun(LPVOID lpParam) {
    // 初始化线程数据
    LPVOID lpvData;
    // 通过 LocalAlloc 申请内存
    lpvData = LocalAlloc(LPTR, 20 * sizeof(char));
    sprintf((char*) lpvData, "Thread %lu", GetCurrentThreadId());
    // 保存当前值 -- 实际上 TLS 保存的是一个指针
    TlsSetValue(tlsThreadName, lpvData);

    for (int i = 0; i < 10; ++i) {
        // 通过 TlsGetValue 获取值
        printf("Thread %s: %d\n", (char*) TlsGetValue(tlsThreadName), i);
    }

    // 释放通过 LocalAlloc 申请的内存
    LocalFree(lpvData);
    return 0L;
}

int main() {
    // 通过 TlsAlloc 申请一个空槽位（索引）
    tlsThreadName = TlsAlloc();

    // 创建两个线程，运行并等待结束
    HANDLE hThread1 = CreateThread(NULL, 0, Fun, NULL, 0, NULL);
    HANDLE hThread2 = CreateThread(NULL, 0, Fun, NULL, 0, NULL);
    WaitForSingleObject(hThread1, INFINITE);
    WaitForSingleObject(hThread2, INFINITE);

    // 释放占用的索引
    TlsFree(tlsThreadName);
    return 0;
}