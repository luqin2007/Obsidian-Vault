计算机支持需要 VT-d，通过 U 盘进入 Provmox VE 部署
# 硬件直通

后台编辑 `/etc/default/grab`，确认 `GRUB_CMDLINE_LINUX_DEFAULT` 有 `intel_iommu=on`
# 网桥

网络 - 创建虚拟 Linux Bridge 网桥，设定 IP 不设定任何绑定，应用后，添加到后面虚拟机的系统中，配置静态 IP
# 虚拟机

1. 传送系统镜像
2. 创建虚拟机
	- 飞牛：飞牛主要是影视，120s，host，8G 内存，64G 硬盘，2 核宿主模式，添加 PCI 设备-原始设备，核显+SATA 控制器，安装到 QEMU 上
	- Ubuntu+CasaOS：跑 Docker，60s，64G 硬盘，4G 内存，安装时取消 LVM Group，安装 OpenSSH，使用 nfs 挂载飞牛的目录
	- Immortal OpenWrt：跑旁路由，宿主，系统用 other 或 Linux 都行，不用硬盘，1G 内存，使用 qm disk import 镜像 local-vm 从镜像创建硬盘