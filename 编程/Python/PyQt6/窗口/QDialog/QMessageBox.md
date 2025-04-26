通过不同图标提供提示、警告、错误、询问、关于等对话框，通过不同的方法弹出

* `information`：信息，显示 `i` 图标
* `question`：问答，显示 `?` 图标
* `warning`：警告，显示 `!` 图标
* `ctitical`：严重错误，显示 `x` 图标
* `about`：关于

每个方法接收若干参数，也可以通过 `setText`、`setIcon`、`setTitle` 等自定义：

* `parent`：父对象
* `title`：对话框标题
* `text`：对话框文本
* `buttons`：标准按钮类型，`QMessageBox.StandardButton` 枚举，多个使用 `|` 组合，默认 `OK`
* `defaultButton`：默认选中的标准按钮，默认第一个按钮

> [!success]
> `QMessageBox` 有返回值，返回值类型为某个 `QMessageBox.StandardButton` 值

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/messageBoxTest.py"
LINES: "6-19"
```
tab: 截图
![[Pasted image 20240711164746.png]]
````

