将表达式通过管道输入 `bc` 即可，也可以通过 `bc` 命令进入交互环境，使用 `quit` 退出

![[../../../../_resources/images/Pasted image 20241120192026.png]]
# 浮点计算

> [!note] 默认除法保留到整数，使用 `scale=n` 设置保留 n 位小数

```reference
file: "@/_resources/codes/linuxshell/calc.sh"
lang: "shell"
```
# 进制转换

`ibase=n` 设置输入数字为 n 进制，`obase=n` 设置输出数字为 n 进制

```shell
echo "obase=2;10" | bc
echo "ibase=16;obase=2;FF" | bc
```
# 其他

`length(n)` 可以计算计算结果的长度，对于浮点不包含小数点和前导 0

```shell
echo "length(123456)" | bc
echo "obase=2;length(123456)" | bc
echo "length(123.456)" | bc
echo "length(0.456)" | bc
echo "length(0.4560)" | bc
```
