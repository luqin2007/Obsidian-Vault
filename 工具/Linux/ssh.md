---
title: ssh, scp
---
1. 确保 ssh 服务启用，没有则需要安装 `openssh-server` 包

```sh
sudo apt install openssh-server
systemctl status ssh
```

2. 配置防火墙

```sh
sudo ufw allow ssh
```

3. 配置密钥登录：将本地计算机公钥文件 `id_rsa.pub` 复制到服务器的 `/.ssh/authorized_keys` 中，可以直接使用 `ssh-copy-id`

```sh
# 确保本地已生成密钥，可通过 ssh-keygen -t rsa 生成
ssh-copy-id 远程用户名@ip
```

> [!fail] Access denied
> 编辑 `/etc/ssh/sshd_config` 文件
> ```
> UsePAM yes
> PasswordAuthentication yes
> ```
> 
> 保存后，重启 ssh 服务
> 
> ```sh
> sudo systemctl restart ssh
> ```
# X11-forwarding

该配置用于通过 ssh 启动窗口应用。`/etc/ssh/sshd_config` 文件中开启 `X11Forwarding` 配置并重启 ssh 服务

![[../../../_resources/images/Pasted image 20250207141550.png]]

客户端 ssh 连接也需要开启 `X11-forwarding` 支持，最好也开启压缩，`MobaXterm` 默认开启（XShell 等都支持）

![[../../../_resources/images/Pasted image 20250207141918.png]]

之后可通过执行 `xclock` 测试，正常应该弹出一个时钟窗口

![[../../../_resources/images/Pasted image 20250207141702.png]]
# scp

使用 ssh 上传、下载文件或目录

```sh
scp -r 目标文件 目标路径
```

本地路径直接写，远程路径使用 `用户名@ip:路径` 的形式
