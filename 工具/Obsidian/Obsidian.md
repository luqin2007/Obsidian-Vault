- 打开控制台：<kbd>Ctrl + Shift + I</kbd>
# 关系图谱

自定义颜色组筛选条件：
- 支持逻辑运算符 `NOT`、`OR` 和 `AND`：`tag:#标签1 OR tag:#标签2`
- 支持正则表达式：`/^(.*)#标签1|(.*)#标签2/`
# Docker 中文方块

基于 VNC 的 Obsidian 中文出现方块

解决方法：

1. 添加环境变量

| 环境变量             | 值                                          |
| ---------------- | ------------------------------------------ |
| DOCKER_MODS      | linuxserver/mods:universal-package-install |
| INSTALL_PACKAGES | fonts-noto-cjk                             |
| LC_ALL           | zh_CN.UTF-8                                |
2. 开启本地输入法

![[../../../_resources/images/Pasted image 20250413161239.png]]
# 参考

```cardlink
url: https://forum-zh.obsidian.md/t/topic/40202/3
title: "【已解决】docker版：latest ，无法显示中文，看语言菜单应该是有中文，但是显示方框和横杠"
description: "sytone/obsidian-remote这个版本也试过，1.5.3的，和linuxserver/obsidian是一样的情况，不知道怎么修正这个问题。  请指教"
host: forum-zh.obsidian.md
favicon: https://forum-zh.obsidian.md/uploads/default/optimized/2X/7/765932938b21ffa04bff1ece8f52f76c27be1bf6_2_32x32.png
image: https://forum-zh.obsidian.md/uploads/default/original/2X/b/bec35fbd23ab28909bb6bfe62cb32eb82dd34019.png
```
# 插件

%% Begin Waypoint %%
- [[Code Styler]]
- **[[Dataview]]**
- [[Embed Code File]]
- [[Execute Code]]
- [[Image in Editor]]
- [[Local REST API]]
- [[Obsidian Columns]]
- [[Spaced Repetition]]
- [[Tabs]]
- [[Templater]]

%% End Waypoint %%