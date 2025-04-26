# 模式匹配

模式匹配用于测试某个表达式结果是否具有某种特征。具体参考 [模式 - 使用 is 和 switch 表达式进行模式匹配。 | Microsoft Learn](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/operators/patterns)

- `is` / `is not` 模式：用于判断某个变量是否为某个类型，或进行 `null` 判断

```csharp
int? a = 12;

if (a is int num) {
    // a = num = 12
} else if (a is not null) {
    // a = null
}

IEnumerable<T> seq;
if (seq is IList<T> list) {
    // do something, list = seq
} else if (seq is null) {
    // do something, seq = null
}
```

- `switch` 模式：对于离散值、关系、[[CSharp 9#Record|Record]]、列表等进行匹配
	- 分支中 `_` 表示无法匹配的行为
	- 解构中 `_` 表示任意一个元素，`...` 表示任意数量元素，也可以声明一个变量接收任意一个元素 

```csharp
// 离散值：枚举，字符串等
string command;

command switch {
    "SystemTest" => ... ,
    "Start"      => ... ,
    "Stop"       => ... ,
    "Reset"      => ... ,
    _            => ... ,
}
```

```csharp
// 关系：数值类大小比较
int temp;

temp switch {
    (> 32) and (< 212) => ... ,
    < 32               => ... ,
    > 212              => ... ,
    32                 => ... ,
    212                => ... ,
}
```

```csharp
// 对于 record 类与结构，可以对对象进行解构后判断
public record Order(int Items, decimal Cost);

Order order;
order switch {
    {Items: > 10, Cost: > 1000.0m } => ... ,
    {Cost: > 500.0m }               => ... ,
    null                            => ... ,
    var other                       => ... ,
}
```

```csharp
// 列表模式可检查列表或数组的元素
string[] values;

values switch {
    [_, "Xxx", _, ..., var abc] => ... ,
    _                      => ... ,
}
```

# 本地函数

包含在成员中的嵌套方法，声明方式与普通函数相同，但有如下限制：
- 不能使用 `value` 作为变量名或参数名，编译器隐式创建该函数引用 `outter`
- 不能被 `public`，`private` 等访问修饰符修饰，可以被 `async`，`unsafe`，`static`，`extern` 修饰
	- `static` 修饰后无法捕获局部变量或实例
	- `extern` 修饰后必须使用 `static` 修饰

本地函数可以用于枚举前的测试

```csharp
public static IEnumerable<int> Seq(int start, int end) {
    if (start < 0 || start > 90)
        throw ...;
    if (end > 100)
        throw ...;
    if (start >= end)
        throw ...;
    return GetSeqEnumerable();

    IEnumerable<int> GetSeqEnumerable() {
        for (int i = start; i <= end; ++i) {
            if (i % 2 == 1) yield i;
        }
    }
}
```

当本地函数捕获了封闭范围中的变量时，将会转换为委托实现。而 lambda 表达式永远转换为委托
# 表达式主体定义成员

不太明白，大致好像是属性、方法、构造、终结器、索引器等可以使用 `=>`

```csharp
public class Test {

    // 只读属性
    private string name;
    public string Name => name;
    // 属性
    private string location;
    public string Location {
        get => location;
        set => location = value;
    }
    // 方法
    public override string ToString() => $"Test Class named {Name}";
    public void Display() => Console.WriteLine(ToString());

    // 构造
    public Test(string loc) => Location = loc;
    // 终结
    ~Test() => Console.WriteLine("Object is finalized.");

    // 索引器
    private string[] strs;
    public string this[int i] {
        get => strs[i];
        set => strs[i] = value;
    }
}
```
# ref 变量

使用 `ref` 声明的局部变量相当于 C++ 的引用

```csharp
int aInt = 1;
ref int b = ref aInt;

int[] aArr = { 1, 2, 3};
b = ref aArr[0];
```

`ref` 变量可以作为返回值，但要求：
- 相应变量的作用域必须包含方法，相应变量的生存期必须超过方法的返回值
	- 不得为方法内创建的成员，可以为静态字段或传入函数的参数
- 不得为字面量 `null`，但可以是可空类型的一个别名
- 不得为常量、枚举成员、按值返回值或 `class`/`struct` 方法
- 不得对异步方法使用引用返回值

```csharp
public ref Person GetXxx() {
    // do something
    // p 是一个 Person 变量，且生存期长于方法
    return ref p;
}
```

`ref struct` 声明表示结构体直接访问托管内存，且始终分配有堆栈
# 异步 Main

`Main` 函数支持使用 `async` 修饰，即以下 Main 函数声明是合法的：
- `static async Task Main()`
- `static async Task Main(string[] args)`
- `static async Task<int> Main()`
- `static async Task<int> Main(string[] args)`
# default(T) 类型推断

在类型可以被推断时，`default(T)` 可简化为 `default`：
- 赋值和初始化时：`T t = default;`
- 形参默认值：`T[] Initialize<T>(int length, T initValue = default);`
- `return` 或 `=>` 结果时：`T GetValue() => default;`
# readonly

- `readonly struct` 结构体表示结构体不可变，且应作为 `in` 传递到其成员方法
- `readonly ref` 表示返回的引用不可变
# 其他

- `private protected` 修饰符：可被该类或当前程序集中该类的派生类访问
