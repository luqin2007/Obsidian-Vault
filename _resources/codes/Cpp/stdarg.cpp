#include<iostream>
#include<cstdarg>

using namespace std;

void fun(const char *name, int arg_count, ...) {
    // fun: a(3)
    cout << "fun: " << name << "(" << arg_count << ")" << endl;

    va_list arg_ptr;
    va_start(arg_ptr, arg_count);
    //  ...[0]: 123
    cout << " ...[0]: " << va_arg(arg_ptr, int) << endl;
    //  ...[1]: hello
    cout << " ...[1]: " << va_arg(arg_ptr, const char*) << endl;
    //  ...[2]: 3.14
    cout << " ...[2]: " << va_arg(arg_ptr, double) << endl;
    va_end(arg_ptr);
}

int main() {
    fun("a", 3, 123, "hello", 3.14);
    return 0;
}