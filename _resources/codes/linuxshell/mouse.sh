#!/bin/bash

trap 'proc_exit' EXIT INT

# 游戏地图为屏幕 1/4 区域
lines=`tput lines`
column=`tput cols`
left=2
right=$[column/2]
top=2
bottom=$[lines/2]

# 绘制长方形游戏地图
draw_map() {
    # 保存中断状态
    save_property=$(stty -g)
    
    tput clear
    tput civis
    echo -e "\e[1m\n\t按 w(上), s(下), a(左), d(右), q(退出) 键, 控制笑脸抓住随机出现的小老鼠.\e[0m"
    for y in `seq $top $bottom`
    do
        local x=$left
        if [[ $y -eq $top || $y -eq $bottom ]]; then
            # 首位填满 #
            while [ $x -le $right ]
            do
                tput cup $y $x
                echo -ne "\e[37;42m#\e[0m"
                let x++
            done
        else
            # 其他位置仅在开始和结束填 #
            for m in $left $right
            do
                tput cup $y $m
                echo -ne "\e[37;42m#\e[0m"
            done
        fi
    done
    echo
}

# 在地图中填充空格，用于清屏
clear_screen() {
    for((i=3;i<=$[bottom-1]; i++))
    do
        space=""
        for((j=3;j<=$[right-1]; j++))
        do
            space=${space}" "
        done
        tput cup $i 3
        echo -n "$space"
    done
}

# 绘制老鼠
draw_mouse() {
    tput cup $1 $2
    echo -en "\U1f42d"
}

# 绘制玩家（笑脸）
draw_player() {
    tput cup $1 $2
    echo -en "\U1f642"
}

# 退出函数
proc_exit() {
    tput cnorm
    stty $save_property
    echo "Game Over."
    exit
}

# 主函数
get_key() {
    # 初始坐标
    man_x=4
    man_y=4

    while :
    do
        tmp_col=$[right-2]
        tmp_line=$[bottom-1]
        rand_x=$[RANDOM%(tmp_col-left)+left+1]
        rand_y=$[RANDOM%(tmp_line-top)+top+1]
        draw_player $man_y $man_x
        draw_mouse $rand_y $rand_x
        if [[ $man_x -eq $rand_x && $man_y -eq $rand_y ]]; then
            proc_exit
        fi

        stty -echo
        read -s -n 1 input
        if [[ $input == "q" || $input == "Q" ]]; then
            proc_exit
        elif [[ $input == "w" || $input == "W" ]]; then
            let man_y--
            [[ $man_y -le $top || $man_y -ge $bottom ]] && proc_exit
            draw_player $man_y $man_x
        elif [[ $input == "s" || $input == "S" ]]; then
            let man_y++
            [[ $man_y -le $top || $man_y -ge $bottom ]] && proc_exit
            draw_player $man_y $man_x
        elif [[ $input == "a" || $input == "A" ]]; then
            let man_x--
            [[ $man_x -le $left || $man_x -ge $right ]] && proc_exit
            draw_player $man_y $man_x
        elif [[ $input == "d" || $input == "D" ]]; then
            let man_x++
            [[ $man_x -le $left || $man_x -ge $right ]] && proc_exit
            draw_player $man_y $man_x
        fi
        clear_screen
    done
}

draw_map
get_key
