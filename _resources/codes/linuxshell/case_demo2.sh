#!/bin/bash

# 匹配 y | yes，不区分大小写
# 匹配 n | no，不区分大小写

read -p "确定？[y|n]" input
case $input in
[Yy] | [Yy][Ee][Ss])
    echo "你选择的是 yes.";;
[Nn] | [Nn][Oo])
    echo "你选择的是 no.";;
*)
    echo "无效输入";;
esac