标签，可显示文本、链接、图片等，支持富文本和 GIF 图片

| 属性或方法                  | 值或返回值                    | 说明                           |
| ---------------------- | ------------------------ | ---------------------------- |
| `alignment`            | `Qt.AlignmentFlag`       | 文本对齐方式                       |
| `textInteractionFlags` | `Qt.TextInteractionFlag` | 文本交互行为，如是否能选中等               |
| `indent`               | 默认 -1                    | 文本缩进值                        |
| `pixmap`               | `QPixmal`                | 显示一个静态图片                     |
| `setMovie()`           | `QMovie`                 | 显示一个 GIF 图片                  |
| `text`                 |                          | 显示文本                         |
| `buddy`                | `QWidget`                | 设置一个伙伴助记符，标签获取焦点后会自动切换到伙伴控件上 |
| `wordWrap`             | `False`                  | 是否延单词边界换行                    |
| `openExternalLinks`    | `False`                  | 是否允许使用超链接（`<a>` 标签）          |
| `selectedText()`       |                          | 获取选中的文本                      |
| `setScaledContents`    |                          | 图片适应控件大小，需要控件固定大小            |
| `setFixedSize(w, h)`   |                          | 固定控件大小                       |

| 信号              | 说明                                      |
| --------------- | --------------------------------------- |
| `linkActivated` | 点击标签中嵌入的超链接，需要 `openExternalLinks=True` |
| `linkHovered`   | 鼠标滑过超链接                                 |

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/labelTest.py"
LINES: "13-39"
```
tab: 截图
![[Pasted image 20240711172045.png]]
````

