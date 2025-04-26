翻译、压缩或删除字符，后接 1 或 2 个数据集，第二个数据集用于替换

| 参数  | 说明          |
| --- | ----------- |
| -s  | 删除连续的多个重复数据 |
| -d  | 删除包含特定集合的数据 |
| -c  | 使用数据集 1 的差集 |


> [!example] 压缩字符串，对于字符串中连续的多个 ` `，只保留一个 ` `
> ```shell
> echo "a   b       c      d      123      " | tr -s " "
> ```

> [!example] 将字符串中所有小写字母替换成大写字母
> ```shell
> echo "hello the world" | tr 'a-z' 'A-Z'
> ```

> [!example] 删除字符串中的 a-e 字母
> ```shell
> echo "hello the world" | tr -d 'a-e'
> ```
