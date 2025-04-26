---
title: "我有特别的 RSS 使用技巧 - DIYgod"
source: "https://diygod.cc/ohmyrss"
author:
  - "[[DIYgod]]"
published: 2019-05-14
created: 2024-11-06
description: "大家都知道 RSS 是一种用来消息聚合的格式规范，有着更高的阅读效率、更好的阅读体验、可以掌握主动权等等优点。 本文不会介绍 RSS 的各种好处和各式各样的阅读器，因为相关网络资料已经足够多了。这里我介绍一下怎样充分挖掘 RSS 的使用价值，因为它的用途一直被大家低估。阅读器…"
tags:
  - "clippings"
---
大家都知道 RSS 是一种用来消息聚合的格式规范，有着更高的阅读效率、更好的阅读体验、可以掌握主动权等等优点。

本文不会介绍 RSS 的各种好处和各式各样的阅读器，因为相关网络资料已经足够多了。这里我介绍一下怎样充分挖掘 RSS 的使用价值，因为它的用途一直被大家低估。

## 阅读器#

从最简单的开始，我们可以看看如何用 RSS 订阅一个博客。

假设你想订阅世界上最可爱的博客 [Hi, DIYgod](https://diygod.me/)，巧的是它已经很贴心地提供了 [RSS 地址](https://diygod.me/atom.xml)，你只需要找一个适合自己的 RSS 阅读器。

这里有几个推荐：

iOS 和 macOS 平台 - Reeder

Android 平台 - Palabre 和 FeedMe

打开阅读器，输入链接，点击订阅

![9102rss1](https://ipfs.crossbell.io/ipfs/bafkreia2qs3hugcjppvgn64c572i427ezlnrww3tqcenjl7asna6csrxsq?img-quality=75&img-format=auto&img-onerror=redirect&img-width=3840)

![9102rss2](https://ipfs.crossbell.io/ipfs/bafybeidgh477wcnrn37o4stwvikf4jpzlgy4ptbvjk6lrjn2hccd6njbaa?img-quality=75&img-format=auto&img-onerror=redirect&img-width=3840)

我们便学会了 RSS 最基础的使用方法。

## 云服务#

这时候你可能会发现一些问题。

只有一直开着电脑或手机才能获取到更新，如果勤劳的 DIYgod 一天更新了 100 篇文章，而 RSS 的输出数量是有限的，等一天后再开电脑，这时候阅读器刷新，你只能看到最新的几篇了（当然 DIYgod 不可能一天更新 100 篇，这个例子不是很好）。

还有，你同时在手机和电脑上订阅了 DIYgod，在电脑上看完，手机上还是未读状态，如果订阅了很多内容，这会很糟糕。

所以我们需要一个服务端来同步和刷新 RSS 内容。

其中用的人数最多的是 Feedly 和 Inoreader。

它们固然很好，但我更推荐功能更强自由度更高的自建 [Tiny Tiny RSS](https://github.com/HenryQW/docker-ttrss-plugins)。

自建不仅可以使数据更可控，它还有丰富的插件可以满足各种各样的需求，比如全文内容提取、Fever API 模拟、DOM 操控、繁体转简体。上面提到的阅读器都可以配合它使用。

![9102rss3](https://ipfs.crossbell.io/ipfs/bafkreiezptmfq26vh53vlxnlr5fiaoii5qtlcvwte6ru5iy67baguphtxe?img-quality=75&img-format=auto&img-onerror=redirect&img-width=3840)

## RSSHub#

看起来很美好，但提供 RSS 订阅的网站实在是太少了，原因很好理解：RSS 不利于网站方的广告投放、隐私搜集、用户存留等商业行为。

我们当然不满于此，于是我发起了 [RSSHub](https://github.com/DIYgod/RSSHub) 项目，项目原理很简单：RSSHub 请求你想要的源站数据，然后把它们以 RSS 格式输出，做到了万物皆可 RSS。

经过近 200 名开发者历时一年多的活跃开发，RSSHub 已经支持了 300 多个网站的近 600 种数据，而且这些数字还在快速增长中。

这里分享一部分我常用的路由：

- 什么值得买排行榜：谨慎订阅，它浪费了我不少钱
- 各种老婆的手办更新：闭着眼买就完事了
- 微小微和猫饼的 bilibili 动态
- DIYgod 关注视频动态：DIYgod 关注的 UP 主们的动态，不用刷很蠢的 B 站动态了
- JFlaMusic 的 Youtube 视频
- Dcard 论坛：一个超级有趣的台湾论坛，适合配合 Tiny Tiny RSS 的繁体转简体插件使用
- PlayStation Store 会员限免游戏：再也不怕忘记领免费游戏（虽然领了也不会玩）
- RSSHub 有新路由啦
- himitsu 的 Twitter 动态：NSFW
- 发小的微博：不会再因为错过发小的微博被骂了
- 即刻工作日闹钟设置提醒
- 公众号 “微小微” 更新
- 豆瓣正在上映的超过 7.5 分的电影
- 知乎热榜

## BT 下载#

假设你是一个美剧爱好者，我们可以看看如何用 RSS 来追权利的游戏第 8 季。

RSSHub 有一些支持 BT 下载的路由，比如权利的游戏字幕组源订阅地址为：[https://rsshub.app/zimuzu/resource/10733](https://rsshub.app/zimuzu/resource/10733)，接着我们加一个 filter 参数过滤出第 8 季内容：[https://rsshub.app/zimuzu/resource/10733?filter=S08](https://rsshub.app/zimuzu/resource/10733?filter=S08)。

然后挑选一个正常的 BT 客户端（迅雷不算），我用的是群晖的 Download Station。

把地址添加到 BT 客户端的 RSS 订阅，这样美剧更新后 BT 客户端就会自动把最新一集下载到硬盘里，晚上下班回家打开电视就可以直接看了。

最近我订阅的美剧和日剧

![9102rss4](https://ipfs.crossbell.io/ipfs/bafkreiaadcqpqkxox6ehgkjpzwb4voqwzlzvuilwqqbvz5fpklwo4lejk4?img-quality=75&img-format=auto&img-onerror=redirect&img-width=3840)

获取到更新并下载完成群晖会发邮件告诉我

![9102rss5](https://ipfs.crossbell.io/ipfs/bafkreiebs7nbvf6j3bofwwmcrvlfxfybwhpycwgxnktyvb6fnzb7d5smmq?img-quality=75&img-format=auto&img-onerror=redirect&img-width=3840)

## 播客#

假设你是一个播客爱好者，我们可以看看如何用 RSS 来扩充你的播客库。

播客客户端可以访问 RSS 检查更新，以下载系列中新的集数收听，RSSHub 或 [getpodcast](https://getpodcast.xyz/) 有一些支持播客的 RSS 可以直接使用，比如用 iOS 自带的播客应用订阅一个网易云音乐的 ASMR 电台：

![9102rss6](https://ipfs.crossbell.io/ipfs/bafkreie6c6xyofmw4xpugwcjvnct46q7ylqb3r6htncy4v5za4yykvqqdm?img-quality=75&img-format=auto&img-onerror=redirect&img-width=3840)

## 联动#

RSS 可以通过 IFTTT 跟各种奇奇怪怪的东西联动。

其中一个使用案例是我的 Telegram 频道：[https://t.me/awesomeDIYgod](https://t.me/awesomeDIYgod)，它通过 IFTTT 监听了很多 RSS 更新，有 DIYgod 的博客更新、DIYgod 的扇贝打卡、DIYgod 的 Twitter 更新、DIYgod 喜欢的网易云音乐、DIYgod 的 bilibili 投币视频...

这样你甚至可以很容易实现通过 RSS 控制开关灯、咕咕鸡自动打印小姐姐的微博、把权利的游戏差评自动发推特艾特编剧等等操作，虽然可能没什么用就是了。

6 月 2 日更新：

一次优秀的联动：[《优雅地下载我的 B 站投币视频》](https://diygod.cc/download-webhook)

以上是我列举的几个适合 RSS 使用的场景和方式，现在大家是不是对 “RSS 是一种用来消息聚合的格式规范” 这句话有了更深的理解呢？