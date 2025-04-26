可以使用 Termux 开启 ssh 服务。第一次使用时需要安装 openssh 工具

> [!error] 无法弹出输入法
> 这个问题多出现在平板上，输入法不支持，可使用 Microsoft Swiftkey 键盘

```sh
pkg install openssh
```

查看用户名、密码、IP 等信息

```sh
whoami   # 用户名
passwd   # 设置密码
ifconfig # 连接 ip
```

然后使用 sshd 启动服务

```sh
sshd
```

> [!error] "libcrypto.so.3" not found
> 表现为以下两点：
> - 执行 `passwd` 时提示：CANNOT LINK EXECUTABLE "passwd": library "libcrypto.so.3" not found: needed by /data/data/com.termux/files/usr/lib/libtermux-auth.so in namespace (default)
> - 执行 `sshd` 时提示：CANNOT LINK EXECUTABLE "sshd": library "libcrypto.so.3" not found: needed by main executable
> 
> 没有安装 `openssl`
> ```sh
> pkg install openssl
> ```

> [!error] sshd: no hostkeys available -- exiting.
> 使用 `ssh-keygen` 生成一个密钥即可
> ```sh
> ssh-keygen -A
> ```

关闭时直接 kill 掉 ssh 的进程即可。
# 参考

```cardlink
url: https://termux.dev/cn/
title: "Termux"
description: "The main termux site and help pages."
host: termux.dev
```

```cardlink
url: https://lixiaopeng.top/article/110
title: "Android上使用termux开启ssh,并在pc端连接-Adamin90"
description: "Android上使用termux开启ssh,并在pc端连接需求背景看同事在公司的android设备上装termux想搞远程adb，周末自己在家也想着玩玩。首先在手机上安装了termux，接下来需要在手机上敲一堆命令有点麻烦，所以想着在手机上开启ssh,就可以通过局域网内的pc连接手机的ssh在电脑上敲命令了。termux可以在https://f-droid.org/zh_Hans/packages"
host: lixiaopeng.top
```

```cardlink
url: https://wenku.csdn.net/answer/cbc4e83e090e4cb4a9cfd8fac9566a51
title: "linux+关闭sshd进程 - CSDN文库"
description: "文章浏览阅读542次。要关闭sshd进程，可以使用以下命令：1. 查看sshd进程的PID：```ps -ef | grep sshd```2. 杀掉sshd进程："
host: wenku.csdn.net
```
