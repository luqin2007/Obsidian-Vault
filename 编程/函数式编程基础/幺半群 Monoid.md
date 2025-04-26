半群 Semigroup：集合 S 有二元运算 $S\times S\to S$，且满足结合律，即 $\forall x, y, z\in S$，有 $(x\cdot y)\cdot z=x\cdot (y\cdot z)$，称数对 $(S, \cdot)$ 为半群，$\cdot$ 为该半群的乘法。上下文明确的情况下，可简称半群 S

**任何 functor 都是半群**

幺半群 Monoid：若半群 S 上的乘法有幺元（单位元），即 $\exists 1\in S$，有 $\forall s\in S, 1\cdot s=s\cdot 1=s$，称 S 为幺半群

半群定义了一个数据集合 S 和一个运算符 $\cdot$，该运算符合结合律，且任意两个集合集合中的元素通过运算符运算后结果仍在集合 S 中。

幺半群则是在半群的基础上，增加了一个单位元，该元素与任何元素 s 进行运算后结果仍为 s。

以集合为例。任意 `List` 组成的集合与运算 `addAll` 组成了一个幺半群。任意两个 `List` 执行 `addAll` 后形成的新列表仍是一个列表，符合半群定义；一个空列表可以认为是一个单位元，符合幺半群定义。

在函数式编程中，`Monoid` 定义的运算通常称为 `concat`，用以合并两个容器的结果。

* 函数可以是 Monoid

  * `identity` 可以作为单位元
  * 任意两个定义域和值域都在相同的集合内，可以通过 compose 进行 concat
* Monad 可以是 Monoid
* Applicative functor 可以是 Monoid：`lax monoidal functor`，通过实现 Monoid 后在还原出 ap

‍
