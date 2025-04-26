单行文本框，可以显示和输入单行文本

| 属性或方法             | 值或返回值<br />                                                                                                                                   | 说明           |
| ----------------- | --------------------------------------------------------------------------------------------------------------------------------------------- | ------------ |
| `placeholderText` | `String`                                                                                                                                      | 提示文本         |
| `alignment`       | `Qt.AlignmentFlag`                                                                                                                            | 对齐方式         |
| `echoMode`        | `QLineEdit.EchoMode`                                                                                                                          | 显示格式，如密码等    |
| `maxLength`       | 32767                                                                                                                                         | 文本框允许输入的最大长度 |
| `readOnly`        | `False`                                                                                                                                       | 是否只读         |
| `text`            | String                                                                                                                                        | 文本框内容        |
| `dragEnabled`     | `False`                                                                                                                                       | 文本框是否可被拖动    |
| `inputMask`       | String                                                                                                                                        | [[字符串掩码]]    |
| `validator`       | `QValidator`，Qt 提供了几种实现：<br />- `QIntValidator`：限制输入的为整数<br />- `QDoubleValidator`：限制输入的为浮点数<br />- `QRegularExpressionValidator`：按正则校验<br /> | 输入文本的验证器     |
| `clear()`         |                                                                                                                                               | 清除文本框内容      |
| `selectAll()`     |                                                                                                                                               | 全选           |
| `setFocuse()`     |                                                                                                                                               | 获取焦点         |

|信号|说明|
| ------| -----------------------------------------------|
|`selectionChanged`|选择的文本改变时|
|`textEdited`|文本被编辑时|
|`returnPressed`|编辑框内按下 `Enter` 时|
|`textChanged`|文本被修改时|
|`editingFinished`|编辑框内按下 `Enter`、`Esc` 或文本框失去焦点时|
|`cursorPositionChanged`|光标位置改变时|
|`inputRejected`|`setValidator` 等设置了验证器的情况下，输入不合法字符时|
# 实例

`````tabs
tab: 测试 1
````tabs
tab: Code
```embed-python
PATH: https://gitee.com/lq2007/py-qt6-demo/raw/master/lineText1.py
LINES: 13-52
```
tab: 截图
![[Pasted image 20240711201312.png]]
````
tab: 测试 2
````tabs
tab: Code
```embed-python
PATH: https://gitee.com/lq2007/py-qt6-demo/raw/master/lineText2.py
LINES: 13-38
```
tab: 截图
![[Pasted image 20240711201341.png]]
````
`````

