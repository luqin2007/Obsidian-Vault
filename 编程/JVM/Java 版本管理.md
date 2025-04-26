# Arch

通过 `pacman` 安装多个版本 JDK 后，可以通过 `archlinux-java` 命令方便的切换系统默认版本

```shell
# 安装 jdk
sudo pacman -S jdk8-openjdk jdk17-openjdk jdk21-openjdk
```

```console
# 显示 java 版本
$ archlinux-java status
Available Java environments:  
  java-17-openjdk  
  java-21-openjdk (default)  
  java-8-openjdk
# 获取当前默认 java 版本
$ archlinux-java get
java-21-openjdk
# 设置默认 java 版本为 java 17
$ sudo archlinux-java set java-17-openjdk
$ java -version
openjdk version "17.0.12" 2024-07-16  
OpenJDK Runtime Environment (build 17.0.12+7)  
OpenJDK 64-Bit Server VM (build 17.0.12+7, mixed mode, sharing)
# 切换回 21
$ sudo archlinux-java set java-21-openjdk
$ java -version
openjdk version "21.0.4" 2024-07-16  
OpenJDK Runtime Environment (build 21.0.4+7)  
OpenJDK 64-Bit Server VM (build 21.0.4+7, mixed mode, sharing)
```
