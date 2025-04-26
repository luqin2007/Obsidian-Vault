# 方法

* `addItem()`：添加字符串或 `QListWidgetItem`
* `addItems()`：添加一组数据
* `insertItem()`：在特定位置插入数据
* `setCurrentItem()`：设置当前选中的数据
* `sortItems()`：排序
* `clear()`：清空数据
# 信号

|信号|参数|触发|
| ------| ------------| -------------------------------------------------|
| `currentItemChanged` | `currentItem, previousItem` |列表框中条目发生变化时|
| `currentRow` | `row`：行号|列表框中当前项发生变化时|
| `currentTextChanged` | `text` |列表当前项文本发生变化时|
| `itemChanged` | `item` |当前项文本发生变化时|
| `itemClicked` | `item` |点击时|
| `itemDoubleClicked` | `item` |双击时|
| `itemEntered` | `item` | `mouseTracking=True` 时接收到鼠标指针时；否则，鼠标移入后按下时|
| `itemPressed` | `item` |某项上按下鼠标时|
| `itemSelectionChanged` ||列表框选中的条目发生变化|
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/listWidget.py"
LINES: "13-21,23-27"
```
tab: 截图
![[Pasted image 20240711212146.png]]
````

