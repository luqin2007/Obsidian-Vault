# 基础算法
## 排序
### 快排

1. 取分界
	- 取左右边界
	- 取中间点
	- 随机位置
2. 调整区间，使分界点左右全部大于等于/小于等于分界点
3. 对分界点左右两个区间递归执行 2 操作

```cpp
template<typename T>
void qsort(T *values, int r, int l = 0) {
    if (l >= r) return;

    T &x = values[l];

    int i = l - 1, j = r + 1;
    while (i < j) {
        while (values[++i] < x);
        while (values[--j] > x);
        if (i < j) std::swap(values[i], values[j]);
    }
    qsort(values, j, l);
    qsort(values, r, j + 1);
}
```
### 快速选择

可以以 $O(n)$ 的复杂度查找第 k 小的数：在快排的基础上，取消对不符合要求部分的排序，逐渐逼近 k 即可

```cpp
template<typename T>
bool qsearch(T *values, int k, int r, int l = 0) {
    if (l > r) return false;
    if (l == r) return true;

    T &x = values[l];

    int i = l - 1, j = r + 1;
    while (i < j) {
        while (values[++i] < x);
        while (values[--j] > x);
        if (i < j) std::swap(values[i], values[j]);
    }
    int sl = j - l + 1;
    if (k <= j ) return qsort(values, k, j, l);
    else return qsort(values, k - sl, r, j + 1);
} 
```
### 归并排序

1. 确定分界点，通常选取序列中点
2. 递归，对数组左右两边递归排序
3. 归并，将排序后的两个序列合并

```cpp
template<typename T>
void msort(T *v, T* tmp, int r, int l = 0) {
    if (l >= r) return;

    int mid = (l + r) / 2;
    msort(v, tmp, mid, l);
    msort(v, tmp, r, mid + 1);

    int i = l, j = mid + 1, p = l;
    while (i <= mid && j <= r) {
        if (v[i] <= v[j]) tmp[p++] = v[i++];
        else tmp[p++] = v[j++];
    }
    while (i <= mid) tmp[p++] = v[i++];
    while (j <= r) tmp[p++] = v[j++];

    for (int k = l; k <= r; ++k) v[k] = tmp[k];
}
```
### 数对

![[Pasted image 20230225164918.png]]

分治：数对包含三种情况，假设红色与绿色两种情况都会在归并过程中计算到，则在两侧递归完成后，只需要再统计黄色情况即可
## 整数二分

整数二分的本质是给定一个边界性质使区间一分为二，使两个区间一个符合要求，一个不符合要求

有单调性的题目一定可以二分，可使用二分的题目不一定有单调性

```cpp
int bsearch1(int l, int r, const std::function<bool(int)>& check) {
    int mid;
    while (l < r) {
        mid = (l + r + 1) / 2;
        if (check(mid)) l = mid;
        else r = mid - 1;
    }
    return l;
}

int bsearch2(int l, int r, const std::function<bool(int)>& check) {
    int mid;
    while (l < r) {
        mid = (l + r) / 2;
        if (check(mid)) r = mid;
        else l = mid + 1;
    }
    return l;
}
```

模板说明：使用时，先确定 check 函数，根据 check 确定更新方法是 `l=mid` 还是 `r=mid`
- 当使用 `l=mid` 时，另一种情况一定是 `r=mid-1`，此时 mid 需要补 +1
- 当使用 `r=mid` 时，另一种情况一定是 `l=mid+1`，此时 mid 不需要补 +1
- 二分模板一定是有解的（check 通过），但解不一定完全是题目解（AcWing 789 题目）

## 高精度

高精度即大整数运算，大整数指位数不小于 $10^6$ 正的整数，即大小在 $10^{10^6}$ 数量级的数值运算，常见有大整数之间的加减法和大整数与小整数的乘除法
- 利用数组（list，vector，。。。）存储每一位（节）值，下标 0 存个位便于进位
- 模拟人工运算 

### 大整数 + 大整数

```cpp
vector<int> add(vector<int> &a, vector<int> &b) {
    vector<int> ret;
    auto size = max(a.size(), b.size());
    ret.reserve(size + 1);
    int sum = 0;
    for (int i = 0; i < size; ++i) {
        if (i < a.size()) sum += a[i];
        if (i < b.size()) sum += b[i];
        ret.push_back(sum % 10);
        sum /= 10;
    }
    if (sum) ret.push_back(sum);
    return ret;
}
```

### 大整数 - 大整数

大整数减法本身只考虑整数运算且结果不小于 0 的情况，因此附带大小检查方法

```cpp
// 比较是否有 a >= b
bool comp(vector<int> &a, vector<int> b) {
    if (a.size() > b.size()) return true;
    if (a.size() < b.size()) return false;
    for (int i = a.size() - 1; i >= 0; --i) {
        if (a[i] != b[i]) return a[i] > b[i];
    }
    return true;
}

// 假定 a >= b
vector<int> sub(vector<int> &a, vector<int> &b) {
    vector<int> ret;
    ret.reserve(a.size());
    int t = 0;
    for (int i = 0; i < a.size(); ++i) {
        t = (i < b.size() ? a[i] - b[i] : a[i]) - t;
        ret.push_back((t + 10) % 10);
        if (t < 0) t = 1;
    }
    while (ret.size() > 1 && ret.back() == 0) ret.pop_back();
    return ret;
}
```

### 大整数 x 小整数

```cpp
// 这里应保证 b > 0
vector<int> mul(vector<int> &a, int b) {
    vector<int> ret;
    ret.reserve(a.size());
    int t = 0;
    for (int i = 0; i < a.size() || t; ++i) {
        if (i < a.size()) t += a[i] * b;
        ret.push_back(t % 10);
        t /= 10;
    }
    return ret;
}
```

### 大整数 ÷ 小整数

```cpp
vector<int> div(vector<int> &a, int b, int &r) {
    vector<int> ret;
    r = 0;
    for (int i = a.size() - 1; i >= 0; --i) {
        r = r * 10 + a[i];
        a.push_back(r / b);
        r %= b;
    }
    reverse(a.begin(), a.end());
    while (ret.size() > 1 && ret.back() == 0) ret.pop_back();
    return ret;
}
```

## 前缀和与差分

### 前缀和

前缀和用于在一次计算取得区间元素的和差

```cpp
// 前缀和
// 下标从 1 到 n，0 用处
int a[n + 1];
int sum[n + 1] {0};
for (int i = 1; i < n; ++i)
    sum[i] = sum[i - 1] + a[i];
// 二维前缀和
int a[n + 1][m + 1];
int sum[n + 1][m + 1];
for (int i = 0; i <= n; ++i) sum[i][0] = 0;
for (int i = 0; i <= m; ++i) sum[0][i] = 0;
for (int i = 1; i <= n; ++i)
    for (int j = 1; j <= m; ++j)
        sum[i][j] = a[i][j] + sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1];
```

一维前缀和可以使用 `partial_sum` 计算：![[cpp 数学计算#partial_sum]]
### 差分

差分是前缀和的逆运算。差分表示为：已知数列 $a_n = \{ a_1, a_2, \cdots, a_n \}$，构造数组 $b_n=\{b_1, b_2, \cdots, b_n\}$，使 $a_i=b_1+b_2+\cdots +b_i$，称 $a_n$ 是 $b_n$ 的前缀和，$b_n$ 是 $a_n$ 的差分，$b_i=a_i-a_{i-1}$

差分的作用在于快速大量的对原数组连续区间内所有数进行加减运算的操作
1. 以 $O(n)$ 的复杂度求取原数组 $\{a_n\}$ 的差分数组 $\{b_n\}$
2. 对 $\{b_n\}$ 对应元素进行加减操作，$b_i=b_i+C$ 操作对应 $a_i, a_{i+1}, \cdots, a_n$ 每个元素都加 C
3. 以 $O(n)$ 的复杂度求取计算后的数组的前缀和 $\{c_n\}$

## 二进制位

### 求第 k 位是几

1. 将第 k 位移至末尾：位移操作
2. 取第 k 位数字：与操作

```cpp
int getK(int n, int k) {
    return n >> k & 1;
}
```

### lobit

返回的是数字的最后一位 1 及之后的数字
- 树状数组
- 统计 1 的个数

| x            | lobit(x)   |
| ------------ | ---------- |
| $(1010)_2$   | $(10)_2$   | 
| $(101000)_2$ | $(1000)_2$ |

```cpp
int lobit(int x) {
    return x & (-x);
}
```

- `-x` = `~x+1`
- `x & (-x)` = `x & (~x+1)`

## 整数离散化

当给定一组数的值域特别大，但数据本身个数不多，可通过将其映射到较小的值域中
- 给定元素可能存在重复元素 - 去重，使用 `unique()` 方法
- 离散化后的值 - 保序离散化，二分查找某一值的下标

## 区间合并

1. 按区间左端点排序
2. 扫描区间，对当前区间可合并区间进行合并

# 数据结构

## 静态链表

通常不使用动态链表（指针实现的链表，`new` 操作非常慢），而是使用数组模拟的链表，这种链表称为静态链表

### 单链表

单链表多用于写邻接表，用于存储图和树

```cpp
int e[1000010], ne[1000010];
int head = -1, idx = 0;

void posh_front(int x) {
    e[idx] = x;
    ne[idx] = head;
    head = idx++;
}

void insert(int x, int k) {
    e[idx] = x;
    ne[idx] = ne[k];
    ne[k] = idx++;
}

void erase_next(int k) {
    ne[k] = ne[ne[k]];
}
```

### 邻接表

邻接表用于表示图，实际是多个单链表

### 双链表

双链表多用于优化某些问题

```cpp
int e[1000010], l[1000010], r[1000010];
int head = -1, idx = 0;

void posh_front(int x) {
    e[idx] = x;
    l[idx] = -1;
    r[idx] = head;
    head = idx++;
}

void insert_after(int x, int k) {
    e[idx] = x;
    r[idx] = r[k];
    l[idx] = k;
    l[r[k]] = idx;
    r[k] = idx++;
}

void insert_before(int x, int k) {
    insert_after(x, l[k]);
}

void erase(int k) {
    if (l[k] != -1) r[l[k]] = r[k];
    if (r[k] != -1) l[r[k]] = l[k];
}
```

### 栈

先进后出

```cpp
int stk[100010];
int tt = -1;

void push(int x) {
    stk[++tt] = x;
}

void pop() {
    tt--;
}

bool empty() {
    return tt >= 0;
}

int v() {
    return stk[tt];
}
```

### 队列

先进先出

```cpp
int que[100010];
int hh = 0, tt = -1;

void enqueue(int x) {
    que[++tt] = x;
}

void dequeue() {
    que[--tt];
}

bool empty() {
    return hh >= tt;


int v() {
    return que[hh];
}
```

### 单调栈、队列

抽象模型：
 - 单调栈：给定一系列数据，找到一个数左边第一个比它小的数（AcWing 830 单调栈）
 - 单调队列：求滑动窗口中的最大最小值（AcWing 154 滑动窗口）

思路：
1. 模拟使用朴素算法暴力解决问题的方法
2. 检查内层遍历元素，删除/跳过无效元素，维持栈/队列单调性

## KMP 字符串

KMP 主要优化以下情况：给定字符串 s 和模板字符串 t，求 s 中包含多少个 t

优化方法：字符串匹配失败后，模板串向前挪动尽可能多的位置，而原串位置指针不变

![[Pasted image 20230226110603.png]]

蓝色串是字符串 s，红色串是模板串 t，当匹配失败后模板移动到黑色箭头指向位置，且字符串 s 的指针（绿色点）不动，其前提是需要满足以下橙色框中字符串相同

![[Pasted image 20230226112136.png]]

经过通过预处理（下面[[#next 数组]]小节）后，可以以 $O(1)$ 的时间直接获取模板前移长度 j，匹配所用总时间复杂度为 $O(len(s)+len(t))$，并占用额外 $O(len(t))$ 的空间复杂度

#### next 数组

*`next` 变量在 c++ 某些头文件中已被定义，可命名为 `ne`*

要想实现 KMP 算法，我们需要求出当模板 t 任意位置匹配失败时，允许模板前移的个数 j，j 取值只与模板串 t 和失败位置有关，将其关联成一歌数组称为 `next` 数组，记为 `j = next[i]`，i  失败位置，设以 i 为终点的后缀字符串为 t'，可知 `next[i]` 表示 t' 中与 t' 的一个前缀完全相同的最长后缀**长度**。

```cpp
const char *str, *pat;
const int lenStr, lenPat;
int ne[lenPat];

// 求取 next[i]
// 此处稍后再说

// 假定 ne[lenPat] 已经求得
// i: 正在检查的输入字符串位，str 下标从 1 开始
// j: 正在检查的模板字符串位，pat 下标从 1 开始
for (int i = 1, j = 0; i <= lenStr; ++i) {
	// 匹配失败，利用 next 数组后退
    while (j && str[i] != pat[j + 1]) j = ne[j];
    if (str[i] == pat[j + 1]) j++;
    if (j == lenPat) {
        int p = i - lenPat + 1;
        // 匹配成功，p 是匹配成功的第一个字符在 str 中的索引
        
        j = 0;
    }
}
```

在计算 `next[]` 数组时，完全可以套用这套公式，原理类似，且要找 `next[i]` 时 `next[1]` 到 `next[i - 1]` 一定已经计算出来了

```cpp
vector<int> kmp(const char* str, const char* pat, int lenStr, int lenPat) {
    vector<int> ret;
    int ne[lenPat];
    memset(ne, 0, lenPat * sizeof(int));

	// next 数组计算
    for (int i = 2, j = 0; i <= lenPat; ++i) {
        while (j && i && pat[i] != pat[j + 1]) j = ne[j];
        if (pat[i] == pat[j + 1]) j++;
        ne[i] = j;
    }

    for (int i = 1, j = 0; i <= lenStr; ++i) {
        while (j && str[i] != pat[j + 1]) j = ne[j];
        if (str[i] == pat[j + 1]) j++;
        if (j == lenPat) {
            // 这里是匹配成功的操作
            ret.push_back(i - lenPat + 1);
            j = 0;
        }
    }
    return ret;
}
```

*AcWing831 KMP字符串*

## Trie 树

用于高效存储、查找字符串集合的数据结构，要求所有串中字符类型不多；同样的，也可以用于存储可以分为有限种类的大量不同数据并快速查找

Trie 树使用树结构存储字符串的每一个字符，并在存在字符串的位置做一个标记

![[Pasted image 20230227082808.png]]

```cpp
struct Trie {
    const static int N = 100010;

    int son[N][26]{}, cnt[N]{}, idx = 0;

    Trie() {
        memset(son, 0, 26 * N * 4);
        memset(cnt, 0, N * 4);
    }

    void insert(const char *str) {
        int p = 0;
        for (int i = 0; str[i]; ++i) {
            if (!son[p][str[i] - '0']) p = ++idx;
            p = son[p][str[i] - '0'];
        }
        cnt[p]++;
    }

    int count(const char *str) {
        int p = 0;
        for (int i = 0; str[i]; ++i) {
            if (!son[p][str[i] - '0']) return 0;
            p = son[p][str[i] - '0'];
        }
        return cnt[p];
    }
};
```

## 并查集

可以在近乎 $O(1)$ 的时间复杂度内快速合并集合和查询两个元素是否在同一集合中
- 初始时，每个元素位于一个单独的集合中，每个集合以树的形式存储元素
- 每个节点除了存储其本身值外还存储其父节点的值（索引）
- 每个集合对应树的父节点不同，树根父节点存储的就是当前树的编号

操作：
- 判断树根：父节点等于其本身
- 求 x 集合编号：递归查询父节点编号，直到树根
- 合并集合：查询两棵树的树根，将其中一个的父节点更改为另一个的值

路径压缩：
在以上操作中，在每次查询操作中，将被查询元素及其路径上的点的父节点全部设置为根节点，使树的高度大致维持在 2，实现多次查询有近似 $O(1)$ 的时间复杂度

按秩合并：

优化效果不明显，基本没啥用

```cpp
struct UnionSet {
    const static int N = 100010;
    int belong[N];
    UnionSet() {
        for (int i = 0; i < N; ++i) {
            belong[i] = i;
        }
    }
    
    int query(int x) {
        if (belong[x] != x)
            // 路径压缩
            belong[x] = query(belong[x]);
        return belong[x];
    }
    
    bool same(int x, int y) {
        return query(x) == query(y);
    }
    
    void merge(int x, int y) {
        if (!same(x, y)) belong[query(x)] = query(y);
    }
};
```

## 堆

堆是一个完全二叉树，对于每一个子树，根节点都是所有子树的最大/小值，可快速查找最大/最小值，以数组模拟存储

```cpp
struct MinHeap {
    const static int N = 10000;

    int heap[N], len;

    void up(int x) {
        if (x > 1 && heap[x] < heap[x / 2]) {
            swap(heap[x], heap[x / 2]);
            up(x / 2);
        }
    }

    void down(int x) {
        int t = x;
        if (x * 2 <= len && heap[x * 2] < heap[t]) t = x * 2;
        if (x * 2 + 1 <= len && heap[x * 2 + 1] < heap[t]) t = x * 2 + 1;
        if (t != x) {
            swap(heap[x], heap[t]);
            down(t);
        }
    }

    void insert(int x) {
        heap[++len] = x;
        up(len);
    }

    void remove(int x) {
        heap[x] = heap[--len];
        down(x);
        up(x);
    }

    void set(int v, int x) {
        heap[x] = v;
        down(x);
        up(x);
    }

    int min() {
        return heap[1];
    }

    // 以 O(n) 时间复杂度调整整个堆
    void adjust() {
        for (int i = len / 2; i; --i) down(i);
    }
};

```

调整整个堆的时间复杂度为 $O(n)$，适用于建堆，一次性输入所有元素后调整。其余操作时间复杂度均为 $O(log(n))$

## Hash

将一个庞大的值域映射到一个比较小的值域范围

通过映射方法产生的新值发生冲突时，通常使用拉链法或开放寻址解决。

一般可以认为每个哈希值对应的元素个数都是常数个。且通常只会进行插入和查询操作

### Hash 方法

数字：通常使用模运算作为映射方法，取离 2 的整数次幂尽可能远质数操作时冲突概率最小。
字符串：前缀 hash，可用于判定字符串或其字串相同
 1. 预处理字符串所有前缀的 hash 值
	 1. 将连续前缀看成一个 p 进制的数，将每个字符看成数字，一般情况下不能将字符看成 0
	 2. 将其转换成十进制
	 3. 对一个相对较小的数 q 取模
	 4. 一般情况下，p=131 或 1331，$q=2^{64}$（直接使用 unsigned long long 存储，溢出即取模），出现冲突的概率很小
 2. 子串哈希：已知字符串其所有的 n 位前缀前缀哈希 $h[n]$，求从第 L 到 R 位字符构成的字串的哈希：$h[R]-h[L]\times p^{R-L+1}$

### 拉链法

使用数组模拟链表存储数据

![[Pasted image 20230227164732.png]]

```cpp
struct Hash {
    const static int N = 100003;

    int v[N];
    // list
    int e[N], ne[N], idx = 1;

    Hash() {
        memset(v, -1, sizeof v);
    }

    int hash(int x) {
        return (x % N + N) % N;
    };

    void insert(int x) {
        int k = hash(x);
        ne[idx] = v[k];
        e[idx] = x;
        v[k] = idx++;
    }

    bool find(int x) {
        int k = hash(x);
        for (int i = v[k]; i != -1; i = ne[i]) {
            if (e[i] == x) return true;
        }
        return false;
    }
};

```

### 开放寻址法

![[Pasted image 20230227174539.png]]

开放寻址法的模通常需要取到给定数据量的 2-3 倍的质数，数组也要有 2-3 倍以尽可能避免冲突。开放寻址法在模后若遇到冲突，直接插入对应位置之后即可。

```cpp
struct Hash {
    // null: 一个绝对娶不到的值
    const static int N = 300007, null = 0x3f3f3f;

    int v[N];

    Hash() {
        memset(v, 0x3f, sizeof v);
    }

    int hash(int x) {
        return (x % N + N) % N;
    };

    int findIdx(int x) {
        int k = hash(x);
        while (v[k] != null && v[k] != x) {
            k++;
            if (k == N) k++;
        }
        return k;
    }

    int find(int x) {
        return v[findIdx(x)] == x;
    }

    void insert(int x) {
        v[findIdx(x)] = x;
    }
};
```

## STL

vector：数组倍增实现变长数组
- 倍增：操作系统分配空间时，与申请的空间大小关系不大，与申请次数有关，因此应尽可能少请求空间次数
deque：双端队列

queue：队列
stack：栈
priority_queue：优先队列（堆，默认大根堆）
- pop()，push()，top()
- 没有 clear()

set, map, multiset, multimap：平衡二叉树（$O(log(n))$）
- upper_bound：返回大于 x 的最小数
- lower_bound：返回大于等于 x 的最小数
- count()
unordered_set, unordered_map, unordered_multiset, unordered_multimap：哈希表（$O(1)$）
- count()

bitset：位压，代替 `bool[]` 省空间
- 所有位运算、比较、位移
- `[]`
- count()
- any()，none()
- set()，reset()：将所有位设为 1/0
- set(k, v)
- flip()，flip(k)
pair：二元组
- make_pair(a, b)
- {a, b}

支持比较运算：
vector：字典序
pair：以 first 为第一关键字，second 为第二关键字

# 图论

树与图

为了方便表达，定义图的基类为：

```cpp
const int INF = 0x3f3f3f3f;

struct Edge {
    // 从 i 指向 j，距离（权重） 为 dist
    int i, j, dist;
    
    // 默认 < 运算比较边的长度
    bool operator<(const Edge &o) const {
        return dist < o.dist;
    }
};

class Graph {
public:

    /// 节点数
    ///   假定所有节点为从 0 开始的连续数字
    ///   假定当只有一个源点时，源点为 0
    int n = 1;

    /// 若图为有向图，表示节点 i 的所有出边指向的节点
    /// 若图为无向图，表示节点 i 的所有边连通的节点
    virtual std::vector<int> out(int i) const = 0;

    /// 节点 i, j 之间的距离（边的权重），若无法直接连通则返回 INF
    virtual int dist(int i, int j) const = 0;

    /// 对图进行 for-in 循环时，循环的是其中的顶点
    /// 约定，循环时，源点总是在最前面
    typedef std::vector<int>::const_iterator const_iterator;
    virtual const_iterator begin() const = 0;
    virtual const_iterator end() const = 0;

    /// 返回图中所有边的一个副本
    virtual std::vector<Edge> edges() const = 0;

    /// 向图中加入节点
    virtual void put(int i) = 0;

    /// 向图中加入边，或用于更新两个节点之间的距离（权重）
    virtual void put(int i, int j, int dist) = 0;
    virtual void put(const Edge &e) = 0;
};
```

约定：n 表示节点数，m 表示边数，dist 数组表示从起点到某个节点的最短距离，0x3F3F3F3F 为正无穷

## DFS BFS

都可以对整个空间搜索
- DFS
	- 使用 stack 存储，额外占用 $O(h)$ 空间
	- 不具有最短路性质
	- 适用于直接暴力搜索，或内存空间比较紧张的情况
- BFS
	- 使用 queue 存储，额外占用 $O(2^h)$ 空间
	- 具有最短路性质
	- 当所有边权重都为 1 时可用于求最短路、拓扑序

## 拓扑序列

> 拓扑序列：若一个由图中所有点构成的序列 A 满足 对于图中每条边 `(x, y)`，x 总是在 y 之前出现，即所有边都是从前指向后的，则 A 为图的一个拓扑序列
> 
> 并非所有图都有拓扑序，只要存在环的有向图一定没有拓扑序列；有向无环图一定存在至少一个拓扑序，因此有向无环图又称拓扑图

> 入度：图中指向某个节点的路径个数称为该点的入度

> 出边：图中从某个节点出发的路径
> 出度：图中某个点出边的个数称为该点的出度

 1. 将入度为 0 的点入队：所有入读为 0 的点都可以作为一个拓扑序列的起始点
 2. 枚举队首 t 的所有出边删除 t 的所有到 j 的出边，若 j 的出度为 0，入队 j
 3. 只要有环，一定存在点没有入队；一个无环图一定可以将所有点入队（有向无环图一定存在入度为 0 的点）

## 最短路

最短路题目重点在于如何建图，如何定义点和边

![[Pasted image 20230301204243.png]]

### 单源最短路

一个点到其他所有点的最短距离

#### 边权全正

dijkstra 算法基于贪心

##### 朴素 dijkstra 算法

时间复杂度 $O(n^2)$，适合于边数多的稠密数组
1. 初始化距离数组 `dist[]`，`dist[i]=∞`，`dist[0]=1`，表示起点到 i 点的最短距离
2. 定义集合 S 表示已确定最短距离的点
3. 对每个节点遍历，取不在 S 中的每个距离最近的点 t
4. 将 t 插入 S，对于 t 的所有出边 x，且 `dist[x] > dist[t] + w` 时，更新 `dist[x] = dist[t] + w`
5. 重复 n 次 3，4，使所有点都在 S 中，查询结束

```cpp
void dijkstra(const Graph &g, int *dist) {
    bool st[g.n]; // st: 标记已检索完成的最短距离的节点
    memset(dist, 0x3F, g.n * sizeof(int));
    memset(st, 0, sizeof st);

    dist[0] = 0;
    for (int i = 0; i < g.n; ++i) {
        int t = -1;
        for (const auto &j: g)
            if (!st[j] && (t == -1 || dist[t] < dist[j]))
                t = j; // 未被检索到且距离源点最近的节点
        st[t] = true;
        for (const auto &j: g.out(t)) // 更新最短距离
            dist[j] = min(dist[j], dist[t] + g.dist(t, j));
    }
}
```

##### 堆优化 dijkstra 算法

时间复杂度 $O(mlog n)$，只是将朴素 dijkstra 中搜索最短距离的部分使用小根堆实现，适用于稀疏图

*使用 STL 优先队列实现堆，相比起手写堆，内部元素有 m 个，总时间复杂度为 $O(mlogm)$*
*但由于使用该算法时通常 $m<n^2$，其时间复杂度在数量级上与手写堆同级*

```cpp
void dijkstra_heap(const Graph &g, int *dist) {
    bool st[g.n]; // st: 标记已检索完成的最短距离的节点
    memset(dist, 0x3F, g.n * sizeof(int));
    memset(st, 0, sizeof st);

    using PII = pair<int, int>; // <距离, 节点 id>
    priority_queue<PII, vector<PII>, greater<>> heap;
    heap.emplace(0, 0);
    st[0] = true;

    while (!heap.empty()) { // 遍历堆，每次取堆顶一定是距离源点最近的节点
        auto &p = heap.top();
        int i = p.second, d = p.first;
        heap.pop();

        if (st[i]) continue;
        st[i] = true; // 由于存在同一个节点重复入堆的可能性，需要判断

        for (const auto &j: g.out(i)) { // 更新距离及堆数据
            if (dist[j] > d + g.dist(i, j)) {
                dist[j] = d + g.dist(i, j);
                heap.emplace(dist[j], j);
            }
        }
    }
}
```

#### 存在负权边

##### Bellman-Ford

时间复杂度 $O(mn)$，基于离散数学
1. 遍历所有边，对每条边设从 a 指向 b，权重为 w
2. 松弛操作：对所有边，更新 `dist[b] = min(dist[a] + w)`
3. 迭代 n 次 1，2 步。
4. **第 k 次迭代表示从起点经过不超过 k 条边到达终点的距离**

负权回路判断：
- 第 n 次迭代时仍有路径点更新，表示路径到更新点之间一定存在负权回路

**存在负权回路的情况下不一定有最短路**

```cpp
void bellman_ford(const Graph &g, int *dist) {
    memset(dist, 0x3F, g.n * sizeof(int));
    dist[0] = 0;
    for (int k = 0; k < g.n; ++k)
        for (const auto &i: g)
            for (const auto &j: g.out(i))
                dist[j] = min(dist[j], dist[i] + g.dist(i, j));
}
```

使用场景：**求经过最多 k 条边后到达终点的最短距离**
- 每次迭代只用上次的结果，以免发生串联，因此每次迭代之前需要备份所有距离
- 最后检查距离时，不存在的边不应直接检查 `0x3f3f3f3f`，由于负权边会使距离无穷的点去更新另一个点，使距离小于 `0x3f3f3f3f` 但不会小太多，可使用 `0x3f3f3f3f/2`

![[Pasted image 20230303073737.png]]

##### SPFA

时间复杂度最坏 $O(mn)$，一般情况下是 $O(m)$ 且优于 Bellman-Ford 算法
- 在 Bellman-Ford 算法基础上，剔除不需要更新的点
- 创建一个队列，堆里面保存每次距离变小的点 t，更新 t 的所有出边，被更新变小的点入队

只要没有负环就可以用 SPFA。但某些情况，如求不超过 k 条边的最短路，不能用 SPFA

```cpp
void spfa(const Graph &g, int *dist) {
    bool st[g.n]; // 队列中的节点，防止重复入队
    queue<int> q; // 队列：所有需要更新的节点
    memset(dist, 0x3F, g.n * sizeof(int));
    memset(st, 0, sizeof st);
    dist[0] = 0;
    st[0] = true;
    q.push(0);
    while (!q.empty()) {
        int i = q.front();
        q.pop();
        st[i] = false;
        for (const auto &j: g.out(i)) {
            if (dist[j] > dist[i] + g.dist(i, j)) {
                dist[j] = dist[i] + g.dist(i, j);
                if (!st[j]) { // 仅入队更新过的节点，其余节点不影响其他节点的距离
                    q.push(j);
                    st[j] = true;
                }
            }
        }
    }
}
```

如果题目数据没有特别卡数据，SPFA 大多数情况下可以过 dijkstra 算法的题目且且比他更快。被卡时只能用堆优化版 dijkstra（多见于稀疏网格图）

SPFA 也可以用于判断负环，原理与 bellman-Ford 算法相同

```cpp
/// spfa 判断负环
/// return: 存在负环则返回 true
bool spfa_ring(const Graph &g, int *dist) {
    bool st[g.n];
    int cnt[g.n]; // 记录更新次数
    queue<int> q;
    memset(dist, 0, g.n * sizeof(int));
    memset(st, 0, sizeof st);
    dist[0] = 0;
    st[0] = true;
    cnt[0] = 0;
    q.push(0);
    while (!q.empty()) {
        int i = q.front();
        q.pop();
        st[i] = false;
        for (const auto &j: g.out(i)) {
            if (dist[j] > dist[i] + g.dist(i, j)) {
                dist[j] = dist[i] + g.dist(i, j);
                cnt[j] = cnt[i] + 1;
                // 更新 n 次，至少经过 n+1 个节点，说明有环
                if (cnt[j] >= g.n) return true;
                if (!st[j]) {
                    q.push(j);
                    st[j] = true;
                }
            }
        }
    }
    return false;
}
```

### 多源汇最短路

> 源点：起点
> 汇点：终点

起点和终点不确定，使用 Floyd 算法，时间复杂度 $O(n^3)$，同样要求不能有负权回路

原理：基于动态规划，状态为 `d[k][i][j]` 表示从 i 到 j 只经过 `0-k` 的中间点所用的最短距离

1. 使用邻接矩阵存储图的所有边 `d[i][j]`
2. 三重 n 次循环 k, i, j 更新距离 `d[i][j]=min(d[i][j], d[i][k]+d[k][j])`

```cpp
void floyd(Graph &g) {
    for (const auto &k: g)
        for (const auto &i: g)
            for (const auto &j: g)
                g.put(i, j, min(g.dist(i, j), g.dist(i, k) + g.dist(k, j)));
}
```

该算法也存在负权边更新无穷的问题，同样使用 `0x3F3F3F3F / 2` 判断

### 总结

1. 多源汇只能用 Floyd 算法，通常 n 很小（几百）
2. m、n 都很大（10w），一般选择 SPFA 或堆优化 dijkstra 算法
3. 稠密图，n 较小 m 很大，接近 $m=n^2$，朴素 dijkstra
4. 求不超过 k 个节点，使用 Bellman-Ford 算法
5. 求是否有负权回路，使用SPFA

## 最小生成树

使所有点之间相互连通的最小距离

### 朴素 Prim 算法

时间复杂度 $O(n^2)$，适用于稠密图
1. 找到集合外距离距离集合最近的点 t
2. 用 t 更新其他点到集合的距离后 `st[t] = true`
	- 点到集合的距离：点到集合内部所有边中最短的边
3. 重复 1 2，迭代 n 次，每一次 t 点中距离最近的边即生成树中的一条边

该算法在迭代过程中与朴素 dijkstra 算法基本相同，只是在更新节点距离时有所不同

```cpp
/// 返回 false 表示不存在最小生成树
bool prim(const Graph &g, Graph &ret) {
    int dist[g.n]; // 该节点到图的距离
    int conn[g.n]; // 当该节点到图的距离为 dist[i] 时连接的节点
    bool st[g.n]; // 节点是否已经是最短生成树的一部分
    memset(dist, 0x3f, sizeof dist);
    memset(conn, -1, sizeof conn);
    memset(st, 0, sizeof st);
    dist[0] = 0;

    for (int k = 0; k < g.n; ++k) {
        int i = -1;
        for (const auto &t: g) // 查找最近点
            if (!st[t] && (i == -1 || dist[t] < dist[i]))
                i = t;

        if (dist[i] == 0x3f3f3f3f) // 存在节点不连通，无最小生成树
            return false;

        ret.put(i); // 节点接入最短生成树
        st[i] = true;
        if (i != 0)
            ret.put(conn[i], i, dist[i]);
        for (const auto &j: g) // 更新该节点所能连接到的点到图的距离
            if (dist[j] > g.dist(i, j)) {
                dist[j] = g.dist(i, j);
                conn[j] = i;
            }
    }
    return true;
}
```

### 堆优化 Prim 算法

时间复杂度 $O(mlogn)$，**非常非常不常用**，Kruskal 算法思路更清晰更简单

优化方式与 dijkstra 相似，使用堆优化排序步骤

### Kruskal 算法

时间复杂度 $O(mlogm)$，大多数时间花费在排序上，常用于稀疏图
1. 将所有边按权重（距离）从小到大排序
2. 枚举每条边，若两端点 a b 不连通，则将该边加入集合（并查集）

```cpp
bool kruskal(const Graph &g, Graph &ret) {
    for (const auto &i: g) ret.put(i);
    // 对边集合排序
    auto edges = g.edges();
    sort(edges.begin(), edges.end());
    // 构造并查集及 find 函数
    int p[g.n];
    for (const auto &i: g)
        p[i] = i;
    function<int(int)> find;
    find = [&p, &find](int a) -> int {
        if (p[a] != a) a = find(p[a]);
        return p[a];
    };
    // 构建图
    int count = 0;
    for (const auto &edge: edges)
        if (find(edge.i) != find(edge.j)) {
            p[find(edge.i)] = p[find(edge.j)];
            ret.put(edge);
            count++;
        }
    // 加入的边数目少于 n-1 则有点未连接
    return count == g.n - 1;
}
```

## 二分图

> 二分图：可以将所有顶点划分成两边，使每一边的所有节点之间没有边

### 染色法

DFS，用于判断二分图，时间复杂度为 $O(m+n)$
1. 遍历每个点 i
2. 若 i 未被染色，用 dfs 为 i 点的所有连通块染色
3. 若某个点有冲突，则不是二分图

> 原理：一个图是二分图，当且仅当图中不含奇数环 => 能被 2 染色

```cpp
// color: 0 或 1
// colors: 染色结果，初始值全为 -1 表示未染色
bool dfs(Graph &g, int i, int color, int *colors) {
    // 染色
    colors[i] = color;
    color = !color;
    for (const auto &j: g.out(i)) {
        if (colors[j] == -1) {             // 未染色
            if (!dfs(g, j, color, colors)) // 染色失败
                return false;
        } else if (colors[j] != color)     // 颜色冲突
            return false;
    }
    return true;
}

bool check(Graph &g) {
    int cs[g.n];
    memset(cs, -1, sizeof cs);
    return dfs(g, 0, 0, cs);
}
```

### 匈牙利法

用于求二分图的最大匹配，最坏情况下为 $O(mn)$，但实际运行时间远小于最坏值
- 最大匹配：在所有点之间，选取一对任意相连的点，能选出的最多不同对数
1. 对某一半面的节点遍历，找到可以配对的另一个点
2. 若对面点已经被匹配了，尝试找到与之匹配的点是否可以匹配其他点

 > 扩展：最大流算法，二分图是最大流的一个特例。大部分最大流算法的运行时间都远小于理论值

```cpp
bool find(Graph &g, int i, bool *st, int *match) {
    for (const auto &j: g.out(i)) {
        if (!st[j]) {
            st[j] = true;
            if (match[j] == -1 || find(g, match[j], st, match)) {
                match[j] = i;
                return true;
            }
        }
    }
    return false;
}

/// part: 二分图的某一边
vector<pair<int, int>> hungarian(Graph &g, set<int> &part) {
    int match[g.n]; // 记录另一半面每个节点所匹配的节点
    memset(match, -1, sizeof match);
    bool st[g.n]; // 标记：保证节点每次只会遍历一次
    for (const auto &i: part) {
        memset(match, 0, sizeof st);
        find(g, i, st, match); // 搜索配对节点
    }

    vector<pair<int, int>> ret;
    for (int i = 0; i < g.n; ++i)
        if (match[i] != -1)
            ret.emplace_back(match[i], i);
    return ret;
}
```

# 数学

## 数论

### 质数验证

试除法，循环条件 `i <= n / i`，时间复杂度 $O(\sqrt n)$

### 分解质因数

试除法，循环条件 `i <= n / i`，时间复杂度最坏 $O(\sqrt n)$，最好 `log n`
- 每次找到因数后去除 n 中所有该因数
- n 中最多包含一个大于 $\sqrt n$ 的质因子，因此循环到 $\sqrt n$ 后剩余的数字若不是 1，则剩余那个也是质数

将 $n!$ 分解成质数：
$$
n! = p_1^{\alpha_1} + p_2^{\alpha_2} + p_3^{\alpha_3} + \cdots + p_k^{\alpha_k}
$$
其中，$p_i$ 是第 i 个质数，$\alpha_i=\lfloor{\dfrac{a}{p_i}}\rfloor+\lfloor{\dfrac{a}{p_i^2}}\rfloor+\cdots+\lfloor{\dfrac{a}{p_i^m}}\rfloor$

### 质数筛法

[[质数]]

### 约数

$$
n = p_1^{\alpha_1}p_2^{\alpha_2}p_3^{\alpha_3}p_4^{\alpha_4}\cdots p_k^{\alpha_k}
$$
- 约数个数：$(\alpha_1+1)(\alpha_2+1)(\alpha_3+1)\cdots$
- 由于约数个数 == 合数个数，又等于 $(n/p_1)+(n/p_2)+\cdots +(n/p_i)$
- int 范围内的整数，约数个数最多个整数，大概在 1500-1600 个左右
- 约数之和等于 $(p_1^0+p_1^1+\cdots+p_1^{\alpha_1})(p_2^0+p_2^1+\cdots+p_2^{\alpha_2})\cdots$

### 辗转相除法

```cpp
int gcd(int a, int b) {
    return b ? gcd(b, a % b) : a;
}
```

### 欧拉函数

给定整数 n，求 1-n 中与 n 互质的数的个数

$$
n = p_1^{\alpha_1}p_2^{\alpha_2}p_3^{\alpha_3}p_4^{\alpha_4}\cdots p_k^{\alpha_k}
$$
$$
\phi(n)=n(1-\dfrac{1}{p_1})(1-\dfrac{1}{p_2})\cdots(1-\dfrac{1}{p_k})
$$

在线性筛中，可以快速求出所有数的欧拉函数，其中：
1. 1 的欧拉函数为 1
2. 质数 n 的欧拉函数为 n-1
3. 循环筛合数时，若 `i % p == 0`，`p * i` 的欧拉函数为 $\psi(i)\times p$
4. 循环筛合数时，若 `i % p != 0`，`p * i` 的欧拉函数为 $\psi(i)\times(p-1)$

#### 欧拉定理

设 a 与 n 互质，则有
$$
a^{\psi(n)}\equiv1\pmod n
$$
#### 费马定理

当 n 为质数时，$\psi(n)=n-1$，则有 $a^\psi(n)=a^{n-1}\equiv 1\pmod n$

### 快速幂

在 $O(\log(k))$ 的时间复杂度内求出 $a^k\mod p$ 的结果，其中 $1\leq a, p, k \leq 10^9$
1. 预处理 $a^{2^0}\mod p$ 到 $a^{2^{\log k}}\mod p$，使用递推，$a^{2^n} \mod p=({a^{2^{n-1}}})^2\mod p$
2. 将 k 转化成二进制表示，设 $k=\sum_{i=1}^t 2^{x_t}$，则有：$a^k=\prod_{i=1}^t a^{2^{x_i}}=a^{\sum_{i=1}^t 2^{x_t}}$

```cpp
int qmi(int a, int k, int p) {
    using ull = unsigned long long;
    int res = 1;
    while (k) {
        if (k % 1) res = (ull) res * a % p;
        k >>= 1;
        a = (ull) a * a % p;
    }
    return res;
}
```

### 扩展欧几里得算法

对于任意正整数 a，b，总是存在非零整数 x，y，使 $ax+by=(a, b)$ 成立，求 x，y

设 $d=(a,b)$，根据欧几里得算法，有：
$$d=ax+by=(a,b)=(b,a\mod b)=by'+(a\mod b)x'$$
将中间部分展开得
$$
\begin{align}
ax+by &= by' + (a \mod b)x'\\
     &= by' + (a - \lfloor\frac{a}{b}\rfloor b)x'\\
     &= by' + ax' - \lfloor\frac{a}{b}\rfloor bx'\\
     &= ax' + b(y'-\lfloor\frac{a}{b}\rfloor x')
\end{align}
$$
整理得
$$
\begin{cases}
x=x'\\
y=y'-\lfloor\dfrac{a}{b}\rfloor x'
\end{cases}
$$
边界条件：利用欧几里得算法计算，当 b=0 时（即欧几里得算法结束时），有 x=1，y=0，d=a

```cpp
int exgcd(int a, int b, int &x, int &y) {
    // 边界条件检查
    if (!b) {
        x = 1;
        y = 0;
        return a;
    }
    // d=(a,b)=(b,a%b)
    int d = exgcd(b, a % b, y, x);
    // y=y'- a/b*x
    y -= a / b * x;
    return d;
}
```

该方法求出的是 x=1 时的一个特解，其通解为：
$$
\begin{align}
x &= x_0 - \dfrac{b}{a}k\\
y &= y_0 + k
\end{align}
$$

## 高斯消元

在 $O(n^3)$ 的时间复杂度内，使用矩阵求解 n 元一次方程组
$$
\begin{cases}
a_{11}x_1+a_{12}x_2+\cdots a_{1n}x_n=b_1\\
a_{21}x_1+a_{22}x_2+\cdots a_{2n}x_n=b_2\\
a_{31}x_1+a_{32}x_2+\cdots a_{3n}x_n=b_3\\
\cdots\\
a_{n1}x_1+a_{n2}x_2+\cdots a_{nn}x_n=b_n\\
\end{cases}
$$
高斯消元后，可得到系数矩阵的阶梯矩阵（下三角矩阵），以此可得方程解的情况。矩阵 $M_{n,n+1}$ 为系数和结果共用的矩阵
$$
\begin{matrix}
a_{11}&a_{12}&\cdots a_{1n}&b_1\\
a_{21}&a_{22}&\cdots a_{2n}&b_2\\
a_{31}&a_{32}&\cdots a_{3n}&b_3\\
\cdots\\
a_{n1}&a_{n2}&\cdots a_{nn}&b_n\\
\end{matrix}
$$
1. 枚举矩阵第 1-n 列，循环第 k 列时找到所有行中该列绝对值最大的一行，将其交换到第一行
2. 若 $a_{1k}\neq0$，第一行第 k 列 $a_{1k}$ 变成 1（该行所有数除 $a_{1k}$），并以此消除之后所有第 k 列数
3. 若 $a_{1k}=0$，且 $a_{1,n+1}=0$ 则有无穷解；若 $a_{1,n+1}\neq0$ 则无解

```cpp

/**
 * @param N 数组最大长度
 * @param n 共有 n 个未知数
 * @param m 一个 (n+1) * (n+2) 的数组，要求行、列从下标 1 开始，前 n * n 是系数矩阵，后 1 * n 为等号右边的常数
 * @return 解的个数，1 代表唯一解，0 代表无解，-1 代表无穷解，当有唯一解时第 k 个未知数解即 m[k][n+1]
 */
int gauss(int n, double m[N][N]) {
    // 浮点 0 判断
    const double eps = 1e-6;

    for (int i = 1; i <= n; ++i) {
        // 1. 找到第 i 列绝对值最大的行，将其移至第 i 行，顺带判断解的个数
        int cr = i;
        for (int j = i + 1; j <= n; ++j)
            if (abs(m[j][i]) > abs(m[cr][i]))
                cr = j;
        if (abs(m[cr][i]) <= eps)
            return m[cr][n + 1] == 0 ? -1 : 0;
        if (cr != i) {
            memcpy(m, m + i, (n + 2) * sizeof(double));         // i -> 0
            memcpy(m + i, m + cr, (n + 2) * sizeof(double));    // cr -> i
            memcpy(m + cr, m, (n + 2) * sizeof(double));        // 0 -> cr
        }
        // 2. 将 m[i][i] 归 1，并将其余所有行第 i 列归零
        if (m[i][i] != 1)
            for (int j = n + 1; j >= i; --j)
                m[i][j] /= m[i][i];
        for (int j = 1; j <= n; ++j)
            if (j != i && abs(m[j][i]) > eps)
                for (int k = n + 1; k >= i; --k)
                    m[j][k] -= m[j][i] * m[i][k];
    }
    return 1;
}
```

## 组合计数

$$
C{b\atop{a}} = \dfrac{a!}{b!(a-b)!}
$$
$$
C{b\atop{a}} = C{b-1\atop{a-1}} + C{b\atop{a-1}}
$$

卢卡斯定理 Lucas：
$$
C{b\atop{a}} \equiv C{{b\mod p}\atop{a\mod p}}C{{b/p}\atop{a/p}} \pmod p
$$
![[Pasted image 20230325115809.png]]

常用思路:
- 预处理：预处理所有值或阶乘（公式 1 2）
- 分解成质数，消去分子分母相同部分（公式 1 +分解质数，通常+高精度）

### 卡特兰数

$$
C{n\atop{2n}} - C{{n-1}\atop{2n}} = \dfrac{C{n\atop{2n}}}{n + 1}
$$

广泛用于求给定两个数的排列，对排列的前缀有要求的情况
- 0-1序列
- 火车进站
- 合法括号序列
- 。。。
 
思路：将给定待求排列转化为网格中求路径问题，每一个排列对应一个路径，每一个路径对应一个排列
![[Pasted image 20230325144725.png]]

## 容斥原理

n 个集合的并集的元素个数，等于1，3，5，。。。，所有奇数个集合交集的元素个数之和，减去 2，4，6，。。。，偶数个集合交集的元素个数

![[Pasted image 20230325150755.png]]

## 简单博弈论

### 公平组合游戏

- 两名玩家交替行动
- 每个玩家可进行的操作与轮到哪位玩家无关
- 玩家不能行动时判负
- 两个玩家都会选择最优方案

任何公平组合游戏都可以转化为有向图游戏

结论：若游戏分为 k 种操作，每种操作可进行 $a_k$ 次，则 $a_1$ 到 $a_k$ 全部异或后为 0 时先手必败，否则必胜

#### SG 函数

1. 已知非负整数集合 S，定义 mex(S) 表示不属于 S 的最小自然数
3. SG 函数：在一个有向图种，
	1. 定义终点状态的 SG 函数值为 0
	2. 从一个状态 $x$ 出发，对于可以达到的所有状态 $y_1, y_2, \cdots, y_n$，$SG(x)=mex \{SG(y_1), SG(y_2), \cdots, SG(y_n)\}$

设起点为 s，当 SG(s)=0 时，先手必败，否则必胜

当有 N 个图，每个图的起点为 $s_1, s_2, \cdots, s_n$，将所有的 $SG(s_1), \cdots, SG(s_n)$ 异或后为 0 则先手必败，否则先手必胜

# 动态规划

- 状态表示
	- 所有可达状态的集合
	- 集合的某种属性（最大值、最小值、数量等）
- 状态计算：集合的划分，将当前集合划分为更小的子集，要求不漏，大多数情况下要求不重
- dp优化：对 dp 问题的计算方程做等价变形
	- 状态 i 只依赖于状态 i-1 的数据，对之前的值无依赖
	- 状态更新时压缩更新量

## 背包问题

给出 n 个物品和容量为 v 的背包，每个物品有对应的体积和价值，求如何选取物品使价值最大，其中第 i 个物品的体积为 vi，价值为 wi

### 0-1 背包

**每件物品最只有一个**

- 状态 f(i, j)，所有 f(i, j) 计算后答案就是 f(n, v)
	- 集合：只考虑前 i 个物品，且总体积不大于 j 的所有物品选法
	- 属性：最大值，所有选法价值的最大值
- 计算：f(i, j) -> 所有不包含 i 物品和不包含 i 物品的集合
	- 所有 1-i 且不包含 i 的物品，体积不超过 j ==> f(i-1, j)
	- 所有 1-i 且包含 i 的物品，体积不超过 j  ==> f(i-1, j-vi) + wi
	- f(i, j) = max(f(i-1, j), f(i-1, j-vi) + wi)

```cpp
#include <iostream>

using namespace std;

const int M = 1010;
int v[M], w[M];
int dp[M][M];

int main() {
    int N, V;
    cin >> N >> V;
    for (int i = 1; i <= N; ++i) {
        cin >> v[i] >> w[i];
    }
    // dp[i][w] 表示选择前 i 个物品装入体积为 w 的背包的最大价值
    for (int i = 1; i <= N; ++i)
        for (int j = 0; j <= V; ++j) {
            dp[i][j] = dp[i - 1][j];
            if (j >= v[i])
                dp[i][j] = max(dp[i][j], dp[i - 1][j - v[i]] + w[i]);
        }

    cout << dp[N][V];
    return 0;
}
```

- 优化
	- i 状态可删除，每次都是只用上一次的数据，注意此时体积应倒序循环，防止数据被覆盖
	- j 只需要遍历不小于 `v[i]` 的体积即可

```cpp
#include <iostream>

using namespace std;

const int M = 1010;
int v[M], w[M];
int dp[M];

int main() {
    int N, V;
    cin >> N >> V;
    for (int i = 1; i <= N; ++i) {
        cin >> v[i] >> w[i];
    }
    // dp[i][w] 表示选择前 i 个物品装入体积为 w 的背包的最大价值
    for (int i = 1; i <= N; ++i)
        for (int j = V; j >= v[i]; --j)
            dp[j] = max(dp[j], dp[j - v[i]] + w[i]);

    cout << dp[V];
    return 0;
}
```

### 完全背包

**每件物品有无限个**

完全背包可以认为在 0-1 背包的基础上，将状态计算从有无到数量的推广
- 计算：f(i, j) -> 所有包含若干个第 i 个物品的选法，总体积有限故而可选有限
	- 所有 1-i 且包含 k 个 i 物品，体积不超过 j  ==> `f(i-1, j-k * vi) + wi * k`

```cpp
#include <iostream>

using namespace std;

const int M = 1010;
// v: 体积; w: 价值
int v[M], w[M];
int dp[M][M];

int main() {
    int N, V;
    cin >> N >> V;
    for (int i = 1; i <= N; ++i) {
        cin >> v[i] >> w[i];
    }
    // dp[i][w] 表示选择前 i 个物品装入体积为 w 的背包的最大价值
    for (int i = 1; i <= N; ++i) {
        for (int j = 0; j <= V; ++j) {
            for (int k = 0; k * v[i] <= j; ++k) {
                dp[i][j] = max(dp[i][j], dp[i - 1][j - k * v[i]] + k * w[i]);
            }
        }
    }
    cout << dp[N][V];
    return 0;
}
```

优化：已知

$$
f[i, j]=f[i-1, j-v[i]\times k]+w[i]\times k
$$
可知
$$
f[i,j]=\max(f[i-1, j], f[i-1,j-v]+w, f[i-1,j-2v]+2w,\cdots)
$$
$$
f[i,j-v]=\max(f[i-1, j-v], f[i-1,j-2v]+w, f[i-1,j-3v]+2w,\cdots)
$$
可推出，$f[i,j]=\max(f[i-1,j], f[i, j-v]+w)$，减少状态

同时，根据 0-1 背包优化第 1 点，可以删除一层数组。这里因为状态计算只依赖于当前状态 j 之前的值，需要他们先更新，反而不能倒序循环 i

```cpp
#include <iostream>

using namespace std;

const int M = 1010;
// v: 体积; w: 价值
int v[M], w[M];
int dp[M];

int main() {
    int N, V;
    cin >> N >> V;
    for (int i = 1; i <= N; ++i) {
        cin >> v[i] >> w[i];
    }
    // dp[i][w] 表示选择前 i 个物品装入体积为 w 的背包的最大价值
    for (int i = 1; i <= N; ++i) {
        for (int j = 0; j <= V; ++j) {
            if (j >= v[i])
                dp[j] = max(dp[j], dp[j - v[i]] + w[i]);
        }
    }
    cout << dp[V];
    return 0;
}
```

### 多重背包

**每件物品有有限个**

多重背包在完全背包上，将状态计算部分做了限制，即每次问题拆分时不一定能拆分到理论最大值，而是给了一个拆分上限（第 i 件物品的数目）。

只贴最内层 k 循环，其他与完全背包基本相同，除了前面需要额外输入一个 `s[i]` 用于存储第 i 个物品的数目

```cpp
for (int k = 0; k * v[i] <= j && k <= s[i] /* k 上限 */; ++k)
    dp[i][j] = max(dp[i][j], dp[i - 1][j - k * v[i]] + k * w[i]);
```

优化：由于 max 无法做减法，无法（直接）使用完全背包的思路优化多重背包。多重背包的优化思路为二进制优化：

设 $s = \max(s[i])$，取整数 n，使 $k=\sum_{i=0}^n 2^i=2^{(n+1)}-1\leq s$，因此构造数列 $\{1,2,4,8,\cdots,2^i,s-k\}$ 即可组合出从 1 到 s 的所有数值（），可实现将循环次数从 s 降低到 $log_2(s)$ 的数量级。可使用 STL `bitmap` 容器。

确定 n 后，将 s 件物品全部拆分，拆成 log(s) 个物品包，对其进行 0-1 背包问题计算即可。

```cpp
#include <iostream>

using namespace std;

// pv: 包体积; pw: 包价值; pn: 包总数
// 25000 = 1000 种物品 * log2(2000 个物品/种)
int pv[25000], pw[25000], pn = 0;
int dp[2010];

int main() {
    int N, V, v, w, s;
    cin >> N >> V;
    // 打包
    for (int i = 1; i <= N; ++i) {
        cin >> v >> w >> s;
        int k = 1;
        // 以 2 的指数个数打包
        while (s >= k) {
            pn++;
            pv[pn] = v * k;
            pw[pn] = w * k;
            s -= k;
            k *= 2;
        }
        // 打包剩余物品
        if (s > 0) {
            pn++;
            pv[pn] = v * s;
            pw[pn] = w * s;
        }
    }

    // 对打包后的 pn 个包进行 0-1 背包
    for (int i = 1; i <= pn; ++i)
        for (int j = V; j >= pv[i]; --j)
            dp[j] = max(dp[j], dp[j - pv[i]] + pw[i]);

    cout << dp[V];
    return 0;
}
```

### 分组背包

**每个物品有一个分类，每类物品最多只能选择一个**

在 0-1 背包问题的基础上，集合为只考虑前 i 类物品，且总体积不大于 j 的所有物品选法，计算则选是选择第 i 组物品的某一个物品。按照 0-1 背包优化第一重循环后代码如下：

```cpp
#include <iostream>

using namespace std;

const int M = 110;

// k 表示第 i 组物品都包含哪些物品，v[i][0] 为该组物品个数
int v[M][M], w[M][M];
int dp[M];

int main() {
    int N, V;
    cin >> N >> V;
    for (int i = 1; i <= N; ++i) {
        cin >> v[i][0];
        for (int j = 1; j <= v[i][0]; ++j)
            cin >> v[i][j] >> w[i][j];
    }
    // dp[i][w] 表示选择前 i 组物品装入体积为 w 的背包的最大价值
    for (int i = 1; i <= N; ++i)
        for (int j = V; j >= 0; --j)
            for (int k = 1; k <= v[i][0]; ++k)
                if (j >= v[i][k])
                    dp[j] = max(dp[j], dp[j - v[i][k]] + w[i][k]);

    cout << dp[V];
    return 0;
}
```

## 线性 DP

递推方程有明显的线性关系，多个状态之间有明显的先后顺序
- 最长、最短子序列问题
	- 集合：`f[i]` - 所有以 i 结尾的子序列的最大长度
	- 计算：`max(f[1]+f[2]+...+f[i-1]) + 1`
		- 以 1, 2, ..., i-1 结尾的最长子序列长度 +1
- 字符串最长公共子序列问题
	- 集合：`f[i, j]` - 字符串 s1 中前 i 个字符组成的字符串，与字符串 s2 中前 j 个字符组成的字符串，其所有公共子串的最长长度
	- 计算：`max(f[i-1,j], f[i,j-1], *f[i-1,j-1]+1)`
		- 当 `a[i]==b[j]` 时包含 `f[i-1,j-1]+1` 项
		- 状态集合根据公共子串是否包含 `s1[i]`，`s2[j]` 划分为四个子集
		- `f[i-1, j-1]` 的情况包含在 `f[i-1,j]` 与 `f[i,i-1]` 中

常用状态：
- 数字对应坐标（网格，三角）
- 起始/终止数字（最短/最长序列）
- 字符位置（多个字符串，每个串占个状态）

## 区间 DP

定义的状态是一个区间

## 计数类 DP

## 数位统计 DP

针对每个数字分情况讨论

## 状态压缩 DP

常利用二进制位表示状态

## 树形 DP

两个属性，从某棵子树选，以及方案特征，n 比较小

## 记忆化搜索

（备忘录递归？）

# 贪心

贪心问题没有套路，难以证明///

## 区间问题

- 排序
	- 按起点排序
	- 按终点排序
	- 双关键字排序
- 选择与测试：按顺序对每个元素进行遍历
- 证明：常用反证法
  
## 其他思路

- 每次选择局部最优解进行合并
- 推公式：将公式推导出来，使用各种不等式比较判断

# 复杂度分析

一般来说，题目要求在 1s 内解决问题，C++ 在 1s 内可执行约 $10^7-10^8$ 次运算，基于数据量常用算法为：
1. 数量很少，$n\leq30$：DFS+剪枝，状压dp 等
2. $n\leq100$：可用时间复杂度为 $O(n^3)$ 的算法：[[#Bellman-Ford]]，[[#动态规划]]
3. $n\leq1000$：可用时间复杂度为 $O(n^2)$，$O(n^2logn)$ 的算法：[[#动态规划]]，[[#朴素 dijkstra 算法]]
4. $n\leq10000$：可用时间复杂度为 $O(n\sqrt n)$ 的算法：块状链表
5. $n\leq100000$：可用时间复杂度为 $O(n\log n)$ 的算法：各种[[#排序]]，树、[[#整数二分]]及基于它们的容器，[[#SPFA]]，凸包，半平面交等
5. $n\leq1000000$：可用时间复杂度为 $O(n)$，常数比较小的 $O(n\log n)$ 的算法：
	- 双指针，[[#Hash]]，[[#KMP 字符串]]，AC自动机，[[#质数筛法]]
	- [[#整数二分]]及基于它们的容器，sort，树状数组，[[#堆]]，[[#朴素 dijkstra 算法]]，[[#堆优化 dijkstra 算法]]，[[#SPFA]]
6. $n\leq10000000$：可用时间复杂度为 $O(n)$ 的算法：双指针，[[#KMP 字符串]]，AC自动机，[[#质数筛法]]
6. $n\leq100000000$：可用时间复杂度为 $O(\sqrt n)$ 的算法：试除法求质数、约数、分解质因数等
6. $n\leq1000000000$：可用时间复杂度为 $O(\log n)$ 的算法：[[#辗转相除法]]，[[#快速幂]]

> [由数据范围反推算法复杂度以及算法内容 - AcWing](https://www.acwing.com/blog/content/32/)
> 
> - $n \le 30$, 指数级别, dfs+剪枝，状态压缩dp
> - $n \le 100$ => $O(n^3)$，floyd，dp，高斯消元
> - $n \le 1000$ => $O(n^2)$，$O(n^2logn)$，dp，二分，朴素版Dijkstra、朴素版Prim、Bellman-Ford
> - $n \le 10000$ => $O(n * \sqrt n)$，块状链表、分块、莫队
> - $n \le 100000$ => $O(nlogn)$ => 各种sort，线段树、树状数组、set/map、heap、拓扑排序、dijkstra+heap、prim+heap、Kruskal、spfa、求凸包、求半平面交、二分、CDQ分治、整体二分、后缀数组、树链剖分、动态树
> - $n \le 1000000$ => $O(n)$, 以及常数较小的 $O(nlogn)$ 算法 => 单调队列、 hash、双指针扫描、并查集，kmp、AC自动机，常数比较小的 $O(nlogn)$ 的做法：sort、树状数组、heap、dijkstra、spfa
> - $n \le 10000000$ => $O(n)$，双指针扫描、kmp、AC自动机、线性筛素数
> - $n \le 10^9$ => $O(\sqrt n)$，判断质数
> - $n \le 10^{18}$ => $O(logn)$，最大公约数，快速幂，数位DP
> - $n \le 10^{1000}$ => $O((logn)^2)$，高精度加减乘除
> - $n \le 10^{100000}$ => $O(logk \times loglogk)，k表示位数$，高精度加减、FFT/NTT
