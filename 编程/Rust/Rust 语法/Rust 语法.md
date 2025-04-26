```rust
// Rust 程序入口函数
fn main() {
    let a = 10;
    let b: i32 = 20;
    let mut c = 30i32;
    let d = 30_i32;
    let e = add(add(a, b), add(c, d));

    println!("( a + b ) + ( c + d ) = {}", e);
}

fn add(i: i32, j: i32) -> i32 {
    // 返回相加值，这里可以省略return
    i + j
}
```

- [[注释]]
- [[变量]]
- [[表达式]]
- [[数据类型/数据类型|数据类型]]
- [[函数/函数|函数]]
- [[流程控制/流程控制|流程控制]]
- [[泛型/泛型|泛型]]
- [[Trait/Trait|Trait]]
- [[宏/宏|宏]]
- [[所有权/所有权|所有权]]
- [[生命周期]]
- [[集合与字符串/集合与字符串|集合与字符串]]
- [[智能指针/智能指针|智能指针]]
- [[unsafe/unsafe|unsafe]]
- [[编译器属性标记]]
- [[包与模块]]
- [[模块私有性]]
- [[Workspace]]
- [[发布]]
- [[测试]]
- [[并发基础]]

---

运行时的参数可以通过 `env` 包获取

```rust
use std::env;  
  
fn main() {  
    println!("运行参数: {:?}", env::args());  
    println!("环境变量: {:?}", env::vars());  
}
```

---

Rust 提供 `rustup`、`rustc` 和 `cargo` 工具更新、生成、编译、运行项目

-  `rustup` ：安装和更新 Rust
    - 更新：`rustup update`
    - 卸载：`rustup self unstall`
-  `rustc` ：编译源码，生成可执行文件，默认在源码目录
    -  `rustc -v`：查看版本，常用于检查 Rust 是否安装成功
    -  `rustc <rs文件>` ：编译源程序
    - `rustc-env=VAR=VALUE`：设置运行时环境变量
-  `cargo` ：生成、调试程序，也用于发布程序或 lib
    -  `cargo -v` ：查看版本，常用于检查 Rust 是否安装成功
    -  `cargo new <项目名>`：创建一个新的 Rust 项目
    - `cargo build [--release]` ：编译项目
    -  `cargo run [--release]`：直接运行项目
    -  `cargo check` ：不编译和运行，仅检查是否有错误