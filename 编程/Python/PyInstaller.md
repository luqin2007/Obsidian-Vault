PyInstaller 是一个将 Python 脚本打包成应用程序的工具，会在项目目录下会产生 `dist`  目录，包含可执行程序

```shell
pip install Pyinstaller
```

若 Python 脚本只有一个文件，可以直接创建

```shell
pyinstaller -F <python 脚本路径>
```

> [!note]
> `-F`：生成独立可执行文件
> `-D`：打包成一个带各种运行所需文件的文件夹，其中包括可执行文件
> `-w`：打包完后运行可执行文件不会弹出命令行窗口

若项目比较大，或包含其他依赖库，需要使用 `spec` 配置文件辅助

```shell
pyi-makespec <脚本入口 py 文件>
```

该命令将产生与文件同名的 `.spec` 配置文件，需要修改 `a = Analysis` 行：

> [!attention]
> 配置文件中路径或文件名必须使用**全路径**，注意  `\` 转义或使用元字符串

- 第一个数组包含打包项目中所有 `.py` 文件，与入口文件同目录的情况下可以只写文件名
- `pathex` 为项目根目录
- `datas` 为一个元组列表，表示项目其他资源。每个元组第一个值为资源目录，第二个值为相对路径

```embed-python
PATH: "https://gitee.com/lq2007/py-qt6-demo/raw/master/示例项目/NetShop/Main.spec"
LINES: "4-16"
```

修改后，即可开始打包

```shell
pyinstaller <spec 文件>
```

出现 `Building COLLECT ... completed successfully.` 即可，在 `dist/<入口文件名>` 目录下生成对应 exe 文件和依赖目录

![[Pasted image 20240717082718.png]]

但还有一点问题。若程序中有依赖的外部资源（即 `datas` 中的配置），需要将 `_internal` 中的对应目录移动出来到 `exe` 同目录下，最后效果如下：

![[Pasted image 20240717082856.png]]
