---
title: 上传文件报413 Request Entity Too Large错误解决办法
source: https://www.jianshu.com/p/3851c3d6eaf1
author:
  - "[[简书]]"
published: 2018-07-25
created: 2025-04-13
description: 产生这种原因是因为服务器限制了上传大小 1、nginx服务器的解决办法 修改nginx.conf的值就可以解决了将以下代码粘贴到nginx.conf内 可以选择在http{ ...
tags:
  - clippings
---
产生这种原因是因为服务器限制了上传大小

## 1、nginx服务器的解决办法

修改nginx.conf的值就可以解决了  
将以下代码粘贴到nginx.conf内

```
client_max_body_size 20M;
```

可以选择在http{ }中设置：client\_max\_body\_size 20m;  
也可以选择在server{ }中设置：client\_max\_body\_size 20m;  
还可以选择在location{ }中设置：client\_max\_body\_size 20m;  
三者有区别  
设置到http{}内，控制全局nginx所有请求报文大小  
设置到server{}内，控制该server的所有请求报文大小  
设置到location{}内，控制满足该路由规则的请求报文大小

同时记得修改php.ini内的上传限制  
upload\_max\_filesize = 20M

注意:如果以上修改完毕后还会出现413错误的话 , 可能是域名问题 , 本人遇到过此类情况 , 记录

## 2、apache服务器修改

在apache环境中上传较大软件的时候，有时候会出现413错误，出现这个错误的原因，是因为apache的配置不当造成的，找到apache的配置文件目录也就是conf目录，和这个目录平行的一个目录叫conf.d打开这个conf.d，里面有一个php.conf

```
目录内容如下： 
# 
# PHP is an HTML-embedded scripting language which attempts to make it 
# easy for developers to write dynamically generated webpages. 
# 

LoadModule php4_module modules/libphp4.so 

# 
# Cause the PHP interpreter handle files with a .php extension. 
# 

SetOutputFilter PHP 
SetInputFilter PHP 
LimitRequestBody 6550000 

# 
# Add index.php to the list of files that will be served as directory 
# indexes. 
# 
DirectoryIndex index.php
```

误就发生在这个LimitRequestBody配置上，将这个的值改大到超过你的软件大小就可以了  
如果没有这个配置文件请将

```
SetOutputFilter PHP 
SetInputFilter PHP 
LimitRequestBody 6550000
```

写到apache的配置文件里面即可。

## 3、IIS服务器（Windows Server 2003系统IIS6）

先停止IIS Admin Service服务，然后  
找到windows\\system32\\inesrv\\下的metabase.xml，打开，找到ASPMaxRequestEntityAllowed 修改为需要的值，然后重启IIS Admin Service服务

1、在web服务扩展 允许active server pages和在服务器端的包含文档  
2、修改各站点的属性 主目录－配置－选项－启用父路径  
3、使之可以上传大文档(修改成您想要的大小就可以了，以字节为单位)  
c:\\WINDOWS\\system32\\inetsrv\\MetaBase.xml

！企业版的windows2003在第592行  
默认的预设置值 AspMaxRequestEntityAllowed="204800" 即200K

将其加两个0，即改为，现在最大就可以上传20M了。  
AspMaxRequestEntityAllowed="20480000"