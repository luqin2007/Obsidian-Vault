一个集按钮和下拉选项于一体的控件
# 方法

* `addItem`，`addItems`：添加一个（或多个）下拉项
* `clear()`：清空下拉项
* `count()`：下拉项数量
* `itemText(i)`，`setItemText(i, text)`：第 `i` 项文本
* `currentIndex()`：当前选中项
* `curTentText()`：当前选中项文本，相当于 `itemText(currentIndex())`
# 信号

* `actived`：选中一个下拉选项时
* `currentIndexChanged`：下拉选项索引变化时
* `highlighted`：选中一个已被选中的下拉选项时
# 实例

````tabs
tab: Code
```embed-python
PATH: https://gitee.com/lq2007/py-qt6-demo/raw/master/comboBox.py
LINES: 13-24,26-30
```
tab: 截图
![[Pasted image 20240711201744.png]]
````

