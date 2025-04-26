方法：
* 添加按钮、分隔符、其它控件：`addAction()`、`addSeparator()`、`addWidget()`
* 添加新工具栏：`addToolBar()`

> [!success]
> `QToolBar` 中的内容不止可以包含按钮 `QAction`，还可以加入任何 `QWidget`

属性：

| 属性                | 类型                   | 说明                                                                                                              |
| ----------------- | -------------------- | --------------------------------------------------------------------------------------------------------------- |
| `icon`            |                      | 图标                                                                                                              |
| `iconSize`        | `QtCore.QSize`       | 图标大小                                                                                                            |
| `movable`         |                      | 工具栏是否可移动                                                                                                        |
| `orientation`     | `Qt.Orientation`     | 工具栏方向                                                                                                           |
| `toolButtonStyle` | `Qt.ToolButtonStyle` | 工具栏内容显示方式<br />- `ToolButtonIconOnly`：仅图标<br />- `ToolButtonTextOnly`：仅文本<br />- `ToolButtonTextUnerIcon`：图标+文本 |
信号：单击工具栏按钮时触发 `actionTriggered` 信号
# 实例

`````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/toolBarTest.py"
LINES: "13-24"
```
tab: 截图
````col
```col-md
![[Pasted image 20240711171352.png]]
```
```col-md
![[Pasted image 20240711171356.png]]
```
```col-md
![[Pasted image 20240711171400.png]]
```
````
`````

