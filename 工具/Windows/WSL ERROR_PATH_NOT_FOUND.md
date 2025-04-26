> [!error] 无法将磁盘“ext4.vhdx”附加到 WSL2： 系统找不到指定的路径。 
> Error code: Wsl/Service/CreateInstance/MountVhd/HCS/ERROR_PATH_NOT_FOUND Press any key to continue.

更新 WSL 导致原本镜像丢失

```sh
wsl --unregister 发行名
wsl --install -d 发行名
```

![[../../_resources/images/Pasted image 20250207231152.png]]