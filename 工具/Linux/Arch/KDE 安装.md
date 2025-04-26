# 安装
## 完整安装

```shell
# 安装 Plasm 桌面
pacman -S plasma
# 安装 KDE 应用
pacman -S kde-applications
```
## 最小安装

```shell
# 安装 Plasm 桌面
pacman -S plasma-desktop
# 安装 KDE 应用
pacman -S kde-applications-meta
```

> [!note] 中文字体包
> -  `noto-fonts`：基础字体包
> - `noto-fonts-cjk`：中日韩字体包
> - `noto-fonts-emoji`：表情字体包
# 启动

```shell
/usr/lib/plasma-dbus-run-session-if-needed /usr/bin/startplasma-wayland
```

使用 `xorg-xinit`

```shell title:.xinitrc
export DESKTOP_SESSION=plasma
exec startplasma-x11
```

或直接使用 `startx` 启动

```shell
startx /usr/bin/startplasma-x11
```
# 开机加载

```shell
systemctl enable sddm.service
```

自动登录可以编辑 `/etc/sddm.conf.d/autologin.conf`

> [!attention] 更换其他桌面管理器时，记得改 `Session` 属性

``` title:autologin.conf
[Autologin]
User=<用户名>
Session=plasma
```
