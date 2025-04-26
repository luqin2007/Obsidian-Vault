---
语言: Rust
语法类型: 中级语法
---

|      Trait       | 说明                                                                        |
| :--------------: | ------------------------------------------------------------------------- |
|     `Debug`      | 允许在调试时通过 `{:?}` 输出实例信息                                                    |
| `Eq`，`PartialEq` | 实现 Eq 必须同时实现 PartialEq, 表示 this == this。<br>`HashMap<K, V>` 中的 K 必须实现 Eq  |
|     `Clone`      | 实现 `clone()` 方法，允许数据进行深拷贝。只有在所有成员都实现了 Clone 的结构体上才能实现 Clone               |
|      `Copy`      | 允许通过只拷贝栈上的位来复制而不需要额外的代码。可以假设复制的速度很快。任何使用 Copy 都可以使用 Clone 实现              |
|      `Hash`      | 允许通过 hash 函数生成一个固定大小的值，其字段必须全部实现 Hash。<br> `HashMap<K, V>`  的 K 必须实现 Hash |
|    `Default`     | 允许使用 `default()` 方法获取一个默认实例                                               |
|      `Drop`      | 析构函数中的自定义代码                                                               |
|      `Try`       | `?` 运算符和 try 代码块                                                          |

# `fml::Debug` 

自动推断创建对应的结构体实现，只需要标注一下即可。

> [!note] 所有 std 库的结构体都实现了 `fml::Debug`

```rust
// derive 属性会自动创建所需的实现，使这个 struct 能使用 fmt::Debug 打印。
#[derive(Debug)]
struct DebugPrintable(i32);
```

# `fml::Display`

> [!success] std 标准库中所有泛型结构体都没有实现 `fmt::Display`，非泛型结构体都实现了 `fmt::Display`

```rust
pub trait Display {
    fn fmt(&self, f: &mut Formatter<'_>) -> Result;
}
```

实现该 Trait 的结构体允许直接在格式化输出中使用 `{}` 占位符输出，可以利用 `write!` 向 `fmt::Formatter` 写入结构体内容

```rust
struct Structure(i32);

impl fmt::Display for Structure {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        write!(f, "{}", self.0)
    }
}
```