滚动条，包含水平滚动条和垂直滚动条，用于扩展窗口有效面积

|属性方法|值|说明|
| ----------| ----| --------------------|
|`minimum`，`maximum`||滚动条最小、最大值|
|`orientation`||滚动条方向|
|`value`||当前滚动条位置|

|信号|说明|
| ------| --------------------|
|`valueChanged`|当前位置变化时|
|`sliderMoved`|当用户拖动滚动条时|
# 例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/scrollBar.py"
LINES: "12-23"
```
tab: 截图
![[Pasted image 20240711203233.png]]
````

