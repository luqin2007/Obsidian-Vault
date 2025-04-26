1. 检查安装并启用 `NetworkManager` 服务

```shell
systemctl status NetworkManager
```

2. 安装 `network-manager-applet` 包并重启

```shell
pacman -S network-manager-applet
```

> [!tip] Arch 可以在安装盘 arch-chroot 安装 NetworkManager

> [!tip] 在安装完 NetworkManager 后，可以通过 `nmcli device wifi connect <SSID> password <PASSWORD>` 联网
