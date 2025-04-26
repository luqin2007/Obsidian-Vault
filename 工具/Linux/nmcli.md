---
title: 网络管理之命令行工具nmcli
source: https://www.cnblogs.com/hokori/p/14265509.html
author:
  - "[[hokori]]"
published: 2021-01-12T09:54:00.0000000&#x2B;08:00
created: 2025-01-30
description: 参考Ubuntu官方文档和Red Hat，本文采用Google翻译。 NETWORKMANAGER 简介 介绍 NetworkManager 提供的默认联网服务是一个动态网络控制和配置守护进程，它尝试在其可用时保持网络设备和连接处于活动状态。仍支持传统 ifcfg 类型配置文件。详情请查看 第 1.
tags:
  - clippings
---
> 参考[Ubuntu官方文档](https://core.docs.ubuntu.com/en/stacks/network/network-manager/docs/index)和[Red Hat](https://access.redhat.com/documentation/zh-cn/red_hat_enterprise_linux/7/html/networking_guide/sec-using_the_networkmanager_command_line_tool_nmcli)，本文采用`Google翻译`。

## ***1***|***0*****NETWORKMANAGER 简介**

### ***1***|***1*****介绍**

`NetworkManager` 提供的默认联网服务是一个动态网络控制和配置守护进程，它尝试在其可用时保持网络设备和连接处于活动状态。仍支持传统 `ifcfg` 类型配置文件。详情请查看 [第 1.8 节 “NetworkManager 及网络脚本”](https://access.redhat.com/documentation/zh-cn/red_hat_enterprise_linux/7/html/networking_guide/sec-NetworkManager_and_the_Network_Scripts)。

**表 1.1. 联网工具及应用程序概述**

| 应用程序或工具 | 描述 |
| --- | --- |
| **NetworkManager** | 默认联网守护进程 |
| **nmtui** | **NetworkManager** 的使用光标的简单文本用户界面（TUI） |
| **nmcli** | 允许用户及脚本与 **NetworkManager** 互动的命令行工具 |
| **control-center** | GNOME Shell 提供的图形用户界面工具 |
| **nm-connection-editor** | 这是一个 GTK+ 3 应用程序，可用于尚未由 **control-center** 处理的某些任务的。 |

`NetworkManager` 可用于以下连接类型：以太网、VLAN、网桥、绑定、成组、Wi-Fi、移动宽带（比如移动网络 3G）及 IP-over-InfiniBand。在这些连接类型中，`NetworkManager` 可配置网络别名、`IP` 地址、静态路由器、`DNS` 信息及 VPN 连接以及很多具体连接参数。最后，`NetworkManager` 通过 D-bus 提供 API，D-Bus 允许应用程序查询并控制网络配置及状态。

## ***2***|***0*****安装 NETWORKMANAGER**

默认在 ubuntu 中安装 **NetworkManager**。必要时可作为 `root` 用户运行以下命令：

```
sudo apt-get install network-manager
```

### ***2***|***1*****NetworkManager 守护进程**

安装成功完成后，NetworkManager服务将在后台运行。您可以通过以下方式查看其当前状态：

```
{19:30}~/Documents/proj ➭ $ systemctl status network-manager.service 
● NetworkManager.service - Network Manager
   Loaded: loaded (/lib/systemd/system/NetworkManager.service; enabled; vendor preset: enabled)
   Active: active (running) since Fri 2021-01-08 15:31:11 CST; 4h 0min ago
     Docs: man:NetworkManager(8)
 Main PID: 915 (NetworkManager)
    Tasks: 4 (limit: 4915)
   CGroup: /system.slice/NetworkManager.service
           ├─ 915 /usr/sbin/NetworkManager --no-daemon
           └─3671 /sbin/dhclient -d -q -sf /usr/lib/NetworkManager/nm-dhcp-helper -pf /run/dhclient
```

现在，您已经成功安装了NetworkManager。

## ***3***|***0*****探索网络状态**

本节说明如何使用nmcli命令行工具检查NetworkManager的连接和设备的状态。

### ***3***|***1*****显示NetworkManager已知设备的状态：**

```
$ nmcli d  
```  

### ***3***|***2*****显示有关此选项的更多信息：**

```
nmcli d --help
```

### ***3***|***3*****显示每个NetworkManager连接的当前状态：**

```
$ nmcli c
```

命令“ c”用于连接，但是实际命令“ connections”的缩写形式。对于devices命令，“-help”显示有关此选项的更多信息。最后，我们可以通过以下方式查看无线接口的状态，包括WiFi和WWAN（蜂窝）：

```
$ nmcli r 
WIFI-HW  WIFI     WWAN-HW  WWAN    
enabled  enabled  enabled  enabled
```

确保启用WiFi / WWAN无线电很重要，这样各个连接类型才能建立连接（我们将在以下部分中指定如何进行连接）。与其他命令一样，“-help”显示用法信息。

观察NetworkManage活动（连接状态，设备或连接属性的更改）：

```
$ nmcli monitor
```

请参阅nmcli连接监视器和nmcli设备监视器，以监视某些连接或设备中的更改。

## ***4***|***0*****配置WiFi连接**

本节说明如何建立WiFi连接。它涵盖了创建和修改连接以及直接连接。

### ***4***|***1*****建立无线连接**

本节将说明如何建立与无线网络的wifi连接。请注意，直接连接将隐式创建一个连接（可以通过“ nmcli c”看到）。这样的命名将遵循“ SSID N”模式，其中N是数字。

首先，确定WiFi接口的名称：

```
$ nmcli d
DEVICE                             TYPE              STATE   		    CONNECTION      
wlo1                                  wifi                已连接  		       TP-LINK_5G_1E0C 
20:F4:78:0F:A1:2C        bt                   已断开              --              
enp2s0f1                         ethernet      不可用              --              
lo                                        loopback    未托管              --   
```   

确保WiFi无线电已打开（这是其默认状态）：

```
$ nmcli r wifi on
```

然后，列出可用的WiFi网络：

```
$ nmcli d wifi list
IN-USE  		SSID             						  MODE  		 CHAN  		RATE        		SIGNAL  	BARS  		 SECURITY  
*       				TP-LINK_5G_1E0C  		Infra  				161   		  270 Mbit/s  	 73      			▂▄▆_  	WPA1 WPA2 
``` 

例如，要连接到接入点`my_wifi`，可以使用以下命令：

```
$ nmcli d wifi connect my_wifi password <password>
```

`<password>`是用于连接的密码，需要使用8-63个字符或64个十六进制字符来指定完整的256位密钥。

### ***4***|***2*****连接到隐藏的网络**

隐藏网络是普通的无线网络，除非请求，否则根本不会广播其SSID。这意味着无法搜索其名称，并且必须从其他来源知道它的名称。

发出以下命令来创建与隐藏网络`<ssid>`关联的连接：

```
$ nmcli c add type wifi con-name <name> ifname wlan0 ssid <ssid>
$ nmcli c modify <name> wifi-sec.key-mgmt wpa-psk wifi-sec.psk <password>
```

现在，您可以通过键入以下内容建立连接：

```
$ nmcli c up <name>
```

`<name>`是为连接指定的任意名称，而`<password>`是网络的密码。为了指定完整的256位密钥，它必须具有8-63个字符或64个十六进制字符。

### ***4***|***3*****更多的信息**

您可以在以下页面上找到更多信息和更详细的示例：

- [https://developer.gnome.org/NetworkManager/unstable/nmcli.html](https://developer.gnome.org/NetworkManager/unstable/nmcli.html)

## ***5***|***0*****配置WiFi接入点**

可以使用网络管理器快照创建WiFi接入点。这可以通过运行来完成

```
$ nmcli d wifi hotspot ifname <wifi_iface> ssid <ssid> password <password>
```

哪里`<wifi_iface>`是wifi网络接口，`<ssid>`是我们正在创建的AP的SSID，连接到该设备的设备可以看到它，并且`<password>`是访问密码（该密码必须介于8-63个字符或64个十六进制字符之间）。如果命令成功，NM将创建一个名为`Hotspot <N>`的连接。

默认情况下，创建的AP提供一个共享连接，因此，如果提供AP的设备也可以访问，则连接到它的设备应该能够访问Internet。

## ***6***|***0*****NMCLI常用的一些命令**

用户和脚本都可使用命令行工具 **nmcli** 控制 **NetworkManager**。该命令的基本格式为：

```
nmcli OPTIONS OBJECT { COMMAND | help }
```

其中 OBJECT 可为 `general`、`networking`、`radio`、`connection` 或 `device` 之一。最常用的选项为：`-t, --terse`（用于脚本）、`-p, --pretty` 选项（用于用户）及 `-h, --help` 选项。在 **nmcli** 中采用命令完成功能，无论何时您不确定可用的命令选项时，都可以按 **Tab** 查看。有关选项及命令的完整列表，请查看 `nmcli(1)` man page。

**nmcli** 工具有一些内置上下文相关的帮助信息。例如：运行以下两个命令，并注意不同之处：

```
$ nmcli help
Usage: nmcli [OPTIONS] OBJECT { COMMAND | help }

OPTIONS
  -t[erse]                                       terse output
  -p[retty]                                      pretty output
  -m[ode] tabular|multiline                      output mode
  -c[olors] auto|yes|no                          whether to use colors in output
  -f[ields] <field1,field2,...>|all|common       specify fields to output
  -g[et-values] <field1,field2,...>|all|common   shortcut for -m tabular -t -f
  -e[scape] yes|no                               escape columns separators in values
  -a[sk]                                         ask for missing parameters
  -s[how-secrets]                                allow displaying passwords
  -w[ait] <seconds>                              set timeout waiting for finishing operations
  -v[ersion]                                     show program version
  -h[elp]                                        print this help

OBJECT
  g[eneral]       NetworkManager's general status and operations
  n[etworking]    overall networking control
  r[adio]         NetworkManager radio switches
  c[onnection]    NetworkManager's connections
  d[evice]        devices managed by NetworkManager
  a[gent]         NetworkManager secret agent or polkit agent
  m[onitor]       monitor NetworkManager changes
```
```
$ nmcli general help
用法：nmcli general { 命令 | help }

命令 := { status | hostname | permissions | logging }

  status

  hostname [<主机名>]

  permissions

  logging [level <日志级别>] [domains <日志域>]
```

在上面的第二个示例中，这个帮助信息与对象 `general` 有关。

`nmcli-examples(5)` man page 有很多有帮助的示例，节选如下：

### ***6***|***1*****基本示例**

显示 **NetworkManager** 总体状态：

```
nmcli general status
```

要控制 **NetworkManager** 日志记录：

```
nmcli general logging
```

要显示所有链接：

```
nmcli connection show
```

要只显示当前活动链接，如下所示添加 `-a, --active`：

```
nmcli connection show --active
```

显示由 **NetworkManager** 识别到设备及其状态：

```
nmcli device status
```

可简化命令并省略一些选项。例如：可将命令

```
nmcli connection modify id 'MyCafe' 802-11-wireless.mtu 1350
```

简化为

```
nmcli con mod MyCafe 802-11-wireless.mtu 1350
```

可省略 `id` 选项，因为在这种情况下对于 **nmcli** 来说连接 ID（名称）是明确的。您熟悉这些命令后可做进一步简化。例如：可将

```
nmcli connection add type ethernet
```

改为

```
nmcli c a type eth
```

> **注意**
> 
> 如有疑问，请使用 tab 完成功能。

### ***6***|***2*****使用 nmcli 启动和停止接口**

可使用 **nmcli** 工具启动和停止任意网络接口，其中包括主接口。例如：

```
nmcli connection down connection-name
nmcli connection up connection-name
nmcli device disconnect interface-name
nmcli device connect interface-name
```

> **注意**
> 
> 建议使用 `nmcli dev disconnect iface *iface-name*` 命令，而不是 `nmcli con down id *id-string*` 命令，因为连接断开可将该接口放到“手动”模式，这样做用户让 **NetworkManager** 启动某个连接前，或发生外部事件（比如载波变化、休眠或睡眠）前，不会启动任何自动连接。

### ***6***|***3*****nmcli 互动连接编辑器**

**nmcli** 工具有一个互动连接编辑器。请运行以下命令使用该工具：

```
nmcli con edit
```

此时会提示您从显示的列表中选择有效连接类型。输入连接类型后，就会为您显示 **nmcli** 提示符。如果您熟悉连接类型，也可以在 `nmcli con edit` 命令中添加 `type` 选项，从而直接进入提示符。编辑现有连接配置的格式如下：

```
nmcli con edit [id | uuid | path] ID
```

要添加和编辑新连接配置，请采用以下格式：

```
nmcli con edit [type new-connection-type] [con-name new-connection-name]
```

在 **nmcli** 提示符后输入 `help` 查看可用命令列表。请使用 `describe` 命令获取设置及其属性描述，格式如下：

```
describe setting.property
```

例如：

```
nmcli> describe team.config
```

### ***6***|***4*****了解 nmcli 选项**

很多 **nmcli** 命令是可以顾名思义的，但有几个选项需要进一步了解：

`type` — 连接类型。

允许值为：`adsl`, `bond`, `bond-slave`, `bridge`, `bridge-slave`, `bluetooth`, `cdma`, `ethernet`, `gsm`, `infiniband`, `olpc-mesh`, `team`, `team-slave`, `vlan`, `wifi`, `wimax`.

每个连接了类型都有具体类型的命令选项。按 **Tab** 键查看该列表，或查看 `nmcli(1)` man page 中的 `TYPE_SPECIFIC_OPTIONS` 列表。`type` 选项可用于如下命令：`nmcli connection add` 和 `nmcli connection edit`。

`con-name` — 为连接配置分配的名称。

如果未指定连接名称，则会以如下格式生成名称：

 ```
 type-ifname[-number]
```

连接名称是 *connection profile* 的名称，不应与代表某个设备的名称混淆（比如 wlan0、ens3、em1 等等）。虽然用户可为根据接口为链接命名，但这是两回事。一个设备可以有多个连接配置文件。这对移动设备，或者在不同设备间反复切换网线时很有帮助。与其编辑该配置，不如创建不同的配置文件，并根据需要将其应用到接口中。`id` 选项也是指连接配置文件名称。

`id` — 用户为连接配置文件分配的身份字符串。

可在 `nmcli connection` 命令中用来识别某个连接的 ID。输出结果中的 NAME 字段永远代表连接 ID（名称）。它指的是 `con-name` 给出的同一连接配置文件名称。

`uuid` — 系统为连接配置文件分配的独有身份字符串。

可在 `nmcli connection` 命令中用来识别某个连接的 UUID。

### ***6***|***5*****创建连接**

```
nmcli connection add type ethernet con-name connection-name ifname interface-name
nmcli connection add type ethernet con-name connection-name ifname interface-name ip4 address gw4 address

## e.g. 创建一个基于eth1接口的连接
# 创建动态连接，即BOOTPROTO默认为DHCP
[root@localhost ~]# nmcli c add type eth con-name dynamic-eth1 ifname eth1
Connection 'dynamic-eth1' (9c0ad8a9-21f6-40b5-9313-e5c7e4b356f1) successfully added.
# 创建静态连接
[root@localhost ~]# nmcli connection add type eth con-name static-eth1 ifname eth1 ip4 172.16.60.10/24
# nmcli connection add type eth con-name static-eth1 ifname eth1 ip4 172.16.60.10/24 gw4 192.168.60.1
Connection 'static-eth1' (0640bf7f-9490-44a8-be96-2e710fb650e6) successfully added.
```

> 创建连接后，NetworkManager 自动将 connection.autoconnect 设定为 yes。还会将设置保存到 /etc/sysconfig/network-scripts/ connection-name 文件中，且自动将 ONBOOT 参数设定为 yes。

### ***6***|***6*****激活连接**

```
nmcli connection up connection-name

## e.g. 激活eth1接口的static-eth1连接
[root@localhost ~]# nmcli c up static-eth1
Connection successfully activated (D-Bus active path: /org/freedesktop/NetworkManager/ActiveConnection/2)
```

### ***6***|***7*****修改连接的IP地址**

```
# 可修改的属性可通过以下命令查看
nmcli c show static-eth1
# 修改命令
nmcli connection modify [--temporary] [id | uuid | path] <ID> ([+|-]<setting>.<property> <value>)+

## e.g. 修改连接static-eth1的ip地址
[root@localhost ~]# ip addr | grep eth1
4: eth1: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP qlen 1000
    inet 172.16.60.10/24 brd 172.16.60.255 scope global eth1
[root@localhost ~]# nmcli c mod static-eth1 ipv4.addr 172.16.60.20/24
[root@localhost ~]# nmcli c up static-eth1
Connection successfully activated (D-Bus active path: /org/freedesktop/NetworkManager/ActiveConnection/3)
[root@localhost ~]# ip a | grep eth1
4: eth1: <BROADCAST,MULTICAST,UP,LOWER_UP> mtu 1500 qdisc pfifo_fast state UP qlen 1000
    inet 172.16.60.20/24 brd 172.16.60.255 scope global eth1
```

### ***6***|***8*****配置连接的DNS**

```
# 设定单个DNS
nmcli connection modify static-eth1 ipv4.dns DNS1
# 设定多个DNS
nmcli connection modify static-eth1 ipv4.dns "DNS1 DNS2"
# 以上命令会替换之前的DNS设置
# 添加某个连接的DNS，需要使用前缀“+”
nmcli connection modify static-eth1 +ipv4.dns DNS3

## e.g. 配置static-eth1连接的DNS
[root@localhost ~]# grep DNS /etc/sysconfig/network-scripts/ifcfg-static-eth1
IPV6_PEERDNS=yes
[root@localhost ~]# nmcli c mod static-eth1 ipv4.dns "114.114.114.114 223.5.5.5"
# 修改连接后，需要重新激活
[root@localhost ~]# nmcli c up static-eth1
Connection successfully activated (D-Bus active path: /org/freedesktop/NetworkManager/ActiveConnection/4)
[root@localhost ~]# grep DNS /etc/sysconfig/network-scripts/ifcfg-static-eth1
DNS1=114.114.114.114
DNS2=223.5.5.5
IPV6_PEERDNS=yes
# 新增DNS
[root@localhost ~]# nmcli c mod static-eth1 +ipv4.dns 223.5.5.6
[root@localhost ~]# nmcli c up static-eth1
Connection successfully activated (D-Bus active path: /org/freedesktop/NetworkManager/ActiveConnection/5)
[root@localhost ~]# grep DNS /etc/sysconfig/network-scripts/ifcfg-static-eth1
DNS1=114.114.114.114
DNS2=223.5.5.5
DNS3=223.5.5.6
IPV6_PEERDNS=yes
```

### ***6***|***9*****使用以下命令在Linux上创建Wi-Fi热点 nmcli**

原始帖子： `https://unix.stackexchange.com/a/310699`

```
nmcli con add type wifi ifname wlan0 con-name Hostspot autoconnect yes ssid Hostspot
nmcli con modify Hostspot 802-11-wireless.mode ap 802-11-wireless.band bg ipv4.method shared
nmcli con modify Hostspot wifi-sec.key-mgmt wpa-psk
nmcli con modify Hostspot wifi-sec.psk "veryveryhardpassword1234"
nmcli con up Hostspot
```

> **注意**
> 
> 如果重启后`nmcli con up Hotspot`不起作用
> 
> 使用此命令启动Hotspot
> 
> ```
> UUID=$(grep uuid /etc/NetworkManager/system-connections/Hotspot | cut -d= -f2)
> nmcli con up uuid $UUID
> ```

  

\_\_EOF\_\_

![](https://en.gravatar.com/userimage/116506819/11ba19ba1f423efa7d37ef49b0eb946f.png?size=200)

本文作者：**[Hokori](https://www.cnblogs.com/hokori/p/14265509.html)**  
本文链接：[https://www.cnblogs.com/hokori/p/14265509.html](https://www.cnblogs.com/hokori/p/14265509.html)  
关于博主：评论和私信会在第一时间回复。或者[直接私信](https://msg.cnblogs.com/msg/send/hokori)我。  
版权声明：本博客所有文章除特别声明外，均采用 [BY-NC-SA](https://creativecommons.org/licenses/by-nc-nd/4.0/ "BY-NC-SA") 许可协议。转载请注明出处！  
声援博主：如果您觉得文章对您有帮助，可以点击文章右下角**【[推荐](https://www.cnblogs.com/hokori/p/)】**一下。您的鼓励是博主的最大动力！