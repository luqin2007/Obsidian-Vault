# define

`#define`、`#undef` 用于注册/取消编译时名称符号
# 条件编译

条件编译包括 `#if`、`#elif`、`#else`、`#endif`，判断条件为符号，判断条件包括 `!`/`==`/`!=`/`||`/`&&`，符号存在相当于 true

```csharp
#define ENTER
#define W10
#if ENTER
    ...
    #if W10
        ...
    #endif
#elif W10 && (ENTER == false)
    ...
#endif
```
# 异常

编译时遇到 `#warning` 会输出后面的提示信息，遇到 `#error` 会报错并停止编译

```csharp
#if DEBUG && RELEASE
    #error "DEBUG 与 RELEASE 不能共存"
#endif
#warning "Hello !!"
```
# 代码块

使用 `#region`、`#endregion` 将某段代码视为有指定名称的一个块，无实际用处，可被编译器识别，让代码在屏幕上更好的布局

```csharp
#region Member Field Declaration
int x;
double d;
#endregion
```
# 行编号

`#line` 可改变编译器警告和错误信息的行号和文件

```csharp
#line 165 "Core.cs"    // 信息指定到 Core.cs 的 165 行
#line default        // 还原原始信息
```
# 编译参数

`#pragma` 可用于抑制或还原编译器警告

```csharp
#pragma warning disable 169        // 禁用 字段未使用 警告
public class MyClass { ... }
#pragma warning restore 169        // 恢复 字段未使用 警告
```
