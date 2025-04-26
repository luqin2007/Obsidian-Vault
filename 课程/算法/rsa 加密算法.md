> 对称加密：加密与解密需要的密钥相同，常见有 `AES`，`DES`，`Blowfish` 等

- 加解密速度快，适用于大数据量加解密
- 密钥管理困难，安全性差

> 非对称加密：存在公钥与私钥，公钥加密的数据只有私钥可解密，私钥加密的数据只有公钥可解密

*很容易找到两个大素数并计算出乘积；但根据乘积反向找到这两个素数则非常困难*

# 密钥生成

1. 准备两个大素数 $a,b>2^{1024}$
2. 计算乘积 $n=pq$
3. 计算 $n$ 的欧拉函数 $m=\psi(n)=\psi(pq)=(p-1)(q-1)$
4. 找一个与 $m$ 互质的数 $e$，使 $(e,m)=1$
5. 计算 $e$ 在模 $m$ 域上的逆元 $d$，即 $ed\equiv1\pmod m$
6. 公钥数对 $(n,e)$，私钥数对 $(n,d)$

# 加密

对于明文 $x'$，通过一定规律（Unicode 编码等）将其转化为数字 $x$，公钥 $(n,e)$，则其密文 $y$ 为：
$$y=x^e\mod n$$

# 解密

对于密文 $y$ 以及私钥 $(n,d)$，其数字原文 $x$ 为
$$x=y^d\mod n$$
之后通过一定规律将其重新转化为明文即可

# 证明

[RSA —— 经典的非对称加密算法 - 知乎 (zhihu.com)](https://zhuanlan.zhihu.com/p/450180396)

$$\begin{aligned}
y^d&\equiv x^{ed}\\
  &\equiv x^{km+1}&\text {$ed\equiv1\pmod m$}\\
  &\equiv x\cdot (x^{m})^k\\
  &\equiv x\cdot x^{k\psi(p)\psi(q)}\pmod n
\end{aligned}$$

