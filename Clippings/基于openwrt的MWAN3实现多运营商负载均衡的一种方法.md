---
title: "基于openwrt的MWAN3实现多运营商负载均衡的一种方法"
source: "https://zhuanlan.zhihu.com/p/352098418"
author:
  - "[[知乎专栏]]"
published:
created: 2025-04-12
description: "本人注重原理分析，要求对其原理掌握，否则按教程操作，你怕是什么都学不会，仔细看，认真记比较好。首先确认一下基本细节1、路由器为openwrt且lan网口总数大于等于3个（有可能部分openwrt交换机部分不支持wan） 2…"
tags:
  - "clippings"
---
[![madebyqwe](https://picx.zhimg.com/v2-49fb711c9686a238bf114beced0801d9_l.jpg?source=172ae18b)](https://www.zhihu.com/people/90726228)

[madebyqwe](https://www.zhihu.com/people/90726228)

made.byqwe.cn前移动gpon工程师前川青铁路运维

26 人赞同了该文章

**本人注重原理分析，要求对其原理掌握，否则按教程操作，你怕是什么都学不会，仔细看，认真记比较好。**

## 首先确认一下基本细节

1、路由器为[openwrt](https://zhida.zhihu.com/search?content_id=166374201&content_type=Article&match_order=1&q=openwrt&zhida_source=entity)且lan网口总数大于等于3个（有可能部分openwrt交换机部分不支持wan）

2、多个运营商不是多拨哈

3、固件支持交换机[vlan](https://zhida.zhihu.com/search?content_id=166374201&content_type=Article&match_order=1&q=vlan&zhida_source=entity)配置和[负载均衡](https://zhida.zhihu.com/search?content_id=166374201&content_type=Article&match_order=1&q=%E8%B4%9F%E8%BD%BD%E5%9D%87%E8%A1%A1&zhida_source=entity)配置

4、ssh使用与linux使用有一定的了解

5、闲的没事情做，有时间折腾

## 正文

首先你的路由器固件需要时openwrt，支持交换机vlan模式，如图

![](https://pic2.zhimg.com/v2-7850b3ae74875481226050b4b40cba73_1440w.jpg)

好的，接下来配置vlan交换机

## 我这边选择lan1与lan4为上网接口，选择端口是随意的，重点是学习原理

![](https://pic2.zhimg.com/v2-8b3d4f58313ef61eb9f396ad575f227b_1440w.jpg)

![](https://pic4.zhimg.com/v2-ed78f98c4701a27efdd39a4e58da64fd_1440w.jpg)

![](https://pic3.zhimg.com/v2-6bed99286f3155593a5e8f7efb4b6de0_1440w.jpg)

前面我们提到我将lan1与lan4设计为上网口，接下来将详细解释配置规则

**1、如果这个lan口是用于上网的，那么他不应该跟lan口其他的接口关联，所以应当关闭其他vlan与这个接口的绑定关系。**

![](https://picx.zhimg.com/v2-2e463774bb4050df3ed04f6fd0d943b5_1440w.jpg)

**lan1在vlan1中描述为关，即lan1与vlan1无关系（设计层面，不考虑物理层面哈）。**

**lan1在vlan2中描述为untagged，该描述表示不对这个lan口进行打上标签，即该接口接收与发出的数据不应该具有标签，配置时一定不要选择其他选项就是了。**

**lan1在vlan3中描述为关，即lan1与vlan3无关系（设计层面，不考虑物理层面哈）。**

## 接下来是lan2与lan3

![](https://pic4.zhimg.com/v2-53eea26590dca914e06bdd3fefd5f23b_1440w.jpg)

由于lan2与lan3是lan口，他们不参与上网的配置，所以他们仅绑定vlan1，这2个接口不参与上网所以在vlan2与vlan3中的配置应该是关。

## lan4与lan1的原理相同，注意规划就行了，不要规划错。

**总结一下，当一个接口需要与其他接口隔离时，使用vlan是最方便的方式之一，其原理就是通过给其内部划分不同的vlan实现隔离，但因接收与发出的数据不带vlan所以不对其他的设备产生影响，重点在于，接收与发出不打上标签，其分离的vlan标签仅在cpu也就是内部运行。每一个独立的区域都应该是一个单独的vlan，当然所有的lan口算一个区域，每一个虚拟的上网口算一个区域，于是，本篇一个具备3个vlan属性。**

接下来是对虚拟的上网口进行配置，文章将使用[dhcp](https://zhida.zhihu.com/search?content_id=166374201&content_type=Article&match_order=1&q=dhcp&zhida_source=entity)模式与pppoe进行演示

## 首先是lan口1的pppoe模式

![](https://pic3.zhimg.com/v2-eb19379368967cb81ba64b629ec6479c_1440w.jpg)

名称建议全大写，后面的配置用用得上，linux是大小写敏感的，所以要区分大小写。

协议按实际情况决定。

一定要注意绑定接口，WAN绑定的lan1，lan1对应绑定的是vlan2，所以我们选择eth0.2这个接口，其中，eth0是这个网卡的名称，.2是子接口2也就是虚拟接口vlan2。总结一下就是WAN绑定的是eth0这个网卡下的已在交换机中绑定了vlan2的那个子接口lan1，于是他的名称就是eth0.2。

## 对于lan4的dhcp

![](https://pic1.zhimg.com/v2-b6b7ac170c9607a3f96f8378a30d2270_1440w.jpg)

emmm，由于删了到路由器192.168.123.0的接口，过不去了，明天再更新吧。还好没有交付到生产环境里面。

**在更新之前，我先来解释一下为什么我删掉了到123.0的路由会导致我无法访问。**

首先我是通过公网访问我的openwrt的，我的openwrt选择了lan1接移动光猫，lan4接主路由，模式是dhcp也就是获取主路由的ip实现该线路的上网功能，我之所以删掉认为其不会断网是因为，lan4用于上网，lan3也跟主路由相连，那按道理应该会环路才对，但是由于lan4内部有vlan3的标签，所以并不会造成环路，那么为什么删了lan4会断网，而不会通过lan3继续与主路由互通呢。原因在于静态路由

**主路由到openwrt的截图**

![](https://pic3.zhimg.com/v2-f3947ab907722f1d7307d9a3e2acdd00_1440w.jpg)

openwrt是192.168.121.0/24网段的，网关为192.168.121.1，lan4获取到的主路由ip是192.168.123.20

那么主路由如果要到192.168.121.0/24这个网段就会通过192.168.123.20访问，如果删掉这个路由就没法提供lan4访问openwrt，因为lan3没有路由（注意是路由）到openwrt，所以不能互通，那么lan3的意义在哪里呢？在于我可以不改变原有架构实现双ip切换，我只需要改变ip地址就可以切换出口路由。我还是给拓扑图吧

![](https://pic3.zhimg.com/v2-43744162c31a780fb88ab90274813148_1440w.jpg)

这个是一个很糟糕的设计，如果不是有vlan的话，他一定会产生环路导致路由器故障。还好划分了vla

那么该怎么优化才能实现当我关闭了lan4依然可以用123.0网段访问121.0网段呢？

首先我没事不会去关闭lan4的上网功能。但是我还是改一下route吧

## 主路由：

![](https://pic3.zhimg.com/v2-c6bc2658fd75f1b9a00cc79fd0658742_1440w.jpg)

上图：删除原有路由

下图：增加一条到192.168.121.20的路由

![](https://pica.zhimg.com/v2-570a61394c119ac11ec62d4b9b8831be_1440w.jpg)

  

下图：新增一条到192.168.121.0/24的路由，由于192.168.121.1已经有明确的路由了，所以这条路由不会走192.168.121.1的数据。

![](https://picx.zhimg.com/v2-45b045c8f544dfd7247684edb25567e9_1440w.jpg)

那么接下来就是该对openwrt的路由进行修改了

![](https://pic4.zhimg.com/v2-05f928dcc4d4b8a3a34c7db0b909f82b_1440w.jpg)

可以看到如果要去123.0他将会从eth0.3出去，那我们加一条从br-lan出去的就行了

![](https://pic3.zhimg.com/v2-38c5137c531deb83aaee8db691e846a4_1440w.jpg)

这样就算删除了lan4，也不会让123.0与121.0的通信中断，当然，openwrt就不能通过123.20实现上网。

![](https://pic3.zhimg.com/v2-aa9835e56e0226dd4dbce019be226ef4_1440w.jpg)

  

我可能哪里出错了吧

![](https://pic3.zhimg.com/v2-6da7ea0812f08a86e547b6e209a70700_1440w.jpg)

先删了192.168.123.20的路由吧

![](https://pica.zhimg.com/v2-f3f216a3ab4343e9f55a85e104f0ac66_1440w.jpg)

![](https://picx.zhimg.com/v2-c94c7c8a6516851d14b9922598b4099d_1440w.jpg)

## 那我就不钻牛角尖了，本期主要是对mwan3进行演示，我们继续。

事实上，如果你打算在lan4上面开启dhcp，最大可能你会获取到192.168.121.1分配的dhcp，这个是随机的，主要看是主路由给ip快，还是openwrt给ip快，为了避免这个问题的产生，所以我使用静态ip

![](https://pic1.zhimg.com/v2-662387d1187fd495ee00447fbae5f082_1440w.jpg)

![](https://pic3.zhimg.com/v2-1c54596d5121cb1ac0ea8424b8bff9d4_1440w.jpg)

看上去对接口的配置已经完成了，如果你这样想，不妨我们假设一下，对于路由器来说，是走WAN还是WAN1呢？你认为在不配置[MWAN3](https://zhida.zhihu.com/search?content_id=166374201&content_type=Article&match_order=1&q=MWAN3&zhida_source=entity)的情况下应该怎么走流量？

答案是，谁最后配置出接口，就走谁，也就是走最后一个可以访问外部网站的路由，如果你按上图配置会得到这样的route信息

![](https://pic3.zhimg.com/v2-96df96271adf4d112266ae00a261fbd4_1440w.jpg)

metric也就是跃点数那一行是空的，于是他选择走pppoe的那条线路

![](https://picx.zhimg.com/v2-7e95302b253aee75a12ef93513815959_1440w.jpg)

**那接下来如果我让lan4后拨号呢**

![](https://pic4.zhimg.com/v2-286d9f250f284b5a92c40536b4758a0d_1440w.jpg)

**首排位置已经发生改变了，那么[traceroute](https://zhida.zhihu.com/search?content_id=166374201&content_type=Article&match_order=1&q=traceroute&zhida_source=entity)看看呢**

![](https://pic3.zhimg.com/v2-4609d2cfcb99b1b3f9e06b933274f7b4_1440w.jpg)

**那要怎么做才能决定主次呢？**

**只需要设置跃点数就行了，跃点数的意思是距离主机的距离，距离越近代表越优先。**

![](https://pic2.zhimg.com/v2-592f4213bb284a6562f0d35cef4996cd_1440w.jpg)

改变跃点数就行了，值随意，建议1-100之间，该值不允许重复，否则就没有意义了。

![](https://pica.zhimg.com/v2-0631b2785bf137c1ab1af19036a7bc2e_1440w.jpg)

改好了，优先走跃点数是39的pppoe，后走跃点数是40的静态，由于没有配置MWAN3所以，他永远会走跃点数是39的pppoe，接下来该配置MWAN3了。

![](https://picx.zhimg.com/v2-05c4f559f0ebee02c2ef6919d79efa0b_1440w.jpg)

## 首先添加接口

![](https://picx.zhimg.com/v2-a270e9469443667c2b772377211796df_1440w.jpg)

## 注意区分大小写。

![](https://pic2.zhimg.com/v2-8650ff3864d5858e2b1abdecc404980d_1440w.jpg)

![](https://pica.zhimg.com/v2-d6c2320ca3c403499a538410eb294594_1440w.jpg)

![](https://pic1.zhimg.com/v2-561bdb029cc330c4ee8824faa9f96144_1440w.jpg)

**配置好了，建议每一步都点一下保存并应用，防止bug对负载均衡的影响。**

**进入策略配置，该配置决定了每条路由承载什么量的流量**

![](https://picx.zhimg.com/v2-2af5fc9c91517241ec56c88251364a8d_1440w.jpg)

名称配置方法是网口名称（比如我的是WAN与WAN1）加上"\_m1*\_*w3",最好这样配置，免得出问题哈。

之所以没有大写是因为这个允许在配置里面绑定接口，所以是否大写，不太重要了。

![](https://pic1.zhimg.com/v2-3da8b0241ff2dc44ef9085e16dd12ca8_1440w.jpg)

![](https://pic4.zhimg.com/v2-f2d37b4dd06de6d1e08cdbc27232f4d5_1440w.jpg)

![](https://pic3.zhimg.com/v2-21e86aed7261fb27f5fbfd49238698b4_1440w.jpg)

记得保存

由于后面的规则叫这个名称，所以就不要改了

![](https://picx.zhimg.com/v2-005598d88534f77eb0bc25ddd500d98b_1440w.jpg)

![](https://picx.zhimg.com/v2-a1c248e8cbdded0eefe4f851200d7207_1440w.jpg)

加进去就可以了。

最后一项可以不动他

![](https://picx.zhimg.com/v2-65853702005f4bb5c27917223497d357_1440w.jpg)

意思解释https的tcp中端口号是443的流量走哪个策略，防止出现异常情况

default\_rule中的意思就是任何ip中的任何端口的任何协议的流量都走balanced这个策略。由于本次是负载均衡，就不动他了，你要是打算改的话，自己看一下就行了。

![](https://pic1.zhimg.com/v2-8ce7e7dbcc8f8c0f021fc0a321486280_1440w.jpg)

最后就是这个，其他的博主都没说，也不知道是不是不重要，但是建议在本地源端口这边选择lan，意思就是lan的流量才进行负载均衡，估计可以除了lan外还有其他的特殊路径可以上网，比如科学什么的，所以才需要在这里选择lan接口。

于是配置就完成了，这样的配置就可以实现负载均衡了，试试吧，不懂在问我。