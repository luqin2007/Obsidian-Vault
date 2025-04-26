QtCharts 由 Riverbank 出品，可绘制各种函数曲线、柱状图、折线图、饼状图等

```shell
pip install PyQt6-Charts PyQt6
```
# 图表

图表类 `QChartView` 派生自 Qt 的 `QGraphicsView` 类，图表图元 `QChart` 派生自 `QGraphicsItem`
- `legent()`：图例
	- `setAlignment`：设置图例位置

```python
chartView = QChartView(self)
chart = QChart()
chartView.setChart(chart)
```
# 序列

序列 `Series` 是 `QtCharts` 图表数据的封装，用于管理数据，以 `QXxxSeries` 命名

| 序列类型                          | 所属图表          |
| ----------------------------- | ------------- |
| `QLineSeries`                 | 所有函数曲线、折线的线型图 |
| `QBarSeries`                  | 柱状图           |
| `QHorizontalStackedBarSeries` | 堆叠柱状图         |
| `QPercentBarSeries`           | 百分比柱状图        |
| `QPieSeries`                  | 饼图            |
| `QScatterSeries`              | 散点图           |
| `QCandlestickSeries`          | 蜡烛图           |
| `QAreaSeries`                 | 区域填充图         |
```python
series = <QXxxSeries>()
series.append(<data>)
chart.addSeries(series)
```
# 坐标

`QCharts` 坐标轴有两种：文字坐标和数值坐标。

> [!note]
> QCharts 可以使用 `chart.createDefaultAxes()` 创建默认坐标轴

文字坐标常用于文字表示的年月、统计对象类型等，使用 `QBarCategoryAxis` 建立

```python
# 坐标列表
title = [ '...', '...', '...' ]
axisX = QBarCategoryAxis()
axisX.append(title)
chart.addAxis(axisX, Qt.AlignmentFlag.AlignButton)
series.attachAxis(axisX)
```

数值坐标可以根据关联数据自适应的调整显示刻度和范围，使用 `QValueAxis` 建立

```python
axisY = QValueAxis()

# 设置属性...
axisY.setXxx(...)

chart.addAxis(axisY, Qt.AlignmentFlag.AlignLeft)
series.attachAxis(axisY)
```
# 实例

````tabs
tab: 函数曲线
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/chart.py"
LINES: "8-9,13-36"
```
<br>

![[Pasted image 20240713124418.png]]
tab: 柱状图/折线图
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/chart2.py"
LINES: "10-11,15-68"
```
<br>

![[Pasted image 20240713135042.png]]
tab: 饼图
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/chart3.py"
LINES: "9-10,14-42"
```
<br>

![[Pasted image 20240713133524.png]]
````

