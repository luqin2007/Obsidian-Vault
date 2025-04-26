#include <iostream>
#include <thread>
#include <future>

using namespace std;

int main() {

    promise<int> p;
    future<int> f = p.get_future();

    thread t_show_value([&] () {
        cout << "Waiting for value..." << endl;
        cout << "Future value is " << f.get() << endl;
    });

    thread t_get_value([&] () {
        cout << "Getting value..." << endl;
        this_thread::sleep_for(chrono::seconds(5));
        cout << "Value updated." << endl;
        p.set_value(10);
    });

    t_show_value.join();
    t_get_value.join();
    return 0;
}