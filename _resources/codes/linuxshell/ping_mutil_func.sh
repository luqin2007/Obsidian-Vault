#!/bin/bash

# 测试某个网段的所有主机的连通性

net="192.168.4"

function multi_ping() {
    ping -c2 -i0.2 -W1 $net.$1 &>/dev/null
    if [ $? -eq 0 ]; then
        echo "$net.$1 is up."
    else
        echo "$net.$1 is down."
    fi
}

for i in $(seq 254)
do
    multi_ping $i &
done
wait