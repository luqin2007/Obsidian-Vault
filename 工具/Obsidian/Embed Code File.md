一个可以展示代码文件片段的插件

````
```embed-<some-language>
PATH: "vault://<some-path-to-code-file>" or "http[s]://<some-path-to-remote-file>"
LINES: "<some-line-number>,<other-number>,...,<some-range>"
TITLE: "<some-title>"
```
````

- `<some-language>`：语言类型，可以在设置中添加
- `PATH`：代码位置，`vault://` 协议开头表示笔记目录
- `LINES`：代码行，可选。连续多行使用 `-` 分隔，多段代码使用 `,` 分隔
- `TITLE`：代码块标题，默认为 `PATH` 内容

# 参考

```cardlink
url: https://github.com/almariah/embed-code-file
title: "GitHub - almariah/embed-code-file"
description: "Contribute to almariah/embed-code-file development by creating an account on GitHub."
host: github.com
favicon: https://github.githubassets.com/favicons/favicon.svg
image: https://opengraph.githubassets.com/3d30369b63b36889695cad06441144938e04373287f07c3a0bc832eba3c7702e/almariah/embed-code-file
```
