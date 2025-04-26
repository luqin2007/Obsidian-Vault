用于制作多系统 U 盘的工具，一个 U 盘可以包含多个系统。`Ventoy2Disk.exe` 可以安装或升级 `Ventoy`，`VentoyPlugson.exe` 选择启动用于打开配置页面。

```cardlink
url: https://www.ventoy.net/cn/download.html
title: "Ventoy"
description: "Ventoy is an open source tool to create bootable USB drive for ISO files. With ventoy, you don't need to format the disk again and again, you just need to copy the iso file to the USB drive and boot it."
host: www.ventoy.net
favicon: https://www.ventoy.net/favicon.ico
```

# 配置

在配置页各个标签中，第一个不带后缀的是默认配置，`legacy` 、`uefi` 、`ia32` 、`mips` 等可以设定从不同平台启动的差异化配置

## 语言

配置页面页面右上角可以切换中英文，在全局控制插件 - `VTOY_MENU_LANGUAGE` 菜单语言可以切换启动页面语言。

## 镜像

以 U 盘根目录为起点，在全局控制插件 - `VTOY_DEFAULT_SEARCH_ROOT` 指定搜索目录中设置一个目录用于存放系统镜像文件，支持 `iso`、`img`、`vhd` 等常见镜像。

后面还有一些细节设置，如过滤某些文件和文件夹、列表显示方式等。

在菜单别名插件中配置菜单别名

## 主题

`Ventoy` 使用 grub 主题，可在相关资源站下载或创建。

```cardlink
url: https://www.gnome-look.org/browse?cat=109
title: "Browse GRUB Themes Latest | https://www.gnome-look.org/browse?cat=109"
description: "Browse GRUB Themes Latest | https://www.gnome-look.org/browse?cat=109 | A community for free and open source software and libre content"
host: www.gnome-look.org
image: https://www.gnome-look.org/stores/media/store_gnome/gnome-look-logo-2.png
```

下载解压到 U 盘，在主题插件页将主题目录中的 `theme.txt` 添加到 `file` 主题配置文件列表中，将目录中的 `.pf2` 文件添加到下方 `fonts` 字体文件列表中。

默认 `Ventoy` 分辨率为 1024 x 768，可在 `gfxmode` 屏幕分辨率中按需选择。

# 参考

```cardlink
url: https://www.ventoy.net/cn/index.html
title: "Ventoy"
description: "Ventoy is an open source tool to create bootable USB drive for ISO files. With ventoy, you don't need to format the disk again and again, you just need to copy the iso file to the USB drive and boot it."
host: www.ventoy.net
favicon: https://www.ventoy.net/favicon.ico
```
