直接编写 Qt 程序

`````col
````col-md
flexGrow=1
===
```python
from PyQt6.QtWidgets\
 import QApplication, QWidget, QPushButton
import sys


def btnFunc():
    print('Button!')


app = QApplication(sys.argv)

w = QWidget()
w.resize(300, 200)
w.move(260, 240)
w.setWindowTitle('PyQt Window')

btn = QPushButton(w)
btn.setText('Click Me')
btn.move(120, 150)
btn.clicked.connect(btnFunc)

w.show()
sys.exit(app.exec())
```
````
````col-md
flexGrow=1
===
引入包，所有 Qt 控件来自 `QtWidgets`

- `QApplication`：创建应用程序
- `QPushButton`：按钮组件

<br>

`btnFunc` 函数用于按钮点击事件

<br>

使用 `QApplication(sys.argv)` 创建应用程序

<br>
<br>

使用 `connect` 设置 `click` 信号的方法（槽）

- `clicked`：点击
- `returnPressed`：回车

`sys.exit(app.exec())` 用于接收输入事件，进入程序主循环
````
`````
# QApplication

`QApplication` 是 GUI 应用类，负责主事件循环、初始化与收尾工作、对话管理等。

> [!info]
> 任何 PyQt GUI 应用有且只有一个 `QApplication`  对象

通过 `QApplication.instance` 可以获取 `QCoreApplication` 对象引用，是 `QApplication` 的指针

任何应用有且只有一个 `QCoreApplication` 对象，该对象不依赖于 QtGui