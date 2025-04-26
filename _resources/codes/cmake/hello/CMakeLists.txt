# CMake 最低版本
cmake_minimum_required(VERSION 3.9)
# 项目名
project(answer)

# 可执行文件, 自动依赖头文件 answer.hpp
add_executable(answer main.cpp answer.cpp)