进度条控件，显示一个进度条表示当前进度

| 属性方法                             | 值                                         | 说明         |
| -------------------------------- | ----------------------------------------- | ---------- |
| `minimum`，`maximum`，`setRange()` | 默认为 0 和 99                                | 进度条最大最小值   |
| `value`                          |                                           | 进度条当前值     |
| `alignment`                      | `Qt.AlignmentFlag`                        | 对齐方式       |
| `layoutDirection`                | `Qt.LayoutDirection`                      | 进度条布局方向    |
| `orientation`                    | `Qt.Orientation`                          | 进度方向       |
| `invertedAppearance`             |                                           | 是否以反方向显示进度 |
| `format`                         | `%p`：当前百分比<br />`%v`：当前进度值<br />`%m`：总步长值 | 进度条文字内容    |
| `textDirection`                  | `QProgressBar.Direction`                  | 进度条文字显示方向  |
| `setProperty()`                  |                                           | 设置任意进度条属性  |
信号主要是 `valueChanged`
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/progressBar.py"
LINES: "14-25,27-41"
```
tab: 截图
![[Pasted image 20240711201137.png]]
````

