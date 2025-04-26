> [!error] ssh: connect to host github. com port 22: Connection refused，因 22 端口被拒绝。

* 查看防火墙是否禁止 22 端口
* 检查 host 是否对 `github.com` 有重定向。某些代理工具，如 Steam++ 允许修改 host 代理 github

![[Pasted image 20240806163613.png]]

* 检查 DNS 服务器，使用 `ipconfig /flushdns` 清空 DNS 缓存
* 使用 443 端口替代 22 端口：修改 `~/.ssh/config`（Windows 是 `C:\User\用户名\.ssh\config`）文件

使用该方法的前提是 `ssh -T -p 443 git@ssh.github.com` 必须能正常连接

```console
Host github.com
User git
Hostname ssh.github.com
IdentityFile ~/.ssh/id_rsa
Port 443
```

* 设置 git 代理

```shell
# http || https
git config --global http.proxy 127.0.0.1:7890
git config --global https.proxy 127.0.0.1:7890

# sock5代理
git config --global http.proxy socks5 127.0.0.1:7891
git config --global https.proxy socks5 127.0.0.1:7891
```

查看代理：

```shell
git config --global --get http.proxy
git config --global --get https.proxy
```

取消代理：

```shell
git config --global --unset-all http.proxy
git config --global --unset-all https.proxy
```