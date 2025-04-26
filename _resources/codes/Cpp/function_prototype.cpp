// 函数原型
int add(int first_value, int second_value, int third_value, int forth_value);
int add(int a, int b, int c);
int add(int, int);

// 具体实现
int add(int v1, int v2, int v3, int v4) {
    return add(v1, v2, v3) + v4;
}

int add(int a, int b, int c) {
    return add(a, b) + c;
}

int add(int a, int b) {
    return a + b;
}