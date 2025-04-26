#include <cstring>

class A {
    int* value;

public:
    A() {
        value = new int[5];
    }
  
    A(const A &another) {
        value = new int[5];
        memcpy(value, another.value, 5 * sizeof(int));
    }
  
    ~A() {
        delete [] value;
        value = nullptr;
    }
};