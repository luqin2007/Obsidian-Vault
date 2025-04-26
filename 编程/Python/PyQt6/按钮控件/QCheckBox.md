复选框 `QCheckBox` 提供一组多选按钮

* 信号：`stateChanged` 或 `toggled` 在复选框状态改变时发出
* 属性：相比单选，多一个半选的状态
    * `triState`：复选框是否为一个三态复选框
    * `checkState`：复选框状态，值为 `Qt.CheckState` 枚举
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/checkButton.py"
LINES: "12-22,30-33"
```
tab: 截图
![[Pasted image 20240711211301.png]]
````

