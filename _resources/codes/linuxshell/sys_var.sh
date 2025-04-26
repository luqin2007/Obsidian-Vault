#!/bin/bash
echo "当前账户是: $USER，当前账户的 UID 是: $UID"
echo "当前账户的根目录是 $HOME"
echo "当前工作目录是 $PWD"
echo "返回 0-32767 的随机数: $RANDOM"
echo "当前脚本进程号: $$"
echo "当前脚本名称为: $0"
echo "当前脚本共 $# 个参数"
echo "当前脚本第一个参数为 $1"
echo "当前脚本第二个参数为 $2"
echo "当前脚本第三个参数为 $3"
echo "当前脚本的所有参数为 $*"
echo "准备创建一个文件..."
touch "$*"
echo "准备创建多个文件..."
touch "$@"

ls /etc/passwd
echo "我是正确的返回状态码 $?，因为上一条命令执行结果没问题"
ls /etc/pas
echo "我是错误的返回状态码 $?，因为上一条命令执行结果错误，无此文件"

