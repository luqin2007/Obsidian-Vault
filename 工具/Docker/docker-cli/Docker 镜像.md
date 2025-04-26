# 定制镜像

* `docker commit`：将现有的镜像叠加上容器存储层，创建新镜像
* `docker build`：从 `Dockerfile` 创建镜像
## `docker commit`

1. 创建容器

```shell
# 创建并启动一个 nginx 容器，映射 80 端口
docker run --name webserver -d -p 80:80 nginx
```

访问主机 IP 即可以访问 Nginx 服务器

2. 修改容器

将 Nginx 服务器首页换成 `Hello World` 。

进入 `webserver` 容器并修改 `index.html`

```shell
docker exec -it webserver bash
echo '<h1>Hello Docker!</h1>' > /usr/share/nginx/html/index.html
exit
```

3. 导出镜像

查看镜像具体改动：`docker diff` 

```shell
docker diff webserver
```

提交修改：`docker commit [option] <container> [<name>: [:<tag>]]` 
* `container`：容器名或 ID
* `name`：仓库名
* `option`：选项
	*  `--author` ：作者
	*  `--message` ：修改内容

```shell
docker commit --author "Tag Wang <twang2218@gmail.com>" --message "修改默认网页" webserver nginx:v2
```

通过 `docker image ls`  查看镜像，`docker history`  查看镜像修改记录
## `docker build`

镜像定制就是向镜像添加配置，将每一条操作写入一个脚本，该脚本即可用来定制、构建镜像。该脚本即 `Dockerfile`

`Dockerfile` 包含若干条指令，每一条指令构建一层。

1. 新建一个目录，创建 `Dockerfile` 文件

```docker title:Dockerfile
FROM nginx
RUN echo '<h1>Hello Docker!</h1>' > /usr/share/nginx/html/index.html
```

2. 生成镜像

```shell
docker build -t nginx:v3 .
```
# 导出镜像

使用 `docker save <image> -o <file>` 导出镜像为一个 tar 包

使用 `docker load -i <file>` 导入镜像
# 查看镜像

使用 `docker images` 可以查看镜像
* REPOSITORY：镜像名
* TAG：版本
* IMAGE ID：镜像 ID
* SIZE：虚拟大小

![[Pasted image 20240806222547.png]]
# 删除镜像

使用 `docker rm <image-name>`  可以删除镜像，`<image-name>`  为镜像名；要删除多个镜像时，使用空格分隔

> [!danger] 一般情况下只有镜像没有使用时才能删除镜像，或使用 `-f`  强制删除
# Docker 镜像服务器

使用 `docker search <name>` 从远程镜像服务器查找镜像

```shell
docker search mysql
```

* NAME：镜像名
* DESCRIPTION：镜像描述
* STARS：其他用户对该镜像的评分
* OFFICIAL：是否为官方镜像

![[Pasted image 20240806222616.png]]

查找到镜像后，使用 `docker pull <name>:<tag>` 下载镜像，`<tag>` 默认为 `latest`

```shell
docker pull mysql
```

>[!ionfo] Docker 通常分为多层，不同层在不同镜像中可以共享

![[Pasted image 20240806222625.png]]

‍
