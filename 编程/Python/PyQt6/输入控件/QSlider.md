提供一个有界的水平或垂直滚动条，根据滑块位置转换成一个整数值

|属性方法|值|说明|
| ----------| ------| ----------------------------|
|`minimum`，`maximum`||滑动条最小、最大值|
|`singleStep`||步长|
|`value`||当前值|
|`tickInterval`||刻度间隔|
|`tickPosition`|`QSlider.TickPosition`|刻度线位置（相对滑块位置）|
|`orientation`|`Qt.Orientation`|方向|

|信号|说明|
| ------| ------------------|
|`valueChanged`|滑块值变化时发送|
|`sliderPressed`|按下滑块时发送|
|`sliderMoved`|拖动滑块时发送|
|`sliderReleased`|释放滑块时发送|
# 例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/slider.py"
LINES: "18-25,32-35"
```
tab: 截图
![[Pasted image 20240711202618.png]]
````

