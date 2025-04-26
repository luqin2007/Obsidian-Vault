Linux Mint 直接安装搜狗输入法无法使用，甚至无法安装。。。

1. `sudo apt install fcitx-data` 安装 fcitx 依赖
2. `sudo apt install fcitx` 安装最新版本的 fcitx
3. 运行官网下载的搜狗输入法安装包，此时可以安装了
4. 使用 Bash，或 `sudo nemo` 将 `/usr/lib/x86_64-linux-gnu/` 目录下 `libQt5` 开头的 `.so` 文件复制到 `/opt/sogoupinyin/files/lib/qt5/lib/` 下，跳过重复文件
5. 重启
