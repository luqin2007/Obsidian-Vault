---
tags:
  - 编程/books
---

# 快速上手

- 判断读完字符串
	- `scanf` 会返回一个 int，当值为 0 时表示没有读到任何值
	- `getchar()` 返回 `EOF`
- `getchar()` 返回一个 `int`，因为 `EOF` 比 `char` 可以表示的最大值更大
- `stdio.h` 定义了 `EXIT_FAILURE` 和 `EXIT_SUCCESS` 作为预定义的 `main` 函数返回值，`EXIT_SUCCESS=0`，也可以传入 `exit()` 结束程序
- 有时定义常量 `NUL` 表示 ASCII 的 `\0`，与 `NULL` 区分
# 数据
## 基本数据类型

- 标准 C 没有定义 long int 一定比 int 长，也没规定每种整形的实际表示范围，但定义了每种整形最少能保存多大的数字
- C 没有赋值语句，只有表达式语句，`=` 连接的赋值也是一种表达式
## 作用域

- 块作用域：`{}` 之间的所有代码
- 文件作用域：所有代码块之外的标识符，直到文件结尾都可以访问
- 原型作用域：函数原型允许标注形参名，该名称不必与函数定义中的形参名匹配，也不必与调用时传递的实参匹配
- 函数作用域：针对语句标签，一个函数中的所有语句标签必须唯一
## 链接属性

决定链接阶段，不同源文件中同名实体指向相同还是不同实体问题。

- `external`：所有源文件中同名个体指向同一个实体，又称全局实体
- `internal`：同一个源文件中所有声明的同名个体指向同一个实体，不同源文件指向不同实体
- `none`：无链接属性的变量总被作为单独个体，即多次声明被当作不同独立实体

判断方式为：
- 文件作用域中的声明默认具有 `external` 的链接属性，代码块中变量默认无链接属性
- 带有 `extern` 关键字修饰的声明具有 `external` 的链接属性
- 带有 `static` 关键字修饰的具有 `external` 属性的声明具有 `internal` 链接属性
## 存储类型

>[!note] 存储类型：Storage Class，存储变量值的内存类型，决定变量如何创建、何时销毁、保持多久

- 静态变量：存储于静态内存中，在程序运行时创建并初始化为 0，在程序执行期间始终存在
	- 声明在任何代码块之外的变量
	- 使用 `static` 修饰的代码块中的变量
	- 所有函数
- 自动变量：存储于堆栈中，声明时不初始化值，离开代码块时销毁，是在代码块中声明的变量的默认类型，可省略 `auto` 关键字
- 寄存器变量：<font color="#ff0000">建议</font>编译器将某变量存于寄存器中，访问效率更高，通过 `register` 修饰的自动变量
# 数组

设 `array` 是一个数组或指针，`array[3]` 与 `3[array]` 等效
## 多维数组

- 多维数组的数组名是一个数组的指针，使用 `(*数组名)[长度]` 表示
	- 长度可以省略，可以匹配任意长度数组：`(*数组名)[]`
	- 长度为 1 时可以作为单元素指针遍历：`(*数组名)[1]`

```c
int matrix[3][10];
// 下面几种都可以
int (*mp)[10] = matrix;
int (*mp1)[] = matrix;
int (*mp2)[1] = matrix;

// 遍历
for (int i = 0; i < 3; ++i) {
    for (int j = 0; j < 10; ++j) {
        printf("%d ", matrix[i][j]);
    }
}
puts("");
// 使用 (*p)[1] 单个遍历
for (int i = 0; i < 30; ++i) {
    printf("%d ", mp2[i][0]);
}
```

- 多维数组初始化时，可以只加最外层的大括号，初始化时按行赋值
## 指针常量

```cpp
// 指向一个常整形的指针
// 指针本身的地址可变，指向的值不可变
int const * p;
// 指向一个整形的常指针
// 指针指向的值可变，本身的地址不可变
int * const p;
```
## 关系运算

- 对于不在同一个数组里的两个指针，`>` 和 `<` 运算符的行为是未知的，不一定正确比较指针大小
# 字符串

- 操作多个字符串的字符串函数，若操作的字符串地址有重叠部分，其结果是未定义的
- 字符串常量（表示为 `char*`）存放在静态存储区，即使没有加 const 也是无法修改的。若要修改字符串值需要使用 `char[]`，如 `char[] = "hello"`

| 函数名部分 | 位置  | 意义                            |
| :---- | --- | ----------------------------- |
| str   | 前缀  | 字符串函数                         |
| strn  | 前缀  | 需要提供字符串长度，而不是根据 `NUL` 判断字符串结束 |
| mem   | 前缀  | 操作内存的函数                       |
| r     | 中缀  | 查找时，从后往前查找                    |
| c     | 中缀  | 查找时，匹配不满足给定条件的情况              |
## 字符串长度

- `strlen(str)` 返回值类型为无符号整形 `size_t`，与 0 进行大小比较是没有意义的

`````col
````col-md
flexGrow=1
===
```c
if (strlen(str1) - strlen(str2) >= 0) {
   // always true
}
```
````
````col-md
flexGrow=1
===
```c
if (strlen(str1) - 10 >= 0) {
   // always true
}
```
````
`````

以上两个 `if` 判断结果总是 `true`，因为无符号与有符号运算完成后结果为无符号，总是大于 0。可能解决的方法有：
- 直接进行大小比较，不进行减法操作
- 运算时转换为有符号整形，但可能溢出（？但一般不会出现）
## 长度受限的字符串

- 带有 `strn` 前缀的字符串函数需要指定字符串长度，如 `strncpy`，`strncat` 等
- 长度受限字符串函数不会检查字符串中的 `\0`，因此在继续使用 `str` 开头的函数或 `printf("%s")` 时需要特别注意
	- `strncat` 例外，它总会在结果字符串后增加一个 `\0`
## 字符串查找

- `strchr`：类似 `strstr`，但针对字符
- `strpbrk`：查找字符串 str 中任意一个字符串 group 中的字符的位置；否则返回 `NULL`
	- `char* strpbrk(const char* str, const char* group)`

```c
void printCharPosition(const char *str, const char *p) {
    if (p) printf("Pos at %lld\n", p - str);
    else printf("Not found\n");
}

int main() {
    const char *string = "Hello there, honey";
    char *ans = strpbrk(string, "aeiou");
    printCharPosition(string, ans);
    return 0;
}
```

- `strspn`：查找 `buffer` 中满足 `group` 中的字符的前缀长度
	- `size_t strspn(const char* buffer, const char* group)`
	- `size_t strcspn(const char* buffer, const char* group)`

```c
size_t len1, len2;
const char *buffer = "25,142,330,Smith,J,239-4123";
// len("25") = 2
len1 = strspn(buffer, "0123456789");
// len("25,142,330,") = 11
len2 = strspn(buffer, ",0123456789");
```

- `strtok`：根据字符串 `sep` 中的任意字符将一个字符串 `str` 分割为多个字符串（每个子串被称为 `token`），并将标记丢弃 **（会改变字符串本身）**
	- `char* strtok(char *str, const char* sep)`
	- 第一个参数 `str` 可以为 `NULL`，默认为上次搜索位置 + 1
	- 类似于其他语言的 `split` 方法，但会修改字符串本身

```c
int main() {
    // 注意不能使用 char str*，否则字符串存在常量区， strtok 无法写入
    char str[] = "Hello there, honey";
    char *sep = " ,";
    size_t len = strlen(str);
    
    for (char *token = strtok(str, sep); token; token = strtok(NULL, sep)) {
        // 输出切分后的字符串
        printf("token: %s\n", token);
        // 输出切分后的原始字符串，使用 | 表示 NUL
        printf("origin: ");
        for (int i = 0; i < len; ++i) {
            if (str[i]) {
                printf("%c", str[i]);
            } else {
                printf("|");
            }
        }
        // token: Hello
        // origin: Hello|there, honey
        
        //token: there
        //origin: Hello|there| honey
        
        //token: honey
        //origin: Hello|there| honey
        printf("\n\n");
    }
    return 0;
}
```
## 异常信息

当调用系统函数时，若失败有时会返回一个整形错误代码 `errno`，该标记指向一个用于描述错误的字符串指针，通过 `strerror` 获取

```
char* strerror(int errno);
```
## 字符操作

在 `ctype.h` 中存在很多字符操作相关函数。使用标准库函数可以提高可移植性

| 函数 | 作用 |
| ---- | ---- |
| iscntrl | 判断字符为控制字符 |
| isspace | 判断字符为空白字符（` `，`\f`，`\n`，`\r`，`\t`，`\v`） |
| isdigit | 判断字符为十进制数字 0-9 |
| isxdigit | 判断字符为十六进制数字 0-9，a-f，A-F |
| islower | 判断字符为小写字母 a-z |
| isupper | 判断字符为大写字母 A-Z |
| isalpha | 判断字符为字母，a-z，A-Z |
| isalnum | 判断字符为字母或数字，a-z，A-Z，0-9 |
| ispunct | 判断字符为标点符号，任何不属于字母或数字的图形字符 |
| isgraph | 判断字符为任何图形字符 |
| isprint | 判断字符为可打印字符，包括图形字符和空白字符 |
| toupper | 将小写字母转化为大写字母 |
| tolower | 将大写字母转化为小写字母 |
## 内存操作

`string.h` 还包括一组直接操作内存的函数
- `memcpy` 操作源和目标内存有重叠时结果未定义，但 `memmove` 允许源和目标重叠
# 结构体

```c
struct SIMPLE { int a; char b; float c; };
typedef struct { int a; char b; float c; } Simple;

struct SIMPLE a;
Simple b;
```

- 使用 `struct 名称 {}` 声明的名称为标签（`tag`），后续通过 `struct 名称` 声明其他结构体
- 使用 `typedef struct {} 名称` 声明的名称为一个新类型，后续直接通过名称声明其他结构体

```c
struct A {
    // 错误：循环引用
    struct A perr;
    // 正确
    struct A *pok;
}
```

注意结构体允许自引用，但只能用于**指针**。

- 不完整声明：当两个结构体之间需要互相引用时，可以使用不完整声明，此声明不带有结构体内容（`{}`）

```c
// 不完整声明
struct B;

struct A {
    struct B *ptr;
};

struct B {
    struct A *ptr;
};
```

但不完整声明只能用于**指针**。

- 结构体在内存中不是紧密排布的，需要满足一定的内存偏移规律以提高访问性能 - 变量地址相对结构体位置的偏移量应当被 4 或 8 整除。
	- 通过 `stddef.h` 中的 `offsetof(type, member)` 检查 `type` 结构体中 `member` 成员的内存偏移量
# 联合

`union` 类型初始化只能初始化成第一个类型

```c
union {
    int a;
    float b;
    char c[4];
} x = { 5 };
```
# 动态内存分配

- `calloc`：类似 `malloc`，但会自动将申请后的内存填充 0
	- `void* calloc(size_t num_elements, size_t element_size);`
- `realloc`：可以扩大或缩小已分配内存块的大小
	- `void* realloc(void* ptr, size_t new_size)`
	- 若扩大时无足够空间，则申请一块足够大的新内存并将原位置数据复制过去，返回新地址
	- 若 `ptr=NULL`，等效于 `malloc`
- `malloc`，`alloc`，`realloc` 失败则会返回 `NULL`
# 高级指针
## 高级声明

如何读一个带有指针的变量的类型（或者翻译成高阶声明更贴切一点？）：

`int f`：整形
`int *f`：整形指针
`int f()`：函数，返回整形

`int *f()`：根据运算符优先级，优先执行函数调用操作符 `()`，然后解引用得到一个 `int`，因此 `f` 是一个函数，函数返回类型为 `int` 指针
`int (*f)()`：第一对括号为聚组使用，优先级更高。运算时先解引用后进行函数调用，因此 `f` 是一个函数的指针，函数返回类型为 `int`
`int *(*f)()`：运算时先解引用，然后进行函数调用，最后再解引用产生一个 `int`，因此 `f` 是一个函数的指针，函数返回类型为 `int` 指针
`int *f[]`：下标访问的优先级高于解引用，运算顺序为先进行下标调用，然后解引用得到一个 `int`，因此 `f` 的类型为一个数组，数组元素为 `int` 指针

`int f()[]`：先执行函数调用，然后进行下标访问，返回一个 `int`，因此 `f` 的类型为一个函数，函数返回值为一个 `int` 数组。但由于函数返回值类型不能是数组，因此该类型非法
`int f[]()`：先执行下标访问，然后进行函数调用，返回一个 `int`，因此 `f` 的类型为一个返回值为 `int` 的函数数组。但由于函数长度不定，因此该类型非法
`int (*f[])()`：先执行下标访问，然后解引用，最后进行函数调用后返回一个 `int`，因此 `f` 类型是一个数组，数组类型为函数指针，函数返回值为 `int`
## 字符串常量

- 字符串字面量实际表示一个指针 - 指向该串首字符的指针
	- `"xyz" + 1`：`"yz"`
	- `*"xyz"`：字符 `x`
	- `"xyz"[2]`：字符 `z`
# 预处理器
## 预定义符号

| 符号         | 含义               |
| ---------- | ---------------- |
| `__FILE__` | 编译文件名（`*.c`）     |
| `__LINE__` | 当前文件行号           |
| `__DATA__` | 编译日期             |
| `__TIME__` | 编译时间             |
| `__STDC__` | 若遵循 ANSI C 则返回 1 |
## `#define`

- 宏定义需要多行内容时，行末使用 `\`
- 一个宏中可以包含另一个宏的内容，但宏不能递归
- 一些编译器允许通过命令行定义宏
- **特别注意自定义宏时候小心展开细节，必要的情况下加入括号**
## 条件编译

`#if` 中 `defined` 可以检查某标签是否被定义。以下各语句块中的语句等效：

`````col
````col-md
flexGrow=1
===
```c
#if defined(symbol)
#ifdef symbol
```
````
````col-md
flexGrow=1
===
```c
#if !defined(symbol)
#ifndef symbol
```
````
`````
## 文件包含

- 标准 C 要求编译器至少支持 8 层头文件包含
- 尽管通过某些方法可以消除嵌套包含的副作用，但总会拖慢编译过程 -- `#include` 的合并过程不可或缺
## 其他指令

- `#error 异常信息`：在编译时产生编译错误
- `#line number ["string"]`：指定行号和文件名（将更改 `__LINE__` 和 `__FILE__`）
- `#progma`：调用编译器特性
- `#`：类似 `;`，无效指令
# 输入输出函数
## 错误报告

任何一个操作系统执行的任务都有失败的可能，尤其是 IO 操作。当系统失败时，标准库通过 `errno` 变量将信息传递给程序。这个变量位于 `errno.h` 中。

**当且仅当库函数失败时才会设置 `errno`，程序错误不会产生**

`stdio.h` 中定义了 `perror` 函数用于简化错误报告类型：

```c
void perror(const char *message);
```

当 `message` 指向一个非空字符串，则打印该字符串，后接一个用于描述当前 `errno` 的信息
## 终止执行

当 `main` 函数结束时会返回一个 `int`，事实上该返回值会最终传递给 `exit` 函数以结束程序。

`stdlib.h` 头文件定义了 `exit` 函数，程序中也可以手动调用以立即退出程序。

```c
void exit(int status);
```

`status` 指示程序是否正常结束：
- `EXIT_SUCCESS`：程序成功执行，正常结束
- `EXIT_FAILURE`：程序运行失败
- 其他：不同编译器意义不同
## IO

### 流

所有的 IO 操作抽象成简单的字节移入移出的操作。这类字节流称为*流（stream）*。程序只需要创建正确的流并按正确的顺序读写数据即可。

> [!note] 流：IO 操作的抽象，提供简单的字节移入移出操作
> 流按内容分为两种：文本流和二进制流，读写内容分别是文本信息和二进制数据。
> - 文本流库函数可以完成诸如不同系统换行符差异的翻译
> - 二进制流则是完全按照实际 IO 数据进行写入和读出

> [!note] 缓冲区：buffer，程序的一块内存，暂存设备数据的内存区域
> - 缓冲区只有被写满或手动调用 `flush` 命令时才会将数据写入到设备或文件中
> - 输入时会在缓冲区空时再从设备或文件读取下一部分数据以填充缓冲区

> [!note] 完全缓冲：fully buffered，写入、读出操作并不直接操作对应的设备，而是读写缓冲区以提高效率。C 大部分流都是完全缓冲的

只有操作系统确定与交互设备无联系时才会使用完全缓冲，其他设备的策略与编译器实现有关。一个常见策略是将输入输出缓冲区联系在一起，在输入时同时刷新输出缓冲区。

缓冲区输出不是立即的，因此 `printf` 不一定在运行时就立即输出，但输出顺序是一定的。可使用 `fflush` 立即输出

```c
printf("...");
fflush(stdout);
```
### 文件

`FILE` 数据结构存于 `stdio.h` 头文件中，用于访问一个流。一般来说这是一个指针。

标准 C 程序运行时都至少提供三个流，其类型都是 `FILE*`
- `stdin`：标准输入流 - 默认输入来源
- `stdout`：标准输出流 - 默认输出设置
- `stderr`：标准错误流 - 错误信息写入位置，默认与 `stdout` 相同

很多系统（DOS、UNIX）都支持通过以下方法重定向输入、输出流

```shell
# 输入流：[data] 文件
# 输出流：[answer] 文件
program < data > answer
```
## 常量

- `EOF`：文件末尾；一些 IO 函数（`fclose`，`fputs` 等）发生错误也返回这个值
	- IO 函数发生错误返回 EOF，未发生错误时返回 0
- `MAX_LINE_LENGTH`：按行读取的缓冲区大小
- `FOPEN_MAX`：同时可以打开的文件数量最大值（包括三个标准流），至少为 8
- `FILENAME_MAX`：编译器支持的最长路径名
## 函数
### 读写函数

`````col
````col-md
flexGrow=1
===

| 类型 | 输入 | 输出 | 描述 |
| ---- | ---- | ---- | ---- |
| 字符 | getchar | putchar | 读写单个字符 |
| 文本行 | gets | puts | 按行的输入输出文本 |
| 格式化文本 | scanf | printf | 格式化输入输出文本 |
| 二进制数据 | fread | fwrite | 读写二进制数据 |

````
````col-md
flexGrow=1
===

| 函数族 | 用于所有流 | 仅用于标准流 | 用于内存字符串 |
| ---- | ---- | ---- | ---- |
| getchar | fgetc，getc | getchar | （直接使用下标） |
| putchar | fputc，putc | putchar | （直接使用下标） |
| gets | fgets | gets | （strcpy） |
| puts | fputs | puts | （strcpy） |
| scanf | fscanf | scanf | sscanf |
| printf | fprintf | printf | sprintf |

````
`````
具体函数族包含的函数：
- `getc`，`putc`，`getchar`，`putchar` 不是函数，是宏
- `int ungetc(int char, FILE *stream);` 可以将之前读出的字符重新塞回缓冲区中
### 流操作函数

`````col
````col-md
flexGrow=3
===
`FILE *fopen(const char *name, const char *mode);`
- 打开一个流。`name` 是文件名，`mode` 为模式
````
````col-md
flexGrow=2
===
| 操作对象 | 读取 | 写入（覆盖） | 追加 | 更新 |
| ---- | ---- | ---- | ---- | ---- |
| 文本数据 | r | w | a | a+ |
| 二进制数据 | rb | wb | ab | ab+ |
````
`````
`FILE *freopen(const char *name, const char *mode, FILE *stream);`
- 重新打开流：先试图关闭已知流，然后重新按前面的参数打开
- 关闭或重新打开失败时返回 NULL
### 刷新、定位函数

- `int fflush(FILE *stream)`：强迫刷新输出缓冲区
- `long ftell(FILE *stream)`：返回当前流读写位置
	- 二进制流：当前位置距离文件起始位置间的字节数
	- 文本流：可用于 `fseek` 的偏移量，但不一定准确反应当前位置到起始位置的字符数
- `int fseek(FILE *stream, long offset, int from)`：设置流读写位置
`````col
````col-md
flexGrow=1
===
- `from`：相对位置
- 副作用：`ungetc` 返回的字符被废弃
````
````col-md
flexGrow=1
===

| `from` 值 | 相对位置 | offset |
| ---- | ---- | ---- |
| SEEK_SET | 文档开头 | 非负值 |
| SEEK_CUR | 当前位置 | 可正可负 |
| SEEK_END | 文档末尾 | 可正可负，正值定位到文件尾之后 |

````
`````
- `void rewind(FILE *stream)`：将读写指针设置回流起始位置，并清除错误提示
- `int fgetpos(FILE *stream, fpos_t *position)`，将文件流当前位置存储到 `position` 中
- `int fsetpos(FILE *stream, const fpos_t *position)`，将文件位置还原到某个流中
### 改变缓冲区

`setbuf(FILE *stream, char *buf)`
- 将设置流缓冲区，`buf` 长度必须是 `BUFSIZ`，该常量定义于 `stdio.h`
- `buf=NULL` 时，关闭流缓冲区，直接读写流

`setvbuf(FILE *stream, char *buf, int mode, size_t size)`
`````col
````col-md
flexGrow=3
===
- 设置流缓冲区，定义缓冲区类型和大小，通常为 `BUFSIZ` 或其整数倍
- `buf=NULL` 时，`size` 必须为 0
- `mode` 指定缓冲区类型
````
````col-md
flexGrow=2
===

| `mode` 值 | 说明 |
| ---- | ---- |
| `_IOFBF` | 指定一个完全缓冲流 |
| `_IONBF` | 指定一个不缓冲流 |
| `_IOLBF` | 指定一个行缓冲流（写入换行符时刷新） |

````
`````
### 流状态信息

- `int feof(FILE *stream)`：流是否位于文件尾
- `int ferror(FILE *stream)`：流是否有错误状态
- `void clearerr(FILE *stream)`：清除流错误状态
### 临时文件

- `FILE *tmpfile(void)`：以 `wb+` 模式创建一个临时文件，文件被关闭或程序结束时自动删除
- `char *tmpnam(char *name)`：创建临时文件名称，每次调用返回一个新名称
	- `name` 为 `NULL` 时，返回一个指向静态数组的指针，即临时文件名
	- `name` 非 `NULL` 时，默认为长度不小于 `L_tmpnam` 的字符串指针为文件名
	- 在不超过 `TMP_MAX` 次调用下，每次返回的值保证不重复且不存在
### 文件操纵函数

- `int remove(const char *name)`：删除文件
- `int rename(const char *old, const char *new)`：重命名或移动文件
# 标准函数库
## 数字函数
### 整形

部分整形参数位于 `<stdlib.h>` 中

数学运算：
- `int abs(int value)`：绝对值
	- `long labs(long value)`
- `div_t div(int numerator, int denominator)`：整形除法
	- `ldiv_t div(long numerator, long denominator)`

`div_t` 结构为：

```c
struct div_t {
    int quot;  // 商
    int rem;   // 余数
}
```

随机数：
- `int rand()`：获取一个 $[0, RAND\_MAX]$ 的随机数，`RAND_MAX` 至少为 32767
- `void srand(unsigned int seed)`：设置随机数种子

字符串转换：
- `int atoi(const char *str)`：字符串转换为数字，忽略前导空格，转换到第一个无效数字串
	- `long atol(const char *str)`：长整型版本
- `long strtol(const char* str, char **unused, int base)`：字符串转换为数字
	- `unsigned long strtoul(const char* str, char **unused, int base)`
	- `unused`：转换后一个字符的指针
	- `base`：基数（进制数）
	- 若值太大，返回 `LONG_MAX` 或 `ULONG_MAX`；值太小返回 `LONG_MIN`；同时，`errno=ERANGE
### 算法

`stdio.h` 存在部分算法
`````col
````col-md
flexGrow=3
===
- `void qsort(void *base, size_t n_elements, size_t el_size, int(*compare)(const void *, const void *))`：对数组进行就地快排
- `void *bsearch(const void *key, const void *base, size_t n_element, size_t el_size, int(*compare)(const void *, const void *))`：二分查找一个特定元素
````
````col-md
flexGrow=1
===

| 参数 | 说明 |
| ---- | ---- |
| base | 操作的数组 |
| n_elements | 数组元素个数 |
| el_size | 每个元素长度 |
| compare | 大小比较函数 |
| key | 待查找元素 |

````
`````
### 浮点

浮点运算多在 `math.h` 头文件中，大多数类型为 `double` 的。包含了大多数数学运算、三角函数等，详见文档
## 时间日期

简化时间和日期处理，位于 `time.h` 

- `clock_t clock(void)`：获取程序开始运行到现在的运行时间（近似）
	- 单位：处理器刻，-1 表示处理器无法提供时间或值过大
	- 常量：`CLOCKS_PER_SEC`，每秒包含的处理器刻
	- 若想获取精确值，应当在 `main` 开始时调用一次，然后求差值

- `time_t time(time_t *time)`：获取当前时间和日期
	- 无法提供时间和日期，或值过大，返回 -1
	- 若参数非 `NULL` ，则参数为存储时间和日期值的变量
	- 标准库并未定义 `time_t` 的具体格式类型
- `char *ctime(time_t *time)`：指向一个格式化后的字符串指针
	- 可能得实现方式为 `asctime(localtime(time))`

```c
time_t time;
time(&time);
// Sun Jul 4 04:02:48 1976\n\0
const char *str = ctime(&time);
```

- `double difftime(time_t *time1, time_t *time2)`：获取两个时间之差，单位为秒
- `struct tm *gmtime(const time_t *time)`：将 `time_t` 转换为 UTC 时间  `tm` 结构体
	- `struct tm *localtime(const time_t *time)`
	- `tm` 结构体可方便访问时间日期的各部分值，均为 `int` 类型

| 成员 | 范围 | 说明 |
| ---- | ---- | ---- |
| tm_sec | 0-61 | 秒 |
| tm_min | 0-59 | 分 |
| tm_hour | 0-23 | 时 |
| tm_mday | 1-31 | 日 |
| tm_mon | 0-11 | 月（0 为一月） |
| tm_year | >=0 | 年（0 为 1900 年） |
| tm_wday | 0-6 | 星期（0 为星期日） |
| tm_yday | 0-365 | 一年第几天（0 为一月一日） |
| tm_isdat |  | 是否为夏令时 |
- `char *asctime(const struct tm *tm_ptr)`：将 `tm` 结构转化为字符串
	- `size_t strftime(char *str, size_t max_size, const char *format, const struct tm *tm_ptr)`：将一个 `tm` 结构体转化为字符串，并按 `format` 定义的模板格式化
		- 字符串长度应小于 `max_size`
		- 返回字符串长度，超过该长度或格式化失败返回 -1
- `time_t mktime(struct tm *tm_ptr)`：将一个 `tm` 结构体转化为 `time_t`
## 非本地跳转

`setjmp.h` 头文件提供类似 `goto` 的跳转机制甚至可以跨作用域跳转。

- `int setjmp(jmp_buf state)`：初始化一个跳转上下文；此时调用的函数称为顶层函数
	- 若用于初始化 `jmp_buf` 变量，返回值为 0
	- 若在其他地方跳转回来，此时返回值为 `longjmp` 函数 `value` 参数的值
- `void longjmp(jmp_buf state, int value)`：跳转到 `state` 指定位置
	- 跳转到 `setjmp` 刚执行完的场景，并将其返回值置为 `value`
## 信号

程序中大多数事件都是程序本身产生的，例如各种语句和输入输出等。还有一些特殊事件由系统或其他程序产生，如中断程序等。

> [!note] 信号：signal，表示一种事件，可能异步发生，有点类似中断。当遇到某个事件后，程序需要做出反应，或执行默认行为。

标准没有规定信号的缺省行为，但通常为终止程序。`signal.h` 头文件记录了标准定义的信号和处理方法。

| 信号 | 说明 |
| ---- | ---- |
| `SIGABRT` | 程序请求异常终止，常由 `abort` 函数引发 |
| `SIGFPE` | 算术错误，常见有算术上溢、下溢，除零错误 |
| `SIGILL` | 非法指令，可能由于不正确的编译器设置（CPU 指令集设置错误）或程序执行流错误（使用未初始化的函数指针调用函数） |
| `SIGSEGV` | 内存非法访问，如访问非法地址或违反内存对齐的边界要求 |
| `SIGINT` | 异步，收到交互性注意信号，常因为用户试图中断程序 |
| `SIGTERM` | 异步，收到终止程序请求，通常不配备信号处理函数 |

- `int raise(int sig)`：引发一个信号
- `void (*signal(int sig, void(*handler)(int) ) )(int)`：指定信号处理函数
	- `sig`：被处理信号
	- `handler`：信号处理函数指针
	- `SIG_DFL`：可作为 `handler` 传入，表示默认函数
	- `SIG_IGN`：可作为 `handler` 传入，表示忽略信号

信号处理函数相对于普通函数，存在一些限制：
- 所有异步信号的处理函数不能用除 `signal.h` 外的任何库函数
- 无法访问除 `volatile sig_atomic_t` 外的其他静态变量
- 除 `SIGFPE` 外，处理函数返回后从信号发生点恢复执行
## 打印可变参列表

打印可变参数需要同时导入 `stdio.h` 和 `stdarg.h`
- `int vprintf(const char *format, va_list arg)`
- `int vfprintf(FILE *stream, const char *format, va_list arg)`
- `int vsprintf(char *buffer, const char *format, va_list arg)`
## 执行环境

- `stdlib.h`：定义了一些退出程序的方法和系统信息方法
	- `void abort()`：表示不正常的终止程序，引发 `SIGABRT` 信号
	- `void atexit(void (func)(void))`：将某函数注册为退出函数，在程序退出时调用
	- `void exit(int status)`：正常终止程序，会按倒序依此调用 `atexit` 注册的函数
	- `char *getenv(const char *name)`：获取当前系统环境，包括环境变量
	- `int system(const char *command)`：由系统执行命令（cmd、bash）
		- 返回值由编译器定义
		- `system(NULL)` 的返回值表示是否存在命令处理器
- `assert.h`：断言，声明某个变量应当为真，否则产生异常
	- `void assert(int expression)`
		- `assert(expression)`：宏定义版
	- 当存在 `#define NDEBUG` 标记时，忽略所有断言
## locale

`````col
````col-md
flexGrow=1
===
C 支持设置 locale，可能会影响部分库行为，适应不同地区的使用习惯（如语言、时间等）。

- `char *setlocale(int category, const char *locale)`
	- 设置整个或部分 locale
	- `locale==NULL` 时返回当前 locale 值
````
````col-md
flexGrow=1
===

| category | 说明 |
| ---- | ---- |
| LC_ALL | 全局 locale |
| LC_COLLATE | 对照序列，影响 `strcoll` 与 `strxfrm` 的行为 |
| LC_CTYPE | 影响 `ctype.h` 中函数使用的字符类型分类信息 |
| LC_MONETARY | 影响格式化货币使用符号 |
| LC_NUMERIC | 影响非货币的数字字符和小数点符号 |
| LC_TIME | 影响 `strftime` 函数行为 |

````
`````
# 参考

```cardlink
url: https://book.douban.com/subject/35216781/
title: "C和指针"
description: "《C和指针》提供与C语言编程相关的全面资源和深入讨论。本书通过对指针的基础知识和高级特性的探讨，帮助程序员把指针的强大功能融入到自己的程序中去。全书共18章，覆盖了数据、语句、操作符和表达式、指针、..."
host: book.douban.com
image: https://img1.doubanio.com/view/subject/l/public/s33774070.jpg
```
