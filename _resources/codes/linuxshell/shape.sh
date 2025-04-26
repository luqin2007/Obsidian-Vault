#!/bin/bash

# 像素三角

for i in $(seq 6)
do
    for j in $(seq $i)
    do
        echo -ne "\e[101m  \e[0m"
    done
    echo
done

for(( i=1; i<=6; i++ ))
do
    for(( j=6; j>=i; j-- ))
    do
        echo -ne "\e[101m  \e[0m"
    done
    echo
done

# 棋盘

for((i=1; i<=8; i++))
do
    for((j=1; j<=8; j++))
    do
        if [[ $[(i+j)%2] -ne 0 ]];then
            echo -ne "\e[41m  \e[0m"
        else
            echo -ne "\e[47m  \e[0m"
        fi
    done
    echo
done

# 乘法表

for ((i=1; i<=9; i++))
do
    for ((j=1; j<=i; j++))
    do
        printf "\e[4%dm %d*%d=%2d \e[0m " $[j%8] $i $j $[i*j]
    done
    echo
done