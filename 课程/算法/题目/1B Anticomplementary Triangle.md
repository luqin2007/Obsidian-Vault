> “范式杯”2023牛客暑期多校训练营1
> https://ac.nowcoder.com/acm/contest/57355/B

难度：Mid
# 题目

There are `n` points $P_1,P_2,\dots,P_n$ in a plane forming a convex polygon. The coordinates of the `i`-th point $P_i$ are $(x_i,y_i)$. You need to find three pairwise distinct indices $r,s,t\in\{1,2,\dots,n\}$, such that there exists a triangle $\triangle ABC$ satisfying  
- $\triangle ABC$ is the anticomplementary triangle of $\triangle P_rP_sP_t$.
- $P_i$ is inside or on the edge of $\triangle ABC$ for all $1 \leq i \leq n$.

$\triangle ABC$ is the anticomplementary triangle of $\triangle XYZ$ if $X,Y,Z$ are the midpoints of edge $BC,CA,AB$ respectively.
## 输入格式

The first line contains an integer $n\ (3 \leq n \leq 10^6)$, indicating the number of points.  
The $i$-th of the next $n$ lines contains two integers $x_i$ and $y_i\ (|x_i|,|y_i|\leq 10^9)$, indicating the coordinates of $P_i$. The vertices are guaranteed to form a convex polygon, and are given in counterclockwise order.
## 输出格式

Output three integers $r,s$ and $t$ indicating the selected indices. If there are multiple solutions, print any of them. You can output $r,s,t$ in an arbitrary order.
## 输入样例

```
4
0 0
1 0
1 1
0 1
```
## 输出样例

```
1 2 3
```
## 输入样例

```
5
-1 -9
3 1
5 8
-1 11
-8 -10
```
## 输出样例

```
2 4 5
```
## 样例解释

Here is the illustration for the second example.
![[Pasted image 20230807170418.png]]
## 代码限制
  
时间限制：C/C++ 2秒，其他语言4秒  
空间限制：C/C++ 524288K，其他语言1048576K  
Special Judge, 64bit IO Format: %lld
# 解析

> 反互补三角形：$\triangle ABC$ 与 $\triangle XYZ$ 中，若满足 X Y Z 分别为 $\triangle ABC$ 三边的中点，则 $\triangle ABC$ 是 $\triangle XYZ$ 的反互补三角形

题目要求找到凸多边形的三个点 $P_r, P_s, P_t$，使该多边形的所有点都不在 $\triangle P_rP_sP_t$ 的反互补三角形 $\triangle ABC$ 之外
# 思路

> 结论：由多边形顶点构成的面积最大的三角形对应的反互补三角形一定能包含所有多边形的点

根据输入和输出的数据量，要求以 `O(n)` 的时间复杂度找到面积最大的三角形所用到的三个顶点
1. 固定 r=1，找到面积最大的点 s，t（经测试，不做这一步也行）
2. 挪动 r，找到在此基础上的面积最大的 r 点
3. 挪动 s，找到在此基础上的面积最大的 s 点
4. 挪动 t，找到在此基础上的面积最大的 t 点
5. 重复 2-4，直到不再需要调整即可
# 答案

```cpp
#include <iostream>

using namespace std;
using ll = long long;
const int N = 3e6+10;

// 表示一个向量或点
struct PV {
    ll x, y;
    
    // 向量构建
    PV operator-(const PV &o) {
        return { x - o.x, y - o.y };
    }
    
    // 向量叉乘的模
    ll operator*(const PV &o) {
        return x * o.y - y * o.x;
    }
};

int n;
PV ps[N];

// 叉乘的模就是向量组成的平行四边形面积
// 可以用来反映三角形面积大小
ll area(int u, int v, int w) {
    return (ps[v] - ps[u]) * (ps[w] - ps[u]);
}

int ne(int i) {
    return i == n ? 1 : i + 1;
}

int pr(int i) {
    return i == 1 ? n : i - 1;
}

int main() {
    cin >> n;
    for (int i = 1; i <= n; ++i) {
        cin >> ps[i].x >> ps[i].y;
    }
    // 这题跳过了第一步
    int r = 1, s = 2, t = 3;
    // 调整，查找最大值
    ll S = area(r, s, t);
    ll SS;
    while (true) {
        if ((SS = area(r, s, ne(t))) > S) {
            t = ne(t);
            S = SS;
        } else if ((SS = area(r, s, pr(t))) > S) {
            t = pr(t);
            S = SS;
        } else if ((SS = area(r, ne(s), t)) > S) {
            s = ne(s);
            S = SS;
        } else if ((SS = area(r, pr(s), t)) > S) {
            s = pr(s);
            S = SS;
        } else if ((SS = area(ne(r), s, t)) > S) {
            r = ne(r);
            S = SS;
        } else if ((SS = area(pr(r), s, t)) > S) {
            r = pr(r);
            S = SS;
        } else break;
    }
    
    cout << r << " " << s << " " << t;
    return 0;
}
```