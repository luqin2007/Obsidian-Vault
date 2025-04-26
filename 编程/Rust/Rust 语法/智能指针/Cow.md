写时复制 `Copy-On-Write` 是用于资源管理的优化策略，当多任务读同一份资源，写入时创建一份副本

```rust hl:5,8
use std::borrow::Cow;

fn main() {
    let foo = "Hello World";
    let mut bar: Cow<str> = Cow::from(foo);
    println!("{bar}");      // 这里没有发生复制
    
    bar.to_mut().push_str(" Rust");  // 这里发生了复制
    println!("{bar}");
    
    println!("{foo}");      // 原来的字符串foo仍然可用，而且没有变化
}
```

# 创建

- 枚举：`Cow::Borrowed` 与 `Cow::Owned` 分别通过借用和原本对象创建

`````col
````col-md
flexGrow=1
===
```rust
let str_ = "Hello World";
let foo: Cow<str> = Cow::Borrowed(str_);
```
````
````col-md
flexGrow=1
===
```rust
let string = String::from("Hello World!");
let bar: Cow<str> = Cow::Owned(string);
```
````
`````

- `Cow::from`：通过 `ToOwned` 类型创建

| 标准类型    | ToOwned 类型 |
| ------- | ---------- |
| `str`   | `String`   |
| `[T]`   | `Vec<T>`   |
| `CStr`  | `CString`  |
| `OsStr` | `OsString` |
| `Path`  | `PathBuf`  |

`Cow<T>` 实现了 `Deref<T>`，但未实现 `DerefMut`，因此对其修改不会影响原数据，而是创建数据副本
- 修改 `Cow` 数据后，变量不再是借用，获得其所有权 - 实际是副本的所有权
- `to_mut()` 获取 `&mut <B as ToOwned>::Owned`

# 消耗

`Cow::into_owned` 获取内部数据
