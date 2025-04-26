将窗口切分成多个选项卡的容器控
* 选项卡管理方法，如 `addTab()`，`insertTab()`，`removeTab()`，`tabText`
* 当前选项卡属性，如 `currentWdiget`，`currentIndex`
* 选项卡属性

| 属性             | 类型                       | 说明                          |
| -------------- | ------------------------ | --------------------------- |
| `tabPosition`  | `QTabWidget.TabPosition` | 选项卡标题位置<br />上北下南左西右东<br /> |
| `tabsClosable` |                          | 是否可以独立关闭选项卡                 |

当选项卡切换时触发 `currentChanged` 信号
# 实例

````tabs
tab: 主程序
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/tabWidget.py"
LINES: "13-20"
```
tab: 页面 1
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/tabWidget.py"
LINES: "22-39,52-66"
```
tab: 页面 2
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/tabWidget.py"
LINES: "41-50"
```
tab: 截图
![[Pasted image 20240712070537.png]]

![[Pasted image 20240712070541.png]]
````


