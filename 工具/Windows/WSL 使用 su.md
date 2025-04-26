WSL2 的 Ubuntu 系统默认没有 `root` 用户密码，无法使用 `su`

> [!error] su: Authentication failure

设置 `root` 密码即可

```shell
sudo passwd root
```

![[../../_resources/images/Pasted image 20241019123139.png]]
