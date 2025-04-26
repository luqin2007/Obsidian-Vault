# 原文地址

```cardlink
url: https://ruanyifeng.com/blog/2018/09/vimrc.html
title: "Vim 配置入门 - 阮一峰的网络日志"
host: ruanyifeng.com
```

---

Vim 是最重要的编辑器之一，主要有下面几个优点。

![](https://www.wangbase.com/blogimg/asset/201809/bg2018091601.jpg)

> - 可以不使用鼠标，完全用键盘操作。
> - 系统资源占用小，打开大文件毫无压力。
> - 键盘命令变成肌肉记忆以后，操作速度极快。
> - 服务器默认都安装 Vi 或 Vim。

Vim 的配置不太容易，它有自己的语法，许许多多的命令。我总是记不清楚，所以就整理了下面这篇文章，列出主要配置项的含义。

## 一、基础知识

Vim 的全局配置一般在`/etc/vim/vimrc`或者`/etc/vimrc`，对所有用户生效。用户个人的配置在`~/.vimrc`。

如果只对单次编辑启用某个配置项，可以在命令模式下，先输入一个冒号，再输入配置。举例来说，`set number`这个配置可以写在`.vimrc`里面，也可以在命令模式输入。

```shell
:set number
```

配置项一般都有"打开"和"关闭"两个设置。"关闭"就是在"打开"前面加上前缀"no"。

```shell
" 打开
set number

" 关闭
set nonumber
```

上面代码中，双引号开始的行表示注释。

查询某个配置项是打开还是关闭，可以在命令模式下，输入该配置，并在后面加上问号。

```shell
:set number?
```

上面的命令会返回`number`或者`nonumber`。

如果想查看帮助，可以使用`help`命令。

```shell
:help number
```

## 二、基本配置

（1）不与 Vi 兼容（采用 Vim 自己的操作命令）
```shell
set nocompatible
```

（2）打开语法高亮。自动识别代码，使用多种颜色显示。

```shell
syntax on
```

（3）在底部显示，当前处于命令模式还是插入模式。

```shell
set showmode
```

（4）命令模式下，在底部显示，当前键入的指令。

```shell
set showcmd
```

比如，键入的指令是`2y3d`，那么底部就会显示`2y3`，当键入`d`的时候，操作完成，显示消失。

（5）支持使用鼠标。

```shell
set mouse=a
```

（6）使用 utf-8 编码。

```shell
set encoding=utf-8  
```

（7）启用 256 色。

```shell
set t_Co=256
```

（8）开启文件类型检查，并且载入与该类型对应的缩进规则。

```shell
filetype indent on
```

比如，如果编辑的是`.py`文件，Vim 就是会找 Python 的缩进规则`~/.vim/indent/python.vim`。

## 三、缩进

（9）按下回车键后，下一行的缩进会自动跟上一行的缩进保持一致。

```shell
set autoindent
```

（10）按下 Tab 键时，Vim 显示的空格数。

```shell
set tabstop=2
```

（11）缩进字符数

```shell
set shiftwidth=4
```

在文本上按下`>>`（增加一级缩进）、`<<`（取消一级缩进）或者`==`（取消全部缩进）时，每一级的字符数。

（12）由于 Tab 键在不同的编辑器缩进不一致，该设置自动将 Tab 转为空格。

```shell
set expandtab
```

（13）Tab 转为多少个空格。

```shell
set softtabstop=2
```

## 四、外观

（14）显示行号

```shell
set number
```

（15）显示光标所在的当前行的行号，其他行都为相对于该行的相对行号。

```shell
set relativenumber
```

（16）光标所在的当前行高亮。

```shell
set cursorline
```

（17）设置行宽，即一行显示多少个字符。

```shell
set textwidth=80
```

（18）自动折行，即太长的行分成几行显示。

```shell
set wrap
```

关闭自动折行

```shell
set nowrap
```

（19）只有遇到指定的符号（比如空格、连词号和其他标点符号），才发生折行。

```shell
set linebreak
```

不会在单词内部折行。

（20）指定折行处与编辑窗口的右边缘之间空出的字符数。

```shell
set wrapmargin=2
```

（21）垂直滚动时，光标距离顶部/底部的位置（单位：行）。

```shell
set scrolloff=5
```

（22）水平滚动时，光标距离行首或行尾的位置（单位：字符）。

```shell
set sidescrolloff=15
```

该配置在不折行时比较有用。

（23）是否显示状态栏。

```shell
set laststatus=2
```

0 表示不显示，1 表示只在多窗口时显示，2 表示显示。

（24）在状态栏显示光标的当前位置（位于哪一行哪一列）。

```shell
set  ruler
```

## 五、搜索

（25）自动高亮对应的另一个括号。

```shell
set showmatch
```

（26）搜索时，高亮显示匹配结果。

```shell
set hlsearch
```

（27）输入搜索模式时，每输入一个字符，就自动跳到第一个匹配的结果。

```shell
set incsearch
```

（28）搜索时忽略大小写。

```shell
set ignorecase
```

（29）智能大小写敏感

```shell
set smartcase
```

如果同时打开了`ignorecase`，那么对于只有一个大写字母的搜索词，将大小写敏感；其他情况都是大小写不敏感。比如，搜索`Test`时，将不匹配`test`；搜索`test`时，将匹配`Test`。

## 六、编辑

（30）打开英语单词的拼写检查。

```shell
set spell spelllang=en_us
```

（31）不创建备份文件。

```shell
set nobackup
```

默认情况下，文件保存时，会额外创建一个备份文件，它的文件名是在原文件名的末尾，再添加一个波浪号（〜）。

（32）不创建交换文件。

```shell
set noswapfile
```

交换文件主要用于系统崩溃时恢复文件，文件名的开头是`.`、结尾是`.swp`。

（33）保留撤销历史。

```shell
set undofile
```

Vim 会在编辑时保存操作历史，用来供用户撤消更改。默认情况下，操作记录只在本次编辑时有效，一旦编辑结束、文件关闭，操作历史就消失了。

打开这个设置，可以在文件关闭后，操作记录保留在一个文件里面，继续存在。这意味着，重新打开一个文件，可以撤销上一次编辑时的操作。撤消文件是跟原文件保存在一起的隐藏文件，文件名以`.un~`开头。

（34）设置备份文件、交换文件、操作历史文件的保存位置。

```shell
set backupdir=~/.vim/.backup//  
set directory=~/.vim/.swp//
set undodir=~/.vim/.undo// 
```

结尾的`//`表示生成的文件名带有绝对路径，路径中用`%`替换目录分隔符，这样可以防止文件重名。

（35）自动切换工作目录。

```shell
set autochdir
```

这主要用在一个 Vim 会话之中打开多个文件的情况，默认的工作目录是打开的第一个文件的目录。该配置可以将工作目录自动切换到，正在编辑的文件的目录。

（36）出错时，不要发出响声。

```shell
set noerrorbells
```

（37）出错时，发出视觉提示，通常是屏幕闪烁。

```shell
set visualbell
```

（38）Vim 需要记住多少次历史操作。

```shell
set history=1000
```

（39）打开文件监视。

```shell
set autoread
```

如果在编辑过程中文件发生外部改变（比如被别的编辑器编辑了），就会发出提示。

（40）如果行尾有多余的空格（包括 Tab 键），该配置将让这些空格显示成可见的小方块。

```shell
set listchars=tab:»■,trail:■
set list
```

（41）命令模式下，底部操作指令按下 Tab 键自动补全。

```shell
set wildmenu
set wildmode=longest:list,full
```

第一次按下 Tab，会显示所有匹配的操作指令的清单；第二次按下 Tab，会依次选择各个指令。

## 七、参考链接

- [Turn your vim editor into a productivity powerhouse](https://opensource.com/article/18/9/vi-editor-productivity-powerhouse)
- [A Good Vimrc](https://dougblack.io/words/a-good-vimrc.html)
- [Vim documentation: options](http://vimdoc.sourceforge.net/htmldoc/options.html)

（完）

### 文档信息

- 版权声明：自由转载-非商用-非衍生-保持署名（[创意共享3.0许可证](http://creativecommons.org/licenses/by-nc-nd/3.0/deed.zh)）
- 发表日期： 2018年9月16日

## 留言（37条）

libinx 说：

除了文章提到的设置，在我的 .vimrc 还添加了匹配 Python 开发的配置，每次新建一个 .py 的文件都可以自动添加脚本的头部信息，很方便。

func SetTitle()  
call setline(1, "\#!/usr/bin/python")  
call setline(2, "\# -*- coding=utf8 -*-")  
call setline(3, "\"\"\"")  
call setline(4, "\# @Author : Li Bin")  
call setline(5, "\# @Created Time : ".strftime("%Y-%m-%d %H:%M:%S"))  
call setline(6, "\# @Description : ")  
call setline(7, "\"\"\"")  
normal G  
normal o  
normal o  
endfunc  
autocmd bufnewfile *.py call SetTitle()  

2018年9月16日 10:49 | [#](http://www.ruanyifeng.com/blog/2018/09/vimrc.html#comment-393189) | [引用](https://ruanyifeng.com/blog/2018/09/vimrc.html#comment-text "引用libinx的这条留言")

fuchao 说：

可以用现成的吖，https://github.com/amix/vimrc

2018年9月16日 11:23 | [#](http://www.ruanyifeng.com/blog/2018/09/vimrc.html#comment-393190) | [引用](https://ruanyifeng.com/blog/2018/09/vimrc.html#comment-text "引用fuchao的这条留言")

必填 说：

习惯用vim的人一定不要错过chrome插件vimium：https://chrome.google.com/webstore/detail/vimium/dbepggeogbaibhgnhhndojpepiihcmeb?hl=en

有了这个神器，我浏览网页都不大需要用鼠标了。

2018年9月16日 13:45 | [#](http://www.ruanyifeng.com/blog/2018/09/vimrc.html#comment-393197) | [引用](https://ruanyifeng.com/blog/2018/09/vimrc.html#comment-text "引用必填的这条留言")