1. 安装中文字体

```shell
sudo pacman -S wqy-microhei ttf-dejavu
```

2. 添加中文语言

编辑 `/etc/locale.conf`，取消 `#zh_CN.UTF-8` 和 `#zh_CN.GB18030` 前的注释，然后执行 `locale-gen`

```shell
sudo locale-gen
```

3. 设置全局英文，防止 `tty` 乱码

```shell title:/etc/locale.conf
export LANG=en_US.UTF-8
```

4. 配置环境变量：`~/.xinitrc`，`~/.xprofile` 最前面设置中文，重启

```shell title:"~/.xinitrc, ~/.xprofile"
export LANG=zh_CN.UTF-8
```
# 参考

```cardlink
url: https://wiki.archlinuxcn.org/zh-hans/%E7%AE%80%E4%BD%93%E4%B8%AD%E6%96%87%E6%9C%AC%E5%9C%B0%E5%8C%96
title: "简体中文本地化 -  Arch Linux 中文维基"
host: wiki.archlinuxcn.org
favicon: https://wiki.archlinuxcn.org/wzh/images/logo.svg
```
