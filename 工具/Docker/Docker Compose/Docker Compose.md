Docker Compose 是 Docker 官方开发的负责对 Docker 容器集群快速编排的工具

Docker Compose 可以允许使用 `docker-compose.yml` 文件定义一组相关联应用作为一个项目，通过配置文件管理 Docker 容器。

1. 使用 Dockerfile 定制应用容器环境
2. 使用 Compose 模板文件定制应用程序
    * 模板文件可以来自于本地或网络，以 `.yml` 或 `.yaml` 为扩展名
    * 模板文件默认读取当前目录中的 `docker-compose.yml` 或 `docker-compose.yaml`
3. 使用 `docker-compose up` 启动应用程序

>[!warning] Windows 下，Docker Desktop 自动安装 Docker Compose，Linux 和 Mac 需要单独安装

|指令|说明|
| --------| ----------------------------------------------------|
|`docker-compose <command> --help`<br />|查看某指令帮助|
|`docker-compose up -d <name>`|构建并启动名为 `<name>` 的容器|
|`docker-compose exec <name> bash`|登入 `<name>` 容器|
|`docker-compose down`|停止 `docker-compose up` 启动的所有容器|
|`docker-compose ps`|列出项目中所有容器|
|`docker-compose build <name>`|构建镜像|
|`docker-compose build --no-cache <name>`|构建镜像（不带缓存）|
|`docker-compose top`|查看各个服务容器内运行的进程|
|`docker-compose logs -f <name>`|查看 `<name>` 的实时日志|
|`docker-compose images`|列出 Compose 文件包含的镜像|
|`docker-compose config`|验证文件配置，输出错误信息|
|`docker-compose events --json <name>`|以 JSON 格式输出 `<name>` 容器的 Docker 日志|
|`docker-compose pause <name>`|暂停容器|
|`docker-compose unpause <name>`|恢复容器|
|`docker-compose stop <name>`|停止容器|
|`docker-compose rm <name>`|删除容器，必须先停止容器|
|`docker-compose restart <name>`|重启容器|
|`docker-compose run --no-deps --rm <name> <command>`|在 `<name>` 容器中不启动关联容器，执行 `<command>` 命令后删除容器|
# 目录

- [[模板文件]]
- [[指令]]