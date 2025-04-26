Android 使用 dex 文件字节码，与 JVM 的 class 字节码有所不同，适合基于寄存器的虚拟机。

Android 设备多使用 ARM 结构，采用基于寄存器的虚拟机。Android 的寄存器最多可以有 64k 个。
* 优点：通过寄存器直接访问数据，速度更快
* 缺点：指令必须指定源和目标，指令会更大

> [!note] Android 字节码指令风格偏向 Intel 风格，即将目标操作数放在最前面，赋值方向从右向左。

通过 `dx` 工具可以将 `.class` 文件编译为 `.dex` 文件。与 `.class` 文件不同，Android 将所有源代码文件都编译进一个 `dex` 文件中，且默认使用小端字节序存储。

![[../../../../_resources/images/字节码 2024-08-03 15.41.53.excalidraw]]

```
struct dex {
    header      header_items;      // 文件头
    string_ids  string_id_item[];  // 字符串索引
    type_ids    type_id_item[];    // 类型索引
    proto_ids   proto_id_item[];   // 方法原型索引
    field_ids   field_id_item[];   // 字段索引
    method_ids  method_id_item[];  // 方法索引
    class_Defs  class_def_item[];  // 类定义
    data        ubyte[];           // 数据区
    link_data   ubyte[];           // 链接数据区
};
```
# dex 结构
## `header`

```
struct header {
    ubyte magic[8];       // 魔数
    int   checksum;       // 校验和
    ubyte signature[20];  // 签名
    uint  file_size;      // 文件大小
    uint  header_size;    // 文件头大小
    uint  endian_tag;     // 大小端字节序
    uint  link_size;
    uint  link_off;
    uint  map_off;
    uint  string_ids_size;
    uint  string_ids_off;
    uint  type_ids_size;
    uint  type_ids_off;
    uint  proto_ids_size;
    uint  proto_ids_off;
    uint  field_ids_size;
    uint  field_ids_off;
    uint  method_ids_size;
    uint  method_ids_off;
    uint  class_defs_size;
    uint  class_defs_off;
    uint  data_size;
    uint  data_off;
};
```

* 魔数：`magic`，开头 8 个字节。前 4 个字节是 `dex\n`，后四个字节是 `dex` 版本号（如 `035\0`）
* 校验和：`checksum`，4 个字节，使用 Adler-32 算法计算的校验和
* 签名：`signature`，20 个字节，dex 文件签名，由 SHA-1 算法生成（不包括前 32 个字节），判断 dex 是否被修改和校验合法性
* 文件大小：`file_size`，4 个字节，dex 文件长度（低位在前，高位在后）
* 文件头大小：`header_size`，4 字节，header 区长度，一般为 0x70（112）
* 大小端字节序：`endian_tag`，默认 0x12345678，表示小端字节序；若为 0x87654321 表示大端字节序
* 其他字段：string，type，field，method，class，data 区域偏移量
## `string_ids`

根据 `string_ids_offset` 找到字符串常量池位置，第一个字节是字符串长度，第二个是字节是指字符串长度类型，0x3C 指 LEB128，字符串最后以 `00` 结尾

> [!note] LEB128
> Little Endian Based 128，小端字节序、变长的编码格式，有利于减小 dex 文件体积
## `type_ids`

存储 `dex` 中所有类型相关信息，`type_ids_offset` 每个指向地址后每个值代表一个 `string_ids` 数组下标

```
struct type_id_item {
    uint descriptor_idx;
};
```
## `proto_ids`

存储 `dex` 中所有方法的原型列表。

```
struct proto_id {
    uint shorty_idx;       // 方法原型简写
    uint return_type_idx;  // 返回值类型描述符
    uint parameters_off;   // 方法参数类型描述符偏移量
};
```

方法类型简写：返回值+形参列表，无括号，引用类型使用 L，例如

|方法|简写|描述符|
| -----------------------------| ------| -----------------------------------------|
|void foo()|V|()V|
|String foo()|L|()Ljava/lang/String;|
|String foo(int x, String y)|LIL|(ILjava/lang/String;)Ljava/lang/String;|
|void foo(int x, String y)|VIL|(ILjava/lang/String;)V|
## `field_ids`

存储所有字段信息的数组

```
struct field_ids {
    ushort class_idx;  // 所在类名，指向 type_ids 的索引
    ushort type_idx;   // 变量类型，指向 types_ids 的索引
    uint   name_idx;   // 变量名，指向 string_ids 的索引
};
```
## `method_ids`

存储所有方法信息的数组

```
struct method_ids {
    ushort class_idx;  // 所在类名，指向 type_ids 的索引
    ushort proto_idx;  // 方法原型，指向 proto_ids 的索引
    uint   name_idx;   // 变量名，指向 string_ids 的索引
};
```
## `class_defs`

存储类信息的数组

````tabs
tab: class_def_item
```
struct class_def_item {
    uint class_idx;         // class 类型，指向 type_ids 的索引
    uint access_flags;      // 类访问修饰符
    uint superclass_idx;    // 基类类型，指向 type_ids 的索引
    uint interfaces_off;    // 实现接口的数据区域的偏移量 or 0
    uint source_file_idx;   // 源文件信息（源文件名），指向 string_ids 的索引
    uint annotations_off;   // 注解数据区域的偏移量 or 0
    uint class_data_off;    // 类详细信息，class_data_item，指向 data 区域的偏移量
    uint static_values_off; // 静态变量初始值偏移量，指向 data 区域
};
```

tab: class_data_item
```
struct class_data_item {
    uleb128 static_field_size;    // 静态字段数量
    uleb128 instance_fields_size; // 非静态成员变量数量
    uleb128 direct_method_size;   // 非虚方法数量
    uleb128 virtual_method_size;  // 虚方法数量
    encoded_field[static_field_size]    static_fields;
    encoded_field[instance_field_size]  instance_fields;
    encoded_method[direct_method_size]  direct_methods;
    encoded_method[virtual_method_size] virtual_methods;
};
```

tab: encoded_method
```
struct encoded_method {
    uleb128 method_idx_diff; // 指向 method_ids 列表的索引
    uleb128 access_flags;    // 访问修饰符
    uleb128 code_off;        // 指向 data 区偏移量，code_item 类型，代码细节
};
```

tab: code_item
```
struct code_item {
    ushort                     registers_size;  // 所需寄存器个数
    ushort                     ins_size;        // 入参占用空间大小
    ushort                     outs_size;       // 调用其它参数传参所需空间大小
    ushort                     tries_size;      // try 块数量
    uint                       debug_info_off;  // 调试信息偏移量，记录行号、局部变量等信息
    uint                       insns_size;      // 方法指令码数组长度
    ushort[insns_size]         insns;           // 方法指令码数组
    ushort                     padding;         // 指令对齐，可选
    try_item[tries_size]       tries;           // try 块信息
    encoded_catch_handler_list handlers;        // 各个 handler 块信息
};
```
````
dex 字节码与 class 字节码有所不同。以下面的代码为例，其中 `v0`、`v1` 是寄存器，`v1` 寄存器存储入参：

```java
static boolean isEmpty(String s) {
    return s == null || s.length() == 0;
}
```

```
0000 if-eqz v1, 0008                                   // if (v1==null) goto 0008
0002 invoke-virtual {v1}, Ljava/lang/String;.length()I // v1.length();
0005 move-result v0                                    // v0 = v1.length()
0006 if-nez v0, 000a                                   // if (v0!=0) goto 000a
0008 const/4 v0, #int 1                                // v0 = 1
0009 return v0                                         // return v0
000a const/4 v0, #int 0                                // v0 = 0
000b goto 009                                          // goto 0009
```
## `data`

数据区，存储 dex 文件大部分重要数据内容
## `link_data`

静态链接文件中使用的数据
# 实例

```java
public int foo(int x, int y) {
    int z = x + y;
    return 2 * z;
}
```

````tabs
tab: JVM 字节码
```java
public int foo(int, int):
  iload_1
  iload_2
  iadd
  istore_3
  iconst_2
  iload_3
  imul
  ireturn
```

tab: Android 字节码
```java
foo:(II)I
  add-int v0, v2, v3
  mul-int/lit8 v0, v0, #int 2
  return v0
```
````
![[../../../../_resources/images/字节码 2024-08-03 15.47.30.excalidraw]]
## 条件跳转

```java
public int foo(int x) {
    if (x >= 0) {
        return x;
    } else {
        return -x;
    }
}
```

```java
DexByteCodeTest.foo:(I)I
0000: if-ltz v1, 0003  // if 寄存器 v1 小于 0 则跳转到 0003
0002: return v1        // 返回寄存器 v1 的值
0003: neg-int v1, v1   // v1 = -v1
0004: goto 0002        // 跳转到 0002
```
## 循环

```java
public static int foo() {
    int sum = 0;
    for (int i = 0; i < 10; i++) {
        sum += i;
    }
}
```

```java
DexByteCodeTest.foo:()I
0000: const/4 v1, #int 0           // 寄存器 v1 值 = 0
0001: move v0, v1                  // 寄存器 v0 值 = v1（0）
0002: move v2, v1                  // 寄存器 v2 值 = v1（0）
0003: const/16 v1, #int 10         // 寄存器 v1 值 = 10
0005: if-ge v0, v1, 000d           // if v0 > v1 跳转到 000d（循环条件）
0007: add-int v1, v2, v0           // v1 = v2 + v0
0009: add-int/lit8 v0, v0, #int 1  // v0 = v0 + 1
000b: move v2, v1                  // v2 = v1
000c: goto 0003                    // 跳转到 0003
000d: return v2                    // 返回 v2 值
```
## switch
### 紧凑 switch

```java
static int chooseNear(int i) {
    switch (i) {
        case 100: return 0;
        case 101: return 1;
        case 104: return 4;
        default:  return -1;
    }
}
```

```
DexByteCodeTest.chooseNear:(I)I
0000: packed-switch v1, 0000000c // 指向一个 packed-switch-payload 结构
0003: const/4 v0, #int -1
0004: return v0                  // default 分支
0005: const/4 v0, #int 0
0006: goto 0004                  // 100 分支
0007: const/4 v0, #int 1
0008: goto 0004                  // 101 分支
0009: const/4 v0, #int 4
000a: goto 0004                  // 104 分支
000b: nop
000c: packed-switch-payload
```

```c
struct packed-switch-payload {
    ushort ident;      // 0x0100
    ushort size;       // case 数量
    int    first_key;  // 起始 switch 值
    int[]  targets;    // 所有 case 选项的代码地址
                       //  [0] = 0x05
                       //  [1] = 0x07
                       //  [2] = 0x03
                       //  [3] = 0x03
                       //  [4] = 0x09
}
```
### 稀疏 switch

```java
static int chooseFar(int i) {
    switch (i) {
        case 1  : return 1;
        case 10 : return 10;
        case 100: return 100;
        default:  return -1;
    }
}
```

```
DexByteCodeTest.chooseFar:(I)I
0000: sparse-switch v1, 0000000e // 指向一个 sparse-switch-data 结构
0003: const/4 v0, #int -1
0004: return v0                  // default 分支
0005: const/4 v0, #int 1
0006: goto 0004                  // 1 分支
0007: const/16 v0, #int 10
0009: goto 0004                  // 10 分支
000a: const/16 v0, #int 100
000c: goto 0004                  // 100 分支
000d: nop
000e: sparse-switch-data (14 units)
```

```c
struct packed-switch-payload {
    ushort ident;    // 0x0200
    ushort size;     // case 数量
    int[]  keys;     // 升序排列的 case 值
                     //  [1, 10, 100]
    int[]  targets;  // 所有 case 选项的代码地址
                     //  [0] = 0x05
                     //  [1] = 0x07
                     //  [2] = 0x0A
}
```
## try-catch

```java
public static void foo() {
    try {
        tryItOut();
    } catch (MyException1 e) {
        handleException(e);
    } catch (MyException2 e) {
        handleException(e);
    }
}
```

```
DexByteCodeTest.foo:()V
0000: invke-static {}, DexByteCodeTest.tryItOut:()V
0003: return void
0004: move-exception v0                                                      // 将异常信息放到寄存器 v0，启动异常处理流程
0005: invoke-static {v0}, DexByteCodeTest.handleException:(LMyException1;)V
0008: goto 0003
0009: move-exception v0                                                      // 将异常信息放到寄存器 v0，启动异常处理流程
000a: invoke-static {v0}, DexByteCodeTest.handleException:(LMyException2;)V
000d: goto 0003

tries:
  try 0000..0003
  catch MyException1 -> 0004
        MyException2 -> 0009
handlers:
  size: 0001
  0001: catch MyException1 -> 0004
              MyException1 -> 0009
```

在 `code_item` 结构中，`try` 和 `catch_handle` 的结构如下：

```
struct try_item {
    uint   start_addr;   // try 块起始地址
    ushort insn_count;   // 覆盖字节码语句数量
    ushort handler_off;  // 处理 handler 的偏移量
};
```

```
struct encoded_catch_handler_list {
    uleb128 size;
    eccoded_catch_handler[handlers_size];
};
```
