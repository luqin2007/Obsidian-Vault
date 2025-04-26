记 U 盘丢了，在仅安装有 Windows（UEFI 引导）的系统上安装 Linux 的过程，基本思路是在 Windows 下安装 grub，使用 grub 引导 Linux iso 安装
# 准备工作

1. 要求磁盘有两个空白空间段，可以通过 Windows 磁盘管理的压缩空间调整（支持调整 C 盘）
	- 一个空白 FAT32 分区，用于安装 grub 和放置 iso 镜像，将镜像复制进去
	- 一段空白空间，无分区，用于安装 Linux，我预留了大概 100G，只是玩玩 50G 就 OK
2. BIOS 准备：关闭快速启动、安全启动。教程说要调整硬盘为 AHCI 模式，实测并不需要。
3. 安装 grub2：[下载](https://ftp.gnu.org/gnu/grub/) Grub2 某个版本的 for windows 包，解压到任意位置

> [!bug] 最新的 2.12 版本无法使用的解决方法
> 下载 [Ubuntu 版本](https://ftp.sjtu.edu.cn/debian/pool/main/g/grub2/grub-efi-amd64-bin_2.12-5_amd64.deb)使用 7zip 解压，复制 `grub-efi-amd64-bin_2.12-5_amd64.deb\data.tar\.\usr\lib\grub\x86_64-efi\` 到 `grub-2.12-for-windows\x86_64-efi\` 覆盖同名目录

切换到 grub for windows 的目录，执行以下命令，其中 `<drive>` 替换为 FAT32 盘符，安装成功后根目录会产生 EFI 和 grub 两个目录：

```sh
grub-install.exe --target=x86_64-efi --efi-directory=<drive>: --boot-directory=<drive>:
```

4. 记录必要内容
	- FAT32 分区的卷标，可以帮我们辅助查找分区
	- Linux iso 文件名 `<iso-file>`
	- Linux 启动文件，可能有以下两种情况：
		- 存在 `vmlinuz` 文件：位于 `boot` 目录下的 `vmlinuz-x86_64`
			- 寻找并记录 `vmlinuz` 文件路径
			- 寻找并记录 `initramfs-x86_64.img` 文件路径，通常位于 `boot` 目录下
			- 寻找并记录 `intel_ucode.img` 文件路径，通常位于 `boot` 目录下
			- 挂载 iso 文件，记下卷标
		- 存在 `vmlinuz` 文件：位于 `casper` 目录下的 `vmlinuz` 或 `vmlinuz.efi` 等
			- 寻找并记录 `vmlinuz` 文件路径
			- 寻找并记录 `initrd` 文件路径，通常位于 `casper` 目录的 `initrd` 或 `initrd.lz`

![[../../_resources/images/Pasted image 20250203130008.png]]
# 安装

重启选择启动项为 grub，进入 grub 命令行

查找 iso 所在分区，使用 `ls` 命令可以查看所有分区，通常由 `hd` 和 `gpt` 两部分组成，hd 为硬盘编号，gpt 为分区编号，记下分区（如 `(hd1,gpt4)`）

![[../../_resources/images/Pasted image 20250203130115.png]]

> [!note] 在 Windows 中可以通过 `diskpart` 查询，两个可能不同，以 grub 的为准。
> ![[../../_resources/images/Pasted image 20250203131219.png]] 

加载驱动，根据 iso 内文件的两种情况，使用不同方式引导

> [!note] 基本过程相似，`linux` 和 `initrd` 有点不同
> 1. 加载 NTFS 和 ISO 驱动（1,2 行 `insmod` 命令）
> 2. 设置 iso 文件路径（`isofile` 变量）
> 3. 设置 grub 目录（第 4 行）
> 4. 配置驱动（第 5 行）
> 5. 设置回环设备，挂载 iso（第 6 行）
> 6. 设置 Linux 内核（第 7 行）
> 7. 设置引导路径（第 8 行）
> 8. 引导启动

- 第一种情况（`boot` 目录的） 

```sh
insmod ntfs
insmod iso9660
set isofile="/<iso文件名>"
search --no-floppy -f --set=root $isofile
probe -u $root --set=pro
loopback loop <分区>$isofile
linux (loop)/<vmlinuz文件> misolabel=<光盘卷标> img_dev=/dev/disk/by-uuid/$pro img_loop=$isofile driver=nonfree
initrd <intel_ucode文件> <initramfs>文件
boot
```

- 第二种情况（`casper` 目录的） 

```sh
insmod ntfs
insmod iso9660
set isofile="/<iso文件名>"
search --no-floppy -f --set=root $isofile
probe -u $root --set=pro
loopback loop <分区>$isofile
linux (loop)/<vmlinuz文件> iso-scan/filename=$isofile quiet splash
initrd (loop)/<initrd文件>
boot
```

![[../../_resources/images/Pasted image 20250203131001.png]]

之后就是正常的光盘安装流程了。
# 参考

```cardlink
url: https://www.bilibili.com/opus/786257612725289076
title: "无U盘无额外软件使用Grub2引导挂载硬盘安装Manjaro等Linux双系统 - 哔哩哔哩"
host: www.bilibili.com
```

```cardlink
url: http://bbs.wuyou.net/forum.php?mod=viewthread&tid=438916
title: "Windows定制Grub2 (2.12版本) 出问题 - GRUB2 - 无忧启动论坛 - Powered by Discuz!"
description: "Windows定制Grub2 (2.12版本) 出问题"
host: bbs.wuyou.net
```

```cardlink
url: https://linux.cn/article-6424-1.html
title: "技术|如何使用 GRUB 2 直接从硬盘运行 ISO 文件"
description: "大多数 Linux 发行版都会提供一个可以从 USB 启动的 live 环境，以便用户无需安装即可测试系统。我们可以用它来评测这个发行版或仅仅是当成一个一次性系统，并且很容易将这些文件复制到一个 U 盘上，在某些情况下，我们可能需要经常运行同一个或不同的 ISO 镜像。GRUB 2 可以配置成直接从启动菜单运行一个 live 环境，而不需要烧录这些 ISO 到硬盘或 USB 设备。 获取和检查可启动的 ISO 镜像 为了获取 ISO 镜像，我们通常应该访问所需的发行版的网站下载与我们架构兼容的镜像文件。如果这个镜像可以从 U 盘启动，那它也应该可以从 GRUB 菜"
host: linux.cn
```
