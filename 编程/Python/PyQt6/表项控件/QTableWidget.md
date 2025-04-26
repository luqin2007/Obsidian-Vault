`QTableWidget` 继承自 `QTableView`，使用标准数据模型，单元格数据为 `QTableWidgetItem`。

与 `QTableView` 相比，多了一些用于修改模型的方法

* `rowCount`，`columnCount`：行数及列数
* `horizontalHeaderLabels`，`verticalHeaderLabels`：表头
* `setItem(r, c, v)`，`setCellWidget`：数据或视图组件
* `findItems(str, QtCore.Qt.MatchExactly)`：查找匹配单元格
* `sortItems(col, QtCore.Qt.SortOrder)`：设置单元格排序
* `setSpan(r, c, rs, cs)`：合并单元格，从 `(r,c)` 开始合并 `rs` 行 `cs` 列
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/tableWidget.py"
LINES: "12-25"
```
tab: 截图
![[Pasted image 20240711223014.png]]
tab: findItems
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/tableWidget2.py"
LINES: "13-40"
```
tab: 截图
![[Pasted image 20240711223235.png]]
tab: 自定义 Widget
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/tableWidget3.py"
LINES: "14-28"
```
tab: 截图
![[Pasted image 20240712064900.png]]
tab: 右键菜单
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/tableWidget4.py"
LINES: "13,32-34,36-53"
```
tab: 截图
![[Pasted image 20240712065048.png]]
````



