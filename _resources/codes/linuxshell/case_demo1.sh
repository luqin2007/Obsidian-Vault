#!/bin/bash

# 使用 case 进行字母匹配

read -p "请输入一个字母: " key
case $key in
a)
    echo "I am a.";;&
b)
    echo "I am b.";;
a)
    echo "I am aa.";;&
c)
    echo "I am c.";;
a)
    echo "I am aaa.";;
*)
    echo "Out of range.";;
esac