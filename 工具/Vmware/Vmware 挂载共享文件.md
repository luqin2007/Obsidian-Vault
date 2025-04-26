`vmware-hgfsclient` 工具可用于列出主机所有共享的文件和目录

![[../../_resources/images/Pasted image 20241207075901.png]]

挂载目录，设挂载到 `/mnt/hgfs`，`uid` 和 `gid` 可以在 Home 目录下执行 `id` 查询

```shell
sudo vmhgfs-fuse .host:/ /mnt/hgfs -o allow_other -o uid=<uid> -o gid=<gid> -o umask=022
```

`-o` 可以设置配置，可以通过 `vmhgfs-fuse -h` 查看
- `allow_other`：允许被任意用户访问，`allow_root` 则表示仅可被 root 用户访问
- `uid`，`gid`：设置文件的所有者
- `umask`：设置文件权限掩码
- `auto_umount`：当终端关闭时，自动取消挂载

将以下内容加入 `/etc/fstab` 可在开机时加载：

```
.host:/ /mnt/hgfs fuse.vmhgfs-fuse allow_other,uid=<uid>,gid=<gid>,umask=022 0 0
```
# 参考

```cardlink
url: https://wiki.archlinuxcn.org/wiki/VMware/%E5%AE%89%E8%A3%85_Arch_Linux_%E4%B8%BA%E8%99%9A%E6%8B%9F%E6%9C%BA#%E9%80%9A%E8%BF%87_-{}-vmhgfs-fuse_%E5%85%B1%E4%BA%AB%E7%9B%AE%E5%BD%95
title: "VMware/安装 Arch Linux 为虚拟机 -  Arch Linux 中文维基"
host: wiki.archlinuxcn.org
favicon: https://wiki.archlinuxcn.org/wzh/images/logo.svg
```
