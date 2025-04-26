#include <iostream>
#include <thread>

using namespace std;

thread_local int value;

int main() {
    thread ta([](){
        value = 5;
        this_thread::sleep_for(chrono::seconds(1));
        // [a] Value is 5
        cout << "[a] Value is " << value << endl;
    });
    thread tb([](){
        value = 15;
        this_thread::sleep_for(chrono::seconds(3));
        // [b] Value is 15
        cout << "[b] Value is " << value << endl;
    });
    thread tc([](){
        value = 55;
        this_thread::sleep_for(chrono::seconds(5));
        // [c] Value is 55
        cout << "[c] Value is " << value << endl;
    });

    ta.join();
    tb.join();
    tc.join();
}