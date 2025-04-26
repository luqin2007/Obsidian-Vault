# systemctl

Linux 中开机自启的软件一般称为服务，可通过 `systemctl` 管理

```shell
systemctl start|stop|status|enable|disable 服务名
```

* start：启动
* stop：关闭
* status：状态查询
* enable：开启开机自启
* disable：关闭开机自启

主要的系统内置服务包括：
* NetworkManager：主网络服务
* network：副网络服务
* firewalld：防火墙服务
* sshd：ssh 服务
## target

```shell
systemctl get-default
systemctl set-default [target]
```

| level | target            |             |
| ----- | ----------------- | ----------- |
| 0     |                   | 关机          |
| 1     |                   | 单用户         |
| 2     |                   | 字符多用户，无网络   |
| 3     | mutli-user.target | 字符多用户       |
| 4     |                   | 字符多用户，unset |
| 5     | graphical.target  | 图形化多用户      |
| 6     |                   | 重启          |

## 添加服务

所有 systemctl 服务都位于 `/etc/systemd/system/` 中

最简单的服务只包含以下几行：

```properties 服务名.service
[Service]
Type=simple/forking
ExecStart=启动命令行
ExecStop=停止命令行
ExecReload=重启命令行
```

（较）完整的属性如下：

```properties title:服务名.service
[Unit]
Description=服务描述，可选
Requires=依赖，可以有多项，可选

[Service]
Type=服务类型，simple/forking(后台服务)
User=执行用户，可选
Group=用户组，可选
Environment=环境变量，可以有多项，可选
ExecStart=启动命令行
ExecReload=重启命令行
ExecStop=停止命令行
Restart=on-failure 表示失败自动重启，可选
RestartSec=等待时间，如 500ms，可选
PrivateTmp=true/false 表示是否给服务分配独立的临时空间，可选

[Install]
WantedBy=multi-user.target 表示允许多用户
```
# 参考

```cardlink
url: https://man.archlinux.org/man/systemctl.1
title: "systemctl(1) — Arch manual pages"
host: man.archlinux.org
favicon: https://man.archlinux.org/static/archlinux-common/favicon.ico
```

```cardlink
url: https://www.cnblogs.com/keystone/p/13158117.html
title: "systemctl添加自定义系统服务 - 菜鸟++ - 博客园"
description: "[Service] Type=forking ExecStart=绝对路径 ExecStop=绝对路径 ExecReload=绝对路径 以上最精简版,文件/usr/lib/systemd/system/服务.service 原理: CentOS7自定义系统服务 CentOS7的服务systemctl"
host: www.cnblogs.com
favicon: https://assets.cnblogs.com/favicon_v3_2.ico
```
