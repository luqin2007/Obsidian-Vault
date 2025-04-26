# 基本样式

Markdown LaTeX 数学公式与标准 LaTeX 公式类似
* 使用 `$ ... $` 表示行间公式：`$y=x+1$` $y=x+1$
* 使用 `$$ ... $$` 则表示独立公式：`$$y= x+1$$`
* 使用 `\` 转义，使用 `{ ... }` 表示一组符号
# 特殊字符
## 希腊字符

常见希腊字符包括

| 代码       | 字符       | 代码       | 字符       | 代码       | 字符       |
| -------- | -------- | -------- | -------- | -------- | -------- |
| `\alpha` | $\alpha$ | `\omega` | $\omega$ | `\sigma` | $\sigma$ |
| `\beta`  | $\beta$  | `\phi`   | $\phi$   | `\theta` | $\theta$ |
| `\delta` | $\delta$ | `\pi`    | $\pi$    | `\Delta` | $\Delta$ |
| `\eta`   | $\eta$   | `\psi`   | $\psi$   | `\Sigma` | $\Sigma$ |
| `\mu`    | $\mu$    | `\rho`   | $\rho$   | `\Pi`    | $\Pi$    |
* 如果存在，首字母大写表示对应的大写字母，如 `\delta` 对应 `\Delta`，`\psi` 对应 `\Psi` 等
* `$o$` 符号就是字母 `o`，不需要转义



其余详见 [[#附录#希腊与希伯来字符]]
## 运算符与标点
### 运算符

一般 `+` `-` 运算符直接用即可。常见其他运算符有：

|运算符|代码|运算符|代码|运算符|代码|运算符|代码|
| --------| ------| --------| ------| --------| ------| --------| ------|
|$\times$|`\times`|$\div$|`\div`|$\cdot$|`\cdot`|$\pm$|`\pm`|
|$\circ$|`\circ`|$\bullet$|`\bullet`|$\diamond$|`\diamond`|$\odot$|`\odot`|
|$\equiv$|`\equiv`|$\neq$|`\neq`|$\sim$|`\sim`|$\approx$|`\approx`|
|$\ll$|`\ll`|$\gg$|`\gg`|$\leq$|`\leq`|$\geq$|`\geq`|
|$\cap$<br />|`\cap`|$\cup$|`\cup`|$\vee$|`\vee`|$\wedge$|`\wedge`|
|$\subset$|`\subset`|$\subseteq$|`\subseteq`|$\subseteqq$|`\subseteqq`|$\in$|`\in`|
|$\nsubseteq$|`\nsubseteq`|$\subsetneq$|`\subsetneq`|$\nsubseteqq$|`\nsubseteqq`|$\subsetneqq$|`\subsetneqq`|
|$\supset$|`\supset`|$\supseteq$|`\supseteq`|$\supseteqq$|`\supseteqq`|$\notin$|`\notin`|
|$\nsupseteq$|`\nsupseteq`|$\supsetneq$|`\supsetneq`|$\nsupseteqq$|`\nsupseteqq`|$\supsetneqq$|`\supsetneqq`|
|$\because$|`\because`|$\therefore$|`\therefore`|$\propto$|`\propto`|$\nmid$|`\nmid`|
大运算符：

| 符号           | 格式            | 符号            | 格式                        | 符号           | 格式            |
| ------------ | ------------- | ------------- | ------------------------- | ------------ | ------------- |
| $\sum$      | `\sum`      | $\prod$      | `\prod`                 | $\coprod$   | `\coprod`   |
| $\biguplus$ | `\biguplus` | $\bigcup$    | `\bigcup`               | $\bigcap$   | `\bigcap`   |
| $\bigoplus$ | `\bigoplus` | $\bigotimes$ | `\bigotimes`            | $\bigodot$  | `\bigodot`  |
| $\bigvee$   | `\bigvee`   | $\bigwedge$  | `\bigwedge`             | $\bigsqcup$ | `\bigsqcup` |
| $\int$      | `\int`      | $\iiiint$    |  `\iiiint`  (支持1-4个 i ) | $\idotsint$  | `\idotsint` |
大运算符，包括极限 `\lim`，作为行内公式时会被压缩以适应行高。使用 `\limits` 和 `\nolimits` 可以控制是否压缩，压缩后上下标会放置在符号顶部和底部
* `\sum_{i=1}^n i`：$\sum_{i=1}^n i$
* `\sum\nolimits_{i=1}^n i`：$\sum\nolimits_{i=1}^n i$
* `\sum\limits_{i=1}^n i`：$\sum\limits_{i=1}^n i$
其余详见[[#附录#运算符]]
### 箭头
#### 水平箭头

水平箭头格式为 `\[long][left][right]arrow`
* `long`：长箭头，$\leftarrow$`\leftarrow`，$\longleftarrow$`\longleftarrow`
* 首字母大写双线：$\leftarrow$`\leftarrow`，$\Leftarrow$`\Leftarrow`
* 左箭头可以用 `\gets`：$x\gets0$，右箭头可以用 `\to`：$x\to0$
* `\Longleftrightarrow` 还可以简写成 `\iff`：$\iff$
#### 意图箭头

意图箭头指的是箭头上下带有文本的箭头，格式分为两种
* 通用方法：使用 `\stackrel`，`\overset`，`\underset`
* `\x[left/right]arrow[底部内容]{顶部内容}`：
  * $\xleftarrow{x+y+z}$，$\xrightarrow {a+b+c}$
  * $\xleftarrow[x+y+z]{}$，$\xrightarrow[x+y+z]{}$
  * $\xleftarrow[x+y+z]{a+b+c}$，$\xrightarrow[x+y+z]{a+b+c}$
#### 普通箭头

普通箭头格式为 `\[方向]arrow`
* 方向：箭头方向，每一组可以两个都有，但至少有一个
    * 水平：`left` ，`right` 
    * 垂直：`up` ，`down` 
    * 斜向
        * 东北-西南：`ne` ，`sw` 
        * 东南-西北：`se` ，`nw` 
* 首字母大写时为双线：$\uparrow$ 与 $\Uparrow$
#### 单边箭头

单边箭头格式为 `[left][right]harpoon[s][up][down]`
* `\leftrightharpoons`：$\leftrightharpoons$
* `\leftharpoonup`，`\leftharpoondown`：$\leftharpoonup$，$\leftharpoondown$
* `\rightharpoonup`，`\rightharpoondown`：$\rightharpoonup$，$\rightharpoondown$

列表及其他箭头详见 [[#附录#箭头符号]]
### 界定符

* `|`，`[`，`]`，`/` 直接用原字符即可

|字符|代码|字符|代码|
| -------------| -----------------| ------| ----------|
|$\mid$|`\vert`，\|，`\mid`|$\|$|`\|`，`\Vert`|
|$\uparrow$ $\Uparrow$|`\uparrow`，`\Uparrow`|$\lceil$ $\rceil$|`\lceil`，`\rceil`|
|$\downarrow$ $\Downarrow$|`\downarrow`，`\Downarrow`|$\lfloor$ $\rfloor$|`\lfloor`，`\rfloor`|
|$\langle$ $\rangle$|`\langle`，`\rangle`|$\ulcorner$ $\urcorner$|`\ulcorner`，`\urcorner`|
|\{ \}|`\{`，`\}`|$\llcorner$ $\lrcorner$|`\llcorner`，`lrcorner`|
|$\backslash$|`\backslash`|||
### 其他
| 符号        | 格式          | 符号          | 格式            | 符号         | 格式           | 符号                        | 格式                           |
| --------- | ----------- | ----------- | ------------- | ---------- | ------------ | ------------------------- | ---------------------------- |
| $\infty$  | `\infty`  | $\cdots$    | `\cdots`    | $\dots$    | `\dots`    | $\varnothing$，$\emptyset$ | `\varnothing`，`\emptyset` |
| $\forall$ | `\forall` | $\exists$   | `\exists`   | $\nexists$ | `\nexists` | $\angle$                  | `\angle`                   |
| $\nabla$  | `\nabla`  | $\triangle$ | `\triangle` |            |              |                           |                              |

详见[[#附录#其他符号]]
# 数学函数

数学函数应当使用普通罗马字母，而不是斜体字母，通过在数学函数前加 `\` 转义即可

常见函数有包括所有三角函数，`\log`，`\lg`，`\ln`，`\min`，`\max`，`\lim`，`\deg` 等

其他详见[[#附录#数学函数]]
# 数学结构
## 行内结构
### 上下标

* 使用 `^` 表示上标：$a^2$，$(a+b)^{i+j}$
* 使用 `_` 表示下标：$a_i$，$s_{i+1}$
* 若一个标记左侧和右侧都有上下标等修饰，使用 `\sideset{左侧}{右侧}{符号}`：$\sideset{^1_2}{_4^3}\bigotimes$
### 删除

* 在任意字符之前增加 `\not`，可以为其增加一条从左上到右下的删除线：$\not a$，$\not=$，$\not\alpha$，$\not\sum$
* 使用 `\require{cancel}` 允许片段删除，好像也不用
    *  `\cancel{被删内容}` ：$\cancel{a+b}$
    *  `\xcancel{被删内容}` ：$\xcancel{a+b}$
    *  `\cancelto{左上角}{被删内容}` ：$\cancelto{0}{a+b}$
### 分数

* 使用 `\frac{分子}{分母}` 表示分数：$\frac{abc}{xyz}$
* 使用 `{分子}\over{分母}` 构建分数：${abc}\over{xyz}$
* 使用 `\cfrac{分子}{分母}` 构建连分数，不会产生字体自动缩小：$\cfrac{abc}{xyz}$
* 使用 `\dfrac{分子}{分母}` 构建的分数字号与独立公式大小相同：$\dfrac{abc}{xyz}$
* 使用 `\tfrac{分子}{分母}` 构建的分数字号与行间公式大小相同：$\tfrac{abc}{xyz}$
### 组合

将任意两个式子上下排列组合成一组表示：
* 同一行内上下并排使用并加括号 `上部\choose 下部` 或 `\binom{上部}{下部}`：$n+1 \choose 2k$，$\binom{n+1}{2k}$
* 同一行内上下并排使用 `上部\atop 下部`：$n+1 \atop 2k$
* 同样支持 `c`，`d`，`t` 前缀调整样式，类似分数

将任意式子与其他符号组合表示：
* 在任意运算符或其他符号顶部添加文字使用 `\stackrel{顶部}{符号}`：$\stackrel{F,T}+$
* 在任意运算符或其他符号顶部添加文字使用 `\overset{顶部}{符号}`：$\overset{F,T}+$
* 在任意运算符或其他符号顶部添加文字使用 `\underset{底部}{符号}`：$\underset{F,T}+$
### 根号

* 使用 `\sqrt[n]{根号下内容}` 表示根号：$\sqrt5$，$\sqrt[5]{a+b}$
* `\surd` 表示不带横线的根号：$\surd 3$
### 求导与积分

* 求导符号就是 `'` 或 `\prime`：$f'$，$f''$，$y\prime$
* 定积分为 `\int`，i 个数为 1-4 代表 1-4 重定积分：$\iint_3^5 xdx$
* 曲线积分使用 `\oint`：$\oint$
* 极限使用 `\lim`：$\lim_{x\rightarrow 0} x$
* `\partial`：$\partial$
* `\mathrm d`，`\rm d`：$\mathrm d$，$\rm d$
### 取模

使用 `\bmod` 表示取模运算：$10\bmod 3=1$
### 顶部/底部修饰

* 横线：`\overline{...}`，`\underline{...}`：$\overline{abc}$，$\underline{abc}$
    * 单字符横线，求平均数 `\bar{...}` ：$\bar x$
* 大括号：`\overbrace{...}`，`\underbrace{...}`：$\overbrace{abc}$，$\underbrace{abc}$
    *  `\overbrace{...}^{...}`  可以在上方标注内容：$\overbrace{a_1, a_2, a_3, \cdots, a_i}^{i:1 到 n}$
    *  `\underbrace{...}_{...}`  可以在下方标注内容：$\underbrace{a_1, a_2, a_3, \cdots, a_i}_{i:1 到 n}$
* 箭头
    * 右箭头：`\overrightarrow{...}` ，`\underrightarrow{...}` ：$\overrightarrow{abc}$，$\underrightarrow{abc}$
    * 左箭头：`\overleftarrow{...}` ，`\underleftarrow{...}` ：$\overleftarrow{abc}$，$\underleftarrow{abc}$
    * 向量：`\vec{...}` ：$\vec a$
* 尖头：`\hat`，`\widehat{...}`：$\hat a$，$\widehat{abc}$
* 波浪线：`\tilde`，`\widetilde{...}`：$\tilde a$，$\widetilde{abc}$
* 点号：`\dot{...}`，点的个数与 `d` 的个数相同：$\dot a$，$\ddot b$
## 其他修饰
### 同余

同余使用 `\pmod{m}` 配合 `\equiv`：$5\equiv 3\pmod2$
### 序号

通过 `\tag{n}` 可以为公式添加一个序号
$$
x=a+b\tag1
$$
### 注释文本

通过 `\text{注释}`  或 `\mbox{注释}`  添加注释文本，注释文本不会识别为公式，不用斜体显示
注释文本中仍可以使用 `$ ... $` 添加公式
* `\mbox`：使用统一的正文样式
* `\text`：根据当前位置调整文本样式
$$
f(n)=\begin{cases}
n/2,&\text{if $n$ is even}\\
3n+1,&\mbox{if $n$ is odd}
\end{cases}
$$
## 公式环境

公式环境由 `\begin{类型}` 开始，`\end{类型}` 结束
### 矩阵

矩阵类型为 `matrix` ，矩阵元素之间使用 `&`  分隔：

$$
\begin{matrix}
1&2&3&4\\
a&b&c&d\\
0&0&0&0
\end{matrix}
$$

`bmatrix` 可以给矩阵加上一个中括号，`Bmatrix` 可以给矩阵加上一个大括号

$$
\begin{bmatrix}
1&2&3&4\\
a&b&c&d\\
0&0&0&0
\end{bmatrix}
\begin{Bmatrix}
1&2&3&4\\
a&b&c&d\\
0&0&0&0
\end{Bmatrix}
$$

`vmatrix` 可以给矩阵加上一道竖线，`Vmatrix` 可以给矩阵加上两道竖线

$$
\begin{vmatrix}
1&2&3&4\\
a&b&c&d\\
0&0&0&0
\end{vmatrix}
\begin{Vmatrix}
1&2&3&4\\
a&b&c&d\\
0&0&0&0
\end{Vmatrix}
$$
### 分段函数

分段函数类型为 `cases`，分段函数自带一个左大括号，且左侧在 y 轴居中

$$
f(x)=\begin{cases}
2x,\,\,x>0\\
3x,\,\,x\le0\\
\end{cases}
$$
### 多行等式

多行等式，即多行式中某个位置对齐，其类型为 `aligned`，对齐位置前用 `&` 标记

$$
\begin{aligned}
f(x)&=(m+n)^2\\
&=m^2+2mn+n^2
\end{aligned}
$$

$$
\begin{aligned} 3^{6n+3}+4^{6n+3}
& \equiv (3^3)^{2n+1}+(4^3)^{2n+1}\\
& \equiv 27^{2n+1}+64^{2n+1}\\
& \equiv 27^{2n+1}+(-27)^{2n+1}\\
& \equiv 27^{2n+1}-27^{2n+1}\\
& \equiv 0 \pmod{91}\\
\end{aligned}
$$

还有一个 `alignedat`，带一个 `{n}`，好像是指定宽度，但看上去都一样

$$
\begin{alignedat}{3}
f(x) & = (m-n)^2 \\
f(x) & = (-m+n)^2 \\
     & = m^2-2mn+n^2 \\
\end{alignedat}
$$
### 表格

表格使用 `array` 类型，带一个参数列表定义列数和对齐方法如 `{c|cc}`
* 每一个字符 `c`，`l`，`r` 表示一列，`c` 为居中，`l` 为左对齐，`r` 为右对齐
* `|` 表示一道垂直分割线

表格中可以有一种特殊的数据 `\hline` 表示一个水平分割线，分割线后不需要换行符

$$
\begin{array}{c|lcr}
n&\text{左对齐}&\text{居中对齐}&\text{右对齐}\\
\hline
1&0.24&1&125\\
2&-1&189&-8\\
3&-20&2000&1+10i\\
\end{array}
$$

多个表格之间可以互相嵌套

$$
\begin{array}{c}
    \begin{array}{cc}
        \begin{array}{c|cccc}
        \text{min}&0&1&2&3\\
        \hline
        0&0&0&0&0\\
        1&0&1&1&1\\
        2&0&1&2&2\\
        3&0&1&2&3
        \end{array}
        &
        \begin{array}{c|cccc}
        \text{max}&0&1&1&3\\
        \hline
        0&0&1&2&3\\
        1&1&1&2&3\\
        2&2&2&2&3\\
        3&3&3&3&3
        \end{array}
    \end{array}
    \\
    \begin{array}{c|cccc}
    \Delta&0&1&2&3\\
    \hline
    0&0&1&2&3\\
    1&1&0&1&2\\
    2&2&1&0&1\\
    3&3&2&1&0
    \end{array}
\end{array}
$$
### 交换图表

交换图表类型为 CD

* `@>>>` 表示右箭头，`@<<<` 表示左箭头，`@AAA` 表示上箭头，`@VVV` 表示下箭头
    *  `>>>` ，`<<<` ，`AAA` ，`VVV`  之间任意位置插入文本表示箭头注释，两个位置则表示在箭头两边都有注释，如 `@>a>b>`  表示箭头上方为 a 下方为 b
* `@=` 表示水平双实线，`@|` 表示垂直双实线，`@` 表示没有箭头的实线

$$
\begin{CD}
A @>a>> B\\
@VbVV \# @VVcV\\
C @>>d> D @>\text{very very long label}>> E
\end{CD}
$$
### 其他环境
#### 单行环境

最简单的公式环境，使用 `equation` 创建，不可使用 `\\` 和 `&`，可以内嵌一些关于对齐的环境

$$
\begin{equation}
f(x)=3x^2+6(x-2)-1
\end{equation}
$$
#### 多公式环境

多公式环境通过 `align` 创建，是最基本的对齐环境。
* 使用 `&` 分割多个对齐单元
* 使用 `\\` 换行
* 支持 `\tag` 设置标号及使用 `\label`，`\ref` 引用

$$
\begin{align}
A_1&=B_1B_2&A_3&=B_1\\
A_2&=B_3&A_3A_4&=B_4
\end{align}
$$
#### 公式组环境

公式组环境通过 `\falign` 创建，与 `align` 基本相同，但单元之间距离为弹性宽度，以便公式两端对齐
#### gather

最简单的多行环境，不提供任何对齐方式，各行公式都使用全局对齐方式

$$
\begin{gather*}
E(X)=\lambda\qquad D(X)=\lambda\\
E(\bar x)=\lambda\\
\end{gather*}
$$
#### multline

首行左对齐，尾行右对齐，其余各行全局对齐，不支持 `&` 分列
#### split

* 允许使用 `&` 分列但最多 2 列，左列右对齐，右列左对齐，使其针对某一个位置对齐
    * 当只有一列时，全部与首行公式右端对齐
* 外层环境不能是 `multline`
* 自身不生成公式序号，序号由外部环境提供，垂直居中
#### -ed

`gather` - `gathered`，`align` - `aligned` 两组环境，其区别在于，不带有 `-ed` 结尾的环境，公式无论大小，都会占满一行
## 界定符匹配

使用 `\left` 与 `\right` 组合，中间填写任意多行结构，可以使[[#界定符]]匹配之间内容的大小：

$$
\left|\begin{matrix}
1&2&3\\
4&5&6\\
7&8&9
\end{matrix}\right|
$$

若某一边不存在界定符，则可以使用 `.`

$$
\left.\begin{matrix}
1\\
2\\
3
\end{matrix}\right\}
$$

可利用界定符和数组实现带分隔符号的矩阵

$$
\left[\begin{array}{cc|c}
1&2&3\\
4&5&6
\end{array}\right]
$$
# 其他
## 间距

* 两个 `quad` 空格 `\qquad`，`quad` 空格 `\quad`：一个 `quad` 空格宽度为一个 m 的长度
* 大空格 `\ `：宽度为 1/3 个 m 的长度
* 中等空格 `\;`：宽度为 2/7 个 m 的长度
* 小空格 `\,`：宽度为 1/6 个 m 的长度
* 紧贴 `\!`：缩窄 1/6 个 m 的长度

`a\!bc\,d\;e\ f\quad g\qquad h`：$a!bc,d;e\ f\quad g\qquad h$
## 字形

* 粗体：`\mathbf{文本内容}`，`\bf{文本内容}`

$$
\mathbf{ABCDEFGHIJKLMNOP}\bf{QRSTUVWXYZabc123}
$$

* 花体：`\mathcal{文本内容}`，`\cal{文本内容}`

$$
\mathcal{ABCDEFGHIJKLMNOP}\cal{QRSTUVWXYZabc123}
$$

* 旧德式字体：`\mathfrak{文本内容}`，`\frak{文本内容}`

$$
\mathfrak{ABCDEFGHIJKLMNOP}\frak{QRSTUVWXYZabc123}
$$

* 等线体：`\mathsf{文本内容}`，`\sf{文本内容}`

$$
\mathsf{ABCDEFGHIJKLMNOP}\sf{QRSTUVWXYZabc123}
$$

* 黑板粗体：`\mathbb{文本内容}`，`\Bbb{文本内容}`

$$
\mathbb{ABCDEFGHIJKLMNOP}\Bbb{QRSTUVWXYZabc123}
$$

* 罗马体：`\mathrm{文本内容}`，`\rm{文本内容}`

$$
\mathrm{ABCDEFGHIJKLMNOP}\rm{QRSTUVWXYZabc123}
$$

* 打印机：`\mathtt{文本内容}`，`\tt{文本内容}`

$$
\mathtt{ABCDEFGHIJKLMNOP}\tt{QRSTUVWXYZabc123}
$$

* 手写体：`\mathscr{文本内容}`，`\scr{文本内容}`

$$
\mathscr{ABCDEFGHIJKLMNOP}\scr{QRSTUVWXYZabc123}
$$

* 意大利体：`\mathit{文本内容}`，`\it{文本内容}`

$$
\mathit{ABCDEFGHIJKLMNOP}\it{QRSTUVWXYZabc123}
$$

* 数字斜体：`\mit{文本内容}`

$$
\mit{ABCDEFGHIJKLMNOPQRSTUVWXYZabc123}
$$

* 加粗斜体：`\boldsymbol`

$$
\boldsymbol{ABCDEFGHIJKLMNOPQRSTUVWXYZabc123}
$$

* 原文照排：`\verb+...+` 或 `\verb|...|` 等，前后有一个相同的字符标记：$\verb+abc+$，$\verb|def|$，$\verb'ghi'$
## 大小

使用 `\large` 和 `\small` 可以使用较大或较小的字号，并影响之后所有内容：$\large Ab+1 Ab+1 \small Ab+1$

对于括号类，可通过增加前缀控制其大小：
* 左符号：`Biggl`，`biggl`，`Bigl`，`bigl`
* 右符号：`Biggr`，`biggr`，`Bigr`，`bigr`

$$
\Biggl(\biggl(\Bigl(\bigl((a)\bigr)\Bigr)\biggr)\Biggr)
$$
## 边框

使用 `\boxed` 和 `\fbox` 可以为公式加边框，`\fbox` 用的大概是罗马体：$\boxed{y=x+1}\ \fbox{y=x+1}$
## 颜色

通过类似 `\color{颜色单词}{内容}` 设置颜色：$\color{red}{abc}\color{green}{def}\color{yellow}{ghi}$

也可以使用 `\color{#RGB}{内容}` 设置颜色：$\color{#ABC}{\verb+#ABC+}$，$\color{#DEF}{\verb+#DEF+}$，$\color{#FF00FF}{\verb+#FF00FF+}$
# 附录
## 希腊与希伯来字符

* 首字母大写对于希腊字母等，如果存在，可以是大写字母；对于一些特殊字符，可能为相似的比较大的字符，如 `\pi` 与 `\Pi`，`\uparrow` 与 `\Uparrow` 等
* 使用 `var` 前缀，如果存在，可以使用变量专用格式，如 `\pi` 与 `\varpi` 等
### 小写希腊字母

| 代码             | 字符         | 代码                | 字符            | 代码             | 字符         | 代码           | 字符       | 代码              | 字符          | 代码            | 字符        |
| -------------- | ---------- | ----------------- | ------------- | -------------- | ---------- | ------------ | -------- | --------------- | ----------- | ------------- | --------- |
|  `\alpha`    | $\alpha$   |  `\varepsilon`  | $\varepsilon$ |  `\iota`     | $\iota$    |  `\xi`     | $\xi$    |  `\varrho`    | $\varrho$   |  `\phi`     | $\phi$    |
|  `\beta`     | $\beta$    |  `\zeta`        | $\zeta$       |  `\kappa`    | $\kappa$   |  `o`       | $o$      |  `\sigma`     | $\sigma$    |  `\varphi`  | $\varphi$ |
|  `\gamma`    | $\gamma$   |  `\eta`         | $\eta$        |  `\lambda`   | $\lambda$  |  `\pi`     | $\pi$    |  `\varsigma`  | $\varsigma$ |  `\chi`     | $\chi$    |
|  `\delta`    | $\delta$   |  `\theta`       | $\theta$      |  `\mu`       | $\mu$      |  `\varpi`  | $\varpi$ |  `\tau`       | $\tau$      |  `\psi`     | $\psi$    |
|  `\epsilon`  | $\epsilon$ |  `\vartheta`    | $\vartheta$   |  `\nu`       | $\nu$      |  `\rho`    | $\rho$   |  `\upsilon`   | $\upsilon$  |  `\omega`   | $\omega$  |
|  `\digamma`  | $\digamma$ |  `\varkappa`    | $\varkappa$   |  `\partial`  | $\partial$ |              |          |                 |             |               |           |
### 大写希腊字母
| 代码         | 字符     | 代码          | 字符      | 代码           | 字符       | 代码         | 字符     |
| ---------- | ------ | ----------- | ------- | ------------ | -------- | ---------- | ------ |
| `\Gamma` | \Gamma | `\Lambda` | \Lambda | `\Sigma`   | \Sigma   | `\Psi`   | \Psi   |
| `\Delta` | \Delta | `\Xi`     | \Xi     | `\Upsilon` | \Upsilon | `\Omega` | \Omega |
| `\Theta` | \Theta | `\Pi`     | \Pi     | `\Phi`     | \Phi     |            |        ||
### 希伯来字母
| 代码         | 字符     | 代码        | 字符    | 代码          | 字符      |
| ---------- | ------ | --------- | ----- | ----------- | ------- |
| `\aleph` | \aleph | `\beth` | \beth | `\daleth` | \daleth ||
## 数学函数

![[Pasted image 20230116221020-20240402015249-knineff.png]]
## 运算符

![[Pasted image 20230116220525-20240402015311-xz5gxbd.png]]
## 箭头符号

![[Pasted image 20230116223240-20240402015322-vhgkbq2.png]]
## 其他符号

![[Pasted image 20230116223257-20240402015327-2y31c51.png]]
