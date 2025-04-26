read -p "请输入用户名: " user
read -p "请输入密码: " pass

if [ ! -z "$user" ]; then
    useradd "$user"
    # 设置密码
    if [ ! -z "$pass" ]; then
        echo "$pass" | passwd --stdin "$user"
    fi
fi