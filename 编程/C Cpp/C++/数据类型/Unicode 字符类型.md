用于不同 Unicode 编码的字符类型

| 类型            | 字符集         | 字符前缀        | 字符串前缀 | 加入版本   |
| ------------- | ----------- | ----------- | ----- | ------ |
| char8_t<br /> | UTF-8<br /> | u8<br />    | 无     | #cpp20 |
|               |             |             | u8    | #cpp17 |
| char16_t      | UTF-16      | u16，u<br /> |       | #cpp11 |
| char32_t      | UTF-32      | u32，U       |       |        |

```cpp
// 用于字符
char8_t u8char = u8'a';
char16_t u16char = u'你';
char32_t u32char = U'好';

// char8_t 可以直接转换成 char，但有警告
char ch = u8char;

// 用于字符串
auto str8 = u8"hello world";
auto str16 = u"你好世界";
auto str32 = U"你好世界";
```

`cuchar`，`string` 等标准库包含相对应类型支持，如 `u32string` 类型。