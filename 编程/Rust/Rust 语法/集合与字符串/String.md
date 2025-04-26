---
语言: Rust
语法类型: 基础语法
---
String 是 Rust 标准库提供的 UTF-8 可变字符串结构。

在 Rust Core 中，字符串只有一种类型 `str`, 它常以引用的形式 `&str` 出现，因此我们常提到的字符串包含了 String 类型和切片 `&str` 类型。

```rust
let a = "123"; // &str
```

> Rust 还存在其他一系列字符串类型，如 `OsString`, `OsStr`, `CString`, `CStr` 等，用的不多。其他 Crate 也提供了更多字符串类型。
> 
> - `OsString`, `OsStr` 主要为了解决 Windows API 使用未经检查的 UTF16 编码，而 Rust 则使用 UTF8 编码，为了保证转换不出错而设置的
> - `CString` , `CStr`  主要为了兼容 C 库中使用 `\0` 结尾的字符串而设计，Rust 核心中的 `str`  类型本身保存长度因此不需要 `\0`

String 类型可以通过以下几个方法创建：

```rust
fn main() {
    let str1 = "hello".to_string(); // 通过 &str 创建
    let str2 = String::new(); // 空字符串
    let str3 = String::from("hello"); 
}
```

可通过 `push`, `push_str`, `add/+`, `format!` 连接字符串

```rust
fn main() {
    // ""
    let mut str = String::new();
    // "Hello"
    str.push_str("Hello");
    // "Hello!"
    str.push('!');
    // "Hello!~~~"
    let str2 = str + "~~~"; // String::add
    // "Hello!~~~, !!!"
    let str3 = format!("{}, {}", str2, "!!!");

    assert_eq!(str3, "Hello!~~~, !!!")
}
```

字符串切片和长度都是字节长度，其长度不一定是字符串字符个数

> [!danger] 对字符串取切片时，起止点必须落在字符边界位置，可使用 `String::chars()` 获取所有字符

```rust
fn main() {
    let str = "你好！";
    // len=9
    println!("len={}", str.len());
    // 0..3=你
    println!("0..3={}", &str[0..3]);
    // thread 'main' panicked at 'byte index 1 is not a char boundary; it is inside '你' (bytes 0..3) of `你好！`', src\main.rs:5:26
    // UTF-8 中，一个汉字使用三个字节表示
    println!("0..1={}", &str[0..1]);
    for x in str.bytes() {
        // 228, 189, 160, 229, 165, 189, 239, 188, 129, 
        print!("{}, ", x)
    }
}
```

修改方法：

- `replace` 及其变形，`push`/`insert`，`pop`/`remove(idx)`/`truncate`，`trim` 等
- `+/+=`  运算符