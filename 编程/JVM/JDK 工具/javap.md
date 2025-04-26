`javap` 是 Java 提供的工具，可以查看 class 文件内部细节。Class 字节码可通过 `javap` 从二进制格式反编译成可读文件格式。

```shell
javap [options] class文件1 class文件2 ...
```

* `-v`，`-verbose`：显示包含常量池的完整信息
* `-l`：只输出行号和局部变量表
    * 局部变量表要求 `javac` 编译时带有 `-g` 属性
* `-public`，`-protected`，`-private`/`-p`：显示 public、protected 或 private 成员
    * 默认显示 `public`，`protected`，和默认级别的方法
* `-c`：输出代码的字节码指令
* `-s`：只输出类型描述符签名
* `-J`：VM 选项

不加选项则反编译成 Java 代码，通常使用 `-v` 查看完整的字节码信息。

```java title:示例代码
public class Main {
    public static int static_public_var = 0;
    private static int static_private_var = 0;
    public static int static_public_func() {
        return 0;
    }
    private static int static_private_func() {
        return 0;
    }
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
```

不加任何参数的情况下，`javap` 默认输出 `public`、`protected` 和 `package-private` 级别的方法。

![[image-20240312204009-nhg0l3t.png]]

如果要显示私有方法和字段，需要加入 `-p` 选项

![[image-20240312204115-gzecl31.png]]

如果需要查看方法和字段的描述符和签名信息，需要加入 `-s` 选项

![[image-20240312204212-ebqq27h.png]]

使用 `-c`，可以反编译看到方法代码的字节码指令

![[image-20240312204352-60g6r3f.png]]

如果需要显示更详细内容，包括版本号、访问权限、常量池等信息，使用 `-v` 选项（比较长，只节选部分）

![[image-20240312204522-iymmqip.png]]
![[image-20240312204534-um5mgsj.png]]

带有 `-l` 可以显示行号和局部变量表，注意局部变量表需要带有 `javac -g` 参数的编译才有

![[image-20240312204727-30whvzb.png]]
