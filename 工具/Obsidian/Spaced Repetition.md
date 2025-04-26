只有使用特定标签标注的部分才可以识别，标签可以在设置中配置
- `#flashcards`：[[#知识卡片]]
- `#review`：[[#复习笔记]]
# 知识卡片
## 单行卡片

问题与答案之间使用 \:\: 分隔即可

```
问题::答案
```

> [!note] 反转卡（Reversed）
> 反转卡产生 `问题-答案` 和 `答案-问题` 两张卡片。第一天两张卡同时显示，可以设置在第二天隐藏关联卡片第二张卡

反转卡使用 \:\:\: 分隔问题和答案

```
问题:::答案
```
## 多行卡片

多行卡片中，问题和答案之间使用一行 `?` 分隔，反转卡使用 `??` 分隔

```
这个问题有很多行
很多行
很多行
?
答案也可以有很多行
很多行
很多行
```
## 完形填空

完形填空支持 ==高亮== 、**加粗** 和 {{ 双括号 }} 三种表现形式，设置中默认只开启了高亮
# 分组

卡片标签支持嵌套，如 `#flashcards/deckA/deckB` 可以被 `#flashcards` 识别，并创建特定的分组

![[Pasted image 20240811073005.png]]

>[!note] 一个卡片可以归属于多个组

- 单独一行的若干标签影响到后面的所有卡片

```
#flashcards/deckA #flashcards/deckB
A::B
C::D
```

![[Pasted image 20240811073655.png]]

- 标签后直接跟卡片，则只影响到后面跟的卡片

```
#flashcards/deckA #flashcards/deckB
A::B
C::D
#flashcards/deckC E::F
```

![[Pasted image 20240811073814.png]]
还支持基于目录的分组，可以自动扫描笔记的子目录，需要在设置中启用
# 复习笔记

复习笔记使用 `#review` 标签，创建一堆属性，看不太懂

```cardlink
url: https://www.stephenmwangi.com/obsidian-spaced-repetition/notes/
title: "Notes - Obsidian Spaced Repetition"
description: "Documentation for the Obsidian Spaced Repetition plugin"
host: www.stephenmwangi.com
favicon: ../assets/favicon.ico
```
