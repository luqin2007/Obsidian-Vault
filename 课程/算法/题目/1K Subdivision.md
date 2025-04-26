> “范式杯”2023牛客暑期多校训练营1
> https://ac.nowcoder.com/acm/contest/57355/K

难度：Easy
# 题目
  
You are given a graph with $n$ vertices and $m$ undirected edges of length $1$. You can do the following operation on the graph for arbitrary times:  

> Choose an edge $(u,v)$ and replace it by two edges, $(u,w)$ and $(w,v)$, where $w$ is a newly inserted vertex. Both of the two edges are of length $1$.

You need to find out the maximum number of vertices whose minimum distance to vertex $1$ is no more than $k$.
## 输入格式
  
The first line contains three integers $n\ (1\leq n\leq 10^5)$, $m\ (0 \leq m \leq 2\cdot 10^5)$ and $k\ (0\leq k\leq 10^9)$.  
Each of the next $m$ lines contains two integers $u$ and $v\ (1\leq u,v\leq n)$, indicating an edge between $u$ and $v$. It is guaranteed that there are no self-loops or multiple edges.
## 输出格式
  
Output an integer indicating the maximum number of vertices whose minimum distance to vertex $1$ is no more than $k$.
## 输入样例

```
5 4 2
1 2
2 3
3 1
4 5
```
## 输出样例

```
5
```
## 输入样例

```
8 9 3
1 2
1 3
1 5
3 4
3 6
4 5
5 6
6 7
7 8
```
## 输出样例

```
15
```
## 输入样例

```
114 0 514
```
## 输出样例

```
1
```
## 样例解释

Here is the illustration for the second example.
![[Pasted image 20230809134239.png]]
## 代码限制

时间限制：C/C++ 1秒，其他语言2秒  
空间限制：C/C++ 524288K，其他语言1048576K  
64bit IO Format: %lld
# 解析

给定 n 个顶点和 m 条边的无向图，顶点下标从 1 开始。可以将任意一条边拆分成两条边，此时图中就多出了一个顶点。问经过若干次拆分后，到 1 距离不超过 k 的顶点最多为多少
# 思路

首先构建从 1 点出发的 BFS 树并去除所有超过 k 步的路径，分裂未达到 k 的节点路径即可
- 某分支不足 k：分裂补全 k
- 某分支存在已访问过的点：分裂补全 k（该点有另一条分支保护，此处可放心分裂）
