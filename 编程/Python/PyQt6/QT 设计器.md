QT Designer 需要安装 qt6-tools

```shell
pip install pyqt6-tools
```

QT Designer 位于 `.local/lib/<python>/site-packages/qt6_applications/Qt/bin/designer`

> [!note]
> 虚拟环境在 `venv/lib/<python>/site-packages/qt6_applications/Qt/bin/designer` 

通过 `.local/bin/qt6-tools designer` 打开

QT 设计器打开后默认先要选择窗体

窗体：Dialog、Main Window、Widget 统称窗体，其他称为窗口

- 窗体模板 templates/forms 包含一些预定义窗体，可以在右侧预览
	- 对话框 dialog 是顶层窗体，不能做为其他窗体组件
	- 主窗体 Main Window 包含中央部件区（Centeral Widget）、菜单栏、状态栏
- 窗口组件：除 Dialog、Main Window、Widget 外的显示页面
## 设计

通过拖拽将组件放置到窗口中，并在属性编辑器中调整属性

![[Pasted image 20240711074026.png]]
## 信号

切换到编辑信号/槽模式

![[Pasted image 20240711074047.png]]

在编辑信号/槽模式下，鼠标移动到控件上，控件显示红色标记。

![[Pasted image 20240711074058.png]]

选中拖拽可以设置控件的信号
- 拖拽到其他控件可以将一个信号转接到其他控件的信号
- 拖拽到窗口空白位置，显示一个类似接地的标记，可以自定义某个信号的槽

# 窗口代码

`.ui` 文件通过 `PyUic` 转换成 py 文件，位于 `<env>/bin/pyuic6`

```shell
pyuic6 -o <.py 文件> <.ui 文件>
```

生成的 `.py` 文件包含一个 `Ui_Dialog` 类，使用 `setupUi` 方法创建和初始化界面

- `retranslateUi` 用于设置对话框标题和控件文本
- `QtCore.QMetaObject.connectSlotsByName(Dialog)` 用于按控件名称绑定槽

创建一个新 `QDialog`  类，该类做以下两件事：

- 创建 `Ui_Dialog` 类并调用 `setupUi` 方法

```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/circleCal2.py"
LINES: "9,10-14"
```

- 创建并实现 QT 设计器中定义的槽

```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/circleCal2.py"
LINES: "9,16-20"
```
# 启动

```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/circleCal2.py"
LINES: "23-28"
```
# PyCharm 整合

| 外部工具        | 程序                    | 实参                                            | 工作目录               |
| ----------- | --------------------- | --------------------------------------------- | ------------------ |
| Qt Designer | `.venv/bin/qt6-tools` | `designer`                                    | `$ProjectFileDir$` |
| PyUIC       | `.venv/bin/pyuic6`    | `$FileName$ -o $FileNameWithoutExtension$.py` | `$ProjectFileDir$` |
