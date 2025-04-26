Docker Swarm 是 Docker 官方集群管理工具，将 Docker 主机池转变为单个虚拟 Docker 主机，通过一个入口统一管理这些 Docker 主机的资源。

![[../../_resources/images/Docker Swarm 2024-08-06 22.59.01.excalidraw]]

* Swarm：SwarmKit，嵌入 Docker 引擎的集群管理和编排工具
* Node：Docker 引擎集群的一个实例，可被视为 Docker 节点
    * 管理节点：Manager Node，管理整个集群管护理工作，包括集群配置、服务管理、分配任务、维护集群状态所需的编排等
    * 工作节点：Worker Node，接收管理节点分配的任务 Task，运行任务，向管理节点通知任务状态
* Service：任务的定义，是群体系统的中心结构，是用户与群体交互的主要根源，通过 `docker service ls`  查看
* Task：运行在工作节点上的容器，或容器包含的应用，是集群中调度的最小单元

![[../../_resources/images/Docker Swarm 2024-08-06 23.00.03.excalidraw]]
# 工作流

‍![[../../_resources/images/Docker Swarm 2024-08-06 23.02.18.excalidraw]]
Swarm manager：
* API：创建对象 - 接收命令并创建 Service 对象
* Orchestrator：服务编排 - 为 Service 对象创建的任务进行编排
* allocater：分配 IP - 为每个任务分配 IP 地址
* scheduler：运行任务 - 安排工作节点运行任务

worker node：
* worker：检查任务 - 连接调度器，检查分配的任务
* executor：执行任务 - 执行分配给工作节点的任务
# 常用指令
## docker swarm
| 指令                                    | 说明           |
| ------------------------------------- | ------------ |
|  `docker swarm init`                | 初始化集群        |
|  `docker swarm join-token worker`   | 查看工作节点 token |
|  `docker swarm join-token manager`  | 查看管理节点 token |
|  `docker swarm join`                | 加入集群         ||
## docker node

|指令|说明|
| ------| ---------------------------|
| `docker node ls` |查看所有节点|
| `docker node ps` |查看当前节点所有任务|
| `docker node rm [-f] <name\|id>` |删除节点，`-f`  表示强制删除|
| `docker node inspect <name\|id>` |查看节点详情|
| `docker node demote <name\|id>` |将管理节点降级为工作节点|
| `docker node promote <name\|id>` |将工作节点升级为管理节点|
| `docker node update <name\|id>` |更新节点镜像|
## docker service

| 指令                                       | 说明                       |
| ---------------------------------------- | ------------------------ |
|  `docker service create`               | 创建服务                     |
|  `docker service ls`                   | 查看全部服务                   |
|  `docker service inspect <name\|id>`   | 查看服务详情                   |
|  `docker service logs <name\|id>`      | 查看服务日志                   |
|  `docker service rm [-f] <name\|id>`   | 删除服务，`-f`  强制删除         |
|  `docker service scale <name\|id>`     | 设置服务数量                   |
|  `docker service rollback <name\|id>`  | 回滚服务                     |
|  `docker service update <name\|id>`    | 服务更新，也可以用于服务扩缩容、回滚、滚动更新等 |
# 集群管理
## 部署

要部署 Docker 集群需要准备一个管理器（管理节点）和至少一个工作节点。

|角色|IP|Hostname|
| ---------| -----------------| ----------|
|manager|192.168.164.139|docker1|
|worker|192.168.164.140|worker1|
|worker|192.168.164.141|worker2|
Docker 容器配置：
* 主机名：使用 `hostnamectl set-hostname <主机名>`  设置主机名
* 防火墙：开放以下端口，或关闭防火墙
    * TCP 2377：集群间通信
    * TCP&UDP 7946：节点间通信
    * UDP 4789：覆盖网络

1. 创建集群

    在 192.168.164.139 上初始化一个集群，该节点将成为 `Leader`  并作为管理节点，其他加入的成员将成为 `Reachable` 

    ```shell
    docker swarm init --advertise-addr=192.168.164.139
    ```
2. 加入集群

    管理节点先查看令牌信息，同时也直接查询出加入集群的指令

    ```shell
    docker swarm join-token manager
    ```

    之后，使用 `docker swarm join`  即可

    ```shell
    docker swarm join --token <token> <manager-ip>:2377
    ```
## 信息

使用 `docker info`  可以查看所有节点信息和 Swarm 集群信息

```shell
docker info
```

通过 `docker node ls`  查看集群环境下各节点信息

```shell
docker node ls
```

*  `MANAGER STATUS` ：节点类型
    *  `Leader` ：管理节点中的主节点，负责集群的集群管理和编排决策
    *  `Reachable` ：管理节点的从节点，当 Leader 不可用时有资格被选为新 Leader
    *  `Unavailable` ：管理节点，但不能与其他管理节点通信
    * 空：工作节点
*  `AVAILABILITY` ：是否可以接收任务
    *  `Active` ：调度程序可以将任务分配给该节点
    *  `Pause` ：调度程序不能将新任务分配给该节点，但现有任务可以继续运行
    *  `Drain` ：调度程序不能将新任务分配给该节点，且会关闭所有该节点的现有任务并调度到其他可用节点
## 删除

1. 删除节点之前，需要将该节点的 `AVAILABILITY`  属性变更为 `Drain` ，确保服务正常

    该指令需要在管理节点执行

    ```shell
    docker node update --availability=drain <name|id>
    ```
2. 若删除管理节点，需要将其降级为工作节点

    一个集群应至少有一个管理节点

    ```shell
    docker node demote <name|id>
    ```
3. 在要移除的节点上运行移除集群

    ```shell
    docker swarm leave
    ```
4. 在管理节点中删除节点

    ```shell
    docker node rm <name|id>
    ```
# 服务管理

集群服务管理仅在管理节点执行
## 创建与删除

1. 创建一个服务，该服务会由管理器随机分配到某个工作节点部署启动

    创建名为 `mynginx` ，使用 `nginx`  镜像的服务（镜像）

    ```shell
    docker service create --replicas 1 --name mynginx -p 80:80 nginx:latest
    ```
2. 查看服务运行信息
    * 查看运行的服务：`docker service ls` 
    * 查看服务详细信息：`docker service inspect <server name|id>` 
    * 查看服务所在节点：`docker ps <server name|id> ` 
3. 删除服务：`docker service rm <server name|id>` 
## 弹性扩容

集群创建后，允许通过指令弹性扩缩容 Service 中的容器数量

```shell
docker service scale <server name|id>=<count>
```

或

```shell
docker service update --replicas <count> <server name|id>
```
## 调度策略

Swarm 选择运行的节点时，根据指定的策略计算最适合节点，目前节点有 `spread` 、`binpack` 、`random` 
*  `random` ：随机选择一个节点，一般用于调试
*  `spread` ：选择运行容器最少的节点，使容器均衡的分布在各个节点上
*  `binpack` ：选择运行容器最集中的节点，最大化避免容器碎片化，尽可能把还未使用的节点留给需要更大空间的容器运行
## 滚动升级

Docker Swarm 允许实现服务的平滑升级，更新服务不停机，客户端无感知

```shell
docker service update --image <image:new-tag> <name>
```

若新镜像导致重新部署失败，可自动回滚。在创建或更新服务时可以通过选项设置相关配置

|选项|默认值|说明|
| ------| --------| ----------------------------------------------------------------|
| `--rollback-delay` |0s|回滚任务结束后，进行下一个任务之前的等待时间|
| `--rollback-failure-action` |pause|当任务无法回滚时的操作，无论 `pause`  还是 `continue`  都会回滚其他任务|
| `--rollback-max-failure-ratio` |0|回滚期间容忍的故障率，0-1 之间的浮点|
| `--rollback-monitor` |5s|每个任务回滚之后的持续时间，在此期间容器停止则认为回滚是否失败|
| `--rollback-parallelism` |1|并行回滚的最大任务数，0 表示同时回滚所有任务|
