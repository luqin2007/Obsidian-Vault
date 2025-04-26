#!/bin/bash

# 结束进度条
trap 'kill $!' INT

# 动态光标字符
rotate="|/-\\"

bar() {
    # 打印一个空格，目的是换行
    printf ' '
    
    while :
    do
        for((i=47;i>=1;i--))
        do
            printf "\b%.1s" "$rotate" # 格式化输出
            rotate=${rotate#?}${rotate%???}
            sleep 0.2
        done
    done
}

bar &    # 开始进度条
sleep 10 # 模拟工作阻塞 47*0.2 约为 10

# 结束
kill $!
echo -e "\b任务结束"