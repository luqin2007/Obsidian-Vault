#include <iostream>
#include <thread>
#include <future>

using namespace std;

int main() {
    packaged_task<int(int)> task ([] (int value) {return value * 2;});
    future<int> f = task.get_future();

    thread(std::move(task), 10).detach();
    cout << "Result is " << f.get() << endl;
    return 0;
}