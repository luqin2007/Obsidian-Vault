---
语言: Rust
语法类型: SDK 工具
---
> [!note] 使用场景
> 分包

允许将一个 Crate 再拆分为多个 Crate，此时可在 `Cargo.toml`  中进行配置

```toml
[workspace]
members = [
    'test_crate'
]
```

之后，在项目目录中新建 Crate:

```shell
cargo new test_crate
# Created binary (application) `test_crate` package
```

此时，项目目录中已经存在 `test_crate`  目录及对应的 `src` , `Cargo.toml`  文件，但没有 `Cargo.lock`  文件。在主项目中添加对应依赖则使用 `path` 

```toml
[dependencies]
test_crate = { path='./test_crate' }
```

之后运行时，需要指定依赖的二进制包

```shell
cargo run -p test_crate
```

同一个工作空间中只存在一个 `Cargo.lock` ，也就是说只存在一份依赖列表，保证了<font color="#9bbb59">使用的依赖版本一致性</font>。但子 Crate 之间依赖并不共享，顶级 `Cargo.toml`  指定的依赖则可被所有子 Cargo 访问

使用 `cargo publish`  发布时，应将所有子 Cargo <font color="#9bbb59">分别发布</font>。

使用 `cargo test`  进行单元测试时，在顶级目录中运行会同时测试所有子 Crate 的测试方法。


‍
