> [!error] Failed t bind to server socket:tcp:localhost: 61616 due to : java. net. BindException

开启 ActiveMQ 失败，问题在于 61616 端口被占用。

独立启动 ActiveMQ 也会启动失败，同样的报错

1. 检查 61616 端口情况
2. Windows：检查 `Internet Connection Sharing (ICS)` 服务

如果实在不行就在其他端口开启

‍
