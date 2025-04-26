Docker 存放数据的资源有两类：
* 由 storage driver 管理的容器层和镜像层，数据难以持久化和共享
*  `data volume`  数据卷，将宿主机目录作为存储使用
# 存储驱动

> [!note] 写时复制
> COW，Copy-On-Write。Docker 镜像层是只读的，启动容器时 Docker 会加载只读镜像层并在顶部加一个读写层。当容器修改了一个现有文件，该文件就会复制到读写层中，原只读文件仍然存在但被隐藏

存储驱动 `storage driver`  实现多层镜像堆叠，为用户提供单一的合并之后的文件系统统一视图，[默认实现](https://docs.docker.com/storage/storagedriver/select-storage-driver/)为 `Overlay2` 。

> 容器由最顶层可写容器层及若干镜像层组成。
>
> * 新数据直接存储在最顶层的容器层
> * 修改现有数据从镜像层复制到容器层，修改后保存到容器层，镜像层只读（写时复制）
> * 多层中有名称相同的文件，用户只可见最顶层的文件

对于无状态应用容器，没有文件需要持久化，适合将数据放在 `storage driver`  中。
# 数据卷

> [!success] 由于没有 `storage driver`  层而是直接读写数据，效率比存储驱动高

数据卷 `data volume` ，又称存储卷，直接被 mount 到容器中的目录或文件。Docker 不会在容器退出时清空数据卷内容。
* 可以在容器间共享和重用数据
* 是文件或目录，而不是未格式化的磁盘
* 修改立即生效且不影响镜像
* 数据卷会一直存在，直到没有容器使用为止

数据卷提供 volume，bind mounts、tmpfs 三种存储技术
*  `volume` ：数据存放在 `/var/lib/docker/volumns`  中，由 Docker 管理，不允许其他进程修改
*  `bind mounts` ：直接挂载主机文件系统的任何目录或文件，主机和容器都可以访问和修改其中文件
*  `tmpfs` ：数据暂存在主机内存中

![[../../../_resources/images/Docker 存储 2024-08-06 22.41.03.excalidraw]]

数据卷注意传播覆盖：
* 绑定空数据卷：容器内目录数据会复制到数据卷中
* 绑定非空数据卷：容器内目录被数据卷数据覆盖
# volume

 `volume`  适合在多个容器之间共享数据，可以忽略宿主机的差异。

使用 `docker volume create <name>`  创建 `volume` 

```shell
docker volume create nginx_volum
```

使用 `docker inspect <name>`  可以查看 `volume`  配置，包括挂载点等

```shell
docker inspect nginx_volume
```

 ![[Pasted image 20240806224135.png]] 

使用 `docker volume rm <name>`  删除一个 `volume` ，`docker volume prune`  删除没有镜像使用的 `volume` 

> [!success] 创建容器时，若未指定任何挂载源，Docker 会自动创建一个匿名数据卷

```shell
# 创建一个匿名卷挂载到 /usr/share/nginx/html
docker run -dt -v /usr/share/nginx/html --name nginx_with_volume nginx
```
# bind mounts

> [!success] `bind mounts`  可以用于在宿主机和容器间共享配置文件、共享代码、build 输出等任何文件。

与 `volume`  相似，但 `bind mounts`  可以指定挂载到宿主机的完整路径。如果不存在，Docker 会自动创建对应目录（**不能创建文件**）

```shell
# 将本地 /nginx/html 挂载到容器 /usr/share/nginx/html
docker run -dt -v /nginx/html:/usr/share/nginx/html --name nginx nginx
```

可以以设置读写权限

```shell
# 将本地 /nginx/html 挂载到容器 /usr/share/nginx/html，但只读
docker run -dt -v /nginx/html:/usr/share/nginx/html:ro --name nginx nginx
```
# tmpfs

将内存的一个区域映射到目录或文件，用于因安全或其他原因，不希望将数据持久化，也不想写入设备存储层。

 `tmpfs mount`  可以使用 `--tmpfs`  或 `--mount type=tmpfs`  使用：（下面两条等效）

```shell
docker run -d -it --name tmptest --mount type=tmpfs,destination=/app nginx:latest
docker run -d -it --name tmptest --tmpfs /app nginx:latest
```

 `--tmpfs`  无法指定其它参数，无法用于 Swarm Service。可选参数有：

*  `tmpfs-size` ：挂载 `tmpfs`  的上限（单位：字节），默认不限制
*  `tmpfs-mode` ：文件模式，默认 1777 表示任何用户都有写入权限

使用 `docker container inspect <contianer name>`  可以查看某容器 `tmpfs`  信息
