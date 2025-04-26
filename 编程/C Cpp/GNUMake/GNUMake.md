`make` 是命令行工具，解释并运行 `makefile` 指令。

| 阶段  | 任务        | 产物                         | 说明                           |
| --- | --------- | -------------------------- | ---------------------------- |
| 编译  | 生成中间代码    | Object File（`.obj` / `.o`） | 检验语法正确性<br>检验函数与变量声明正确性（头文件） |
| 链接  | 链接函数和全局变量 |                            | 找不到实现则产生 Linker Error        |

> [!note] 库文件：多个中间文件打包，`.lib`（Library File）或 `.a`（Archive File）

---

- [[makefile]]
- [[make]]
- [[规则]]
- [[命令]]
- [[变量]]
- [[条件判断]]
- [[函数]]
- [[隐含规则]]
- [[库文件]]
# 异常

> [!error] Makefile:2: *** missing separator.  Stop.

缩进使用一个 tab，空格无效

> [!hint] VSCode 等 IDE 可能会把 Tab 替换成空格
# 参考

```cardlink
url: https://seisman.github.io/how-to-write-makefile/index.html
title: "跟我一起写Makefile — 跟我一起写Makefile 1.0 文档"
host: seisman.github.io
```
