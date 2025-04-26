#include <pthread.h>
#include <cstdio>

pthread_key_t key;

// 线程执行的任务方法
void *fun(void *p) {
    // 初始化线程变量
    char name[21];
    pthread_t thread = *static_cast<pthread_t *>(p);
    pthread_getname_np(thread, name, (size_t) 20);
    pthread_setspecific(key, name);

    for (int i = 0; i < 10; ++i) {
        // 通过 TlsGetValue 获取值
        printf("Thread %s: %d\n", (char *) pthread_getspecific(key), i);
    }
  
    return nullptr;
}

int main() {
    // 申请一个键
    pthread_key_create(&key, nullptr);
    // 创建两个线程，执行方法
    pthread_t ps0, ps1;
    pthread_create(&ps0, nullptr, fun, &ps0);
    pthread_create(&ps1, nullptr, fun, &ps1);
    pthread_join(ps0, nullptr);
    pthread_join(ps1, nullptr);
    // 移除键
    pthread_key_delete(key);
    return 0;
}