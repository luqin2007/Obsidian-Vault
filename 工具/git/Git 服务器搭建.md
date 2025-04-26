这里我用 `WSL` 的 `Ubuntu 22.04.1` 为例，已将源设置为国内源。

1. 创建 git 用户名并设置权限，专用于 git 仓库修改

```shell
groupadd git # 创建组 git
adduser git -g git # 新用户就叫 git, 设置其用户组为 git
```

2. 初始化对应 git 仓库

```shell
cd ..
cd git # 进入 git 用户的主文件夹 /home/git
mkdir gittmp.git # 创建 git 项目文件夹，一般 git 仓库目录以 .git 结尾
chown -R git:git gittmp.git # 将 gittmp.git 的所有者设置为 git 组的 git 用户
cd gittmp.git/
git --bare init # 初始化仓库
```

3. 设置认证方法，这里使用 ssh 登录

```shell
cd .. # 返回 /home/git
mkdir .ssh # 创建 /home/git/.ssh 用于保存公钥
chmod 755 .ssh # /home/git/.ssh 目录只能由拥有者修改，其他用户只能访问
cd .ssh # 进入 /home/git/.ssh
nano authorized_keys # 这里用 nano 打开（创建） authorized_keys 文件，也可以用 echo, vi 等
```

打开 `authorized_keys` 后，向里面添加 rsa 公钥即可，公钥在客户端使用

```shell
ssh-keygen -t rsa -C "你自己的github对应的邮箱地址"
```

密码可选，加的话会在每次 push 时要求密码创建。然后 `用户名/.ssh/id_rsa.pub` 文件内容即公钥。

```shell
chmod 644 authorized_keys # 修改 authorized_keys 权限，不可执行
```

4. 配置 ssh 服务器

首先安装 `openssh-server`，这里以 `ubuntu` 为例

```shell
sudo apt-get install openssh-server # 安装服务器
sudo service ssh start # 启动 ssh 服务
```

之后，配置 `ssh` 服务器

```shell
sudo nano /etc/ssh/sshd_config
```

在里面将 `Port` 到 `ListenAddress` 部分前面的注释取消掉：

![[Pasted image 20240806163417.png]]

之后重启服务器

```shell
sudo service ssh restart
```

然后查看当前服务器 ip，`inet` 字段即为 ip

```shell
ifconfig
```

![[Pasted image 20240806163425.png]]

之后，在本地使用 ssh 连接一下试试

```shell
ssh -T git@[之前的 ip 地址]
```

![[Pasted image 20240806163434.png]]

这样表示没问题了，可以将远程的仓库 `clone` 到本地了：`git clone [之前创建的用户名]@[ip 地址]:[仓库目录]`。

```shell
git clone git@172.31.35.231:/home/git/gittmp.git
```

![[Pasted image 20240806163441.png]]
