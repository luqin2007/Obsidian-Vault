![[../../../_resources/images/Drawing 2024-08-06 22.15.21.excalidraw]]

`Docker` 是一种轻量级虚拟化技术
* 轻量，高效：由 Docker 引擎负责调度和隔离，不需要每个容器运行一个系统
* 灵活，可维护，可扩展，易迁移：使用 `Dockerfile` 进行配置
* 权限提升：容器内用户可以获取宿主机权限**甚至 root 权限**
* 硬件不隔离：**容器受到攻击易被扩散到主机**

`Docker` 分为三部分：
* 镜像：容器的模板，只读，可以包含一个完整的系统、服务器和依赖组件等
* 容器：通过镜像创建的运行实例，是独立运行的一个或一组程序
* 镜像仓库：集中存放镜像的地方，如 Docker Hub

镜像是应用系统的静态存在形式，包括应用程序的文件、依赖的其他组件  
容器是应用系统的动态存在形式，是程序运行时的状态

容器系统本身分为三部分：
* 容器规范，容器配置文件的格式。Open Container Initiative（OCI）发布了 `runtime spec` 和 `image format spec` 两个规范
* 容器 runtime，程序真正运行的地方。
* 容器管理工具，对内与 runtime 交互，对外与用户交互

|容器类型|容器|容器管理工具|
| ----------| ------| ---------------|
|Docker|runc|Docker Engine|
|Linux|lxc|lxd|
|CoreOS|rkt|rkt cli|

>[!note] 编排
>orchestration，包括容器管理、调度、集群定义和服务发现等。容器通过容器编排引擎组成微服务应用，实现业务需求

* Docker Swarm：Docker 容器编排引擎
* Kubernetes：Google 开发的编排引擎，支持 Docker 和 CoreOS
* Mesos：通用集群调度平台，与 Marathon 共同提供编排引擎功能

# 安装

* Windows：先安装 WSL2 并将其设为默认版本
* Linux：需要添加 Docker 的源

使用 `docker run hello-world` 可以检查是否正常安装

![[Pasted image 20240806221809.png]]

# 引擎

旧版本 Docker 由 LXC、Docker daemon 组成，现阶段 Docker 由 Docker client、daemon、containerd、runc 组成。

* `runc`：OCI 容器运行时标准的参考实现，实质是一个轻量级、针对 Libcontainer 的命令行交互工具，用于创建容器
* `containerd`：管理容器生命周期，管理镜像等
# 目录

- [[操作容器]]
- [[Docker 镜像]]
- [[Docker 容器]]
- [[应用容器化]]
- [[网络模式]]
- [[Docker 存储]]
- [[日志管理]]
