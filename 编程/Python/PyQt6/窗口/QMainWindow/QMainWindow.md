主窗口，`QWidget` 的一个派生类，包括菜单栏、工具栏、状态栏、标题栏等
* 有一个中心窗口
* 不能 `setLayout`
* 可以停靠 QDockWidget，相关属性和方法见 `QDockWidget` 章节
* 默认包含菜单栏（[[QMenuBar]]）、工具栏（[[QToolBar]]）和状态栏（[[QStatusBar]]），相关属性见相关章节

| 属性                      | 默认值                        | **说明**                         |
| ----------------------- | -------------------------- | ------------------------------ |
| `toolButtonStyle`<br /> | `ToolButtonIconOnly`       | 工具栏样式，仅图标                      |
|                         | `ToolButtonTextOnly`       | 仅文本                            |
|                         | `ToolButtonTextBesideIcon` | 文本在图标旁边                        |
|                         | `ToolButtonTextUnderIcon`  | 文本在图标下边                        |
|                         | `ToolButtonFollowStyle`    |                                |
| `animated`              | `True`<br />               | 是否展示动画                         |
| `documentMode`          | `False`<br />              | 文档模式，不会呈现选项卡框架                 |
| `tabShape`<br />        | `Rounded`                  | 主窗口标签 `TabWidget` 样式，圆形（小圆角矩形） |
|                         | `Triangular`               | 三角形（梯形）                        |
| `addToolBar`            |                            | 添加工具栏                          |
| `centralWidget`         | `NULL`                     | 窗口中心控件                         |
# 实例

~~~tabs
tab: MainWindow
````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/mainWin.py"
LINES: 5-26
```
tab: 截图
![[Pasted image 20240711170838.png]]
````
tab: 菜单栏、工具栏、状态栏
````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/mainWin2.py"
LINES: 8-50
```
tab: 截图
![[Pasted image 20240711170938.png]]
![[Pasted image 20240711170942.png]]
![[Pasted image 20240711170945.png]]
````
~~~


