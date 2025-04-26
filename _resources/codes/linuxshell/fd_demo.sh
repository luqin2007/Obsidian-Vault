#!/bin/bash

# 创建测试文件
touch test.txt
ls -l /proc/$$/fd  # 查看所有使用的文件描述符
exec 12>test.txt   # 创建只写的文件描述符 12

echo hello >&12    # 写测试
echo "world" >&12
cat test.txt

cat <&12           # 读测试

exec 12<&-         # 关闭描述符
echo "jacob" >&12  # 关闭后写测试

# 删除测试文件
rm ./test.txt