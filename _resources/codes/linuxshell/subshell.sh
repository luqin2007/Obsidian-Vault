#!/bin/bash

hi="hello"
echo "++++++++++++++"
echo "+我是父 Shell +"
echo "++++++++++++++"
echo "PWD=$PWD"
echo "bash_subshell=$BASH_SUBSHELL"

# 开启子进程
(
    sub_hi="I am a subshell"
    echo -e "\t++++++++++++++"
    echo -e "\t+我是子 Shell +"
    echo -e "\t++++++++++++++"
    echo -e "\tPWD=$PWD"
    echo -e "\tbash_subshell=$BASH_SUBSHELL"
    echo -e "\thi=$hi"
    echo -e "\tsub_hi=$sub_hi"
)

# 返回父进程
echo "++++++++++++++"
echo "+返回父 Shell +"
echo "++++++++++++++"
echo "PWD=$PWD"
echo "bash_subshell=$BASH_SUBSHELL"
echo "hi=$hi"
echo "sub_hi=$sub_hi"