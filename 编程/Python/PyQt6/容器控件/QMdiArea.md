多文档页面，一个 `QMdiArea` 由一个主容器和若干子窗口组成，子窗口类型为 `QMidSubWindow` 类实例。

控制子窗口的相关方法有：
* 管理窗口：`addSubWindow()`，`removeSubWindow()`，`subWindowList()`
* 激活窗口：`setActiveSubWindow()`，`closeActiveSubWindow()`
* 窗口排列：`cascadeSubWindows()` 级联显示，`tileSubWindows()` 平铺显示
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/mdi.py"
LINES: "13-36"
```
tab: 截图
![[Pasted image 20240712083834.png]]
![[Pasted image 20240712083842.png]]
![[Pasted image 20240712083846.png]]
````

