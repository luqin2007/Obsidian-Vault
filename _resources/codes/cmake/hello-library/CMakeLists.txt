cmake_minimum_required(VERSION 3.9)
project(answer)

add_library(answer STATIC answer.cpp)
add_executable(main main.cpp)

target_link_libraries(main answer)
