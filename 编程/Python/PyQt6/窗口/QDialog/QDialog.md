`QDialog` 是对话框基类，一般用来执行短期任务，或与用户进行互动。
* 应用程序模态对话框：该对话框为程序唯一能与用户交互的部件
* 窗口模态对话框：阻止该对话框的父窗口、父窗口的父窗口直到顶层窗口的交互
* 非模态对话框：对话框不影响应用程序交互，即使应用程序退出对话框仍存在

> [!warning]
> 模态对话框会阻塞调用线程

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/dialogTest.py"
LINES: "5-18,21-26"
```
tab: 截图
![[Pasted image 20240711170356.png]]
````
# 属性

* `sizeGripEnabled`：是否显示右下角抓痕，该抓痕用于调整对话框大小

> [!success]
> 不影响通过拖拽右下角端点调整对话框大小

* `modal` 与 `windowModality`：设置应用程序模态框与窗口模态框

# 子类

`QDialog` 有几个子类，为 Qt 预设的几种对话框

* [[QMessageBox]]：弹出信息框
* [[QInputDialog]]：输入对话框
* `QFontDialog`、`QColorDialog`：选择字体和颜色

  * 直接使用 `getFont()` 和 `getColor()` 即可
  * 返回 `(value, ok)`，`ok=False` 表示取消的情况
* [[QFileDialog]]：文件选择
