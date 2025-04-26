计数器控件 `QSpinBox` 和 `QDoubleSpinBox` 均派生自 `QAbstractSpinBox`，前者用于整数，后者用于浮点数。

计数器可以通过点击右侧上、下按钮或键盘上、下方向键调整内部数值，或手动输入。
# 方法或属性

|方法或属性|值|说明|
| ------------| -------------| ------------------------------|
|`minimum`|整型默认 0|计数器下界|
|`maximim`|整型默认 99|计数器上界|
|`value`||计数器当前值|
|`singleStep`|整型默认 1|计数器步长|
|`decimals`|浮点默认 2|浮点计数器精度|
|`setRange()`||设置计数器最大、最小值和步长|
# 信号

* `valueChanged`：每次点击向上、向下按钮时
* `textChanged`：计数器中的值发生变化
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/spinBox.py"
LINES: "13-25"
```
tab: 截图
![[Pasted image 20240711202017.png]]
````

