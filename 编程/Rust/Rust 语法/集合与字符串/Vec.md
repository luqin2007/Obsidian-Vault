---
语言: Rust
语法类型: 基础语法
---
`Vec<T>` 允许在一个数据结构中存储多于一个值，他们在内存中相邻排列（数组）。每个 Vec 进存储一种元素。创建 Vec 可通过 `Vec::new` 或 `vec!` 宏

```rust
fn main() {
    let vec1: Vec<i32> = Vec::new();
    let vec2 = vec![5, 2, 3];
}
```

构造器：

|        方法名        | 说明            |
| :---------------: | ------------- |
|      `new`      | 创建一个不限制长度的队列  |
|    `new_in`     | 指定 Allocator |
| `with_capacity` | 创建并初始化数组长度    |
|     `from`      | 从其他可迭代对象中创建队列 |

附带大量方法，如 `len()`, `is_empty()` 等属性，swap(), reverse(), sort(), push(), pop(), drain(range) 等编辑方法，first(), get() 等获取方法，iter, windows, chunks 等遍历方法。

- 带有 `_mut` 后缀的版本返回可变指针
- 带有 `_unchecked` 后缀的版本表示不检查下标上下界
- 带有 `r` 前缀版本表示从右向左

````col
```col-md
flexGrow=1
===
# Vec

- 希望元素按插入顺序排列，新数据只会追加到结尾
- 需要一个可调整大小、在堆上分配的数组
- 需要模拟一个堆栈
```
```col-md
flexGrow=1
===
# VecDeque

- 希望在队列的两端都可以插入数据
- 需要一个双端队列
```
```col-md
flexGrow=1
===
# LinkedList

- 需要未知规模的 Vec，且不能容忍经常的重建、复制数组
- 有效的拆分、追加列表
- 功能确实需要一个链表实现
```
````
