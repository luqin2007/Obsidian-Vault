#!/bin/bash

# 候选字符集：大小写字符和数字
key="qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789"

randpass() {
    if [ -z "$1" ]; then
        echo "randpass 需一个参数指定提取的随机数个数"
        return 127
    fi

    # 生成密码
    pass=""
    for i in `seq $1`
    do
        num=$[RANDOM%${#key}]
        local tmp=${key:num:1}
        pass=${pass}${tmp}
    done
    echo $pass
}

# 测试
randpass 8
randpass 16