> https://ac.nowcoder.com/acm/contest/72647
> https://ac.nowcoder.com/acm/contest/72647/E

难度/题目：(A,B组) E

> [!note]
> 官方答案 dp 解法 
# 题目

小红拿到了一棵有根树，其中有一些节点被染成了红色。树的根节点是 1 号节点。  
小红希望你给每个节点的权值赋值为 1 或者 2，需要满足每个红色节点的子树节点权值之和为 3 的倍数。  
请你帮小红求出赋值的合法方案数。由于答案可能过大，请对$10^9+7$取模。
## 输入格式

第一行输入一个正整数$n$，代表节点的数量。  
第二行输入一个长度为$n$的字符串，第$i$个字符为'R'代表$i$号节点被染成红色，为'W'代表未被染色。  
第三行输入$n-1$个正整数$a_i$，第$i$个正整数代表$i+1$号节点的父亲编号。  
$1\leq n \leq 10^5$
## 输出格式

一个整数，代表赋值的方案数模$10^9+7$的值。
## 样例
### 输入

```
3
RWW
1 1
```
### 输出

```
2
```
### 解释

有 111 和 222 两种方案
### 输入

```
3
RRR
1 1
```
### 输出

```
0
```
## 代码限制

时间限制：C/C++ 1秒，其他语言2秒  
空间限制：C/C++ 262144K，其他语言524288K  
64bit IO Format: %lld
# 解析
# 思路

我们记录 `dp[u][i][j]` 代表以 `u` 节点的子树，当 `u` 节点赋值为 `i` 的时候，该子树权值和模3等于 `j` 的方案数。在 `dfs` 的时候转移即可。

> [!note]
> 树形dp：树形存储 dp 结果，使用 dfs 遍历，先初始化所有叶节点，然后自下而上合并数据
# 答案

```cpp
#include <bits/stdc++.h>

using ll = long long;

// 幂运算
template <typename T, auto f = std::multiplies<T>()>
constexpr static T power(T a, int64_t b) {
    assert(b >= 0);
    T res;
    // 初始化：数字为 1，ModInt 为 1
    if constexpr (std::is_arithmetic_v<T>) {
        res = 1;
    } else {
        res = a.identity();
    }
    // 快速幂
    while (b) {
        if (b & 1) {
            res = f(res, a);
        }
        b >>= 1;
        a = f(a, a);
    }
    return res;
}

// 一个整形，运算时自动取模
template <typename T, T MOD>
struct ModInt {
    using prod_type = std::conditional_t<std::is_same_v<T, int>, int64_t, __int128>;
    T val;
    constexpr ModInt(const int64_t v = 0) : val(v % MOD) { if (val < 0) val += MOD; }
    constexpr ModInt operator+() const { return ModInt(val); }
    constexpr ModInt operator-() const { return ModInt(MOD - val); }
    constexpr ModInt inv() const { return power(*this, MOD - 2); }
    constexpr friend ModInt operator+(ModInt lhs, const ModInt rhs) { return lhs += rhs; }
    constexpr friend ModInt operator-(ModInt lhs, const ModInt rhs) { return lhs -= rhs; }
    constexpr friend ModInt operator*(ModInt lhs, const ModInt rhs) { return lhs *= rhs; }
    constexpr friend ModInt operator/(ModInt lhs, const ModInt rhs) { return lhs /= rhs; }
    constexpr ModInt &operator+=(const ModInt x) {
        if ((val += x.val) >= MOD) val -= MOD;
        return *this;
    }
    constexpr ModInt &operator-=(const ModInt x) {
        if ((val -= x.val) < 0) val += MOD;
        return *this;
    }
    constexpr ModInt &operator*=(const ModInt x) {
        val = prod_type(val) * x.val % MOD;
        return *this;
    }
    constexpr ModInt &operator/=(const ModInt x) { return *this *= x.inv(); }
    bool operator==(const ModInt b) const { return val == b.val; }
    bool operator!=(const ModInt b) const { return val != b.val; }
    friend std::istream &operator>>(std::istream &is, ModInt &x) noexcept { return is >> x.val; }
    friend std::ostream &operator<<(std::ostream &os, const ModInt x) noexcept { return os << x.val; }
    constexpr static ModInt identity() { return 1; }
    constexpr ModInt pow(int64_t p) { return power(*this, p); }
};
using Z = ModInt<int, 1'000'000'007>;

int main() {
    std::ios::sync_with_stdio(false);
    std::cin.tie(nullptr);

    int n;
    std::cin >> n;
    std::string s;
    std::cin >> s;
    // 树
    std::vector<std::vector<int>> g(n);
    for (int i = 1; i < n; i++) {
        int f;
        std::cin >> f;
        f--;
        g[f].push_back(i);
    }

    // 一种递归函数对象的写法
    std::vector dp(n, std::vector(3, std::vector<Z>(3)));
    auto dfs = [&](auto &&self, int u) -> void {
        dp[u][1][1] = 1;
        dp[u][2][2] = 1;
        for (auto v : g[u]) {
            self(self, v);
            for (int i = 1; i <= 2; i++) {
                std::vector<Z> tmp(3);
                for (int j = 0; j < 3; j++) {
                    for (int k = 0; k < 3; k++) {
                        tmp[(j + k) % 3] += dp[u][i][j] * (dp[v][1][k] + dp[v][2][k]);
                    }
                }
                dp[u][i] = tmp;
            }
        }

        if (s[u] == 'R') {
            for (int i = 1; i <= 2; i++) {
                dp[u][i][1] = 0;
                dp[u][i][2] = 0;
            }
        }
    };

    dfs(dfs, 0);

    auto get = [&](int u) -> Z {
        Z res = 0;
        for (int i = 1; i <= 2; i++) {
            for (int j = 0; j < 3; j++) {
                res += dp[u][i][j];
            }
        }
        return res;
    };

    std::cout << get(0) << "\n";

    return 0;
}

```