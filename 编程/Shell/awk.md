# 不可思议的编程语言 awk

`awk` 会逐行扫描输入内容，根据条件过滤并指定对应动作。相对于 `sed`，更倾向于数据扫描、过滤、统计汇总等操作。
# 基础语法

文档的每行数据称为记录，默认以 ` ` 或 `\t` 分隔成若干字段（列）。

`awk` 语法由一系列条件和动作组成。条件可以是正则或比较，动作通常是各种函数
- 不同条件之间可以无分隔，也可以以若干空格分隔
- `{}` 中多个动作之间通过 `; ` 分隔

```shell
awk <options> '条件{动作} 条件{动作} 条件{动作}...' 文件...
```

> [!note] 没有指定条件时默认匹配所有内容；没有指定动作时默认使用 `print` 输出
## 变量

通过 `awk -v` 自定义变量，未定义变量也可以直接使用但值为空或 0

```shell
awk -v x="Jacob" -v y=11 "{print x,y}" ~/test
```

声明变量时使用 `$变量名` 可以访问环境变量；输出时需要使用 `"'$变量名'"` 转换为字符串

```shell
x="hello"
y=11
awk -v i=$x '{print i, "'$y'"}' ~/test
```
## 内建变量

| 内建变量     | 描述                 |
| -------- | ------------------ |
| FILENAME | 当前输入文档的名称          |
| FNR      | 当前记录在文档的行号         |
| NR       | 当前记录在整个数据流的行号      |
| $0       | 当前行的全部数据内容         |
| $n       | 当前行的第 n 个字段的内容（n>=1) |
| NF       | 当前记录的字段（列）个数       |
| FS       | 输入字段分隔符，默认为空格或 Tab |
| RS       | 输入记录分隔符，默认为换行符\n   |
| OFS      | 输出字段分隔符，默认为空格      |
| ORS      | 输出记录分隔符，默认为换行符\n   |

```shell
free | awk '{print $2}'
```

通过变量可以通过 `FS` 变量设置字段分隔符，`RS` 设置记录分隔符，分隔符为一个字符或字符集合

> [!tip] `-` 集合中只能放在开头或结尾，否则表示连续的一段字符如 `a-z`

```shell
echo banana:lemon,pear--apple:grape | awk -v FS="," '{print NF,$1}'
echo banana:lemon,pear--apple:grape | awk -v FS="[:,-]" '{print NF,$1}'
```

`-F` 参数是修改分隔符的一个快捷方式

```shell
echo banana:lemon,pear--apple:grape | awk -F, '{print NF,$1}'
echo banana:lemon,pear--apple:grape | awk -F"[:,-]" '{print NF,$1}'
```
## print 指令

`print` 用于输出指定字符串，
- 不带参数表示输出整段数据
- `,` 可用于接收多个字符串，输出时使用 ` ` 分隔
- 直接连接多个字符串，中间没有分隔符，如 `"总计有"NR"行"`
## 条件匹配
### 正则

| 比较符号 | 描述           |
| ---- | ------------ |
| //   | 全行数据正则匹配     |
| !//  | 对全行数据正则匹配后取反 |
| //   | 对特定数据正则匹配    |
| !~// | 对特定数据正则匹配后取反 |

```shell
awk '/world/{print}' ~/test
```

`~` 可用于仅对某字段进行匹配

```shell
awk '$1~/world/{print}' ~/test
```
### 值比较

支持 `==` 和 `!=` 相等性比较，数字还支持 `>`，>=，`<`，`<=` 大小比较
### 其他

- `&&`：逻辑与
- `||`：逻辑或
- `BEGIN`：读取任何记录之前，常用于数据初始化
- `END`：读完所有记录之后，常用于数据汇总，此时 `NR` 等变量存储的是最后一行记录的信息

```shell
awk 'BEGIN{print "OK"} END{print "总计有"NR"行"}' ~/test
```

> [!example] 统计磁盘总大小
> ```shell
> df | tail -n +2 | awk '{sum+=$4} END{print sum}'
> ```

> [!example] 统计 `/etc` 下所有以 `.conf` 结尾的文件数量和总大小
> ```shell
> ls -l /etc/*.conf | awk '{sum+=$5} END{print NR, sum}'
> ```
# 条件判断

`awk` 指令序列中可以使用 `if` 做进一步条件判断

```shell
if(条件判断1) {
    动作指令1;
} else if(条件判断2) {
    动作指令2;
}
...
else {
    动作指令n;
}

```

> [!important] 动作指令必须在 `{}` 中，单行也不能省略 `{}`
# 数组

`awk` 支持普通一维和多维数组、关联数组等，通过索引访问

```shell
awk 'BEGIN{a[0]=11;a[1][0]=22;tom['age']=33;print a[0],a[1][0],tom['age']}'
```

数组数字下标也可以用字符串访问

```shell
awk 'BEGIN{a[0]=11} END{print a[0],a["0"]}'
```

使用 `if (值 in 数组) { 动作指令序列 }` 可以判断值是否为数组索引

```shell
awk 'BEGIN{\
a[88]=55;a["book"]="pen";\
if("88" in a){print "Yes"}else{print "No"}\
if("pen" in a){print "Yes"}else{print "No"}\
}'
```
# 循环
## for

使用 `for(变量 in 数组) { 动作指令序列 }` 可以遍历数组，变量将指代数组索引

```shell
awk 'BEGIN{\
a[10]=11;a[88]=22;a["book"]=33;a["work"]="home";\
for(i in a) { print i"="a[i]}\
}'
```

`awk` 还支持 C 的 `for` 循环和 `while` 循环

```shell
for(表达式1;表达式2;表达式3){动作指令序列;}
```

> [!example] 用户输入一个字符串，输出字符串在文件中哪一行哪一列
> ```shell
> read -p "查找字符：" key
> awk '{\
> for(i=1;i<=NF;i++) {if($i~/'$key'/) print "发现 '$key' 位于 "NR" 行，"i" 列"}\
> }' ~/test
> ```
## while

```shell
while(条件判断){动作指令序列;}
```

> [!example] 输出三角形
> ```shell
> awk '{i=1;\
> while(i<=NR){printf "%-2d",i;i++};print ""\
> }' ~/test
> ```
## 中断

`awk` 支持 `continue`，`break` 和 `exit` 控制循环
# 函数
## 内置 IO 函数

- `getline`：立即读下一记录并复制给 `$0`，更新 `NF`，`NR`，`FNR` 等内置变量
- `next`：停止处理当前记录，开始处理下一条记录
- `system(cmd)`：指定 `shell` 指令，`awk` 将新建一个

## 内置数值函数

- `cos`，`sin` 等三角函数
- `int` 取整
- `rand` 返回 `[0,1)` 之间的随机数
- `srand(n)` 设置随机数种子
## 内置字符串函数

- `length(s=$0)`：获取字符串长度
- `index(s, sub)`：查找子串位置
- `match(s, reg)`：查找正则匹配位置
- `toupper(s)`，`tolower(s)`：转换大小写
- `split(s, arr, ch=FS)`：切分字符串 `s` 到 `arr` 数组
- `sub(reg, s, t=$0)`：将字符串 `t` 中第一个能正则匹配 `reg` 的部分替换为 `s`
	- `gsub(reg, s, t=$0)`：替换全部字符串
- `substr(s, i, len=length(s)-i)`：从 `i` 截取长度为 `len` 的子串
## 内置时间函数

- `systime`：当前时间距离 `1970-01-01 00:00:00` 的秒数
## 自定义函数

自定义函数在所有条件块之外

```shell
awk 'function 函数名(参数列表){命令序列}'
```