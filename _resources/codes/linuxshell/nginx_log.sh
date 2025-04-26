#!/bin/bash

# Nginx 标准日志分析脚本
# 1. 页面访问量 PV
# 2. 用户量 UV
# 3. 人均访问量
# 4. 每个 IP 访问次数
# 5. HTTP 状态码统计
# 6. 累计页面流量（字节）
# 7. 热点数据

GREEN='\e[32m'
WHITE='\e[0m'
LINE='echo ++++++++++++++++++++++++++++++++++++++++++'

read -p "请输入日志文件：" logfile
echo

# 统计页面访问量 PV
PV=$(cat $logfile | wc -l)

# 统计用户量 UV（IP）
UV=$(cut -f1 -d' ' $logfile | sort | uniq | wc -l)

# 统计人均访问次量
AveragePV=$(echo "scale=2;$PV/$UV" | bc)

# 统计每个 IP 访问次数
declare -A IP
while read ip other
do
    let IP[$ip]+=1
done < $logfile

# 统计各种 HTTP 状态码
declare -A STATUS
while read ip dash user time zone method file protocol code size other
do
    let STATUS[$code]++
done < $logfile

# 统计页面流量
while read ip dash user time zone method file protocol code size other
do
    let BodySize+=$size
done < $logfile

# 统计热点数据
declare -A URI
while read ip dash user time zone method file protocol code size other
do
    let URI[$file]++
done < $logfile

echo "分析结果："

$LINE
echo -e "累计 PV 量：$GREEN$PV$WHITE"
echo -e "累计 UV 量：$GREEN$UV$WHITE"
echo -e "平均用户访问量：$GREEN$AveragePV$WHITE"

$LINE
echo -e "累计访问字节数：$GREEN$BodySize$WHITE"

$LINE
for i in 200 404 500
do
    if [ ${STATUS[$i]} ]; then
        echo -e "$i 状态码出现次数：$GREEN ${STATUS[$i]} $WHITE"
    else
        echo -e "$i 状态码出现次数：$GREEN 0 $WHITE"
    fi
done

$LINE
for i in ${!IP[@]}
do
    printf "%-15s 的访问次数为：$GREEN%s$WHITE\n" $i ${IP[$i]}
done
echo

echo -e "$GREEN 访问量大于 500 的URI：$WHITE"
for i in "${!URI[@]}"
do
  if [ ${URI[$i]} -gt 500 ]; then
      echo '----------------------------------'
      echo "$i"
      echo "${URI[$i]} 次"
      echo '----------------------------------'
  fi
done