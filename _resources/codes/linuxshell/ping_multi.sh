#!/bin/bash

# 测试某个网段的所有主机的连通性

function multi_ping() {
    ping -c2 -i0.2 -W1 $net.$1 &>/dev/null
    if [ $? -eq 0 ]; then
        echo "$net.$1 is up."
    else
        echo "$net.$1 is down."
    fi
}

num=100 # 最大进程
net="192.168.4"
pipefile=/tmp/multiping_$$.tmp

mkfifo $pipefile
exec 12<>$pipefile
for i in `seq $num`
do
    echo "" >&12 &
done

for i in $(seq 254)
do
    read -u12
    {
        multi_ping $i
        echo "" >&12
    } &
done
wait

rm -rf $pipefile
