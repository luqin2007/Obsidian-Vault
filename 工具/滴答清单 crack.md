---
title: 新人初试 滴答清单 crack - 吾爱破解 - 52pojie.cn
source: https://www.52pojie.cn/thread-1881288-1-1.html
author:
  - erichz
published: 
created: 2024-12-15
description: "[md]# 滴答清单/ticktick crack for windows## 0.前言新人第一次发帖，已经阅读版规，有违规的地方希望各位指出，我会及时改正想要使用windows平台的滴答清单，但是 ..."
tags:
  - clippings
---
## 滴答清单/ticktick crack for windows

### 0.前言

新人第一次发帖，已经阅读版规，有违规的地方希望各位指出，我会及时改正  
想要使用windows平台的滴答清单，但是只发现了在github上的ticktick的crack.

> [https://github.com/yazdipour/unlimited-ticktick-windows](https://github.com/yazdipour/unlimited-ticktick-windows)  
> 个人猜测滴答的破解方法类似，但是approach2的修改不知道为什么没法编译。误打误撞破解成功，下面给出我的步骤。

### 1.下载软件和工具

软件是滴答清单最新官方5.0.5.0

> [https://www.dida365.com/down/getApp/download?type=win64](https://www.dida365.com/down/getApp/download?type=win64)  
> 工具是doSpy，我使用的是这个版本  
> [https://github.com/kkwpsv/dnSpy/releases/tag/6.1.9](https://github.com/kkwpsv/dnSpy/releases/tag/6.1.9)
> 
> ### 2.步骤
> 
> #### 安装滴答清单，使用dnSpy到安装目录下打开ticktick.exe，打开ticktick\_WPF.Models.UserModel
> 
> #### 1.修改public DateTime
> 
> 修改如下所示
> 
> ```csharp
> // Token: 0x17001375 RID: 4981
> // (get) Token: 0x06009423 RID: 37923 RVA: 0x00051E59 File Offset: 0x00050059
> public DateTime? proEndDate
> {
> get
> {
> return new DateTime?(new DateTime(2030, 12, 25));
> }
> }
> ```
> 
> #### 2.修改public bool pro
> 
> 修改如下所示
> 
> ```csharp
> // Token: 0x1700137A RID: 4986
> // (get) Token: 0x0600942C RID: 37932 RVA: 0x00012168 File Offset: 0x00010368
> public bool pro
> {
> get
> {
> return true;
> }
> }
> ```
> 
> ### 3.保存模块并退出，就能正常使用会员功能了