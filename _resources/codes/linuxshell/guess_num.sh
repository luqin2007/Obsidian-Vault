#!/bin/bash

# 猜字游戏

num=$[RANDOM % 100]
count=0
while :
do
   read -p "一个 1~100 的数字，你猜是多少？" guess
   count=$[ count+1 ]
   if [ $guess -gt $num ]; then
       echo "Oops, 猜大了"
   elif [ $guess -lt $num ]; then
       echo "Oops, 猜小了"
   else
       echo "恭喜你，猜对了，共猜了 $count 次"
       exit
   fi
done