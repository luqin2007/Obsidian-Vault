提供一个可供输入的文本框和按钮组成的输入框，也是通过各种方法弹出
* `getInt`：输入整数
* `getDouble`：输入浮点
* `getText`：输入任意文本
* `getItem`：下拉选项菜单

> [!success]
> 方法返回两个结果：`(value, ok)`，`ok=False` 对应取消的情况

方法参数包括：
* `parent`、`title`、`text`
* `defaultValue`、`minValue`、`maxValue`
* `stepValue`：步长（仅 `getInt`）
* `decimals`：小数点位数（仅 `getDouble`）
* `items`、`defaultitem`：选项及默认选项（仅 `getItem`）

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/inputDialogTest.py"
LINES: "5-36"
```
tab: 截图
![[Pasted image 20240711170114.png]]
![[Pasted image 20240711170119.png]]
![[Pasted image 20240711170122.png]]
````

