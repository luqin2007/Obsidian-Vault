# init 访问器

提供一致性语法初始化对象成员，在构造对象时为只读属性创建一个可修改的窗口，构造完毕后结束，且可以在 `with` 表达式中使用后。

`init` 访问器适用于类、结构体、记录

```csharp
public struct WeatherObservation
{
    public DateTime RecordedAt { get; init; }
    public decimal TemperatureInCelsius { get; init; }
    public decimal PressureInMillibars { get; init; }
}

var now = new WeatherObservation 
{ 
    RecordedAt = DateTime.Now, 
    TemperatureInCelsius = 20, 
    PressureInMillibars = 998.0m 
};
```
# new()

- 在已知对象类型时，创建对象时可直接使用 `new()` 省略对象类型

```csharp
private List<string> strs = new();
```

- `new` 可配合 `init` 访问器

```csharp
WeatherStation station = new() { Location = "Seattle, WA" };
```
# Record

使用 `record` 关键字创建一个记录类型。记录类是一个引用类型，封装数据类
- 允许 `==` 判断
- 允许继承，但记录只能继承自另一个记录，类不能继承自记录
- 实现 `ToString()` 方法：`类名 { 属性 = 值, 属性 = 值, ... }`

可直接声明一个带有不可变属性的记录类

```csharp
public record Person(string FirstName, string LastName);

Person p = new("First", "Last");
```

也可以展开属性和字段，但需要 `require` 修饰，此时允许可变属性

```csharp
public record Person {

    // 只读属性
    public require string FirstName { get; init; }
    // 可变属性
    public require string LastName { get; set; }
}
```
## 位置语法

即第一种创建记录的方式，通过声明参数位置来记录属性。record 类的记录语法创建的属性都是只读的，编译器会自动实现一个构造函数

位置语法和单独写的属性或字段可共存

```csharp
public record Person(string FirstName, string LastName) {
    public string[] PhoneNumbers { get; init; }
}

Person p = new("First", "Last") {
    PhoneNumbers = new string[1];
}
```
## 不可变性

record 类中，由编译器生成的所有方法均不会修改类中的属性
## 值相等性

对于两个 record 类，若其类型相匹配，且所有属性和字段一致，则这两个记录是相等的

对于引用类型的属性和字段，比较的是记录的标识，即指向同一个对象的两个引用是相等的
## 非破坏性修改

通过 `with` 表达式可以修改一个记录并返回一个新记录，旧记录不会被修改

```csharp
public record Person(string FirstName, string LastName) {
    public string[] PhoneNumbers { get; init; }
}

Person p = new("First", "Last") {
    PhoneNumbers = new string[1];
}

Person p2 = p1 with {
    FirstName = "John";
}

// copy
Person p3 = p1 with { }
```
# 顶级语句

单文件应用程序可使用顶级语句，即认为该文件的所有代码都位于 `Main` 函数中，从而省略 `Main` 函数声明，像脚本一样写程序，自上而下运行。

```csharp
System.Console.WriteLine("Hello World!");
```
# 模式匹配增强

- 允许使用 `and`，`or`，`not`
- 允许关系模式，与给定常数做大小判断
- 允许模式组合使用括号
- 允许类模式匹配特定类型
# static lambda

`static` 可修饰 lambda 表达式或匿名函数，类似于 `static` 局部函数，无法捕获局部变量或实例
# foreach

`foreach` 现在适用于任何行为与 `IEnumerable<T>` 相同的类，即存在 `public GetEnumerator()` 方法并返回一个迭代器即可
# 互操作性

指访问其他本地库（C 库等）的互操作性增强
- `nint` 与 `nuint` 类型表示本机大小整数（？？？）
- `delegate*` 表示函数指针，使用 `calli` 操作码调用
	- `unmanaged delegate*` 可用于指定其他调用约定
- 属性可用于本地函数
	- `SkipLocalsInitAttribute` 属性表示禁用 `localsinit` 标志
# 代码生成器

（看不太懂，以下摘自 MSDN：C# 9.0 中的新增功能）

类似于 roslyn 分析器或代码修补程序。 区别在于，代码生成器会在编译过程中分析代码并编写新的源代码文件。 典型的代码生成器会在代码中搜索属性或其他约定。

代码生成器使用 Roslyn 分析 API 读取属性或其他代码元素。 通过该信息，它将新代码添加到编译中。 源生成器只能添加代码，不能修改编译中的任何现有代码。

为代码生成器添加的两项功能是“分部方法语法”和“模块初始化表达式”的扩展。 首先是对分部方法的更改。 在 C# 9.0 之前，分部方法为 `private`，但不能指定访问修饰符、不能返回 `void`，也不能具有 `out` 参数。 这些限制意味着，如果未提供任何方法实现，编译器会删除对分部方法的所有调用。 C# 9.0 消除了这些限制，但要求分部方法声明必须具有实现。 代码生成器可提供这种实现。 为了避免引入中断性变更，编译器会考虑没有访问修饰符的任何分部方法，以遵循旧规则。 如果分部方法包括 `private` 访问修饰符，则由新规则控制该分部方法。 有关详细信息，请查看[分部方法（C# 参考）](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/keywords/partial-method)。

代码生成器的第二项新功能是模块初始化表达式。 模块初始化表达式是附加了 [ModuleInitializerAttribute](https://learn.microsoft.com/zh-cn/dotnet/api/system.runtime.compilerservices.moduleinitializerattribute) 属性的方法。 在整个模块中进行任何其他字段访问或方法调用之前，运行时将调用这些方法。 模块初始化表达式方法：

- 必须是静态的
- 必须没有参数
- 必须返回 void
- 不能是泛型方法
- 不能包含在泛型类中
- 必须能够从包含模块访问

最后一个要点实际上意味着该方法及其包含类必须是内部的或公共的。 方法不能为本地函数。 有关详细信息，请参阅 [`ModuleInitializer` 属性](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/attributes/general#moduleinitializer-attribute)。
