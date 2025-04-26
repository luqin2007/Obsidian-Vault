#!/bin/bash

# 创建命名管道
pipefile=/tmp/procs_$$.tmp
mkfifo $pipefile
exec 12<>$pipefile

# 写入 5 行任意数据，用于控制进程数量
for i in {1..5}
do
    echo "" >&12 &
done

# read -u 从文件描述符中读行
for j in {1..20}
do
    read -u12 # 读一行，当管道空时利用管道阻塞暂停进程创建
    {
        echo -e "\e[32mstart sleep No.$j\e[0m"
        sleep 5
        echo -e "\e[32mstop sleep No.$j\e[0m"
        echo "" >&12 # 进程结束，向管道写回数据
    } &
done
wait