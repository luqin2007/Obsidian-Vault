# 连接 Zerotier

> [!success] 路由器下的设备可以通过路由访问 Zerotier 连接的其他设备，Zerotier 其他设备访问不到路由器下其他设备。

1. 在 Openwrt 中直接安装 `zerotier` 插件

![[../../../_resources/images/Pasted image 20240831183109.png]]

2. 配置 Zerotier 服务：通过 `ssh` 连接路由器，创建并启动 Zerotier

```shell
echo "config zerotier sample_config
    option enabled 1
" > /etc/config/zerotier
service zerotier start
```

3. 可选：配置自己的 `planet`、`moons.d` 服务器

```shell
# 复制默认配置
mkdir /etc/zerotier
cp -r /var/lib/zerotier-one/* /etc/zerotier/
# 将配置文件定位到新目录
service zerotier stop
echo "config zerotier 'sample_config'
    option enabled '1'
    option config_path '/etc/zerotier'
    option copy_config_path '1'
" > /etc/config/zerotier
```

使用 SCP 服务将文件上传到对应位置：
- `planet`：`/etc/zerotier`
- `moon` 节点文件：`/etc/zerotier/moons.d/`

![[../../../_resources/images/Pasted image 20240831190648.png]]

重启 Zerotier，检查节点

```shell
service zerotier start
zerotier-cli peers
```

![[../../../_resources/images/Pasted image 20240831191052.png]]

4. 加入网络，记得在 [[../../ZeroTier/ZeroTier|ZeroTier]] 授权路由器

```shell
zerotier-cli join 85edc1bd140ecbfc # 85edc1bd140ecbfc 为网络 ID
# 查看路由器 ID
zerotier-cli listnetworks
```

![[../../../_resources/images/Pasted image 20240831214658.png]]

5. 在每次修改配置，如加入网络等，重新拷贝持久化目录

```shell
cp -r /var/lib/zerotier-one/* /etc/zerotier/
service zerotier restart
```

6. 开放 9993 端口：网络 - 防火墙 - 通信规则（`Traffic Rules`），添加一条新规则

![[../../../_resources/images/Pasted image 20240831192131.png]]

7. 配置路由：成功连接 Zerotier 服务器后，设备会生成一个 `zt` 开头的设备，该设备名称与 `zerotier-cli listnetworks` 命令中的设备相同

![[../../../_resources/images/Pasted image 20240831211108.png]]

在网络 - 防火墙 - 常规设置中创建一个新区域，表示 `Openwrt` 作为子路由时的主路由网络，直接全部允许

![[../../../_resources/images/Pasted image 20240831211959.png]]

在网络 - 接口 - 接口中添加一个新接口，不配置协议，指向 Zerotier 生成的设备

![[../../../_resources/images/Pasted image 20240831211324.png]]

在弹出的编辑页中的防火墙设置中选择刚刚创建的区域，保存并应用。

# 路由配置

> [!success] 解决主路由设备访问子路由下设备的问题

进入 Zerotier 后台，添加 Openwrt 路由

![[../../../_resources/images/Pasted image 20240831212722.png]]

记下 Openwrt 路由器的 IP 地址，点击 `Routes` 添加新路由

![[../../../_resources/images/Pasted image 20240831222641.png]]
- `target`：路由器设备网段，可以在 openwrt 接口部分查询

---

其他内网穿透插件：
- `frp`
- `nps`
- `ngrok`

有公网 IP 使用动态 DNS 解析更好