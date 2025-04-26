# 结构体

- 允许使用 `record` 修饰 `struct` 创建记录结构体，包含以下两种形式：
	- `record struct`
	- `readonly struct struct`
- 允许创建无参构造，并初始化属性和字段
- `with` 左侧可以是任何结构体类型，也可以是匿名（引用）类型
# 内插字符串处理程序

一种类型，从内插字符串表达式创建结果字符串
- 需要被 `[System.Runtime.CompilerServices.InterpolatedStringHandlerAttribute]` 属性修饰
- 需要实现一个构造，带有 `int literalLength, int formatCount` 两个参数
- 需要实现 `public void AppendLiteral(string s)` 方法
- 需要实现 `public void AppendFormatted<T>(T t)` 方法

```csharp
[InterpolatedStringHandler]
public MyInterpolatedStringHandler {

    StringBuilder sb;

    public MyInterpolatedStringHandler(int literalLength, int formatCount) {
        sb = new StringBuilder(literalLength);
    }

    public void AppendLiteral(string s) {
        sb.Append(s);
    }

    public void AppendFormatted<T>(T t) {
        sb.Append(t?.ToString());
    }

    // 获取结果
    internal string GetFormattedText() => sb.ToString();
}
```

使用时，传入该类型参数即可

```csharp
public void useMyInterpolatedString(MyInterpolatedStringHandler builder) {
    string s = builder.GetFormattedText();
    // do something
}
```

当同时存在自定义插值处理程序和接收 `string` 类型参数的方法时，编译器优先使用自定义插值程序的重载版本

```csharp
useMyInterpolatedString($"1+2={1+2}");
```

编译器使用差值处理程序时，过程为：
- 编译器创建一个构造处理程序，并传递字符串文本总长度和占位符数量
- 编译器为每个部分和每个占用符调用 `AppendLiteral` 或 `AppendFormatted` 方法
# global using

`global` 可以修饰 `using` 指令，表示该指令适用于本次编译中（通常是项目中）的所有源文件
# namespace 声明

可以直接使用 `namespace 命名空间名;` 声明之后的成员全部属于指定命名空间

```csharp
namespace MyNamespace;
```
# 扩展属性模式

进行模式匹配时，引用对象的属性模式可以继续扩展

```csharp
// 等效于 {Prop1: {Prop2: pattern}}
{ Prop1.Propp2: pattern }
```
# lambda 表达式

- 允许持有 lambda 表达式的变量使用 `var` 类型，编译器自动推断 `Func` 或 `Action` 委托
- 允许 lambda 表达式声明返回类型
- lambda 表达式可以应用属性
# const 插值字符串

当一个插值字符串的每个占位符均为常量字符串时（注意不允许为数值常量），允许使用该插值字符串初始化 `const` 字符串
# 记录的 sealed ToString

记录重写 `ToString` 方法时，允许使用 `sealed` 修饰，禁止编译器为该记录的子类生成默认 `ToString` 方法，保证与基类行为一致性
# 允许部分析构声明

之前不允许析构声明中仅初始化部分值

```csharp
(int x, int y) = point;
// x y 必须全部初始化
int x1 = 0;
int y1 = 1;
(x1, y1) = point;
```

以下形式现在被允许：

```csharp
int x = 0;
(x, int y) = point;
```
# AsyncMethodBuilder

允许在方法上使用 `AsyncMethodBuilder` 生成器
# CallerArgumentExpression

`CallerArgumentExpression` 属性允许指定编译器使用另一个文本表示当前形参，多用于诊断

```csharp
public static void Validate(bool condition,
							[CallerArgumentExpression("condition")] string? message=null)
{
    if (!condition) // message
    {
        throw new InvalidOperationException($"Argument failed validation: <{message}>");
    }
}
```
# line pragma

支持 `#line pragma` 格式
