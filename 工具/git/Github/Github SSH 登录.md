# 创建密钥

若曾经创建过的密钥默认保存在 `用户文件夹/.ssh/id_rsa`

```shell
ssh-keygen -t rsa -C "email"
```

通过 Linux 的 ssh-keygen 工具生成密钥，在 Windows 下可以使用 git bash 或其他 bash 命令行，如 Cygwin 的命令行
* `-t`：密钥加密类型，默认即 rsa，故 `-t rsa` 可省略
* `-C`：注释，通常后面接邮箱
* `-f`：更改默认保存的位置和文件名

创建时会提示输入密码，可有可无，不想加直接回车，加了每次 push 都会提示输入密码

创建完成后，默认会在 `用户文件夹/.ssh` 下创建 `id_rsa` 和 `id_rsa.pub` 文件，`pub` 文件为公钥，另一个文件为私钥

![[Pasted image 20240806163520.png]]

# 设置 GitHub

浏览器登录 Github，进入 Settings -> SSH and GPG keys，点击 New SSH key

![[Pasted image 20240806163527.png]]

在 Add new 中，Title 部分随意填写，Key 部分粘贴前面 `id_rsa.pub` 文件中的全部内容，点击 Add SSH key 即可

![[Pasted image 20240806163540.png]]

# 校验

若设置成功，则可以登陆到 Github 的 ssh

```shell
ssh -T git@github.com
```

若前面设置了密码，登陆时会提示输入密码

![[Pasted image 20240806163547.png]]

出现这种提示表示设置成功