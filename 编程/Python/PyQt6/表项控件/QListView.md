`QListWidget` 是 `QListView` 的子类，其中 `QListView` 是基于模型的，Qt 提供了一个数据存储模型 `QListWidgetItem`

|方法|说明|
| ------| ----------------------------|
|`setModel`|设置模型作为 List 的数据源|
|`selectedItem(n)`|选中条目 `n`|
|`isSelected(n)`|条目 `n` 是否被选中|

同时也提供信号：
* `clicked`：点击某项时触发
* `doubleClicked`：双击某项时触发
# 实例

````tabs
tab: Code1
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/listView1.py"
LINES: "13-19"
```
tab: 截图 1
![[Pasted image 20240711211749.png]]
tab: Code2
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/listView2.py"
LINES: "13-27"
```
tab: 截图 2
![[Pasted image 20240711211910.png]]
````

