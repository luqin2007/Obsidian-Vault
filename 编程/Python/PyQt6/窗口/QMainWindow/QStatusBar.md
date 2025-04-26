在窗口底部显示的一个水平条，用于显示状态信息，位于 `QMainWindow` 的 `statusBar` 属性

状态栏对象
  * `addWidget`：添加一个控件
  * `addPermanentWidget`：添加永久控件
状态栏临时信息
  * `showMessage(str, time)` 显示一条临时信息，留存时间为 `<time>` 毫秒，`0` 为永久保留
  * `clearMessage()` 清除正在显示的临时信息
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/statusBarTest.py"
LINES: "12-17"
```
tab: 截图
![[Pasted image 20240711171453.png]]
````


