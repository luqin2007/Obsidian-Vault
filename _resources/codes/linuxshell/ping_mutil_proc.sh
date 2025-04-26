#!/bin/bash

# 测试某个网段的所有主机的连通性

net="192.168.4"

for i in $(seq 254)
do
(
    addr="$net.$i"
    ping -c2 -i0.2 -W1 $addr &>/dev/null
    if [ $? -eq 0 ]; then
        echo "$addr is up."
    else
        echo "$addr is down."
    fi
) &
done
wait