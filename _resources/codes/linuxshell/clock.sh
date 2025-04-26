#!/bin/bash

# 当 Ctrl+C 时恢复终端光标
trap 'tput cnorm;exit' INT

clear

number=(
'  0000000000      111     2222222222  3333333333 44    44     5555555555  6666666666   777777777   888888888  9999999999 '
'  00      00    11111             22          33 44    44     55          66           77     77   88     88  99      99 '
'  00      00   111111             22          33 44    44     55          66           77     77   88     88  99      99 '
'  00      00       11             22          33 44    44     55          66                  77   88     88  99      99 '
'  00      00       11     2222222222  3333333333 44444444444  5555555555  6666666666          77   888888888  9999999999 '
'  00      00       11     22                  33       44             55  66      66          77   88     88          99 '
'  00      00       11     22                  33       44             55  66      66          77   88     88          99 '
'  00      00       11     22                  33       44             55  66      66          77   88     88          99 '
'  0000000000  1111111111  2222222222  3333333333       44     5555555555  6666666666          77   888888888  9999999999 '
)

# 当前时间
now_time() {
    hour=$(date +%H)
    min=$(date +%M)
    sec=$(date +%S)

    hour_left=`echo $hour/10 | bc`
    hour_right=`echo $hour%10 | bc`
    min_left=`echo $min/10 | bc`
    min_right=`echo $min%10 | bc`
    sec_left=`echo $sec/10 | bc`
    sec_right=`echo $sec%10 | bc`
}

# 打印数字
print_time() {
    begin=$[$1*12]
    for i in `seq 0 ${#number[@]}`
    do
        tput cup $[i+5] $2
        echo -en "\e[32m${number[i]:$begin:12}\e[0m"
    done
}

# 打印时间分隔符
# echo \u 支持 Unicode 符号，\u2588 为一个方块
print_punct() {
    tput cup $1 $2
    echo -en "\e[32m\u2588\e[0m"
}

while :
do
    tput civis
    now_time
    print_time $hour_left 2
    print_time $hour_right 14
    print_punct 8 28
    print_punct 10 28
    print_time $min_left 30
    print_time $min_right 42
    print_punct 8 56
    print_punct 10 56
    print_time $sec_left 58
    print_time $sec_right 70
    echo
    sleep 1
done