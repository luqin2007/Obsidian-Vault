默认情况下，Docker 为容器提供无限的资源，尽可能使用宿主的资源。也可以通过参数限定。

>[!warning] 许多功能需要宿主机内核支持，可以使用 `docker info`  查看，不支持的功能会出现在最后的 `WARNING`  条目中

# 内存限制

>[!danger] OOM 异常：Out of memory exception，没有足够内存进行任务，通常由于内存溢出、内存泄漏等问题产生

产生 OOM 异常时，dockerd 通过调整 Docker 守护进程优先级减轻被系统杀死的风险。

每个容器在 Linux 也有自己的分数：

* `--oom-score-adj:<score>`：指定容器评分，评分高的优先被杀，范围 `[-1000, 1000]`

内存限制为一个整数接 `b`、`k`、`m`、`g` 后缀，表示 `B`、`KB`、`MB`、`GB`
* `--memory`、`-m`：内存可用最大值。设置该参数后允许的最小值为 `4m`
* `--kernel-memory`：容器可使用的最大内核内存量，可能会阻塞宿主机

```shell
docker run -it name streel --memory 256m
```

* `--memory-reservation`：允许指定小于 `--memory` 的软限制，当宿主机内存不足时会激活该限制，仅 `--memory` 设置后可用
* `--oom-kill-disable`：关闭 OOM 优先级机制，仅 `--memory` 设置后可用

```shell
docker run -it name streel --memory 256m --memory-reservation 128m
```

所有内存限制可以通过 `/sys/fs/cgroup/memory/docker/<container id>/memory` 查询，单位字节

* `.../memory.limit_imen_bytes`：`-memory` 值
* `.../memory.soft_limit_in_bytes`：`--memory-reservation` 值
* `.../memory.oom_control`：`--oom-kill-disable`，1 表示开启
* `.../memory.memsw.limit_in_bytes`：`--memory-swap` 值
## 交换分区

* `--memory-swap`：容器可用交换分区大小
* `--memory-swappiness`：容器使用交换分区的倾向性，可取值 `[0-100]`，100 为能用就用
# CPU 限制

通常来说，CPU 密集型程序优先级越低越好，IO 密集型优先级越高越好

Linux 核心进程调度基于完全公平调度 CFS（Completely Fair Scheduler），基于权重的公平队列算法。每个任务关联一个 `vruntime` 表示 `占用时间/优先级` 的值，每次挑选该值较小的任务运行

*  `--cpus <core-count>` ：任务可占用的 CPU 数量，可以为小数，不能超过总 CPU 核心数
  > [!info] 设置为 1 也有可能在多个核心运行，但总占用为 1 个核
* `--cpuset-cpus <n>`：指定任务使用第 n 个 CPU
* `--cpu-shares <n>`：对 CPU 按比例划分

`--cpu-shares` 可以在运行时修改 `/sys/fs/cgroup/cup,cpuacct/docker/<container id>/cpu.shares` 修改，修改后立即生效
# 底层技术

## Cgroup

`Controlgroups`，群组控制技术，Docker 利用该技术对共享资源进行分配、限制、审计及管理等。

该技术利用 Linux 内核提供的限制、记录、隔离进程使用物理资源的功能

>[!info] 子系统：资源控制器，每种子系统都是一个资源的分配器

先挂载子系统，在每个子系统内创建不同 Cgroup 节点，完成对硬件资源的控制，以此取代虚拟化技术分割资源。

Cgroup 默认提供诸多资源组，用于管理几乎所有服务器资源：
* CPU：`cpu`，`/sys/fs/cgroup/cpu/docker`
* 内存：`mem`，`/sys/fs/cgroup/memory/docker`
* 磁盘 IO：`iops`，`/sys/fs/cgroup/blkio/docker`

Docker 会在宿主机该目录下为每个容器创建一个 `cgroup` 目录，以容器 id 为名
## Namespace

`Namespace` 管理容器的全局唯一资源，如网卡设备、文件系统等，实现容器间的资源隔离。

`Namespace` 主要为每个容器提供进程间隔离，每个容器有独立的运行空间，每个运行空间有独立且可使用的系统。`Namespace` 保证容器之间不会互相干扰，每个容器也不会访问命名空间之外的资源。

Linux 提供 6 种 `Namespace`

* `Mount Namespace`：简称 `mnt Namespace`，让每个容器有自己独立的文件系统
* `UTS Namespace`：让每个容器有自己独立的主机名和域名，使之可以成为网络上的独立节点
* `IPC Namespace`：让每个容器有自己独立的共享内存和信号量，实现容器内进程间通信与宿主独立。与 VM 不同的是，容器进程交互实质上是宿主机上具有相同 `PID Namespace` 的进程之间的交互
* `PID Namespace`：让每个容器有自己独立的 PID，不同 `Namespace` 可以有相同的 PID
  > [!info] 容器中 PID=1 的进程是容器本身进程，但不是宿主机的 `init`  进程
* `Network Namespace`：简称 `net Namespace`，让每个容器具有自己的网卡设备、路由等资源（网络设备，IP 地址，路由表，`/proc/net` 等），每个容器使用 `veth` 的方式将虚拟网卡与宿主机网卡的一个 `Docker Bridge` 连接
* `User Namespace`：隔离安全相关的标识符和树形，包括用户、组、用户空间等
  > [!info] 容器中允许存在 ID=0 的用户
## 联合文件系统 AUFS

分层、轻量级、高性能文件系统，允许将不同文件系统结构叠加到一起，使之看起来像一个文件系统
## LXC

提供共享宿主机内核的系统级虚拟化方法，运行时不需要加载系统内核，多容器共享宿主机一个内核以提高启动速度，减少物理资源占用
