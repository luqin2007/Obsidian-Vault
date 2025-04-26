> “范式杯”2023牛客暑期多校训练营1
> https://ac.nowcoder.com/acm/contest/57355/A

难度：Mid
# 题目

Given a 01-sequence (sequence consisting only of 0s and 1s) `s` of length `n`, you need to find two arrays `x` and `y` such that

- Array `x` and `y` are of equal length, and the length should not exceed 120.
- $1\leq x_i< y_i\leq n$ for all $1\leq i\leq m$, where `m` is the length of the two arrays.
- Algorithm $\mathrm{SORT}(x,y)$ can sort all 01-sequences of length `n` correctly **except for** the given one. Here algorithm $\mathrm{SORT}(x,y)$ takes as input a 01-sequence `a` of length `n`, and is described as follows:
	- Iterate over all integers from 1 to m. For each integer i during the iteration, swap $a_{x_i}$ and $a_{y_i}$ if $a_{x_i}$>$a_{y_i}$.
## 输入格式

The input contains multiple test cases.  
The first line of the input contains an integer $T\ (1 \leq T\leq 10^4)$, the number of test cases.  
For each test case, the first line contains an integer $n\ (2 \leq n\leq 16)$.  
The next line contains `s`, the 01-sequence of length `n`. It is guaranteed that sss is not sorted, i.e., there exists some integers $1 \leq i<j \leq n$ such that $s_i=1$ and $s_j=0$.
## 输出格式

For each test case, output $m\ (0 \leq m \leq 120)$ in the first line, indicating the length of array `x` and `y`.  
In the `i`-th of the next `m` lines, print $x_i$ and $y_i\ (1 \leq x_i<y_i \leq n)$ separated by a space.
## 输入样例

```
2
2
10
3
110
```
## 输出样例

```
0
2
1 2
2 3
```
## 代码限制

时间限制：C/C++ 1秒，其他语言2秒  
空间限制：C/C++ 524288K，其他语言1048576K  
Special Judge, 64bit IO Format: %lld
# 解析

给定长度为 `n` 的未排序 01 串 `a`，求两个长度为 `m` 的数组 `x`，`y`，按顺序执行：
	对第 i 组 x，y 中的元素，当 $a_{x_i} > a_{y_i}$ 时，交换 $a_{x_i}$ 与 $b_{y_i}$
要求对于除 a 外任意长度为 n 的 01 串，都可以按从小到大排序；而对于给定串 a 则无法正确排序

输出时，先输出数组长度 m，再输出两列，分别代表 x y 数组
# 思路

排序方面，可以直接使用冒泡排序的顺序，主要是如何判断，或者制定一个规则，使排序对串 a 无效，而对其他串有效。

与串 a 相同，则满足以下两个条件：
1. 给定串中 0 1 的位置与 a 串中 0 1 的位置相同
2. 给定串中 0 1 的个数与 a 串中 0 1 的个数相同

按 a 串中 0 和 1 的位置，将所有数字分成 2 组
- 0组：a 串中 0 对应位置的数字
- 1组：a 串中 1 对应位置的数字

寻找一组 0-1 对位置 l，r 作为 a 串的错误位置。取 l 为 a 串中第一个 1 的位置，r 为 a 串中最后一个 0 的位置。由于 a 是未排序的，一定满足最后一个 0 在第一个 1 之前。
- 将 l 位置上的数字与 0 组非 l 位置的数字比较并交换 -> l 位置为 0 组数字的最小值
- 将 1 组非 r 位置的数字与 r 位置上的数字比较并交换 -> r 位置为 1 组数字的最大值

对于除 l r 的剩余元素，先通过冒泡排序将其排好，m+=(n-3)(n-2)/2=105

之后，将 l r 元素与已经排好的其他元素进行合并。设 0 组元素的个数为 k，则整个串被分为了四段：1-l-k-r-n
- 将 l 与 $(l, k]$ 倒序比较
- 将 $[1,l)$ 与 l 比较
- 将 $[k+1,r)$ 与 r 比较
- 将 r 与 $[n,r)$ 倒序比较
# 答案

```cpp
#include <iostream>
#include <vector>

using namespace std;

char a[20];
vector<int> g0, g1;
vector<pair<int, int>> ans;

void resolve() {
    ans.clear();
    g0.clear();
    g1.clear();
    // 0 读入
    int n;
    cin >> n >> a + 1;
    // 1 分组，确定 l r
    int l, r;
    for (int i = 1; i <= n; ++i) {
        if (a[i] == '0') {
            g0.push_back(i);
        } else {
            g1.push_back(i);
        }
    }
    l = g1.front();
    for (auto &a: g1) if (a != l) ans.emplace_back(l, a);
    r = g0.back();
    for (auto &a: g0) if (a != r) ans.emplace_back(a, r);
    // 2 冒泡
    vector<int> id;
    for (int i = 1; i <= n; ++i)
        if (i != l && i != r)
            id.push_back(i);
    for (int i = 0; i < n - 2; ++i)
        for (int j = i + 1; j < n - 2; ++j)
            ans.emplace_back(id[i], id[j]);
    // 3 调整
    int k = g0.size();
    for (int i = k; i > l; --i) ans.emplace_back(l, i);
    for (int i = 1; i < l; ++i) ans.emplace_back(i, l);
    for (int i = k + 1; i < r; ++i) ans.emplace_back(i, r);
    for (int i = n; i > r; --i) ans.emplace_back(r, i);
    // 4 输出
    cout << ans.size();
    for (auto &[x, y]: ans) {
        cout << endl << x << " " << y;
    }
}

int main() {
    int t;
    cin >> t;
    while (t--) {
        resolve();
        cout << endl;
    }
}
```
