`QTableView` 中使用 `QStandItemMode` 作为默认模型，通过函数 `setItem(row, col, item)` 存储 `QStandItem` 类型对象
# 属性 

- `rowHeight`，`columnWidth` 表示行高和列宽
* `verticalHeader`、`horizontalHeader`：获取水平、垂直表头，类型为 `QHeaderView`
* `verticalScrollBar`、`horizontalScrollBar`：水平、垂直滚动条，类型为  `QAbstractScrollArea`
    * `QAbstractScrollArea#setSliderPosition(n)`：滚动到某个位置
* `editTriggers`：`QAbstractItemView.EditTrigger` 类型，是否可编辑
    * `NoTriggers`：不能编辑
    * `CurrentChanged`：随时都能编辑
    * `DoubleClicked`：双击选中内容编辑
    * `SelectedClicked`：单击选中内容编辑
    * `EditKeyPressed`：按下修改键编辑
    * `AnyKeyPressed`：按下任意键编辑
    * `AllEditTriggers`：包含上面所有条件
* `selectionBehavior`：`QAbstractItemView.SelectionBehavior` 类型，表格选择行为

    * `SelectItems`：选中单元格
    * `SelectRows`：选中一行
    * `SelectColumn`：选中一列
* `textAlignment`：单元格文字对齐方式
* `showGrid`：是否显示网格线
# 方法

| 方法                                                         | 说明                 |
| ---------------------------------------------------------- | ------------------ |
| `showGrid()`                                               | 显示一个网格             |
| `hideRow()`，`hideColumn()`<br />                           | 显示、隐藏行、列<br />     |
| `showRow()`，`showColumn()`                                 |                    |
| `selectRow()`，`selectColumn()`                             | 选择行、列              |
| `resizeColumnsToContents()`，`resizeRowsToContents()`<br /> | 根据每个行或列的内容自动分配空间大小 |
| `setEditTriggers()`                                        | 隐藏表头               |
# 信号

点击时触发 `itemClicked(QTableWidgetItem*)` 信号
# 模式

`QTableView` 还支持其他类型模式

| 模式                         | 描述                    |
| -------------------------- | --------------------- |
| `QStringListModel`         | 仅存储一组字符串              |
| `QStandardItemModel`       | 存储任意层结构的数据            |
| `QDirModel`                | 对文件系统封装               |
| `QSqlQueryModel`           | 对 SQL 查询结果进行封装        |
| `QSqlTableModel`           | 对 SQL 表进行封装           |
| `QSqlRelationalTableModel` | 对带有外键的 SQL 表进行封装      |
| `QSortFilterProxyModel`    | 一个代理模型，可以对其他模型进行排序和过滤 |
|                            |                       |
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/tableView.py"
LINES: "13-26"
```
tab: 截图
![[Pasted image 20240711222446.png]]
````

