#!/bin/bash

# 结束进度条
trap 'kill $!' INT

bar() {
    while :
    do
        pound=""
        for((i=47;i>=1;i--))
        do
            pound+="#"
            printf "|%s%${i}s|\r" "$pound" # 格式化输出
            sleep 0.2
        done
    done
}

bar &    # 开始进度条
sleep 10 # 模拟工作阻塞 47*0.2 约为 10

# 结束
kill $!
echo "任务结束"