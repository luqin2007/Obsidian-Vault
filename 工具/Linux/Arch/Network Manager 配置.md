# 安装

若系统安装过程中安装了，可以跳过

```shell
pacman -S networkmanager
```
# 启动

```shell
systemctl start NetworkManager.service
```

![[../../../_resources/images/Pasted image 20240901103322.png]]

NetworkManager 提供一个命令行界面 `nmcli` 和一个图形界面 `nmtui`

> [!note] 命令简写
> - `nmcli connection`：`nmcli con`
> - `nmcli device`：`nmcli dev`
# 连接无线

图形界面：直接使用 `nmtui` 选择 `activate a connection` 即可

命令行：

```shell
# 列出网络设备
nmcli device
# 查找网络
nmcli device wifi list
# 连接网络
nmcli device wifi connect <SSID/BSSID> password <密码>
# 连接隐藏网络
nmcli device wifi connect <SSID/BSSID> password <密码> hidden yes
# 断开网络
nmcli device disconnect ifname <DEVICE>
# 关闭 WiFi
nmcli radio wifi off
```
# 管理无线

```shell
# 列出连接列表
nmcli connection
# 激活已保存连接
nmcli connection up <NAME/UUID>
# 删除连接
nmcli connection delete <NAME/UUID>
# 编辑链接
nmcli connection edit <NAME>
nmcli connection modify <NAME> <PROP> <VALUE>
```