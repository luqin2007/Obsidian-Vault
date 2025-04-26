# iostat

用于查看 CPU、磁盘的详细监控指标，该命令位于 `sysstat` 包中

```shell
iostat [-x] [刷新间隔] [刷新次数]
```

`-x`：显示更多信息

![[Pasted image 20240806165400.png]]

* rKB/s：每秒发送到设备的读取请求数
* wKB/s：每秒发送到设备的写入请求数
* %util：磁盘利用率
# sar

## sar -n DEV

监控网络状态

```
sar -n DEV [刷新间隔] [刷新次数]
```

第一次使用提示无法打开 `/var/log/sa/saXX`，使用 `sar -o XX` 创建对应文件即可。

![[Pasted image 20240806165413.png]]

* IFACE：本地网卡接口名称
* `[r|t]x[...]/s`：r 代表接收，t 代表发送，表示每秒钟发送或接收的某种数据包
    * pck：数据包
    * KB：KB 为单位的数据包大小（实时网速）
    * cmp：压缩数据包
    * mcst：多播数据包