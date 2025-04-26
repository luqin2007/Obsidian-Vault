安装以下包后重启：
- `open-vm-tools`
- `open-vm-tools-desktop`
# Arch

Arch 及其衍生发行版没有 `open-vm-tools-desktop`，无法共享剪贴板是因为缺少 `gtkmm3` 包，参考 [Vmware Install](https://wiki.archlinux.org/title/VMware/Install_Arch_Linux_as_a_guest#Installation)

若无法自动抓取、释放鼠标，可以重装 `x86-input-vmmouse` 后重启