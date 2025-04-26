简单易用的 Python 控制台进度条库

> [!error] 已经有一年没更新了

![[../../../_resources/images/demo.gif]]
# Bar

进度条，基础类为 `Bar`，其他几种类都是 `Bar` 参数不同效果不同
`````col
````col-md
flexGrow=1
===
```python
from progress.bar import Bar

# Processing |################################| 10/10
bar = Bar('Processing', max=10)
for i in range(10):
    # do something
    sleep(0.5)
    bar.next()
```
````
````col-md
flexGrow=1
===
```python
from progress.bar import Bar

# Processing |################################| 10/10
bar = Bar('Processing', max=10)
# 简化遍历
for i in bar.iter(range(10)):
    # do something  
    sleep(0.5)
```
````
`````
- `Processing`：进度条标题，即进度条前显示的文本
- 具名参数
	- `max`：进度条最大值
	- `bar_prefix`，`bar_suffix`：进度条前后边界字符
	- `empty_fill`，`fill`：进度空或满时的填充字符
	- `suffix`：后缀，默认 `%(index)d/%(max)d`，可用参数如下：

| Name         | Value                             |
| ------------ | --------------------------------- |
| `index`      | 当前进度值                             |
| `max`        | 最大进度值                             |
| `remaining`  | 剩余进度                              |
| `progress`   | 进度（0-1）= `index / max`            |
| `percent`    | 进度（百分比，不包含 `%`）= `progress * 100` |
| `avg`        | 平均速度= `index / elapsed`           |
| `elapsed`    | 已经执行了多少秒                          |
| `elapsed_td` | `elapsed` 的可打印字符                  |
| `eta`        | 估算剩余时间= `remaining * avg`         |
| `eta_td`     | `eta` 的可打印字符                      |
# Spinner

进度未知时使用

```python
from progress.spinner import Spinner

spinner = Spinner('Loading ')
for i in range(random.randint(100, 300)):
    # do something
    sleep(0.3)
    spinner.next()
```

- `phases`：数组，所有状态字符
# 参考

```cardlink
url: https://github.com/verigak/progress/
title: "GitHub - verigak/progress: Easy to use progress bars for Python"
description: "Easy to use progress bars for Python. Contribute to verigak/progress development by creating an account on GitHub."
host: github.com
favicon: https://github.githubassets.com/favicons/favicon.svg
image: https://opengraph.githubassets.com/4c6d32f3ddbdd952ed54ecbdc3b906192fd04391e1d894352c3dd14a6badb073/verigak/progress
```
