## [【轶哥博文】平板电脑安装Ubuntu教程-以V975w为例，Z3735系列CPU通用](http://wyrme.lofter.com/post/418b24_94ce288#)

最近尝试在昂达V975w平板电脑和intel stick中安装ubuntu，经过分析，发现存在一个非常大的坑。但因为这个坑，此教程适合大部分平板电脑、电脑棒、intel nuc设备安装Ubuntu。

本教程适合以下读者：

1、希望给自己的x86架构设备安装Ubuntu操作系统；

2、希望使用32位的GRUB安装64位Ubuntu操作系统；

3、想折腾Ubuntu系统在平板电脑或intel stick（intel电脑棒）中的使用；

本教程不适合以下读者：

1、纯小白或纯粹不懂Linux命令的爱好者；

2、没有折腾精神的人；

3、非程序员爱好者。

由于部分平板电脑CPU不支持64位的GRUB引导程序，故而导致Ubuntu系统安装失败，而官网的Ubuntu系统只有64位支持UEFI引导。除了WIFI、蓝牙等驱动需要自己上网搜索安装之外，系统主要的安装和引导的具体方法如下：

1、获取IOS镜像文件

方法一：如果你的CPU是Z3735D/F，那么下面这个IOS镜像直接拿去用吧（在官方的基础上增加了驱动，镜像来源：[www.linuxium.com.au](http://www.linuxium.com.au)）：

链接: [http://pan.baidu.com/s/1jGS9ux4](http://pan.baidu.com/s/1jGS9ux4) 密码: qbb6

方法二：如果你的CPU不支持64位引导程序，或者说你想自己手动修改官方的IOS镜像用于在平板电脑中安装系统，请到[官网](http://www.ubuntu.com/)下载镜像文件，需要注意，请勿下载国产修改版，不保证能够支持平板使用。请务必下载64位版本，32位不支持UEFI引导。

2、制作安装U盘

下载完成后下载U盘制作工具：'[Rufus](http://rufus.akeo.ie/?locale=zh_CN)' on Windows or 'dd' on Linux

制作一个UEFI引导的U盘。

3、修改UEFI引导文件

如果使用方法一获取的IOS镜像文件，此步忽略。

下载[bootia32.efi](http://wyr.me/wp-content/uploads/2015/12/bootia32.efi_.zip)文件并解压复制到'EFI\BOOT'目录下。

请先链接USB键鼠，使用快捷键进入BIOS（Z3735通常是DEL/ESC），修改BOOT顺序为UEFI引导的U盘。进入GRUE菜单后选择（Try Ubuntu With Install），如果之前配置的32位引导文件正确，此时你将直接进入Live CD模式的Ubuntu系统。在这个临时系统中的大部分操作都是无效的，不会被保存记录。

此时我们会看到桌面有安装Ubuntu操作系统的快捷方式，先别忙安装，看完这部分内容。点击左上角第一个应用（搜索），搜索"Disks"，进入硬盘管理软件，查看你的本地硬盘。特别提示，在平板电脑或intel stick等小型设备中，通常是SD卡模式，但绝非USB磁盘。通常显示在列表第一项。

![IMG8219](http://wyr.me/wp-content/uploads/2015/12/IMG_8219-1024x768.jpg)[http://wyr.me/wp-content/uploads/2015/12/IMG_8219.jpg](http://wyr.me/wp-content/uploads/2015/12/IMG_8219.jpg)Ubuntu分区查看

由上图我们看到主硬盘所在路径为/dev/mmcblk0。由于我已经安装好了Ubuntu系统，所以看到其中有三个分区，第一个是存储EFI文件的FAT分区，第二个是存储文件的Ext4分区。如果你的平板设备还是win系统，这里应该是NTFS分区+FAT分区。这都不是重点，重点在于需要记录你的主硬盘所在路径“/dev/mmcblk0”还是“/dev/sda1”。

记录后点击桌面的快捷方式安装ubuntu到本地硬盘。

![安装ubuntu](http://wyr.me/wp-content/uploads/2015/12/IMG_8214-1024x768.jpg)[http://wyr.me/wp-content/uploads/2015/12/IMG_8214.jpg](http://wyr.me/wp-content/uploads/2015/12/IMG_8214.jpg)

![安装ubuntu过程](http://wyr.me/wp-content/uploads/2015/12/IMG_8213-1024x768.jpg)[http://wyr.me/wp-content/uploads/2015/12/IMG_8213.jpg](http://wyr.me/wp-content/uploads/2015/12/IMG_8213.jpg)

安装完毕进入下一步。

安装完毕重启我们将发现无法进入到操作系统，而是进入了EFI SHELL模式，早在意料之中，因为这类平板的CPU不支持64位的UEFI引导，但并不意味着不支持64位操作系统。

此时我们还是进入BIOS使用之前的U盘引导启动，进入GRUB菜单后不要选择，点击键盘中的"c"按钮，进入GRUB2命令行模式。

进入该模式后，输入“ls”列出硬盘分区。

此时会看到类似(hd0,gtp1)或(hd1,msdos1)之类的项。这是你的硬盘分区。其中hd0为根目录所在的磁盘，IDE硬盘用hd开始，SCSI硬盘用sd开头。软盘用fd开头。命名和linux不大一样。是从0算起。

我们需要找出linux内核所在分区。

使用"ls (hdX,gtpX)/boot"，其中的“X”请手动替换为上一步出现过的数字，这里肯定要有逗号","的，如果出现一大串结果，显示了你的linux内核文件，说明就是这个分区。记录X的值。

假设你在执行"ls (hd0,gtp2)/boot"的时候出现值，那么下一步执行：

**set root=(hd0,gtp2)**

然后输入需要输入内核路径，“linux /boot/vmlinuz* root=/dev/mmcblk0p2”其中*号为内核版本，输入/boot/vmlinuz后按tab键可以进行自动补全。mmcblk0p2为根目录所在的分区,其中“mmcblk0”是第二步查看分区记录的值，后面的"p2"是我猜的，你顺着p1\p2\p3猜测一下，能执行就对了。完整的命令例子如下：

**linux (hd0,gpt2)/boot/vmlinuz-3.13-xxxx root=/dev/mmcblk0p5**

**initrd /initrd.img**

**boot**

最后成功进入本地Ubuntu系统，这一步如果不成功的话就多尝试一下，修改上面涉及的各个值，祝你好运。

到这步已经成功了一半了，但是没人愿意每次启动都使用USB的GRUB引导并手动输入引导命令，这会很麻烦。进入本地Ubuntu后，调出终端，继续输入如下命令：

**sudo apt-get update**

**sudo apt-get -y purge grub-efi-amd64 grub-efi-amd64-bin grub-efi-amd64-signed**

**sudo apt-get -y install grub-efi-ia32-bin grub-efi-ia32 grub-common grub2-common**

**sudo grub-install --target=i386-efi /dev/mmcblk0p2 --efi-directory=/boot/efi/ --boot-directory=/boot/ 【这里的“mmcblk0p2 ”就是上一步你执行成功的那个值】**

**sudo grub-mkconfig -o /boot/grub/grub.cfg**

![最后一步](http://wyr.me/wp-content/uploads/2015/12/IMG_8216-1024x768.jpg)[http://wyr.me/wp-content/uploads/2015/12/IMG_8216.jpg](http://wyr.me/wp-content/uploads/2015/12/IMG_8216.jpg)执行完毕后重启，发现Ubuntu引导正常，不需要USB引导也可以进入系统。恭喜！安装成功！

![安装Ubuntu成功](http://wyr.me/wp-content/uploads/2015/12/IMG_8217-1024x768.jpg)[http://wyr.me/wp-content/uploads/2015/12/IMG_8217.jpg](http://wyr.me/wp-content/uploads/2015/12/IMG_8217.jpg)

当然了，执行第四步的前提条件是连接了网络，这里需要先安装好网络相关的驱动。如果遇到问题，可以给我留言，我会帮助你的。

高分屏幕在win下很鸡肋，因为windows对高分辨显示屏的支持是在太差了。而Ubuntu不同，使用Ubuntu的显示设置可以像Mac一样进行缩放，有类似Ritina的显示效果，执行各种命令管理远程Linux服务器很赞。

![使用Ubuntu](http://wyr.me/wp-content/uploads/2015/12/IMG_8218-e1450366132111-768x1024.jpg)[http://wyr.me/wp-content/uploads/2015/12/IMG_8218.jpg](http://wyr.me/wp-content/uploads/2015/12/IMG_8218.jpg)
