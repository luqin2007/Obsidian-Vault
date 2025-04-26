#!/bin/bash

# 性能变量
local_time=$(date +"%Y%m%d %H:%M:%S")
# ipconfig eth0 ...
# grep ->         inet 169.254.23.183  netmask 255.255.0.0
# tr   ->  inet 169.254.23.183 netmask 255.255.0.0
# cut  -> 169.254.23.183
local_ip=$(ifconfig eth0 | grep netmask | tr -s " " | cut -d " " -f3)
# /proc/meminfo ...
# grep -> MemAvailable:     743792 kB
# tr   -> MemAvailable: 727840 kB
# cut  -> 727840
free_mem=$(cat /proc/meminfo | grep Avai | tr -s " " | cut -d " " -f2)
# ** WSL2 没有 MemAvailable 字段，使用 MemFree **
[[ $free_mem == '' ]] && free_mem=$(cat /proc/meminfo | grep MemFree | tr -s " " | cut -d " " -f2)
# df ...
# grep -> rootfs         499781956 109141876 390640080  22% /
# tr   -> rootfs 499781956 109142152 390639804 22% /
# cut  -> 390639804
free_disk=$(df | grep "/$" | tr -s " " | cut -d " " -f4)
# /proc/loadavg -> 0.52 0.58 0.59 1/9 280
# cut           -> 0.59
cpu_load=$(cat /proc/loadavg | cut -d " " -f3)
login_user=$(who | wc -l)
procs=$(ps aux | wc -l)
# vmstat ...
# ** WSL2 无法使用 vmstat **
# ** 每次运行，根据系统当前状态不同，结果不同 **
# tail ->  0  0      0 132780  78864 677832    0    0     0     0 1399 2237  1  2 97  0  0
# tr   ->  3 0 0 97864 78968 677536 0 0 416 888 3837 4815 39 29 31 1 0
# cut  -> 1374
     irq=$(vmstat 1 2 | tail -n +4 | tr -s ' ' | cut -d ' ' -f12) # 中断
      cs=$(vmstat 1 2 | tail -n +4 | tr -s ' ' | cut -d ' ' -f13) # 上下文切换
usertime=$(vmstat 1 2 | tail -n +4 | tr -s ' ' | cut -d ' ' -f14) # 用户态
 systime=$(vmstat 1 2 | tail -n +4 | tr -s ' ' | cut -d ' ' -f15) # 内核态
  iowait=$(vmstat 1 2 | tail -n +4 | tr -s ' ' | cut -d ' ' -f17) # IO 等待时间

# 报警
# ** WSL2 没有 mail 命令 **
# 内存不足 1G 时向 root 发送邮件报警
[ $free_mem -lt 1048576 ] && \
    echo "$local_time Free memory not enough.
    free_mem: $free_mem on $local_ip" | \
    mail -s Warning root@localhost
# 硬盘空间不足 10G 时向 root 发送邮件报警
[ $free_disk -lt 10485760 ] && \
    echo "$local_time Free disk not enough.
    root_free_disk: $free_disk on $local_ip" | \
    mail -s Warning root@localhost
# CPU 15min 平均负载超过 4 时向 root 发送邮件报警
result=$(echo "$cpu_load > 4" | bc)
[ $result -eq 1 ] && \
    echo "$local_time CPU load too high.
    CPU 15 average load: $cpu_load on $local_ip" | \
    mail -s Warning root@localhost
# 当前系统在线人数超过 3 时向 root 发送邮件报警
[ $login_user -gt 3 ] && \
    echo "$local_time Too many user.
    $login_user users login to $local_ip" | \
    mail -s Warning root@localhost
# 实时进程数超过 500 时向 root 发送邮件报警
[ $procs -gt 500 ] && \
    echo "$local_time Too many procs.
    $procs procs running on $local_ip" | \
    mail -s Warning root@localhost
# 实时 CPU 中断数超过 5000 时向 root 发送邮件报警
[ $irq -gt 5000 ] && \
    echo "$local_time Too many interupts.
    There are $irq interupts on $local_ip" | \
    mail -s Warning root@localhost
# 实时 CPU 上下文切换数超过 5000 时向 root 发送邮件报警
[ $cs -gt 5000 ] && \
    echo "$local_time Too many Context Switches.
    $cs of context switches per second on $local_ip" | \
    mail -s Warning root@localhost
# 用户态进程 CPU 占用超过 70% 时向 root 发送邮件报警
[ $usertime -gt 70 ] && \
    echo "$local_time CPU load too high.
    Time spend running non-kernal code: $usertime% on $local_ip" | \
    mail -s Warning root@localhost
# 内核态进程 CPU 占用超过 70% 时向 root 发送邮件报警
[ $systime -gt 70 ] && \
    echo "$local_time CPU load too high.
    Time spend running kernal code: $systime% on $local_ip" | \
    mail -s Warning root@localhost
# CPU 消耗大量时间等待磁盘 I/O 时向 root 发送邮件报警
[ $iowait -gt 40 ] && \
    echo "$local_time Disk too slow.
    CPU spend too many time wait disk I/O: $iowait on $local_ip" | \
    mail -s Warning root@localhost