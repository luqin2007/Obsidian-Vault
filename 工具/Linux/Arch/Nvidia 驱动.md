# 安装

```shell
lspci -k | grep -A 2 -E "(VGA|3D)"
```

![[../../../_resources/images/Pasted image 20240901105857.png]]

`NVIDIA` 设备中，`Kernel driver in use` 即当前设备驱动程序，若为  `nouveau` 则需要安装驱动

| 驱动代号                        | 内核版本        | 驱动包                 | 说明                                                                                                     |
| --------------------------- | ----------- | ------------------- | ------------------------------------------------------------------------------------------------------ |
| `NV110`、`GMXXX`             | `linux`     | `nvidia`            |                                                                                                        |
| `NV110`、`GMXXX`             | `linux-lts` | `nvidia-lts`        |                                                                                                        |
| `NV110`、`GMXXX`             | 其他          | `nvidia-dkms`       | 安装过程详见 [DKMS](https://wiki.archlinuxcn.org/wiki/DKMS#%E5%AE%89%E8%A3%85)                               |
| `NV110`、`GMXXX`             | 其他          | `nvidia-beta`       | 其他驱动不可用时使用                                                                                             |
| `NV116`、`TUXXX`             | `linux`     | `nvidia-open`       |                                                                                                        |
| `NV116`、`TUXXX`             | 其他          | `nvidia-open-dkms`  | Beta 状态，可能有问题                                                                                          |
| `NVE0`、`GKXXX`              |             | `nvidia-470xx-dkms` | Linux 5.18 需要 `ibt=off` [内核参数](https://wiki.archlinuxcn.org/wiki/%E5%86%85%E6%A0%B8%E5%8F%82%E6%95%B0) |
| `NVC0`、`GF1XX`              |             | `nvidia-390xx-dkms` |                                                                                                        |
| `NV5x`、`NV8x`、`NV9x`、`NVAx` |             | `nvidia-340xx-dkms` |                                                                                                        |

使用 `pacman -S` 安装对应驱动包后，重启系统即可

# DRM 设置

Nvidia 驱动默认未开启 KMS 晚加载（AMD 驱动和 `nouveau` 默认开启），需要开启 DARM 内核显示模式

在 `/etc/modprobe.d/` 目录下创建任意 `.conf` 结尾的文件，内容为：

```conf
options nvidia_drm modeset=1
```

保存后重启，执行 `cat /sys/module/nvidia_drm/parameters/modeset` 返回 `Y` 表示生效

> [!note] 早启动
> 若需要使驱动最早加载，需要将 `nvidia`、`nvidia_modeset`、`nvidia_uvm`、`nvidia_drm` 添加到 `initramfs` 中

# Xorg 配置

使用 `nvidia-xconfig` 创建默认配置

使用 `nvidia-settings` 详细配置，无参表示打开 GUI

# 参考 

```cardlink
url: https://wiki.archlinuxcn.org/wiki/NVIDIA
title: "NVIDIA"
host: wiki.archlinuxcn.org
```

```cardlink
url: https://wiki.archlinuxcn.org/wiki/NVIDIA_Optimus
title: "NVIDIA Optimus"
host: wiki.archlinuxcn.org
```
