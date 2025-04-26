```sh
# 安装 Apache2 服务器
sudo apt install apache2
# 挂载硬盘并设置权限
sudo fdisk -l                                      # 查看硬盘路径，我硬盘为 /dev/sdb1
sudo mkdir /var/www/webdav                         # 创建挂载目录
sudo mount /dev/sdb1 /var/www/webdav
sudo chown -R www-data:www-data /var/www/webdav
# 配置 Apache2
sudo htpasswd -c /etc/apache2/webdav.passwd lq2007 # 用户名、密码 P2zaAXJzCSkd_aDmiw53
sudo nano /etc/apache2/sites-available/webdav.conf # 配置 WebDAV 服务器
sudo a2ensite webdav.conf                          # 加载配置
sudo a2dissite 000-default.conf                    # 卸载默认配置
sudo a2enmod dav                                   # 加载 dav 模块
sudo a2enmod dav_fs
sudo systemctl restart apache2
```

```xml title:/etc/apache2/sites-available/webdav.conf
<VirtualHost *:80>
    ServerAdmin lq2007lq@hotmail.com
    ServerName example.com
    DocumentRoot /var/www

    <Directory /var/www/>
        Options Indexes FollowSymLinks MultiViews
        AllowOverride None
        Require all granted
    </Directory>

    Alias /webdav /var/www/webdav

    <Location /webdav>
        DAV On
        AuthType Basic
        AuthName "WebDAV"
        AuthUserFile /etc/apache2/webdav.passwd
        Require valid-user
    </Location>
</VirtualHost>
```

- Windows：可以直接挂载网络驱动器（需要修改注册表解除 https 限制），也可以使用 Alist/NetMount/Raidrive 等挂载
- Linux：使用 `davfs` 挂载