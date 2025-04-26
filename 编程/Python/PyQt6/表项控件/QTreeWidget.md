树，继承自 `QTreeView` ，数据值为 `QTreeWidgetItem`  类型
# QTreeWidget

> [!note]
> QTreeWidget 创建时没有顶层项，因此需要手动设置 `insertTopLevelItem`

| 方法与属性                                        | 说明         |
| -------------------------------------------- | ---------- |
| `invisibleRootItem`                          | 根节点（不可见？）  |
| `selectedItems`                              | 选中的项目      |
| `setColumnWidth(col, w)`                     | 设置列宽       |
| `insertTopLevelItem` , `insertTopLevelItems` | 插入顶层索引项目列表 |
| `addTopLevelItem` , `addTopLevelItems`       |            |
| `expandAll()`                                | 展开所有节点     |
# QTreeWidgetItem

| 方法与属性        | 说明                                 |
| ------------ | ---------------------------------- |
| `text`       | 节点文本                               |
| `checkState` | （添加复选框并）设置选中状态，类型为 `Qt.CheckState` |
| `icon`       | 显示图标                               |
| `addChild()` | 插入子项                               |
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/treeWidget.py"
LINES: "11-47"
```
tab: 截图
![[Pasted image 20240712092719.png]]
tab: 动态节点
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/treeWidget2.py"
LINES: "22-57"
```
tab: 截图
![[Pasted image 20240712094846.png]]
````

