# 环境变量设置

- `XMODIFIERS=@im=fcitx`
- 删除 `GTK_IM_MODULE/QT_IM_MODULE/SDL_IM_MODULE`

> [!note] 经测试，不删除下面三个环境变量也没问题

![[../../../_resources/images/Pasted image 20240905154001.png]]

# 系统设置

系统设置 - 虚拟键盘中设置为 `fcitx5`

> [!note] 经测试，设置为 `fcitx5 Wayland 启动器` 也可以

![[../../../_resources/images/Pasted image 20240905153433.png]]
# 应用设置

应用程序添加 `--enable-features=UseOzonePlatform --ozone-platform=wayland --enable-wayland-ime` 启动参数

![[../../../_resources/images/Pasted image 20240905153635.png]]
