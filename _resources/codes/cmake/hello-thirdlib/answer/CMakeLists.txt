cmake_minimum_required(VERSION 3.9)
project(libanswer)

add_library(libanswer STATIC answer.cpp)
target_include_directories(libanswer PUBLIC 
                           ${CMAKE_CURRENT_SOURCE_DIR}/include)