一种列状层叠选项卡，内部元素为 `QGroupBox`
* 方法
    * `addItem`，`removeItem()`
* 属性
    * `itemText`，`itemIcon`：某选项卡文本、图标
    * `itemEnabled`：选项卡是否可用
    * `currentIndex`：当前选项卡索引
* 信号：`currentChanged` 当切换选项卡时触发
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/toolbox.py"
LINES: "16-17,42,43-63"
```
tab: Data
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/toolbox.py"
LINES: "16-42"
```
tab: 截图
> [!col]
>> [!col-md]
>> ![[Pasted image 20240712084029.png]]
>
>> [!col-md]
>> ![[Pasted image 20240712084033.png]]
````



