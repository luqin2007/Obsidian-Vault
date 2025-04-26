那么，有什么用呢？  
我也不知道啊，我的极3Pro也不能刷机啊啊啊啊

```cardlink
url: http://www.hiwifi.wtf/
title: "极路由ROOT local-ssh利用工具"
host: www.hiwifi.wtf
```

访问 [极路由ROOT local-ssh利用工具 (hiwifi.wtf)](http://www.hiwifi.wtf/) 它会提供两个网址，同时打开

![[Pasted image 20240806170246.png]]

第一个进入将 token 填入 local token 输入框
![[Pasted image 20240806170255.png]]

第二个打开后是一个 json，将其中 uuid 后的值（不带双引号）填入 uuid 输入框
![[Pasted image 20240806170306.png]]

然后点击提交。网站会提供 cloud token，填入第一个网页的 cloud token 输入框
![[Pasted image 20240806170314.png]]
![[Pasted image 20240806170320.png]]

点击提交，提示 `Success: ssh port is 22` 表示临时 root 权限获取成功。
![[Pasted image 20240806170329.png]]

ssh 连接地址就是管理地址，可以在极路由后台查询
![[Pasted image 20240806170336.png]]

打开命令行，进行连接，密码即后台管理密码

```shell
ssh 192.168.199.1 -p 22 -l root
```

![[Pasted image 20240806170345.png]]

出现 该界面表示登陆成功。运行以下命令开启永久 ssh 登录：

```shell
/etc/init.d/dropbear enable && /etc/init.d/dropbear start
```

至此永久 root 权限获取成功
