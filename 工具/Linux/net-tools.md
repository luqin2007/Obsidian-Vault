---
title: "net-tools: 网络相关常用工具"
---

# ifconfig

可以用来查看本机各个网卡的 IP 地址，后可接网卡名（如 `eth0` 只看某个设备的信息）

![[Pasted image 20240806165447.png]]

* 127.0.0.1：可以代表本机（localhost）
* 0.0.0.0：可以指代本机，某些限制规则中表示所有 IP，也可以用来确定端口绑定关系
# netstat

查看每个进程使用的端口和连接的详细信息

```shell
netstat -anp
```

可使用 `| grep` 过滤