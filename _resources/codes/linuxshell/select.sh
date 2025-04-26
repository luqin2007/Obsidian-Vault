#!/bin/bash

echo "请选择一个选项："

select item in "CPU" "IP" "MEM" "exit"
do
    case $item in
    "CPU")
        uptime;;
    "IP")
        ip a s;;
    "MEM")
        free;;
    "exit")
        exit;;
    *)
        echo error;;
    esac
done