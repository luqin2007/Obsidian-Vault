#include <iostream>
#include <string>

using namespace std;

// 以毫米 mm 记录的线段长度
struct Length {
    const size_t len_mm;
};

// 字面量运算符
Length operator "" _mm(size_t v) {
    return Length{v};
}

Length operator "" _cm(size_t v) {
    return Length{v * 10};
}

Length operator "" _m(size_t v) {
    return Length{v * 1000};
}

Length operator "" _km(size_t v) {
    return Length{v * 1000000};
}

// 其他运算符
Length operator+(const Length &a, const Length &b) {
    return Length{a.len_mm + b.len_mm};
}

Length operator-(const Length &a, const Length &b) {
    return Length{a.len_mm - b.len_mm};
}

// 自定义 ostream 运算符用于直接输出 Length 结构体
basic_ostream<char>& operator<<(basic_ostream<char> &s, const Length &len) {
    if (len.len_mm) {
        size_t v = len.len_mm;
        while (v) {
            if (v >= 1000000) {
                s << (v / 1000000) << "km";
                v %= 1000000;
            } else if (v >= 1000) {
                s << (v / 1000) << "m";
                v %= 1000;
            } else if (v >= 10) {
                s << (v / 10) << "cm";
                v %= 10;
            } else {
                s << v << "mm";
                break;
            }
        }
    } else {
        s << "0mm";
    }
    return s;
}

int main() {
    Length a = 5_cm + 10_km - 100_m + 257_mm;
    cout << a << endl;
    return 0;
}