#!/bin/bash

# 计算从 1 到 n 的和
read -p "请输入一个正整数: " num
sum=$[num*(num+1)/2]
echo -e "\e[32m$num 以内整数和为 $sum\e[0m"

# 计算三角形面积
read -p "请输入三角形底边长度: " bottom
read -p "请输入三角形高: " height
area=$(echo "scale=1;$bottom*$height/2" | bc)
echo -e "\e[32m三角形面积为 $area\e[0m"

# 计算梯形面积
read -p "请输入梯形上底长度: " top
read -p "请输入梯形下底长度: " bottom
read -p "请输入梯形高: " height
area=$(echo "scale=1;($top+$bottom)*$height/2" | bc)
echo -e "\e[32m梯形面积为 $area\e[0m"

# 计算圆形面积
read -p "请输入圆形半径: " r
area=$(echo "scale=2;3.14*$r^2" | bc)
echo -e "\e[32m圆形面积为 $area\e[0m"

# 空间格式转化
echo "3282820 KiB 等于多少 GiB ?"
G=$(echo "3282820/1024/1024" | bc)
echo -e "\e[32m答案是${G}GB\e[0m"

# 时间格式转化
read -p "请输入秒数: " sec
ms=$[sec*1000]
echo -e "\e[33m${sec}s=${ms}ms\e[0m"
us=$((sec*1000*1000))
echo -e "\e[33m${sec}s=${us}us\e[0m"
hour=$(echo "scale=2;$sec/60/60" | bc)
echo -e "\e[33m${sec}s=${hour}h\e[0m"

