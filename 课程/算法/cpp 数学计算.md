# partial_sum

```cpp
OutputIt partial_sum(InputIt first, InputIt last, OutputIt d_first[, BiOp op]);
```

计算数列的前 n 项和并保存在 `OutputId` 中。在不提供运算器 `op` 的情况下，使用 `+` 运算符

该函数执行后，`d_first` 第 n 项值为从 `first` 开始的前 n 项和，其预期结果为：

```cpp
d_first[0] = first[0];
d_first[1] = d_first[0] + first[1];
d_first[2] = d_first[1] + first[2];
d_first[3] = d_first[2] + first[3];
// ...
```

C++ 要求该函数恰好应用 `last - first - 1` 次二元运算，C++ 20 之后对每次加法使用 `std::move` 语义

