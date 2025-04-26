#!/bin/bash

fibo=(0 1 1)

read -p "请输入第 i 个数：" index

# size=2
# while [ $index -gt $size ]
# do
#     let size+=1
#     let fibo[$size]=fibo[$size-1]+fibo[$size-2]
# done

for ((i=2; i<=index; i++))
do
    let fibo[$i]=fibo[$i-1]+fibo[$i-2]
done

echo "fibo[$index]=${fibo[$index]}"