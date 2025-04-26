一个用于清除 Git 不良提交的工具，用于替代 `git-filter-branch`

1. 创建仓库副本

```shell
git clone --mirror <仓库名>
```

2. 删除不良提交

> [!tip] `bfg` 只会删除历史提交中的内容，不会影响当前版本文件

```shell
java -jar bfg.jar <参数> <仓库地址>
```

- 参数：确定删除哪些提交
	- `--delete-files id_{dsa,rsa}`：删除 `id_dsa`、`id_rsa`
	- `--strip-blobs-bigger-than 50M`：删除大于 50M 的文件
	- `--replace-text passwords.txt`：将 `passwords.txt` 文件中出现的文本替换成 `REMOVE***`
- 仓库地址：之前 `clone` 下的目录（通常为 `项目名.git`）

3. 重新提交

```shell
git reflog expire --expire=now --all && git gc --prune=now --aggressive
git push
```

# 参考

```cardlink
url: https://rtyley.github.io/bfg-repo-cleaner/
title: "BFG Repo-Cleaner by rtyley"
description: "A simpler, faster alternative to git-filter-branch for deleting big files and removing passwords from Git history."
host: rtyley.github.io
```
