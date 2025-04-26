%% Begin Waypoint %%
- [[标签]]
- [[BFG Repo-Cleaner]]
- [[Git 服务器搭建]]
- **Github**
	- [[Github SSH 登录]]
	- [[port 22 Connection refused]]

%% End Waypoint %%
# 提交

`add` 将修改的文件添加到暂存区，使用 `rm` 在暂存区中标记删除一个文件
- 后接文件名，支持通配符 `*`

```shell
git add *
```

`commit` 发起一个提交，将当前环境中 ` add ` 加入的文件提交到当前（本地）仓库

```shell
git commit -m 提交描述
```

描述包含空格时可以使用 `""` 包裹
## 分支

一个 Git 项目可以有多个分支，每个分支代表从某个节点开始为某个目的而与主干产生差异的提交集合

使用 `git branch 分支名 <提交引用>` 从某个节点创建分支，默认为 `HEAD` 

```shell
git branch newbranch
```

> [!note] 提交引用
> - `HEAD`：当前位置，详见[[../../编程/C Cpp/C++/指针#HEAD]]
> - `hash`：某提交的 hash，通过 `git log` 查看
> - `tag`：详见[[标签]]
> - 分支名：某分支位置
> - 相对位置：详见[[../../编程/C Cpp/C++/指针#相对引用]]

然后使用 `git checkout 分支名` 切换到指定分支并签出其中的最新内容

>[!note] Git 2.23 新增 `switch` 用于切换分支

```shell
git checkout newbranch 
```

使用 `git branch -b 分支名` 相当于同时执行上面两条命令
## 合并

分支任务结束后，可以将其中的提交合并到主干，两种模式：

`````col
````col-md
flexGrow=1
===
- `merge`：合并分支时产生一个特别提交，该提交同时继承于两个父节点，形成一个环

```shell
# 当前分支为 main
git checkout main
# 将 bugFix 分支合并到 main
git merge bugFix
```

若当前分支的最新提交与被合并分支已经合并过，git 只会将指针移到目标分支对应位置

```shell
# 切换到 bugFix 分支
git checkout bugFix
# 将 main 合并到 bugFix 分支
git merge main
```

git 会将 bugFix 的指针移动到之前合并产生的提交

````
````col-md
flexGrow=1
===
![[../../../_resources/images/Pasted image 20240829185328.png]]
![[../../../_resources/images/Pasted image 20240829185912.png]]
````
`````

`````col
````col-md
flexGrow=1
===
- `rebase`：将当前分支的所有提交复制到目标分支中，按时间顺序插入，并将当前分支指针指向目标分支最新节点

```shell
# 当前分支为 bugFix
git checkout bugFix
# 将 bugFix 合并到 main
git rebase main
```

若当前分支的最新提交与被合并分支已经合并过，git 只会将指针移动到目标分支对应位置

```shell
# 切换到 main 分支
git checkout main
# 将 main 分支合并到 bugFix
git rebase bugFix
```
````
````col-md
flexGrow=1
===
![[../../../_resources/images/Pasted image 20240829190924.png]]
![[../../../_resources/images/Pasted image 20240829190938.png]]
````
`````
# 指针

## HEAD

> [!note] 分离 `HEAD` 状态：当前 `HEAD` 指针与分支最新提交不在同一个位置。

> [!missing] 分离 `HEAD` 状态下不能进行 `commit` 操作

`HEAD` 指向当前工作的最后一次提交

>[!tip] 查看 HEAD 指针
>
> `HEAD` 指针保存在 `.git/HEAD` 文件中，可以通过 `cat .git/HEAD` 查看
> 
> 如果 `HEAD` 指向一个引用，可以通过 `git symbolic-ref HEAD` 查看其指向

`checkout 提交节点` 命令可以移动当前 `HEAD` 指针

```shell
git checkout <提交节点>
```

提交节点可以是 `branch` 名、提交的 `hash`、`tag` 等

> [!note] `log` 可以查看提交记录，其中包含每个提交的 hash

```shell
git log
```
## 相对引用

表示某个节点的前几个节点

- `^`：某节点的前一个节点
	- `^n` 表示某节点的第 n 个父节点，用于父节点来自 `merge` 的情况，从 1 开始

```shell
# 将 HEAD 移动到 main 的前一个节点
git checkout main^
# 将 HEAD 移动到 main 的前两个节点
git checkout main^^
```

- `~n`：某节点的前 n 个节点

```shell
# 将 HEAD 移动到 main 的前一个节点
git checkout main~1
# 将 HEAD 移动到 main 的前两个节点
git checkout main~2
```

相对引用可以使用 `HEAD`，以相对当前节点移动

```shell
# 将 HEAD 向前移动一个提交
git checkout HEAD^
# 将 HEAD 向前移动四个提交
git checkout HEAD~4
```
## 移动分支

使用 `git branch -f 分支名 提交` 将分支强制指向另一个位置

```shell
# 将 main 分支移动到当前 HEAD 指针之前三个提交的位置
git branch -f main HEAD^3
```
## 撤销提交

撤销 `commit` 提交的方法主要有两种：

`````col
````col-md
flexGrow=1
===

- `reset`：将分支回向上移动，退到某个节点

```shell
git reset HEAD~1
```

`reset` 只是移动仓库指针位置，原本修改的内容不变，但不再处于暂存区（即未被 `add` 的状态）

`reset` 只适用撤销本地仓库的提交，已提交到远程仓库的提交无法使用

````
````col-md
flexGrow=1
===
![[../../../_resources/images/Pasted image 20240829195404.png]]
````
`````

`````col
````col-md
flexGrow=1
===

- `revert`：创建一个新提交，该提交与指定指针的上一个指针状态相同

```shell
git revert HEAD
```

图示的状态中，`C2'` 提交后，仓库中所有文件状态与 `C1` 提交后的状态相同，即撤销了 `C2` 提交

````
````col-md
flexGrow=1
===
![[../../../_resources/images/Pasted image 20240829195949.png]]
````
`````
# 整理
## 复制

`````col
````col-md
flexGrow=1
===
使用 `cherry-pick` 可以将指定提交内容复制到当前指针（`HEAD`）之后

```shell
# cherry-pick <提交1> <提交2> ...
git cherry-pick C2 C4
```
````

````col-md
flexGrow=1
===
![[../../../_resources/images/Pasted image 20240829201148.png]]
````
`````
## 交互式 rebase

在 `rebase` 时，使用 `--interactive` 或 `-i` 进入交互式变基状态，Git 会打开一个文本文件列出所有需要添加的提交记录
- 调整提交顺序
- 删除提交
- 合并提交
# 远程

使用 `git remote` 设置远程仓库

```shell
git remote add origin <git 地址>
git push --set-upstream origin master
```
## clone

使用 `git clone <地址>` 从远程克隆仓库，将远程仓库下载到本地

>[!note] 远程分支：`clone` 后生成的从远程仓库下载来的分支，自动具有 `HEAD` 分离状态

远程分支的命名方式为 `<远程仓库名>/<分支名>`，远程仓库名默认为 `origin`
## 获取更新

使用 `fetch` 命令可以更新远程仓库的信息

1. 从远程仓库下载本地缺失的提交记录
2. 更新远程分支

> [!note] `fetch` 不会修改除 `.git` 外其他文件，也不会切换本地仓库的指针
## 应用更新

获取到远程仓库的数据后，可以使用 `cherry-pick`、`rebase`、`merge` 等命令合并到本地仓库。

Git 专门提供一个 `pull` 命令用于合并远程与本地仓库分支

- `git pull` 等效于 `git fetch` + `git merge`
- `git pull --rebase` 等效于 `git fetch` + `git rebase`

> [!note] merge 与 rebase 优劣
> - `merge` 保持了提交树历史，但会使提交树很乱，盘根错杂
> - `fetch` 保持提交树干净，但修改了提交树历史
## 推送

使用 `git push` 将当前仓库推送到远程仓库

> [!fail] 如果本地仓库落后于远程仓库，`push` 失败
## Pull Request

当远程服务器锁定 `main` 分支时，通过 Push Request 推送分支，由具有更新权限的人审阅并合并

>[!error] ! [远程服务器拒绝] main -> main (TF402455: 不允许推送 (push) 这个分支; 你必须使用 pull request 来更新这个分支.)

1. 新建一个分支，完成修改并推送到该分支
2. 申请 Pull Request 到远程分支

> [!tip] 若直接提交到 main 中，可使用 `reset` 重置 `main` 与远程分支一致
## 分支跟踪

Git 自动关联远程与本地同名仓库，也可以自定义

- `git checkout -b 本地分支名 远程名/远程分支名` 创建一个指定跟踪分支的本地分支
- `git branch -u 远程名/远程分支名 本地分支名` 修改某分支跟踪的远程分支
## 其它参数
### push

```shell
git push <remote> <local>
```

1. 切换到本地仓库 `<local>`
2. 获取所有提交
3. 将本地仓库 `<local>` 新增的提交合并到远程 `<remote>`

指定仓库的 `push` 指令不受 `HEAD` 的影响，不指定则使用 `HEAD` 的仓库

```shell
git push origin main
```

当 `<local>` 支持的格式为 `refspec`

> [!note] refspec：自造词，Git 能识别的位置

本地仓库名：将本地仓库提交到跟踪的远程仓库
`<source>:<destination>`：自定义本地和远程仓库位置，`<source>` 为一个位置引用，`<destination>` 为远程分支名，远程分支不存在时自动创建
`:<destination>`：删除远程分支
### fetch

```shell
git fetch <remote> <refspec>
```

与 `push` 相反，将远程某分支的提交合并到某个位置，或创建本地分支
### pull


`````col
````col-md
flexGrow=1
===
```shell
git pull origin foo
```
````
````col-md
flexGrow=1
===
```shell
git fetch origin foo
git merge origin/foo
```
````
`````

`````col
````col-md
flexGrow=1
===
```shell
git pull origin bar:bugFix
```
````
````col-md
flexGrow=1
===
```shell
git fetch origin bar:bugFix
git merge bugFix
```
````
`````
