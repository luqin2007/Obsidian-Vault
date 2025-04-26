---
语言: Rust
语法类型: 基础语法
---
`HashMap<K, V>` 存储了一组键值对，通过 `insert` 插入，`get` 获取，`entry` 可以返回一个 `Entry`, 包装了是否存在该键，或根据旧值更新新值

```rust
// 检查若不存在则插入
let mut scores = HashMap::new();
scores.insert(String::from("Blue"), 10);

scores.entry(String::from("Yellow")).or_insert(50);
scores.entry(String::from("Blue")).or_insert(50);

// 更新
let text = "hello world wonderful world";
let mut map = HashMap::new();

for word in text.split_whitespace() {
let count = map.entry(word).or_insert(0);
    *count += 1;
}
```

````col
```col-md
flexGrow=1
===
# HashMap

一个键值对集合
```
```col-md
flexGrow=1
===
# BTreeMap

- 需要一个键值对集合
- 键值对需要按一定顺序排列，以便遍历所需
- 关心键的大小，需要比较键的大小
- 需要根据键的大小获取一系列值或查找最大、最小键的值
```
```col-md
flexGrow=1
===
# HashSet

值为 `()` 的 `HashMap`
- 仅需要放置一系列不重复的值
- 需要知道是否包含某些值
```
```col-md
flexGrow=1
===
# BTreeSet

值为 `()` 的 `BTreeMap`
- 需要放置一系列不重复的值
- 关心值的大小
```
````
