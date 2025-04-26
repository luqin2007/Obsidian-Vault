C 风格字符串相关函数位于 `cstring`/`string.h` 头文件中

> 引入该头文件除了为了处理 c 风格字符串，还常用于 memcpy，memset 等函数的引入

* 字符串长度
    * `strlen` / `wstrlen`：接受一个 `char*` 或 `wchar_t*`，返回一个到 `\0` 的长度
    * `strnlen` / `wcsnlen`：接受一个 `char*` 或 `wchar_t*` 和一个 `size_t` 类型的参数，指定最大长度
* 字符串连接
    * `strcat`：在第一个字符串后链接第二个字符串，已弃用：不一定有足够的连续内存，可能覆盖其他数据
    * `strcat_s`：安全版本，通过一个 `cerrno` 头文件中定义的返回值判断是否连接成功
        * 0：连接成功
        * `EINVAL`：源字符串或目标字符串中有一个是 `nullptr`
        * `ERANCE`：源字符串长度过小，无法容纳目标字符串长度
* 字符串复制
    * `strcpy`：将字符串复制到目标数组，第一个参数是目标位置，第二个参数是原位置
    * `strcpy_s` / `wcscpy_s`：安全版本在字符串地址为 `nullptr` 或目标字符串长度不足时抛出异常
* 字符串比较
    * `strcmp` / `wcscmp`：若所有字符相同，返回 0；否则返回第一个不同字符的差值
* 字符串搜索
    * `strstr`：返回一个指针，指向第一个参数中第二个参数字符串的位置，不存在则返回 `nullptr`。

```cpp
#include<cstring>

using namespace std;

int main() {
    const char *pstr{"aaabbbcccdddaaacccbbbdddaaaeeeaaa"};
    const char *psearch{"aaa"};
    int count{0};
    char *pfound, *pcurrent{const_cast<char *>(pstr)};
    while (true) {
        pfound = strstr(pcurrent, psearch);
        if (pfound) {
            ++count;
            // find at 140697701994504
            // find at 140697701994507
            // find at 140697701994519
            // find at 140697701994531
            cout << " find at " << (intptr_t) pcurrent << endl;
            pcurrent = pfound + strlen(psearch);
        } else {
            break;
        }
    }

    // There are 4 aaa in aaabbbcccdddaaacccbbbdddaaaeeeaaa
    cout << "There are " << count << " " << psearch << " in " << pstr << endl;
    return 0;
}
```
