文件选择对话框，可以直接调用方法打开对话框

| 方法                | 参数               | 说明                  |
| ----------------- | ---------------- | ------------------- |
| `getOpenFileName` |                  | 打开文件，返回选择文件的名称和输入文本 |
|                   | `parent`，`title` |                     |
|                   | `defaultdir`     | 默认目录，支持 `.` 和 `/`   |
|                   | `extfilter`      | 扩展名过滤描述             |
| `getSaveFileName` |                  | 保存文件                |

也可以创建一个 `QFileDialog` 对象自定义对话框

| 方法                | 参数              | 说明                                  |
| ----------------- | --------------- | ----------------------------------- |
| `setFileMode`     |                 | 显示描述文件，应为 `QFileDialog.FileMode` 枚举 |
|                   | `AnyFile`       | 任何文件                                |
|                   | `ExistingFile`  | 已存在的文件                              |
|                   | `ExistingFiles` | 已存在的多个文件                            |
|                   | `Directory`     | 目录                                  |
| `setFilter`       |                 | 显示过滤描述的文件和目录，应为 `QDir.Filter` 枚举    |
|                   | `Files`         | 多个文件                                |
|                   | `Dirs`          | 多个目录                                |
|                   | `NoFilter`      | 不过滤                                 |
|                   | `AllDirs`       | 所有目录                                |
| `exec()`          |                 | 执行选择文件                              |
| `selectedFiles()` |                 | 获取选择文件列表                            |
# 实例

````tabs
tab: Code
```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/fileDialogTest.py"
LINES: "8-44"
```
tab: 截图
![[Pasted image 20240711170645.png]]
````


