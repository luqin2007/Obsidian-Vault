定时执行任务
- `crond`：`corn` 的守护进程，用于执行脚本
- `crontab`：管理定时任务的工具

# 命令格式

- `*`：取所有数字（忽略）
- `/`：每多少单位时间执行一次
- `a-b`：在第 `[a, ..., b]` 时间节点执行，共执行 `b-a+1` 次
- `a,b`：在第 `a, b` 时间节点执行，共执行 2 次

![[../../../_resources/images/Pasted image 20240917111458.jpg|50%]]

# crontab

```shell
crontab [-u <用户名>] <options>
```

- 用户名默认当前用户
- `-e`：编辑工作表
- `-l`：列出所有任务
- `-r`：删除任务

# 工具

```cardlink
url: https://cron.qqe2.com/
title: "在线Cron表达式生成器"
description: "通过这个生成器,您可以在线生成任务调度比如Quartz的Cron表达式,对Quartz Cron 表达式的可视化双向解析和生成."
host: cron.qqe2.com
```

# 参考

```cardlink
url: https://www.runoob.com/w3cnote/linux-crontab-tasks.html
title: "Linux Crontab 定时任务 | 菜鸟教程"
description: "linux内置的cron进程能帮我们实现这些需求，cron搭配shell脚本，非常复杂的指令也没有问题。 cron介绍  我们经常使用的是crontab命令是cron table的简写，它是cron的配置文件，也可以叫它作业列表，我们可以在以下文件夹内找到相关配置文件。  /var/spool/cron/ 目录下存放的是每个用户包括root的crontab任务，每个任务以创建者的名字命名 /etc/crontab 这个文件负责调度各种管.."
host: www.runoob.com
```

```cardlink
url: https://www.runoob.com/linux/linux-comm-crontab.html
title: "Linux crontab 命令 | 菜鸟教程"
description: "Linux crontab 命令  Linux 命令大全 Linux crontab 是 Linux 系统中用于设置周期性被执行的指令的命令。  当安装完成操作系统之后，默认便会启动此任务调度命令。 crond 命令每分钟会定期检查是否有要执行的工作，如果有要执行的工作便会自动执行该工作。 注意：新创建的 cron 任务，不会马上执行，至少要过 2 分钟后才可以，当然你可以重启 cron 来马上执行。 Linux 任务调度的工作主要分为.."
host: www.runoob.com
```
