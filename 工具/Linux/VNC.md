用于远程连接可视化窗口

1. 安装 `TightVNC` / `TigerVNC`

```sh
sudo apt install tightvncserver
```

2. 使用 `vncserver` 初始化并启动 VNC 服务

> [!note] 使用  `vncpasswd` 可用于重新配置密码和仅查看等信息

> [!note] Would you like to enter a view-only password (y/n)?
> 该项询问 VNC 服务器是否仅可查看（不能使用鼠标、键盘等输入）

![[../../../_resources/images/Pasted image 20250207134812.png]]

> [!note] VNC 实例与端口
> 默认登陆端口为 5901，实例名为 `:1`。可以开启多个端口，名称依次为 `:2`，`:3` 等，端口依次为 5902，5903 等

3. 根据系统桌面环境配置 VNC（`~/.vnc/xstartup`），重启 VNC

> [!note] 结束
> ```sh
> vncserver -kill 实例名
> ```
> ![[../../../_resources/images/Pasted image 20250207142623.png]]

> [!warning] 获取桌面环境 DE
> - `echo $XDG_CURRENT_DESKTOP`
> - `screenfetch`
> 
> 注意在 ssh 下无法使用（显示为空），因为显示的是当前会话的，ssh 会话环境没有 DE
# 桌面环境配置
## xfce4

``` title:"~/.vnc/xstartup"
#!/bin/sh

unset SESSION_MANAGER
unset DBUS_SESSION_BUS_ADDRESS
exec startxfce4
```
# 使用已存在会话
## x0vncserver

正常情况下，VNC 启动后创建一个新桌面会话。若要显示已存在的桌面，可以使用 `x0vncserver` 服务或 `x11vnc`

1. 安装 `tigervnc-standalone-server` 和 `tigervnc-scraping-server`
2. 使用 `vncpasswd` 配置登录密码
3. 配置 `~/.vnc/xstartup` [[#桌面环境配置]] 
4. 使用 `x0vncserver` 转发当前桌面

```sh
x0vncserver -rfbport 5901 -display :0 -PasswordFile ~/.vnc/passwd
```

- rfbport：VNC 端口
- display：显示器编号（适用于多个显示器的情况）
- PasswordFile：密码文件（由 `vncpasswd` 创建）

| 选项                  | 默认值     | 说明                   |
| ------------------- | ------- | -------------------- |
| AcceptKeyEvents     | 1       | 允许操作键盘               |
| AcceptPointerEvents | 1       | 允许使用鼠标               |
| AlwaysShared        | 0       | 允许同时被多人连接            |
| SecurityTypes       | VncAuth | 连接主机的认证方式，None 表示无密码 |
| rfbport             | 5908    | 登录端口                 |
使用 `x0vncserver -kill` 停止服务
## x11vnc

```sh
sudo apt install x11vnc
x11vnc -display :0 -usepw -autoport 5901
```

- display：显示器编号（适用于多个显示器的情况）
- usepw：使用密码
- autoport：端口
- listen：监听 IP