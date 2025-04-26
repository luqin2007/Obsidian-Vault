若出现控制台乱码，原因是 Windows 控制台默认使用 GBK 编码，而代码中使用 UTF8，解决方法有两种解决办法
# 通用解法

将源代码的字符集设置为 GBK，并使用外部控制台（不推荐）
# Idea

帮助 -> 自定义 VM 选项 中添加如下内容：

```
-Dfile.encoding=UTF-8
```

Java 17+ 可能还有乱码，需要调整成 `compat`

```
-Dfile.encoding=COMPAT
```
# CLion

控制台输出编码设为 UTF8

```cpp
system("chcp 65001");
```
