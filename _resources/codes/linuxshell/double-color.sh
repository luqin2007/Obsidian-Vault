#!/bin/bash

RED="\e[91m"
BLUE="\e[34m"
NONE="\e[0m"
red_ball=""

while :
do
    clear
    echo "--选双色球--"

    code=$[RANDOM%33+1]
    echo $red_ball | grep -w $code && continue
    red_ball+=" $code"
    echo -en "$RED$red_ball$NONE"
    
    count=$(echo $red_ball | wc -w)
    if [ $count -eq 6 ]; then
        echo -e " $BLUE$[RANDOM%16+1]$NONE"
        break
    fi
done

