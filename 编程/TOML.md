TOML 全称为 Tom's Obvious, Minimal Language，文件使用 `.toml` 作为扩展名，其 MIME 类型为 `application/toml`
* 大小写敏感
* UTF-8 编码
* 空白字符：Tab（`0x09`）或空格（`0x20`）
* 换行符：LF（`0x0A`）或 CRLF（`0x0D0A`）
* 注释以 `#` 开头。

```toml
# 这是一条注释，不包含任何内容。
```
# 键值对

```toml
key = "value"
```

键为直接的字面量，值不能是空或注释。

```toml
key = # 非法：键没有值
```

完整的键值对需要在同一行上，且一行只有一个键值对，内联表例外。有些数据支持多行，其开头也必须在同一行中

```toml
key1 = "这是一行文本"
key2 = """
这是多行文本
多行文本支持跨行，但开头的标志 \"\"\" 必须与 key 同行"""
```

不能重复定义键

```toml
key = "value1"
key = "value2" # 非法，键 key 已定义
```
## 裸键

不使用引号包裹的键，可以使用任何 ASCII 字母和数字、下划线、连接线，即 `[A-Za-z0-9-_]` ，键前后的空格会作为缩进删除（`trim`）

```toml
key1 = "value 1"
key 2 = "value 2"
key-3 = "value 3"
1234 = "1234"
```
## 字符串键

通过标准的字符串写法作为键，此规则可以使用更加广泛的键名

> [!warning]
> 如非必要推荐使用裸键

```toml
"key" = "value"
"ʎǝʞ" = "value"
'quoted "value"' = "value"
"127.0.0.1" = "value"
```

相同的字符串键与裸键视为同一个键

```toml
key = "value"
"key" = "value" # 非法，键 key 已定义
```

字符串键允许使用空字符串

```toml
 = "value" # 非法：裸键不允许使用空键名
"" = "value" # 合法
```
## 点分隔键

键名可以包含 `.` ，实际上表示一张表的某个键。

`````col
````col-md
```toml

name = "name"

physical.color = "orange"
physical.shape = "round"

site."google.com" = "true"



3.1415926 = "PI" # 这是个迷惑行为
```
````
````col-md
```json
{
    "name": "name",
    "physical": {
        "color": "orange",
        "shape": "round"
    },
    "site": {
        "google.com": true
    },
    // 这是个迷惑行为
    "3": {
        "1415926": "PI"
    }
}
```
````
`````

若一个键已经被定义，且非表，那不能将其再定义为表

```toml
name = "name"
name.first = "First Name" # 非法：name 非表
```
# 字符串

标准字符串通过 "" 包裹，并支持使用转义符；`"""` 定义多行文本，第一行紧跟开头的换行会被忽略。

```toml
str1 = """
Rose are red
Violets are blue"""
# str1 == str2
str2 = """Rose are red
Violets are blue"""
```

最后一个非空白字符为未转义的 \ 时，会移除他之后的空格和换行，直到出现有意义的行

```toml
str1 = "The quick brown fox jumps over the lazy dog."
# str1 == str2
str2 = """
The quick brown \        
  
 fox jumps over \
 the lazy dog."""
```

多行文本支持使用最多两个连续双引号而不需要转义

```toml
str1 = """这里有两个引号: ""。"""
str2 = """这里有三个引号：""\"。"""
str3 = """这里有九个引号：""\"""\"""\"。"""
```

使用 `''` 或 `'''` 包裹单行或多行文本。其中的文本不支持转义。

> [!attention] 多行文本不支持连续使用三个 `'`

```toml
winPath = 'C:\Users\lq2007'
quote = 'Tom "Dubs" Preston-Werner'
regex = '<\i\c*\s>'
```
# 数字

整数：64 位有符号整型且允许以 _ 分割，不允许前导 0

```toml
int1 = 1
int2 = +1
int3 = -1
int4 = 123_456_789
int5 = 0123 # 非法：不允许使用前导 0
```

非负整数允许使用 2/8/16 进制表示，十六进制数大小写不敏感，此时允许前导 0 但不允许 `+` 符号，前缀与数字间不允许使用 `_`

```toml
# 16 进制
hex1 = 0xDEADBEEF
hex2 = 0xDeadBeef
hex3 = 0xdead_beef

# 8 进制
oct1 = 0o1234567
oct2 = 0o755

# 2 进制
bin1 = 0b11010110
```

浮点数：IEEE 754 64 位浮点值，允许使用 e 或 E 表示 10 的指数，允许使用下划线分割

```toml
flt1 = +1.0
flt2 = 3.14
flt3 = -1.0
# 指数
flt4 = 5e10
flt5 = 5e-10
flt6 = -2E-2
```

特殊浮点数使用小写表示

```toml
sf1 = inf # 等价于 +inf
sf2 = +inf
sf3 = -inf

sf4 = nan
sf5 = +nan
sf6 = -nan
```
# 布尔

```toml
bool1 = true
bool2 = false
```
# 日期与时间

日期与时间实现 RFC 3339 规定

```toml
# 基础
odt1 = 1979-05-27T07:32:00Z
odt2 = 1979-05-27T00:32:00-07:00
odt3 = 1979-05-27T00:32:00.999999-07:00
# 空格替换 T
odt4 = 1979-05-27 07:32:00Z
# 忽略石头 T
ldt1 = 1979-05-27T07:32:00
ldt2 = 1979-05-27T00:32:00.999999
# 时间
ld1 = 1979-05-27

lt1 = 07:32:00
lt2 = 00:32:00.999999
```
# 数组

数组以 `[数据]` 声明，内可以包含多种不同值。`[]` 可以存在空格

```toml
values = [
  1, 2, 3,
  [4, 5, 6],
  "Foo Bar", # 末尾 , 可放入
]
```
# 表

表通过 `[表名]` 定义，允许表为空

```toml
[table]
```

表名与键名的要求相同，也不允许重复定义

```toml
[dog."tater.man"]
```

每个 TOML 在处于第一个自定义的表前的内容都处于一个根表，根表没有名字。
# 内联表

提供更为紧凑的方法表示表，必须在同一行内，以 `{}`  包裹

`````col
````col-md
```toml
name = {first = "Tom", last = "Preston-Werner"}


point = {x = 1, y = 2}


animal = {type.name = "Pug"}
```
````
````col-md
```toml
[name]
first = "Tom"
last = "Preston-Werner"
[point]
x = 1
y = 2
[animal]
type.name = "Pug"
```
````
`````
内联表是只读的

```toml
name = {first = "Tom"}
name.last = "Preston-Werner" # 非法：不能向内联表中添加元素
```
# 表数组

使用 `[[]]`  表示表数组

`````col
````col-md
```toml

[[products]]
name = "Hammer"
sku = 738594937

[[products]]  # 数组里的空表

[[products]]
name = "Nail"
sku = 284758393

color = "gray"
```
````
````col-md
```json
{
  "products": [
    { "name": "Hammer", "sku": 738594937 },


    { },


    { "name": "Nail", "sku": 284758393, "color": "gray" }
  ]
}
```
````
`````
每次对表数组的引用都指向当前表数组最后一个元素

`````col
````col-md
```toml

[[fruits]]

name = "apple"
[fruits.physical]  # 子表
color = "red"
shape = "round"

[[fruits.varieties]]  # 嵌套表数组
name = "red delicious"

[[fruits.varieties]]
name = "granny smith"

[[fruits]]
name = "banana"

[[fruits.varieties]]
name = "plantain"
```
````
````col-md
```json
{
  "fruits": [
    {
      "name": "apple",
      "physical": {
        "color": "red",
        "shape": "round"
      },
      "varieties": [
        { "name": "red delicious" },

        { "name": "granny smith" }
      ]
    },
    {
      "name": "banana",
      "varieties": [
        { "name": "plantain" }
      ]
    }
  ]
}
```
````
`````
