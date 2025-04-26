# readonly

`readonly` 可用于修饰成员，表示该成员不会修改该实例状态，类似 C++ 函数声明中的 `const` 后缀，编译器会对其进行优化

- 方法
```csharp
class XY {
    int X;
    int Y;

    public readonly double Sum() {
        return X + Y;
    }
}
```

- 属性与索引器

```csharp
public class Counter {
    private int count;
    public int Count {
        readonly get => count;
        set => count = value;
    }
}
```

使用只读属性时，可自动生成 `init` 方法

```csharp
public int Count { get; init; }
```
# 接口默认成员

接口的函数等成员可有一个默认实现，类似于 Java 的 `default` 方法

```csharp
interface AInterface {
    int Sum(int a, int b) {
        return a + b;
    }
}
```
# 模式匹配

增加新的可匹配模式，详见 [[CSharp 7#模式匹配]]
- switch 表达式
- 属性、元组、位置
# using 声明

`using` 语句可直接声明变量，声明的变量会在作用域末尾释放

```csharp
void ReadFile() {
    using StreamReader reader = ...;
    // do something ...

    // 资源将在函数执行完毕后释放
}
```
# 本地函数

本地函数可以使用 `static` 修饰了，前提是：
- 本地函数不会被转换成委托，即没有捕获封闭范围中的变量
- 本地函数捕获的变量不会被其他转换为委托的 lambda 或本地函数捕获
# ref 结构类型

在声明结构体时可以使用 `ref struct`。`ref struct` 在栈上分配，不能转义到托管堆，因此其成员和行为有限制：
- 不能是数组
- 不能是类或非 `ref struct` 声明的类型
- 不能实现接口
- 不能被装箱为 `System.ValueType` 或 `System.Object`
- 不能作为类型参数，不能被 lambda 表达式或本地函数捕获
- 不能在 `async` 方法中使用，但可以在返回 `Task` 或 `Task<T>` 的同步方法中使用 `ref struct` 变量
- 不能在迭代器中使用

```csharp
public ref struct RefStruct {
    public bool IsValid;
    public Span<int> Inputs;
    public Span<int> Outputs;
}
```

> [!note]
> `ref` 与 `readonly` 共同修饰 `struct` 时，`readonly` 必须在 `ref` 之前
# 可空引用类型

可空类型现在可以用于引用类型，此时 `default(T)` 有效且为 `null`，且编译器会进行对 `null` 的静态分析
# await foreach

异步数据流，在检查下一个元素时可以为循环的每次迭代创建任务，适用于实现了 `IAsyncEnumerable<T>` 接口的集合，或满足以下条件的对象：
- 具有 `public GetAsyncEnumerator()` 方法（必须是 `public` 且是无参的，但可以是扩展方法）
- `GetAsyncEnumerator()` 方法返回的对象具有 `public Current` 属性和 `public MoveNextAsync()` 方法
- `GetAsyncEnumerator().MoveNextAsync()` 方法返回值为 `Task<bool>` 或 `ValueTask<bool>`
# Range

新增范围运算符 `..`，用于创建 `System.Range` 对象，表示一个范围
- 运算符左侧没有值时，表示从容器开头开始
- 运算符右侧没有值时，表示到容器结尾
- 范围表示的是左闭右开区间
- 左右两侧的值可以是数字或 `Index` 变量

`System.Range` 对象可用于列表或数组的下标访问，可创建其切片

```csharp
int[] oneThroughTen =
{
    1, 2, 3, 4, 5, 6, 7, 8, 9, 10
};

Write(oneThroughTen, ..);
Write(oneThroughTen, ..3);
Write(oneThroughTen, 2..);
Write(oneThroughTen, 3..5);
Write(oneThroughTen, ^2..);
Write(oneThroughTen, ..^3);
Write(oneThroughTen, 3..^4);
Write(oneThroughTen, ^4..^2);

static void Write(int[] values, Range range) =>
    Console.WriteLine($"{range}:\t{string.Join(", ", values[range])}");
// Sample output:
//      0..^0:      1, 2, 3, 4, 5, 6, 7, 8, 9, 10
//      0..3:       1, 2, 3
//      2..^0:      3, 4, 5, 6, 7, 8, 9, 10
//      3..5:       4, 5
//      ^2..^0:     9, 10
//      0..^3:      1, 2, 3, 4, 5, 6, 7
//      3..^4:      4, 5, 6
//      ^4..^2:     7, 8
```
# ??=

允许 `??=` 运算符表示 `??` 运算符的合并赋值

```csharp
a = a ?? b;
a ??= b;
```
# 非托管构造

泛型约束 `where` 增加 `unmanaged` 约束，表示非托管类型（确切说是非指针、不可为 null 的非托管类型），不能与 `struct` 或 `class` 混用（强制要求类型为 `struct`）

```csharp
class UnManagedWrapper<T> where T: unmanaged { ... }
```

非托管类型包括
- 所有数字类型，包括 `sbyte`，`byte`，`short`，`ushort` 等，也包括 `decimal`
- `char`，`bool`
- 任何枚举、指针、仅包含非托管类型字段的结构体
# 其他

不知道具体啥意思
- [嵌套表达式中的 Stackalloc](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/operators/stackalloc)
- [内插逐字字符串的增强功能](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/tokens/interpolated)
