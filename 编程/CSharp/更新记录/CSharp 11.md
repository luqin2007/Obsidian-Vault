# 泛型属性

允许为 `Attribute` 类添加泛型

```csharp
public class GenericAttribute<T>: Attribute { ... }

[GenericAttribute<string>]
public string Method() => default;
```

在 C# 11 前，对应写法为：

```csharp
pulic class TypeAttribute: Attribute {
    public TypeAttribute(Type t) => ParamType = t;
    public Type ParamType { get; }
}

[TypeAttribute(typeof(string))]
public string Method() => default;
```

注意给定的类型必须满足 `typeof` 的要求，泛型类型必须完全构造
- 不允许使用 `dynamic`，应使用 `object`
- 不因需使用 `string?`，应使用 `string`
- 不允许使用 `(int x, int y)`，应使用 `ValueTuple<int, int>`
# 泛型数学支持

（不太懂，以下摘自 MSDN C#11 中的新增功能）

有几个语言功能支持泛型数学支持：

- `static virtual` 接口中的成员
- 已检查的用户定义的运算符
- 宽松移位运算符
- 无符号右移运算符

可以在接口中添加 `static abstract` 或 `static virtual` 成员，以定义包含可重载运算符、其他静态成员和静态属性的接口。 此功能的主要场景是在泛型类型中使用数学运算符。 例如，可以在实现 `operator +` 的类型中实现 `System.IAdditionOperators<TSelf, TOther, TResult>` 接口。 其他接口定义其他数学运算或明确定义的值。 可以在有关[接口](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/keywords/interface#static-abstract-and-virtual-members)的文章中了解新语法。 包含 `static virtual` 方法的接口通常是[泛型接口](https://learn.microsoft.com/zh-cn/dotnet/csharp/programming-guide/generics/generic-interfaces)。 此外，大部分将声明一个约束，即类型参数[实现声明接口](https://learn.microsoft.com/zh-cn/dotnet/csharp/programming-guide/generics/constraints-on-type-parameters#type-arguments-implement-declared-interface)。

可以在[探索静态抽象接口成员](https://learn.microsoft.com/zh-cn/dotnet/csharp/whats-new/tutorials/static-virtual-interface-members)教程或 [.NET 6 中的预览功能 - 泛型数学](https://devblogs.microsoft.com/dotnet/preview-features-in-net-6-generic-math/)博客文章中了解详细信息并亲自尝试该功能。

泛型数学对语言创建了其他要求。

- 无符号右移运算符：在 C# 11 之前，若要强制无符号右移，需要将任何带符号整数类型强制转换为无符号类型，执行移动，然后将结果强制转换回带符号类型。 从 C# 11 开始，可以使用 `>>>`[_无符号移动运算符_](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/operators/bitwise-and-shift-operators#unsigned-right-shift-operator-)。
- 宽松的移动运算符要求：C# 11 删除了第二个操作数必须是 `int` 或隐式转换为 `int` 的要求。 此更改允许在这些位置使用实现泛型数学接口的类型。
- 已检查和未检查的用户定义运算符：开发人员现在可以定义 `checked` 和 `unchecked` 算术运算符。 编译器根据当前上下文生成对正确变体的调用。 有关 `checked` 运算符的详细信息，可以阅读有关[算术运算符](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/operators/arithmetic-operators)的文章。
# IntPrt

`nint` 类型别名为 `System.IntPtr`

`nuint` 类型别名为 `System.UIntPtr`
# 多行字符串插值

字符串插值内的语句（ `{}` 内的内容）允许多行
# 列表模式

匹配模式增加列表模式，可用于展开数组和列表，详见 [[CSharp 8#模式匹配]] 
# 方法组转换

允许（但不一定）将方法组转换为委托（不太懂）
# 原始字符串文本

由 `"""..."""` 包围的文本称为原始字符串，内部空格、换行、其他特殊字符不需要转义
- 左引号之后、右引号之前紧跟的换行不属于字符串

```csharp
string msg = """
this is a long message
"Hello World!"
"""
```

可以使用多个 `$` 开头，表示几层 `{}` 内为插值内容

```csharp
// $$ 表示插值为 {{ ... }} 内的内容
string msg = $$"""
1+1={{ 1 + 1 }}
"""
```
# 结构体值的默认值

任何 `struct` 类型的字段都会自动初始化为其默认值
# `string` 匹配 `Span<char>`

模式匹配中字符串可用于匹配 `Span<char>` 或 `ReadOnlySpan<char>`
# 扩展 nameof 范围

`nameof` 获取的方法名包含类型参数名、参数名称等，常用于可空性分析
# UTF8 字符串字面量

可在字符串字面量后指定 `u8` 表示 UTF8 编码字符串

```csharp
var s = "hello"u8;
```
# required

`required` 修饰的属性和字段表示强制对象构造时初始化这些值
# ref

- `ref struct` 中可以声明 `ref` 字段，支持 `System.Span<T>` 等
- `ref` 声明允许添加 `scoped` 修饰符
# 文件本地类型

（不太懂，以下摘自 MSDN C#11 中的新增功能）

从 C# 11 开始，可以使用 `file` 访问修饰符创建其可见性范围限定为其声明时所在的源文件的类型。 此功能可帮助源生成器创建者避免命名冲突。 可以在语言参考中有关[文件范围类型](https://learn.microsoft.com/zh-cn/dotnet/csharp/language-reference/keywords/file)的文章中详细了解此功能。
