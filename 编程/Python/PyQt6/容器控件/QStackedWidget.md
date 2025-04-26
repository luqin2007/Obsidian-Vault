堆栈窗口，容纳一组多个 `Widget`，但同时只能显示一个 `Widget`，可以通过特殊方法切换。

|属性与方法|类型|
| ------------| -----------|
| `addWidget(w)`<br />`insertWidget(idx, w)`<br />|插入 `Widget` |
| `removeWidget`<br />|移除 `Widget` |
| `widget(idx)`，`indexOf(w)` |索引与 `Widget` |
| `currentWidget` |当前 `Widget` |
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/stackedWidget.py"
LINES: "13-35"
```
tab: Page1
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/stackedWidget.py"
LINES: "37-52"
```
tab: Page2
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/stackedWidget.py"
LINES: "54-62"
```
tab: 截图
![[Pasted image 20240712080141.png]]
![[Pasted image 20240712080144.png]]
````


