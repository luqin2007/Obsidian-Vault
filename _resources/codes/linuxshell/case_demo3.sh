#!/bin/bash

# 开启扩展通配符支持
shopt -s extglob

read -p "输入：" key
case $key in
?([Nn])o)
    echo "输入了 [Nn]o 或 o";;
+([Yy]))
    echo "输入了至少 1 个[Yy]";;
t*(o))
    echo "输入了 t, to, too, tooo...";;
@([0-9]))
    echo "输入了单个数字";;
!([[:punct:]]))
    echo "输入的不是标点";;
*)
    echo "输入的是其他东西";;
esac