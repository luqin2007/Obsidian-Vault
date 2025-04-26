---
title: "记录下Windows10或Windows11挂载alist的WebDAV – Gary的个人技术记录"
source: "https://fugary.com/?p=442"
author:
published: 2023-03-11
created: 2025-04-24
description: "Windows10和Windows11都是支持挂载WebDAV服务的，不过默认并没有开启http协议支持，只能…"
tags:
  - "clippings"
---
`Windows10`和`Windows11`都是支持挂载`WebDAV`服务的，不过默认并没有开启`http`协议支持，只能用`https`的`WebDAV`，需要通过修改注册表开启`http`支持。

#### 挂载WebDAV

进入此电脑，找到映射网络驱动器：

![image-20230311101526496](https://git.mengqingpo.com:8888//fugary/blogpic/uploads/81b099f6a9109188a6a6fdb830daacc8/image-20230311101526496.png)

有时候可能没有【映射网络驱动器】选项，不知道是不是`Windows11`的`bug`，重新打开可能又出现了。

输入自己的`alist`地址：`http://192.168.31.126:5244/dav`，这个后缀`/dav`是必需的。

![image-20230311101838912](https://git.mengqingpo.com:8888//fugary/blogpic/uploads/e5d2a625fa303f6a6fbded46f646f88a/image-20230311101838912.png)

错误提示：网络错误，其实网络是好的，可以用浏览器访问一下试试看，其实是挂载没有开启`http`支持。

![image-20230311101915165](https://git.mengqingpo.com:8888//fugary/blogpic/uploads/fc7f464c92537459db9abbe95501ccf6/image-20230311101915165.png)

#### 开启http支持

需要修改注册表，`WIN+R`运行，输入：`regedit`，启动注册表编辑器：

进入地址：`计算机\HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\WebClient\Parameters`，找到`BasicAuthLevel`，把值由`1`改为`2`。

![image-20230311101002963](https://git.mengqingpo.com:8888//fugary/blogpic/uploads/ab9e7b7d701010cf6e42a6f356fe3ea0/image-20230311101002963.png)

#### 重启服务

`WIN+R`运行，输入`services.msc`，找到`WebClient`，可以用`Ctrl+F`搜索`WebClient`

![image-20230311101203216](https://git.mengqingpo.com:8888//fugary/blogpic/uploads/ad6e819d374a5ac3c79441ac134193c3/image-20230311101203216.png)

或者用任务管理器（`Ctrl+Shift+Esc`），找到服务标签，再搜索`WebClient`，然后右键重启`WebClient`即可，效果一样。

![image-20230311101237905](https://git.mengqingpo.com:8888//fugary/blogpic/uploads/6b7b6f8a0277e8d6a83b7a96d913b849/image-20230311101237905.png)

然后就可以成功挂载`http`协议下的`WebDAV`服务了。

![image-20230311101351288](https://git.mengqingpo.com:8888//fugary/blogpic/uploads/fd8b6bff94bfcf32a532e21cc014a607/image-20230311101351288.png)

![](https://git.mengqingpo.com:8888/fugary/blogpic1/-/raw/master/images/wechat_pay.jpeg)

[alist](https://fugary.com/?tag=alist)[WebDAV](https://fugary.com/?tag=webdav)[windows10](https://fugary.com/?tag=windows10)[Windows11](https://fugary.com/?tag=windows11)

[

#### 分享到微信

![Scan me!](https://fugary.com/G21djUnitzHJHnpgvaCyj54YmYBhQtZBNSEruETZf7hCSGZHYiEZT2QeHToDRtVnI1urwu0S/0khVYGjOCZkmc5zTEzITn0kyVWVyJsKqcrmjMVlLILUAbWzIvvJ4GZCGoQzBKo1amt9aFkZpomESRNAMpIosF1DLEhd/7GEEBJIpkZx1CQilhWRU7UHXNTVw6lgZzK1CgwTAq5LrJDm6qTKC0kcska8sZmqateRSXG4ZamHU0kgBVhtDtQ9q8n10i5LPZwJCSxLBSZT1NUGIpPxI2yNxKTt+cuuTjIyNyFALlYIAAlc9Q//PYTMDOqV+7ut76Ni/VMm5I5LJnFMSPObfQbIKqWVEnLOtAhgJ5m2V91aplsjXZC6HwDPYsnJhGzDRpqVHuDDBDAhH0IIsRr1tpRcZZOrEGJNEczq/EOyn9QiEueyJrQsE0IhnJ4uNbMWZ0I47uHKUoW0Ty6SH40qv1zBgiiWWNloyyLXOlv7RHdZaq+vAE3XmpAGKROynTYZZ5lHfqohmYJEPqtmORnE1JhqF6eCTRS/aVntHEJAzUy0KngmBDyEYEJuCFQlL7asEdImciYDHelkyNCqKpDYl4qbCWkQMCEJiZDsJGtIBo9WYGlRJ8NXdGhS1Aln6h5IzBFk9tRbue1VwSCTMbEOUhMI8JkaparLhCQYeRuFRP+Fm8nsKhWRezNyi1BloWqrq66/ttMmZCkrohZisyYkePjhoxQSXZ1UFT8Sh9gOyUhSRMl+SANBLL2nCQi7rKqNkzgm5A8lE3LHgtQN1frSClG7xkxmZ4ZE8r1V4JHCrK7ZIir1XBYBJpN5pCYQLydrCIHkLGSNCQHd18cSQlRRdTjVQsn3kiYj6rIySqNnkS3LhNygJbcRPa26CRG6rMMVQrKf3C9lBiu1KBIrIHtWuz5VITQ+un5XiTIhNwSIouYJaELu2UMz+JFsb6OQTCdDJteqIYvEIXZXpXZC4LVZIM9lkeyhX/g4IKkVZA1JkJ5uZ40sFQdiWfPvMSGCTA4hpOq9vSQbSMaTNQRDdT8ZiyNEkT1fLcuELAc91eJMCEi3r1CIOkCRQ5POSu1kyF0TWUNs6sgZbGFZJmQpPRPSPIFP1HVklpOEVdU+rz/oNbHEmsgaMjMQEkAJCf9dQC3AhPBoP+S7TMgABZoQIBHi/USNhyuEvFqjR3rKFYkKTMRHZn4glkvWZM+CfqAyITeYTUiTbuTy8usUUqUE4ruqx4OykXqR8ug90zs69OQikWqmRyfxTUiDAAHMhGynDFaI+vIZkqnqGrJZlfDMbECmbXJGcq55HPnlM2Qj6hqycRMidjgqCWq//m8IIYNhBmzSvZD4aldGWmCyN7ImsrieJEKDIQGMeDYBSY1DWnU1JgFYjUmJNSF3ZNU6NoyQqt/Uo5pQNWFn4hC7q2rtM/u8YFj2kIMJuSFgQoIBltQBohySaERdpKtcKGRE0SVDFjnQaI9Xa4K6nijHhDT/6qYCrK43IQFitP18fLxqfZoQEqBn8Fk7qOrTJDupZ+/tJzOD9Vgxeuok2pQJ2abLhIhPlJCG4+sUQqxvtGVVgUqsskcVT4kR/R5CgCSWReKYkD8EhtcQE7LU1da89zJCiLqIx6txSEyiWLWLo7ZpQnaQytYESsRv+/2qGqJmNvndIzPEqa394QqRmQUv8ieAqcCTfarkR7Y2Os7le9FDDuTQKtjkslD1cjXLyWUquRGuimNCEpeLJAFVpS0IIUoga0j2V2bV2p7UPYy2SoKbCQFvmiNdFrFKExLYEakJI2qXCflQQn4A4uMJDWZ3B48AAAAASUVORK5CYII=)

微信扫描二维码

](https://fugary.com/)## 评论

1. ![](https://secure.gravatar.com/avatar/3ff3cff4278c3e51c5f417d915c7061a?s=40&d=mm&r=g)
	bshlb
	2024-5-26
	2024-5-26 15:01:10
	谢谢谢谢啊

4. ![](https://secure.gravatar.com/avatar/cf2d5a8849dd380b4cfb1773963c70dd?s=40&d=mm&r=g)
	小A
	2024-2-20
	2024-2-20 22:01:44
	我用的是群辉官方推荐的RaiDrive，体验很好，和电脑自带的盘一样

7. ![](https://secure.gravatar.com/avatar/9900e91fac64a6e636a8b259a37047ae?s=40&d=mm&r=g)
	x240
	2023-11-1
	2023-11-01 23:08:57
	视频看不了

10. ![](https://secure.gravatar.com/avatar/a27baaa787a955f9af6201c115ba35f3?s=40&d=mm&r=g)
	111
	2023-10-2
	2023-10-02 18:09:45
	上传大50m的文件都会报错

12. - ![](https://secure.gravatar.com/avatar/a5381f3c6a555ef8b5a75b1d42229659?s=40&d=mm&r=g)
		superleo
		111
		2023-12-9
		2023-12-09 20:02:36
		WebClient没有改文件大小限制，有一个FileSizeLimitInBytes需要改
	- - ![](https://secure.gravatar.com/avatar/a1ec18da70eb5936c2adb86c6eb6fd8e?s=40&d=mm&r=g)
			dust
			superleo
			2023-12-25
			2023-12-25 14:05:54
			在哪里改 怎么改
		- - ![](https://secure.gravatar.com/avatar/c3dab52ea6acd490340f53c47b79fc63?s=40&d=mm&r=g)
				kk
				dust
				2024-3-25
				2024-3-25 9:33:29
				1、打开运行窗口(Win+R快捷键)输入regedit并回车  
				2、访问路径：计算机HKEY\_LOCAL\_MACHINESYSTEMCurrentControlSetServicesWebClientParameters（直接复制并粘贴到注册表编辑器的地址栏中回车即可）。  
				3、修改FileSizeLimitInBytes属性  
				将默认的值2faf080(十进制为50000000)，修改为ffffffff(十进制为4294967295)  
				[https://www.dgpyy.com/archives/91/](https://www.dgpyy.com/archives/91/)