---
title: 修复 aria2 端口异常 - Keenwon's Blog
source: https://keenwon.com/aria2-binding-port-errors/
author: 
published: 
created: 2025-02-21
description: keenwon's blog
tags:
  - clippings
---
2023-04-01 · 354 chars · 2 min read

[![](https://keenwon.com/assets/image/f39ac524.png)](https://keenwon.com/about/)[keenwon](https://keenwon.com/about/)

前两天我打开电脑，发现 aria2 的所有任务都是「已停止」的状态，提示「未知错误」，点进入详情，提示 `errors occurred while binding port`。

开启 aria2 的日志，看到下面几行：

```prism
2023-03-28 23:46:14.892986 [ERROR] [PeerListenCommand.cc:84] IPv4 BitTorrent: failed to bind TCP port 51413Exception: [SocketCore.cc:312] errorCode=1 Failed to bind a socket, cause: Address already in use2023-03-28 23:46:14.893191 [ERROR] [RequestGroupMan.cc:572] Exception caughtException: [BtSetup.cc:212] errorCode=1 Errors occurred while binding port.
```

在网上搜索了一大通，基本都是说端口占用。在 ubuntu 内查找端口占用、修改防火墙，都不起作用。由于我是在 win11 的 wsl 内安装 aria2 的，又修改了 windows 的防火墙规则，依然不起作用。

差点就放弃了，直到看到[这个回答](https://superuser.com/questions/1486417/unable-to-start-kestrel-getting-an-attempt-was-made-to-access-a-socket-in-a-way)，说 windows 保留了一些端口不能使用，在 powershell 里输入 `netsh interface ipv4 show excludedportrange protocol=tcp`：

```prism
PS C:\Users\keenwon> netsh interface ipv4 show excludedportrange protocol=tcp
协议 tcp 端口排除范围
开始端口    结束端口----------    --------     49738       49837     49838       49937     50000       50059     *     50060       50159     50260       50359     50360       50459     50460       50559     51217       51316     51317       51416     51417       51516
* - 管理的端口排除。
PS C:\Users\keenwon>
```

破案了，我用的是[这个](https://github.com/P3TERX/aria2.conf)配置，默认端口 51413，刚好在排除范围 51317 ~ 51416 内。最坑的是我还修改成 51414、51415 试过...

知道原因解决起来就简单了，直接改到 52000，启动正常。

晚上仅有的两三个小时，就这么没了...