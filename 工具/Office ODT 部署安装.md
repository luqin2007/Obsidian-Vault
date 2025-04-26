使用 Office Tools Plus 部署 Visio 时，提示需要先卸载 Office 才能安装

![[../_resources/images/Pasted image 20241214174956.png]]

1. 下载 Office Deployment Tool

```cardlink
url: https://www.microsoft.com/en-us/download/details.aspx?id=49117
title: "Download Office Deployment Tool from Official Microsoft Download Center"
description: "The Office Deployment Tool (ODT) is a command-line tool that you can use to download and deploy Click-to-Run versions of Office, such as Microsoft 365 Apps for enterprise, to your client computers."
host: www.microsoft.com
```

2. 去 [Offiice 自定义工具](https://config.office.com/deploymentsettings)生成对应应用版本的配置 xml
3. 修改 XML 内容，修改更新渠道为 Current

![[../_resources/images/Pasted image 20241214202029.png]]

4. 安装

```shell
setup.exe /configure <xml 配置文件>
```
# 参考

```cardlink
url: https://learn.microsoft.com/zh-cn/microsoft-365-apps/deploy/install-different-office-visio-and-project-versions-on-the-same-computer
title: "支持在同一计算机上安装不同版本的 Office、Project 和 Visio 的方案 - Microsoft 365 Apps"
description: "向 IT 管理员提供有关哪些版本的 Office、Project 和 Visio 可以一起安装在同一台计算机上的信息。"
host: learn.microsoft.com
image: https://learn.microsoft.com/en-us/media/open-graph-image.png
```
