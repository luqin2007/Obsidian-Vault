使用 `javac` 将源文件编译成 `.class`  文件，使用十六进制工具即可打开查看。也可以或通过 `javap` 反编译，或通过 `jclasslib` 等可视化工具直接查看 `.class`  文件。

![[Pasted image 20240306012524-20240312004140-44tyf0g.png]]
class 字节码包含以下几种基本数据结构：

| 类型    | 说明                                      |
| ----- | --------------------------------------- |
| u1    | 1 字节无符号整型                               |
| u2    | 2 字节无符号整型                               |
| u4    | 4 字节无符号整型                               |
| table | 表，保存相同类型数据的变长结构，由一个 u2 长度和一组连续的其他类型数据组成 |
| 结构体   | 存储一组数据的数据结构，类似 C 的结构体                   |

![[Pasted image 20230824222144-20240312000918-xelkq3m.png]]

字节码文件主要有下面几个部分组成：


| 变量名                                        | 类型                   | 作用              |
| ------------------------------------------ | -------------------- | --------------- |
|  `magic`                                 |  `u4`              | [[魔数]]          |
|  `minor_version`                         |  `u2`              | [[版本号]]         |
|  `major_version`                         |  `u2`              |                 |
|  `constant_pool_count`                   |  `u2`              | [[常量池]]         |
|  `constant_pool[constant_pool_count-1]`  |  `cp_info`         |                 |
|  `access_flags`                          |  `u2`              | [[继承关系\|类访问标记]] |
|  `this_class`                            |  `u2`              | 类索引             |
|  `super_class`                           |  `u2`              | 超类索引            |
|  `interfaces_count`                      |  `u2`              | 接口表索引           |
|  `interfaces[interfaces_count-1]`        |  `u2`              |                 |
|  `fields_count`                          |  `u2`              | [[字段表]]         |
|  `fields[fields_count-1]`                |  `field_info`      |                 |
|  `methods_count`                         |  `u2`              | [[方法表]]         |
|  `methods[methods_count-1]`              |  `method_info`     |                 |
|  `attributes_count`                      |  `u2`              | [[属性表]]         |
|  `attributes[attributes_count]`          |  `attribute_info`  |                 |
