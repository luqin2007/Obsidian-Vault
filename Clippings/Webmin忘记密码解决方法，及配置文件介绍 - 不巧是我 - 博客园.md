---
title: "Webmin忘记密码解决方法，及配置文件介绍 - 不巧是我 - 博客园"
source: "https://www.cnblogs.com/cucuad/articles/10152780.html"
author:
published:
created: 2025-04-21
description: "Webmin忘记密码解决方法，及配置文件介绍 Webmin忘记密码解决方法，及配置文件介绍 Webmin忘记Web登陆时候的密码，无法登陆，可以通过基本方法是通过changepass.pl可以修改密码 首先找到changepass.pl这个文件目录 找到路径 /opt/superlink/chang"
tags:
  - "clippings"
---
## Webmin忘记密码解决方法，及配置文件介绍

Webmin忘记Web登陆时候的密码，无法登陆，可以通过基本方法是通过changepass.pl可以修改密码

首先找到changepass.pl这个文件目录

找到路径

/opt/superlink/changepass.pl /etc/superlink admin  123456

/opt/webmin/changepass.pl /etc/webmin 用户名  密码

changepass安装目录 webmin安装目录 用户名  密码

这个命令的作用是用123456替换admin用户原来的密码，如果你连**用户名也不知道了**，可以按如下方法查找：

cat /etc/webmin/miniserv.usersFRz11BO91WR.Y:0::::1545307436

webmin的配置文件：/etc/webmin/miniserv.conf，常见重要参数如下

| 参数及缺省值 | 说    明 |
| --- | --- |
| port=10000 | 设置HTTP服务器监听的端口,默认事10000。如需修改端口号，可直接在此修改，但是一般还要再修改一下防火墙就行了iptables,目录：/etc/sysconfig/iptables。然后分别重启一下webmin和iptables。  重启：/etc/init.d/webmin restart或 service webmin restart 我是直接找到webmin下的restart文件重启的。 /etc/webmin/restart  重启：service iptables restart |
| root=/usr/sfw/lib/webmin | Web文件的根目录 |
| ssl=0 | 是否支持SSL，如果设置为1，  这个HTTP服务器将提供HTTPS服务 |
| session=1 | 是否支持Session，如果设置为1，  在访问Web服务之前必须先完成用  户登录和认证，用户的登录和认证  由session\_login.cgi实现 |
| logfile=/var/webmin/miniserv.log | 日志文件 |
| errorlog=/var/webmin/miniserv.error | 错误日志文件 |
| userfile=/etc/webmin/miniserv.users | 存放HTTP服务器的用户名和密码，changepass.pl可以修改用户密码 |
| keyfile=/etc/webmin/minserv.pem | 存放HTTP服务器的私钥和公钥证书 |