`seq` 可用于构造数列：`seq <start> <step> <end>`
- 只有两个数字参数时表示省略 `<step>=1`
- 只有一个参数时表示省略 `<start>=1`，`<step>=1`

```shell
seq 1 2 10
```

使用 `-s` 可以指定生成序列的分隔符，默认 `\n`

```shell
seq -s"|" 1 2 10
```

