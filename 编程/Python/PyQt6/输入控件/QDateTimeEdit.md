允许通过鼠标和键盘编辑日期和时间的控件

| 属性方法                        | 值                                                                                               | 描述         |
| --------------------------- | ----------------------------------------------------------------------------------------------- | ---------- |
| `displayFormat`             | `yyyy`：年，4位<br />`MM`：月份，2位<br />`dd`：日，2位<br />`HH`：小时，2位<br />`mm`：分钟，2位<br />`ss`：秒，2位<br /> | 时间与日期格式    |
| `minimumDate`，`maximumDate` |                                                                                                 | 控件的最大、最小日期 |
| `time`，`date`，`dateTime`    |                                                                                                 | 编辑的时间和日期   |
| `calendarPopup`             | False                                                                                           | 弹出日历       |

|信号|说明|
| ------| ----------------------|
|`dateChanged`|日期发生改变时|
|`timeChanged`|时间发生改变时|
|`dateTimeChanged`|时间或日期发生改变时|
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/dateTime.py"
LINES: "14-24,31-35"
```
tab: 截图
![[Pasted image 20240711202411.png]]
````



