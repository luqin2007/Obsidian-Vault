> [!error] yay: error while loading shared libraries: libalpm.so.14: cannot open shared object file : No such file or directory

`yay` 更新错误，需要重新安装 `yay`

```shell
sudo pacman -S --needed git base-devel
git clone https://aur.archlinux.org/yay.git
cd yay
makepkg -si
```
