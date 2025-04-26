旋钮，本质是一个类似滑块的控件，但样式是一个圆形旋钮

|属性方法|说明|
| --------------| --------------|
|`fixedSize`|旋钮大小|
|`minimum`，`maximum`，`setRange()`|取值范围|
|`notchesVisible`|是否显示刻度|

信号方面，该变旋钮会触发 `valueChanged` 信号
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/dial.py"
LINES: "15-23"
```
tab: 截图
![[Pasted image 20240711202816.png]]
````

