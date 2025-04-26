# 主构造函数

允许 `class` 和 `struct` 使用主构造函，所有显示声明的其他构造必须使用 `this()` 调用主构造函数

```csharp
public class Point(int X, int Y) {
    public Point(): this(0, 0) {}
}
```
# 任意类型 using

可以通过 `using` 创建任意类型的别名，不再局限于命名类型。
- 元组
- 数组
- 指针
- 不安全类型
# 内联数组

用于创建固定大小的 `struct` 数组

```csharp
[System.Runtime.CompilerServices.InlineArray(10)]
public struct Buffer {
    private int _element0;
}

var buffer = new Buffer();
for (int i = 0; i < 10; ++i) {
    buffer[i] = i;
}

foreach(var i in buffer) {
    Console.WriteLine(i);
}
```
# 拦截器

> [!info]
> 拦截器是一项试验性功能，需要在项目文件中设置
>     `<Features>InterceptorsPreview</Features>`
> 元素。

可以在编译时以声明方式将对_可拦截_方法的调用替换为对其自身的调用。 通过让拦截器声明所拦截调用的源位置，可以进行这种替换。 拦截器可以向编译中（例如在源生成器中）添加新代码，从而提供更改现有代码语义的有限能力。

在源生成器中使用_拦截器_修改现有编译的代码，而非向其中添加代码。 源生成器将对可拦截方法的调用替换为对拦截器方法的调用。

如果你有兴趣尝试拦截器，可以阅读[功能规范](https://github.com/dotnet/roslyn/blob/main/docs/features/interceptors.md)来了解详细信息。 如果使用该功能，请确保关注此预览功能的功能规范中的变更。 功能最终确定后，我们将在本站点上添加更多指南。
