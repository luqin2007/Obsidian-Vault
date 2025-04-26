---
title: 通过USB让手机连接电脑上网 | 校园网
source: https://www.cnblogs.com/zhuangjie/p/15268782.html
author:
  - "[[小庄的blog]]"
published: 2021-09-14T17:54:00.0000000&#x2B;08:00
created: 2025-02-20
description: Gnirehtet – 为 Android 设备提供反向网络连接[Windows、macOS、Linux] 准备： gnirehtet （选择gnirehtet-rust-win64.zip）：https://github.com/Genymobile/gnirehtet 使用：压缩放在喜欢的地方，
tags:
  - clippings
---
## Gnirehtet – 为 Android 设备提供反向网络连接\[Windows、macOS、Linux\]

## 准备：

gnirehtet （选择[gnirehtet-rust-win64.zip](https://github.com/Genymobile/gnirehtet/releases/download/v2.5/gnirehtet-rust-win64-v2.5.zip)）：https://github.com/Genymobile/gnirehtet   使用：压缩放在喜欢的地方，运行时打开

adb：https://xiazai.zol.com.cn/detail/45/445733.shtml（非官网）使用：解压到 gnirehtet文件夹下

[![](https://img2020.cnblogs.com/blog/2160655/202112/2160655-20211228170310742-1192479264.png)](https://img2020.cnblogs.com/blog/2160655/202112/2160655-20211228170310742-1192479264.png)

## 开始使用：

USB连接手机，选择“传输文件”，执行 gnirehtet-run.cmd文件，此时会弹出点击“  开启调试” 允许（会安装一个软件），关闭重新打开  gnirehtet-run.cmd 

[![](https://wwwapprcn.macapp8.com/Gnirehtet-2.jpg)](https://wwwapprcn.macapp8.com/Gnirehtet-2.jpg)

[![](https://wwwapprcn.macapp8.com/Gnirehtet-4.png)](https://wwwapprcn.macapp8.com/Gnirehtet-4.png)

## 测试

关闭流量，wifi，查看手机是否还能否上网，如果不能再退出重新执行gnirehtet-run.cmd （手机不用拔），实现不行，请往下看：

下图第二个不需要其它环境，但第一个需要java运行环境（需要安装JDK），解决方案是换另一种看看。

[![](https://img2023.cnblogs.com/blog/2160655/202212/2160655-20221203145051086-1544980219.png)](https://img2023.cnblogs.com/blog/2160655/202212/2160655-20221203145051086-1544980219.png)

学习自：http://www.apprcn.com/gnirehtet.html