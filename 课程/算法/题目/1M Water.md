> “范式杯”2023牛客暑期多校训练营1
> https://ac.nowcoder.com/acm/contest/57355/M 

难度：Mid
# 题目

Walk Alone is thirsty and he wants to drink water. He wants to drink exactly $x$ units of water, but he does not have an appropriate measuring cup. He only has two water bottles of volume $A$ and $B$ respectively. He found out that he could do the following operations with the two bottles:  
- Fill one of the two bottles with water.
- Spill all water from one of the two bottles.
- Drink all water from one of the two bottles.
- Transfer as much water as possible from one bottle to the other with no water overflow. Formally speaking, if the two bottles contain $a$ and $b$ units of water respectively, he can only transfer $\min(a,B-b)$ water from $A$ to $B$ or $\min(b,A-a)$ from $B$ to $A$.

Walk Alone wants to do as few operations as possible. Can you tell him the minimum number of operations he should do to drink exactly $x$ units of water?
## 输入格式

The input contains multiple test cases.  
The first line of the input contains an integer $T\ (1\leq T\leq 10^5)$, the number of test cases.  
Each test case contains only one line with three integers $A,B,x\ (1 \leq A,B,x\leq 10^9)$, the volume of the two bottles and the required volume of water he will drink, respectively.
## 输出格式

For each test case, output the minimum number of operations in one line. If it is impossible for him to drink exactly $x$ units of water, output −1-1−1.
## 输入样例

```
5
2 5 1
2 3 7
4 6 3
3 13 8
12 9 6
```
## 输出样例

```
5
6
-1
15
5
```
## 样例解释

In the first example, Walk Alone will do the following operations:  
1. Fill the second bottle.  
2. Transfer $2$ units of water from the second bottle to the first one.  
3. Spill all water from the first cup.  
4. Transfer $2$ units of water from the second bottle to the first one, after which there is exactly one unit of water in the second bottle.  
5. Drink all water in the second bottle.
## 代码限制
  
时间限制：C/C++ 1秒，其他语言2秒  
空间限制：C/C++ 524288K，其他语言1048576K  
64bit IO Format: %lld
# 解析

倒水问题。两个杯子，需要喝掉给定数目的水的最小步骤或 -1，可用操作包括：
- 灌满某个杯子
- 清空一个杯子
- 将一个杯子的水尽可能多的倒入另一个杯子，另一个杯子不溢出
- 喝掉杯子中的水
# 思路

将每个杯子的状态使用数学语言描述。可以看到每个杯子之内的水都是 a b 两个数字的组合，可以表示成 $A=(r_a,s_a)=r_aa+s_ab$，$B=(r_b,s_b)=r_ba+s_bb$，其中 A B 为两个杯子内实际的水量，a b 为两个杯子的容量，r s 为系数。外加喝掉的水为 $S$ 那么，每一步操作都可以用数学语言表达：
- 灌满：$A=(1,0)$ 或 $B=(0,1)$
- 清空：$A=(0,0)$ 或 $B=(0,0)$
- 倒水：以从 A 向 B 倒水为例：
	- 若 $(r_a+r_b,s_a+s_b)\le(0,1)$，$A=(0,0)$，$B=(r_a+r_b,s_a+s_b)$
	- 若 $(r_a+r_b,s_a+s_b)\gt(0,1)$，$A=(r_a+r_b,s_a+s_b)$，$B=(0,1)$
- 喝水：$A=(0,0), S+(r_a,s_b)$ 或 $B=(0,0),S+(r_b,s_b)$

从结果上来看，S 也必然能写成 $x=S=(r_s,s_s)=r_sa+s_sb$ 的形式。于是原题可拆解成两步：
- 根据 a，b，x，求出 r，s，如果无解则输出 -1 （扩展欧几里得算法）
- 根据求出的 r，s，分配倒水喝水的最小步骤为 $\max{(2(r_s+s_s),2|r_s-s_s|-1)}$
	- $step \le\max{(2(r_s+s_s),2|r_s-s_s|-1)}$
		- $r_s\ge0$，$s_s\ge0$ 时，喝 $r_s$ 杯A，$s_s$ 杯B恰好满足要求
		- ${r_s}\cdot{s_s}\lt0$ 时，设 $r_s\ge0$（反之同理），则满 A 杯的水可用来倒入 B 或喝掉，B 则只有倒掉
			- 当优先满足倒水需求时，可以不齐每次 B 的行动一定是 2 步
				- 当 A 剩余水能填满 B 时，A 向 B 倒水次数+清空 B 水次数算入 B
				- 当 A 剩余水无法填满 B 时，A 仍向 B 倒水，但 B 不会清空；但 A 要填满，该次数算入 B
				- 最后一杯 B 无需倒掉
			- 当水都被倒掉后，只剩下喝水，每次灌水-喝水需要 2 步
	- $step \ge\max{(2(r_s+s_s),2|r_s-s_s|-1)}$
		![[Pasted image 20230809104407.png]]
综上，使用 `exgcd` 计算出 $r_s, s_s$ 后，对附近的 $(r_s+kB,s_s-kA)$ 取最小值即可
# 答案

```cpp
#include <iostream>

using namespace std;
using ll = long long;

ll exgcd(ll a, ll b, ll &x, ll &y) {
    if (!b) {
        x = 1;
        y = 0;
        return a;
    }

    ll d = exgcd(b, a % b, y, x);
    y -= a / b * x;
    return d;
}

void solve() {
    ll a, b, x;
    cin >> a >> b >> x;

    ll s, t;
    ll d = exgcd(a, b, s, t);
    if (x % d) {
        // 无法达成
        cout << -1;
        return;
    }
    // 全部条件对最大公约数简化
    a /= d;
    b /= d;
    x /= d;
    s *= x;
    t *= x;


    ll ans = 1e18;
    // 搜索最小值
    ll t0 = -s / b;
    for (ll i = t0 - 1; i <= t0 + 1; ++i) {
        ll j = s + b * i, k = t - a * i;
        if (j >= 0 && k >= 0) ans = min(ans, 2 * (j + k));
        else ans = min(ans, 2 * (abs(j) + abs(k)) - 1);
    }
    t0 = t / a;
    for (ll i = t0 - 1; i <= t0 + 1; ++i) {
        ll j = s + b * i, k = t - a * i;
        if (j >= 0 && k >= 0) ans = min(ans, 2 * (j + k));
        else ans = min(ans, 2 * (abs(j) + abs(k)) - 1);
    }
    cout << ans;
}

int main() {
    int t;
    cin >> t;
    while (t--) {
        solve();
        cout << endl;
    }
    return 0;
}
```