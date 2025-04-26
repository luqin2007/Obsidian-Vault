一个可以停靠在 [[../窗口/QMainWindow/QMainWindow]] 的控件，既可以停靠在窗口上下左右四边，又可以浮动存在。

* `QMainWidget` 操纵 `QDockWidget` 的属性和方法

| 属性和方法                  | 值                                                                                                                                                                                                                                                                              | 说明                                                               |
| ---------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | ---------------------------------------------------------------- |
| `addDockWidget()`      |                                                                                                                                                                                                                                                                                | 停靠一个窗口                                                           |
| `splitDockWidget()`    |                                                                                                                                                                                                                                                                                | 分隔两个停靠在同侧的窗口，形成类似 `QSplitter` 的效果                                |
| `tabifiedDockWidget()` |                                                                                                                                                                                                                                                                                | 添加停靠一个窗口，形成多标签                                                   |
| `removeDockWidget()`   |                                                                                                                                                                                                                                                                                | 移除并隐藏停靠窗口，停靠窗口不会被删除                                              |
| `restoreDockWidget()`  |                                                                                                                                                                                                                                                                                | 恢复停靠窗口状态                                                         |
| `dockWidgetArea()`     | `Qt.DockWidgetArea`                                                                                                                                                                                                                                                            | 指定窗口可停靠区域，`NoDockWidgetArea` 表示只能停靠在插入处                          |
| `resizeDocks()`        |                                                                                                                                                                                                                                                                                | 改变停靠窗口列表尺寸                                                       |
| `takeCentralWidget()`  |                                                                                                                                                                                                                                                                                | 移除中心窗口                                                           |
| `dockNestingEnabled`   |                                                                                                                                                                                                                                                                                | 属性：停靠窗口是否可以嵌套                                                    |
| `dockOptions`          | `QMainWindow.DockOption`<br />- `AnimatedDocks`：动画方式停靠，等效 `animate`<br />- `AllowNestedDocks`：允许嵌套，等效 `dockNestingEnabled`<br />- `AllowTabbedDocks`：允许标签页形式上下堆叠停靠<br />- `ForceTabbedDocks`：强制标签页形式停靠，不能相邻存放<br />- `VerticalTabs`：垂直标签页方式<br />- `GroupedDragging`：允许成组拖动标签页 | 属性：窗口停靠属性，可用 `\|` 组合<br />默认：`AnimatedDocks \| AllowTabbedDocks` |
* `QDockWidget` 自身的属性和方法

| 属性和方法            | 值                   | 说明        |
| ---------------- | ------------------- | --------- |
| `widget`         |                     | 窗口内容      |
| `floating`       |                     | 是否可浮动     |
| `allowedAreas`   | `Qt.DockWidgetArea` | 可停靠区域     |
| `features`       |                     | 停靠窗口的功能属性 |
| `titleBarWidget` |                     | 停靠窗口标题栏组件 |
* `QDockWidget` 相关信号

| 信号                    | 说明                            |
| --------------------- | ----------------------------- |
| `featuresChanged`     | 停靠窗口特性 `features` 发生改变时触发     |
| `topLevelChanged`     | 浮动属性 `floating` 发生改变时触发       |
| `allowedAreasChanged` | 允许停靠区域 `allowedAreas` 发生改变时触发 |
| `visibilityChanged`   | 窗口可见性（显示/隐藏）该变时触发             |
| `dockLocationChanged` | 停靠窗口位置变化时触发                   |
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/dockWidget.py"
LINES: "14-49"
```
tab: 截图
![[Pasted image 20240712082827.png]]
![[Pasted image 20240712082831.png]]
````


