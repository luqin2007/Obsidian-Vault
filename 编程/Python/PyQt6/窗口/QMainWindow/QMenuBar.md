菜单栏 `QMenuBar` 可以通过构造函数创建，也可以通过 `QMainWindow#menuBar()` 获取

> [!note]
> 在 Qt Designer 中，菜单的动作位于动作编辑器中

相关属性
  * `defaultUp`：弹出方向，默认向下，若不适合则自动调整其他方向
  * `nativeMenuBar`：使用本机菜单栏（？），依赖于平台支持
    * `true` 表示在本菜单栏中使用，不在其父级窗口中
    * `false` 表示菜单栏保留在窗口中
相关方法
  * `text`，`setText`：菜单项文本
  * `title`，`setTitle`：QMenu 小控件标题
  * `setEnabled()`：设置按钮是否为启用状态
  * `setShortcut()`：关联快捷键到操作按钮
  * `addMenu()`，`addAction()`，`addActions()`，`addSeparator()`：添加菜单、菜单项、分隔符
  * `clear()`：清除所有菜单项内容

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/menuTest.py"
LINES: "12-32"
```
tab: 截图
![[Pasted image 20240711171139.png]]
![[Pasted image 20240711171142.png]]
````

