---
title: "WINNAS轻松搭：EMBY特殊影视资源刮削方案 - 梦雨玲音"
source: "https://www.rainlain.com/index.php/2024/11/05/2585/"
author:
published:
created: 2025-04-14
description: "小姐姐们的刮削方案"
tags:
  - "clippings"
---
2024年11月5日 /

本教程为系列教程，旨在使用Windows系统搭建完善的NAS各项服务。应该是全网WINNAS系统性、完整性最强的教程。系列其他教程请BLOG内搜索“WINNAS轻松搭”或通过下方链接跳转：

1、硬件、系统+影视流媒体服务  
[从零开始搭建自己的流媒体服务器](https://www.rainlain.com/index.php/2023/04/16/1619/)  
[EMBY媒体资源库进阶刮削方案](https://www.rainlain.com/index.php/2024/02/28/2023/)  
[EMBY特殊影视资源刮削方案  
](https://www.rainlain.com/index.php/2024/11/05/2585/)2、音频流媒体服务（HIFI方向）  
[自建高音质流媒体服务器](https://www.rainlain.com/index.php/2024/10/24/2544/)  
3、漫画/小说平面媒体  
[自建在线漫画/小说库](https://www.rainlain.com/index.php/2024/11/14/2616/)  
4、DOCKER应用布置  
[各类AI大模型统一接口管理系统](https://www.rainlain.com/index.php/2024/12/17/2798/)  
5、娱乐相关  
[SillyTavern（酒馆）从入门到精通](https://www.rainlain.com/index.php/2024/11/19/2645/)

喂饭：[这里](https://www.rainlain.com/index.php/2023/09/24/1755/)

没想到EMBY系列还有第三期！而且，看样子未来还会有第四期（直播内容）的教程。在过去一段时间里，BLOG主也尝试了非EMBY的方案搭建流媒体服务，但是用了一圈，无论是易用性、稳定性还是功能性和拓展性，都是EMBY当之无愧第一。那么今天的教程稍微有点超纲，主要是教大家如果给二次元或者三次元的小姐姐影视资源进行刮削的教程。毕竟这类型资源跟一般的正常向电影、剧集不太一样，国内完整且完善的教程也凤毛麟角。另外由于这类型资源本身存在敏感性，刮削方案是会随着时间失效的。如果失效BLOG主可能会撰写替代方案，如果本帖并未注明失效，那就是还可用的状态。

一、二次元小姐姐刮削

二次元的小姐姐刮削现在其实已经变得非常简单了，跟大多数常规番剧一样，使用[TMDB](https://www.themoviedb.org/)数据库即可完成90%以上番剧的识别（需要梯子）。但跟一般常规的番剧刮削不一样的是，TMDB刮削需要注意以下几个重要的点。

1、建立库

在EMBY建立“电视节目”的库，刮削来源选择TheMovieDb和MyAnimeList。后面封面之类的也需要勾选这两个。另外记住TheMovieDb的顺序要位于MyAnimeList上面，EMBY是根据这个排序进行刮削的。

![](https://www.rainlain.com/wp-content/uploads/2024/11/1730799464-image-1024x483.png)

2、关于插件

EMBY的MyAnimeList是通过对应插件实现的，如果我们发现自己地EMBY里面没有MyAnimeList的选项，可以进入插件商店安装。随后我们可以进入站点注册一个账号，并申请对应的[API](https://myanimelist.net/apiconfig)。但BLOG主实测其实不需要申请API也可以正常刮削，且因为主力刮削的来源仍然是TMDB，所以是否申请API随意。申请API要是看不懂自己开个翻译就可以看懂了，大致内容可以参考下面截图，就不做过多的介绍。

![](https://www.rainlain.com/wp-content/uploads/2024/11/1730800447-image-1024x525.png)

![](https://www.rainlain.com/wp-content/uploads/2024/11/1730800529-image-1024x994.png)

![](https://www.rainlain.com/wp-content/uploads/2024/11/1730800152-image-1024x254.png)

3、关于文件命名

我们需要手动（暂时还没找到自动方案），将番剧扔到新建的文件夹中。文件夹的命名直接复制TMDB的名称，这样可以确保100%刮削准确。这一步建议千万别省，这会为后期整理节省很多时间和精力。这里也可以看到下图，所有名称后面带年份的都是TMDB能刮削的，而没有年份的，则是TMDB缺乏数据，通过MyAnimeList进行刮削的。

这里需要注意的是[TMDB网站](https://www.themoviedb.org/)必须注册账号登录，才能解除R18内容的限制，直接进入网站搜索是没有结果的。其次，部分番剧的名称跟TMDB收录的名称不一致，这种情况下搜索会陷入僵境。这时我们需要使用[anidb](https://anidb.net/)以及[MyAnimeList](https://myanimelist.net/)网站进行搜索。BLOG主推荐anidb，因为可以直接反查TMDB的网址，节省不少时间。

![](https://www.rainlain.com/wp-content/uploads/2024/11/1730799057-image-1024x701.png)

4、翻译文本

即使根据上面教程刮削，但是最终结果仍然会有些问题。比如部分刮削内容不是中文，而是英文或日文。这时候我们可以进入emby源数据库，利用[这个](https://immersivetranslate.com/zh-Hans/)浏览器插件（EDGE/CHROME），使用“增强输入框”功能对非中文内容进行快速翻译。暂时这是BLOG主已知的条件下，效率最高的翻译方案。另外如果需要提升翻译质量，推荐使用DeepSeek大模型，具体使用方式参考BLOG主写的[另一篇](https://www.rainlain.com/index.php/2024/10/09/2496/)教程。

![](https://www.rainlain.com/wp-content/uploads/2024/11/1730801004-image-1024x460.png)

![](https://www.rainlain.com/wp-content/uploads/2024/11/1730801133-image-1024x817.png)

二、三次元小姐姐刮削

三次元的刮削比二次元就要复杂一些了，毕竟TMDB等常规的元数据网站并没有收录相关的内容。BLOG主提供的方案也不一定是最方便的，但是考虑到当前刮削的难度，有个可靠的方案就不错了。

1、JavSP项目

首先介绍的事github上的[JavSP](https://github.com/Yuukiy/JavSP)项目，该项目当前运行状态良好，一直保持更新。该项目仅能在Windows上运行，这也非常契合我们的WINNAS主题。下载好项目，并运行EXE文件。等待一会之后就会弹出一个选择文件夹的框框。选择待整理的所有小姐姐影视作品的根目录，JavSP就会自动整理、刮削所有信息。

![](https://www.rainlain.com/wp-content/uploads/2024/11/1730804731-image-1024x547.png)

![](https://www.rainlain.com/wp-content/uploads/2024/11/1730804882-image-1024x672.png)

2、安装插件

完成第一步刮削后，可以将所有整理好的文件转移到EMBY读取的文件夹内。随后我们需要第二个github项目[MetaTube](https://metatube-community.github.io/README_ZH/#_3)。我们不需要进行后端部署，因为前面第一步已经替代了metatube的后端，我们只需要在[release](https://github.com/metatube-community/jellyfin-plugin-metatube/releases)上下载EMBY相关插件就行。插件加压缩之后会获得DLL文件，放到EMBY服务端的plugins目录下即可。参考路径：“Emby-Server\\programdata\\plugins”。

![](https://www.rainlain.com/wp-content/uploads/2024/11/1730805323-image-1024x704.png)

![](https://www.rainlain.com/wp-content/uploads/2024/11/1730805415-image-1024x655.png)

随后进入EMBY的插件页面，找到MetaTube的设置界面，在Server一栏填入：https://metatube.pyhdxy.com/，这个服务器地址并不重要，因为我们并不需要真正接入该服务器刮削，具体的刮削内容，我们第一步已经处理完毕了。

3、新建影视库

新建影视库“电影”，并在元数据下载器中，仅指定MetaTube，后面的图像获取也同样仅指定MetaTube。完成这些设置之后，所有的刮削设置就已经完成了。往后新添加影片，只需要让JavSP项目自动刮削一次，然后放进EMBY就会自动进行刮削。只要JavSP项目不失效，那么这个刮削方案就不会出问题。

![](https://www.rainlain.com/wp-content/uploads/2024/11/1730805579-image-1024x552.png)

三、总结

这次没办法放效果图了233也别问blog主要服务器链接。EMBY的刮削自此终于算是圆满了。毫无疑问，当前刮削还无法做到真正的全自动无需手操的地步。特别对于新建库的朋友，花上一晚数个小时去整理库是必不可少的过程。如果觉得麻烦，直接通过文件夹观看也不是不行。甚至于直接小黄鱼买影视库，也就是几十块钱一个月。自己组NAS的乐趣，或许有一部分就来自于折腾和学习，愿各位也能从WINNAS中找到属于自己的乐趣。