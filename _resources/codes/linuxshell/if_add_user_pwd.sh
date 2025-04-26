read -p "请输入用户名: " user
read -p "请输入密码: " pass

# [ ! -z "$user" -a ! -z "$pass" ]
if [ ! -z "$user" ] && [ ! -z "$pass" ]; then
    useradd "$user"
    echo "$pass" | passwd --stdin "$user"
fi