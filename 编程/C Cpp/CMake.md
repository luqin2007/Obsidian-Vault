
根据 `CMakeLists` 生成构建脚本：`cmake -B build`
- -B：指定构建目录
- -G：指定构建工具

构建：`cmake --build build`
# CMakeLists

CMake 脚本位于 `CMakeLists.txt` 中，基本结构为：

```cmake
# 设置 CMake 最低版本
cmake_minimum_required(VERSION <minimum-cmake-version>)
# 设置项目名
project(<project-name>)

# 添加可执行文件
add_executable(<target> <dependencies>)
```

> [!note] `${CMAKE_CURRENT_SOURCE_DIR}`：环境变量，表示当前编译的项目目录
# 依赖库

使用 `add_library` 添加库依赖

```cmake
# 添加库
add_library(<lib-name> <lib-type> <files>...)
# 链接
target_link_libraries(<target> <lib-names>)
```

- `lib-type`：库类型，如静态库 `STATIC` 等

```reference
file: "@/_resources/codes/cmake/hello-library/CMakeLists.txt"
lang: "cmake"
```

使用 `find_package` 查找系统中安装的第三方库

```cmake
find_package(<name> [<version>] [REQUIRE])
```

- `<version>` 可选，可以指定一个版本
- `REQUIRE` 可选，需要指定库必须存在
# 子项目

子项目位于单独的子目录，具有各自的 `CMakeLists.txt`。通过 `add_subdirectory` 执行子项目的 `CMakeLists.txt`

```reference title:CMakeLists.txt
file: "@/_resources/codes/cmake/hello-subdir/CMakeLists.txt"
lang: "cmake"
start: 4
end: 4
```

通过 `target_include_directories` 包含子项目头文件，使用时不需要指定子路径

```cmake
target_include_directories(<lib-name> <range> <include-dirs...>)
```

- `<lib-name>`：目标名，必须是 `add_library` 或 `add_executable` 添加的名称
- `range`：头文件的影响范围
	- `PRIVATE`：`include_directories`，仅当前项目中可直接访问
	- `INTERFACE`：`interface_directories`，仅依赖此项目的其他项目可以直接访问
	- `PUBLIC`：`include_directories` + `interface_directories`

> [!example] 
> ```dirtree
> - answer
>   - include
>     - answer.hpp
>   - answer.cpp
>   - CMakeLists.txt
> - main.cpp
> - CMakeLists.txt
> ```

```reference title:answer/CMakeLists.txt
file: "@/_resources/codes/cmake/hello-subdir/answer/CMakeLists.txt"
lang: "cmake"
start: 4
end: 6
```

```reference title:main.cpp
file: "@/_resources/codes/cmake/hello-subdir/main.cpp"
start: 2
end: 2
```

```reference title:answer/answer.cpp
file: "@/_resources/codes/cmake/hello-subdir/answer/answer.cpp"
start: 1
end: 1
```

