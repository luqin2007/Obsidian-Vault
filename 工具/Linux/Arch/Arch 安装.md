# 配置 `live` 环境

1. 配置键盘布局：默认即 `us`，不需要变更
2. 联网，WiFi 使用 `iwctl`，进入 `iwd` 交互环境
	1. 列出设备名：`device list`，记下无线网卡名如 `wlan0`，下面用 `<dev>` 代替
	2. 扫描网络：`station <dev> scan`
	3. 列出扫描结果：`station <dev> get-networks`
	4. 连接：`station <dev> connect <SSID>`，若有密码后面会要求输入
	5. 断开：`station <dev> disconnect`
3. 确保时间同步开启：`timedatectl`

# 调整硬盘分区

- 使用 `UEFI` 引导模式，故必须有一个 EFI 分区，通常不小于 100MB
	- 最小 2MB
	- 若要包含 `/boot`，至少 400MB
	- 若存在 `Windows` 构成双系统，至少 300MB
- 必须有一个 `/` 根分区，至少 23G
- 推荐有一个 `swap` 交换分区，若存在则至少 4G

> [!note] `cfdisk` 有一个图形界面

使用 `fdisk -l` 查看所有分区和磁盘，使用 `fdisk /dev/<disk>` 管理磁盘的分区
- `n`：创建分区，EFI 分区可以在 2048 扇区，但一定是第一个分区
- `t`：分区类型，EFI 为 `EFI System`，`swap` 为 `Linux swap`，`/` 为 `Linux root x86-64`
- `r`：删除分区
- `w`：保存并退出

分区创建完成后需要格式化
- `EFI` 分区：使用 `mkfs.fat -F 32 /dev/<分区>`
- 交换空间：不需要格式化，使用 `mkswap /dev/<分区>` 初始化
- 其他：使用 `mkfs.ext4` 或其他类型

分区挂载：
- `/` 挂载到 `/mnt`
- `EFI` 分区挂载到 `/mnt/efi`
- `swap` 分区使用 `swapon` 启用

```shell
mount /dev/<根目录分区> /mnt
mount --mkdir /dev/<EFI 分区> /mnt/efi
swapon /dev/<swap 分区>
```

# 安装系统

为提高效率，最好切换到国内源

```cardlink
url: https://help.mirrors.cernet.edu.cn/archlinux/
title: "Arch Linux 软件仓库镜像使用帮助 - MirrorZ Help"
description: "Arch Linux 软件仓库镜像使用帮助 - 镜像使用帮助 - MirrorZ Help 致力于成为一个开源、开放、且持续更新的开源软件镜像的帮助文档整合站点，旨在帮助高校间推广开源软件的使用。"
host: help.mirrors.cernet.edu.cn
favicon: https://help.mirrors.cernet.edu.cn/favicon/favicon.svg
image: https://help.mirrors.cernet.edu.cn/og-help.mirrors.cernet.edu.cn/default.png
```

安装必要软件包：`pacstrap -K /mnt 软件包...`
- 基础软件包与内核：`base`，`linux`，`linux-firmware`
- CPU 微码：Intel 为 `intel-ucode`，AMD 为 `amd-ucode`
- 网络管理器：`networkmanager`，`netctl` 等，不要同时安装多个
- 文档：`nano`
- 帮助：`man-db`，`man-pages`，`texinfo`

配置系统

- `fstab` 分区

```shell
genfstab -U /mnt >> /mnt/etc/fstab
```

- 配置新系统

```shell
arch-chroot /mnt
# 时区
ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
hwclock --systohc
# 本地化，推荐只使用英文
# /etc/locale.conf：LANG=en_US.UTF8
locale-gen
# 主机名
/etc/hostname
# root 密码
passwd
# 创建一个新用户
useradd -m <用户名>
passwd <用户名>
```

# 安装引导程序

选择使用 grub 作为引导程序

```shell
# 安装工具
pacman -S grub efibootmgr
# 安装 grub
grub-install --target=x86_64-efi --efi-directory=/efi --bootloader-id=GRUB
grub-mkconfig -o /boot/grub/grub.cfg
```

使用 `exit` 退出 `chroot` 环境，`reboot` 重启 

# 参考

```cardlink
url: https://wiki.archlinuxcn.org/wiki/%E5%AE%89%E8%A3%85%E6%8C%87%E5%8D%97
title: "安装指南"
host: wiki.archlinuxcn.org
```
