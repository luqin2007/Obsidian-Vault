#!/bin/bash
#Version:2.0
clear
echo -e "\e[42m---------------------------------\e[0m"
echo -e "#\e[2;10H这里是菜单\t\t#"
echo -e "#\e[32m 1.查看网卡信息\e[0m                #"
echo -e "#\e[33m 2.查看内存信息\e[0m                #"
echo -e "#\e[34m 3.查看磁盘信息\e[0m                #"
echo -e "#\e[35m 4.查看CPU信息\e[0m                 #"
echo -e "#\e[36m 5.查看账户信息\e[0m                #"
echo -e "\e[42m---------------------------------\e[0m"
echo

read -p "请输入选项[1~5]: " key
case $key in
1)
    ifconfig | head -2;;
2)
    mem=$(free | grep Mem | tr -s " " | cut -d " " -f7)
    echo "本机内存剩余 ${mem}K.";;
3)
    root_free=$(df | grep /$ | tr -s " " | cut -d " " -f4)
    echo "本机根分区剩余容量为 ${root_free}K.";;
4)
    cpu=$(uptime | tr -s " " | cut -d " " -f13)
    echo "本机 CPU 15min 平均负载为 $cpu.";;
5)
    login_number=$(who | wc -l)
    total_number=$(cat /etc/passwd | wc -l)
    echo "当前系统用户为 $USER"
    echo "当前登录系统的账户数量为 $login_number"
    echo "当前系统的总用户数量为 $total_number";;
*)
    echo "输入有误，超出 1~5 范围"
esac