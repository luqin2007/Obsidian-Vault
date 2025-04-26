---
语言: Rust
语法类型: 中级语法
---
`Result<T, E>` 是一个枚举，包含 `Ok` 和 `Err` 两个值。

```rust
use std::fs::File;

fn main() {
    let f = File::open("hello.txt");

    let f = match f {
        Ok(file) => file,
        Err(error) => panic!("Can't open file because {:?}", error)
    };
}
```

`Result` 存在一系列简写方法: `unwarp` 系列方法和 `expect` 方法，`expect` 允许自定义错误信息

```rust
use std::fs::File;

fn main() {
    // 解包，若有错误则执行闭包
    let f0 = File::open("hello0.txt").unwrap_or_else(|error| {
        // Warn: 系统找不到指定的文件。 (os error 2)
        println!("Warn: {}", error);
        File::create("hello0.txt").unwrap()
    });

    // 解包，若有错误直接通过 panic 抛出
    let f1 = File::open("hello1.txt").unwrap();
}
```

传递异常也是很常见的操作，将异常传递给外部方法处理

```rust
fn read_from_file(file: &str) -> Result<String, io::Error> {
    let f = File::open(file);

    let mut f = match f {
        Ok(file) => file,
        // 传递 1
        Err(error) => return Err(error)
    };

    let mut result = String::new();
    match f.read_to_string(&mut result) {
        Ok(_) => Ok(result),
        // 传递 2
        Err(e) => Err(e)
    }
}
```

此时可通过 `?` 运算符简写，函数返回值必须是 `Result` 类型。

> [!important] `main()`  函数允许返回 `Result<(), E>` 类型

> [!success] `?` 运算符也可以用于 `Option`  枚举

```rust
fn read_from_file(file: &str) -> Result<String, io::Error> {
    // 传递 1
    let mut f = File::open(file)?;
    let mut result = String::new();
    // 传递 2
    f.read_to_string(&mut result)?;
    Ok(result)
}
```

`?` 支持链式调用

```rust
fn read_from_file(file: &str) -> Result<String, io::Error> {
    let mut result = String::new();
    //            传递 1                        传递 2
    File::open(file)?.read_to_string(&mut result)?;
    Ok(result)
}
```