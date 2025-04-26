多行文本框，可显示和输入多行文本，支持 HTML 文档和垂直滚动条

````col
```col-md
flexGrow=1
===
| 方法或属性                 | 说明          |
| --------------------- | ----------- |
| `plainText`           | 多行文本内容（纯文本） |
| `html`                | HTML 网页内容   |
| `textColor`           | 文本颜色        |
| `textBackgroundColor` | 背景颜色        |
| `wordWrapMode`        | 是否自动换行      |
| `clear()`             | 清空          |
```
```col-md
flexGrow=1.3
===
| 信号                         | 说明        |
| -------------------------- | --------- |
| `textChanged`              | 文本该变时     |
| `selectionChanged`         | 选中文本该变时   |
| `cursorPositionChanged`    | 光标位置变化时   |
| `currentCharFormatChanged` | 当前字符格式变化时 |
| `copyAvailable`            | 复制可用时     |
| `redoAvailable`            | 恢复可用时     |
| `undoAvailable`            | 撤销可用时     |
```
````
# 实例

`````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/textEdit.py"
LINES: "13-36"
```
tab: 截图
````col
```col-md
![[Pasted image 20240711201623.png]]
```
```col-md
![[Pasted image 20240711201646.png]]
```
````
`````


