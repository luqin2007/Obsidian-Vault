```shell
pip install matplotlib
```

`matplotlib` 是 Python 使用最多的模块之一，与 QtCharts 不同点有：
- 使用 `Figure` 类表示一个图表，`FigureCanvas` 作为图表视图
- 数据不需要封装，直接绘制
- 坐标轴不是独立对象，而是使用 `add_axes()` 添加，使用 `plot` 方法绘制
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/matplotlib.py"
LINES: "11-12,14-17,19-34"
```
tab: 截图
![[Pasted image 20240713144105.png]]
````

