提供月份视图，通过鼠标或键盘选择具体日期

| 属性或方法                       | 值              | 说明                |
| --------------------------- | -------------- | ----------------- |
| `selectedDate`              | `QDate`        | 当前选定的日期，默认为当前日期   |
| `minimumDate`，`maximumDate` | `QDate`        | 最大、最小日期           |
| `firstDayOfWeek`            | `Qt.DayOfWeek` | 默认 `Sunday`，每周第一天 |
| `gridVisible`               |                | 组件是否显示网格          |
| `setDateRange()`            |                | 设置可选日期范围          |

|信号|说明|
| ------| --------------------|
|`clicked`|点击日期发送信号|
|`selectionChanged`|日期改变时发送信号|
|`actived`|日历激活时发出信号|
|`currentPageChanged`|该变页时发出信号|
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/calendar.py"
LINES: "13-24"
```
tab: 截图
![[Pasted image 20240711201029.png]]
````

