Java 虚拟机指令由一个字节长度的操作码（`opcode` ）和若干操作数（`operand` ）组成，其结构如下：

```java
<opcode> [<operand>, <operand>, ...]
```

> [!note]
> 字节码的操作码只有 1 个字节，最多只有 256 个，现在已经使用超过 200 个

大部分指令是类型相关的，如函数返回值指令中，`ireturn` 返回整型，`dreturn` 返回浮点。

字节码操作数使用大端表示，即高位在前，低位在后，如 `getfield 00 02`  表示 `getfield 0x00 << 8 | 0x02` 
# 指令前后缀

````col
```col-md
flexGrow=4
===
很多指令会根据操作对象的类型不同，带有不同的前缀。

后面代表类型的部分使用 `<T>` 取代，如加载指令为 `<T>load`，则针对 `int` 类型的加载指令为 `iload`
```
```col-md
flexGrow=1
===
| 值   | 类型             |
| --- | -------------- |
| `i` | `int`          |
| `l` | `long`         |
| `f` | `float`        |
| `d` | `double`       |
| `a` | 引用类型           |
| `b` | `boolean/byte` |
| `c` | `char`         |
| `s` | `short`        |
```
````
# 加载和存储指令

操作局部变量表和操作数栈

> [!important]
> `long` 和 `double` 占操作数栈中的两个栈位置
## load

加载指令 `load` 用于将局部变量表的值加载到操作数栈上（入栈），根据待加载的类型和位置有多个变种：

| 类型            | 操作数 | 说明                                      | 适用类型                     |
| ------------- | --- | --------------------------------------- | ------------------------ |
| `<T>load_<n>` |     | 将局部变量表中第 `n` 个变量加载到栈上，`n` 可取 0,1,2,3    | `i, l, f, d, a`          |
| `<T>load`     | `n` | 将局部变量表中第 `n` 个变量加载到栈上                   | `i, l, f, d, a`          |
| `<T>aload`    |     | 从栈顶取一个整数 `n` 和数组 `arr`，将 `arr[n]` 加载到栈上 | `i, l, f, d, a, b, c, s` |
## store

`store` 存储指令是将栈顶的数据弹出并保存到局部变量表中，根据待存储的类型和位置有不同的变种：

| 类型                | 操作数 | 说明                                            | 适用类型                     |
| ----------------- | --- | --------------------------------------------- | ------------------------ |
| `<T>store_<n>`    |     | 将栈顶变量弹出并保存到局部变量表第 `n` 个变量上，`n` 可取 0,1,2,3     | `i, l, f, d, a`          |
| `<T>store`        | `n` | 将栈顶变量弹出并保存到局部变量表第 `n` 个变量上                    | `i, l, f, d, a`          |
| `<T>astore`<br /> |     | 从栈顶取一个整数 `n` 和数组 `arr`，将栈顶数据弹出并保存到 `arr[n]` 中 | `i, l, f, d, a, b, c, s` |
## 常量加载

* `ldc`：`load constant`，引入常量池中的常量
* `bipush`：`byte immediate push`，直接引入 byte 范围的数字
* `sipush`：`short immediate push`，直接引入 short 范围的数字

| 指令            | 操作数 | 描述                                       |
| ------------- | --- | ---------------------------------------- |
| `aconst_null` |     | 将 `null` 入栈                              |
| `iconst_m1`   |     | 将 `int -1` 入栈                            |
| `iconst_<n>`  |     | 将 `int n` 入栈，n 可选 0-5                    |
| `lconst_<n>`  |     | 将 `long n` 入栈，n 可选 0 或 1                 |
| `fconst_<n>`  |     | 将 `float n` 入栈，n 可选 0-2                  |
| `dconst_<n>`  |     | 将 `double n` 入栈，n 可选 0-1                 |
| `bipush`      | 数字  | 将 -128 到 127 范围的整型入栈                     |
| `sipush`      | 数字  | 将 -32768 到 32767 范围的整型入栈                 |
| `ldc`         | 索引  | 将常量池中的 `int`、`float`、`String` 类型常量入栈     |
| `ldc_w`       | 索引  | 将常量池中的任意类型常量入栈，其寻址范围是两个字节，用于编号 255 之外的变量 |
| `ldc2_w`      | 索引  | 将常量池中的 `long`、`double` 类型常量入栈，寻址范围是两个字节  |
## 操作数栈指令

这里的指令用于直接操作操作数栈。简单的操作栈指令包括：

|简单指令|说明|
| ----------| -------------------------------------------|
| `pop` |弹出（丢弃）栈顶元素|
| `pop2` |弹出栈顶元素，适用于 `long` 和 `double` |
| `dup` |将栈顶元素复制一份后压栈|
| `dup2` |将栈顶元素复制一份后压栈，适用于 `long` 和 `double` |
| `swap` |交换栈顶的两个元素|
稍微复杂点的有

* `dup_x1`：复制栈顶的值，并将其插入到栈中第三个位置。其代码描述为：

`````col
````col-md
```java
Stack stack; // 当前栈
v1 = stack.pop();
v2 = stack.pop();
stack.push(v1);
stack.push(v2);
stack.push(v1);
```
````
````col-md
![[image-20240313015002-tdkhxct.png]]
````
`````
其使用场景如 `++i` 操作：
`````col
````col-md
```java
// 设当前 id=42
private int id;
public int incAndGetId() {
    return ++id;
}
```
````
````col-md
![[image-20240313015941-0v4phx5.png]]
````
`````
* `dup_x2`：复制栈顶的值，并将其插入到栈中第四个位置。
* `dup2_x1`、`dup2_x2`：适用于 `long`、`double` 版本
# 控制跳转指令

有条件和无条件分支跳转，包括 `if`、`?:`、循环、`try-catch` 等涉及到流程控制的都由该系列指令完成

> [!note]
> 除 `ret` 外，表中所有指令的参数都是跳转地址。

| 指令类型         | 指令                                         | 说明                     |
| ------------ | ------------------------------------------ | ---------------------- |
| 条件转移<br />   | `ifeq`                                     | 若栈顶整数等于 0 跳转           |
|              | `iflt`                                     | 若栈顶整数小于 0 跳转           |
|              | `ifle`                                     | 若栈顶整数小于等于 0 跳转         |
|              | `ifne`                                     | 若栈顶整数非 0 时跳转           |
|              | `ifgt`                                     | 若栈顶整数大于 0 跳转           |
|              | `ifge`                                     | 若栈顶整数大于等于 0 跳转         |
|              | `ifnull`                                   | 若栈顶对象引用为 null 跳转       |
|              | `ifnonnull`                                | 若栈顶对象引用非 null 跳转       |
|              | if_icmpeq<sup>(if int compare equal)</sup> | 若栈顶两个 int 相等则跳转        |
|              | `if_icmpne`                                | 若栈顶两个 int 不等则跳转        |
|              | `if_icmplt`                                | 若栈顶两个 int 是小于关系则跳转     |
|              | `if_icmpgt`                                | 若栈顶两个 int 是大于关系则跳转     |
|              | `if_icmple`                                | 若栈顶两个 int 是小于等于关系则跳转   |
|              | `if_icmpge`                                | 若栈顶两个 int 是大于等于关系则跳转   |
|              | `if_acmpeq`                                | 若栈顶两个引用相等则跳转           |
|              | `if_acmpne`                                | 若栈顶两个引用不等则跳转           |
| 复合条件转移<br /> | `tableswitch`                              | `case` 紧凑时 `switch` 跳转 |
|              | `lookupswitch`                             | `case` 稀疏时 `switch` 跳转 |
| 无条件转移<br />  | `goto`                                     | 无条件跳转                  |
|              | `goto_w`                                   |                        |
|              | `jsr`                                      |                        |
|              | `jsr_w`                                    |                        |
|              | `ret`                                      |                        |
## for-in 字节码原理

`for-in` 循环形式实际上就是 `for` 循环的语法糖

`````col
````col-md
```java
public static int sum(int[] numbers) {
    int sum = 0;
    for (int i : numbers) {
        sum += i;
    }
    return sum;
}
```
````
````col-md
![[JVM 字节码 2024-08-01 17.45.06.excalidraw]]
````
`````

```java title:等效代码 fold
public static int sum(int[] numbers) {
    int sum = 0;
    int[] $array = numbers;
    int $len = $array.length;
    for (int $i = 0; $i != $len; $i++) {
        int i = $array[$i];
        sum = sum + i;
    }
    return sum;
}
```
## switch-case 字节码原理

* 紧凑的 switch：当 case 的值比较紧凑时，`tableswitch` 会生成一个跳转地址表，并将缺失的值补齐，实现 $O(1)$ 的时间复杂度查找。

`````col
````col-md
```java
public static int chooseNear(int i) {
    switch (i) {
        case 100: return 0;
        case 101: return 1;
        case 104: return 4;
        default:  return -1;
    }
}
```
````
````col-md
```java
public static int chooseNear(int i) {
    switch (i) {
        case 100: return 0;
        case 101: return 1;
        case 104: return 4;
        case 102:
        case 103:
        default:  return -1;
    }
}
```
````
`````
![[../../../../_resources/images/JVM 字节码 2024-08-01 17.59.13.excalidraw]]
* 断层的 switch：当 case 值比较分散时，switch 采用 `lookupswitch` 的形式，并使用二分查找的方式进行搜索，时间复杂度 $O(\log n)$

`````col
````col-md
```java
public static int chooseNear(int i) {
    switch (i) {
        case 1: return 1;
        case 10: return 10;
        case 100: return 100;
        default:  return -1;
    }
}
```
````
````col-md
![[JVM 字节码 2024-08-01 18.13.10.excalidraw]]
````
`````
* 字符串的 switch 实际是对字符串 `hashCode()` 的 switch，之后使用 `equals()` 进行二次检查的语法糖

```java
public static int test(String name) {
    switch (name) {
        case "Java":
            return 100;
        case "Kotlin":
            return 200;
        default:
            return -1;
    }
}
```

```java title:等效代码 fold
public static int test(String name) {
    String $str = name;
    int $matchIndex = -1;
    switch ($str.hashCode()) {
        case -2041707231: { // goto #50
            if ("Java".equals($str)) {
                $matchIndex = 0;
                break; // goto #61
            }
            break;
        }
        case 2301506: { // goto #36
            if ("Kotlin".equals($str)) {
                $matchIndex = 1;
                break; // goto #61
            }
            break;
        }
        default: break; // goto #61
    }
    switch ($matchIndex) { // #61
        case 0: return 100; // goto #88
        case 1: return 200; // goto #91
        default: return -1; // goto #95
    }
}
```

![[../../../../_resources/images/JVM 字节码 2024-08-01 18.16.09.excalidraw]]
当发生哈希冲突时等效代码如下：

`````col
````col-md
```java
public static int test(String name) {
    switch (name) {
        case "Aa":
            return 100;
        case "BB":
            return 200;
        default:
            return -1;
    }
}
```
````
````col-md
```java
public static int test(String name) {
    String $str = name;
    int $matchIndex = -1;
    switch ($str.hashCode()) {
        case 2112: {
            if ("Aa".equals($str)) {
                $matchIndex = 0;
            } else if ("BB".equals($str)) {
                $matchIndex = 1;
            }
            break;
        }
        default: break;
    }
    switch ($matchIndex) {
        case 0: return 100;
        case 1: return 200;
        default: return -1;
    }
}
```
````
`````
## ++ 运算符字节码原理

`````col
````col-md
```java
public static void test() {
    int i = 0;
    int j = i++;
    int k = ++i;
}
```
````
````col-md
```java
// 等价代码
public static void test() {
    int i = 0;
    int tmp = i; // tmp: 栈顶
    i = i + 1;   // iinc 不用操作数栈
    j = tmp;
    i = i + 1;
    k = i;
}
```
````
`````
![[Pasted image 20240801223140.png]]
## try-catch-finally 字节码原理

`````col
````col-md
```java
public void test() {
    try {
        tryItOut();
    } catch (RuntimeException e) {
        handleException(e);
    } finally {
        handleFinally();
    }
}
```

* `athrow` 可以将栈顶的异常对象抛出，对应 `throw` 关键字
* 产生异常时，跳转地址通过查询异常表获取
* `finally` 块代码是直接插入到每个分支，而不是通过跳转实现的
* 使用的局部变量表比 Class 文件记录的变量表多 1（图中编号 24 指令），用于直接抛出异常
* 产生异常时，虚拟机自动将异常信息对象（`Throwable`）入栈，跳转到 `catch` 分支时栈顶元素为异常信息
````
````col-md
```java
// 等价实现
public void test() {
    try {
        tryItOut();
        handleFinally();
    } catch (RuntimeException e) {
        try {
            handleException(e);
            handleFinally();
        } catch (Throwable ee) {
            handleFinally();
            throw ee;
        }
    } catch (Throwable e) {
        handleFinally();
        throw e
    }
}
```
````
`````
![[../../../../_resources/images/JVM 字节码 2024-08-01 22.34.32.excalidraw]]
与普通的 try-catch 块相比，虚拟机在进入 `finally` 块前将 `return` 结果进行缓存，因此 `finally` 块不影响返回值。

`````col
````col-md
```java
public static int test() {
    int i = 100;
    try {
        return i;
    } finally {
        ++i;
    }
}
```
![[JVM 字节码 2024-08-01 22.37.48.excalidraw]]
````
````col-md
```java
// 等价实现
public void test() {
    try {
        tryItOut();
        handleFinally();
    } catch (RuntimeException e) {
        try {
            handleException(e);
            handleFinally();
        } catch (Throwable ee) {
            handleFinally();
            throw ee;
        }
    } catch (Throwable e) {
        handleFinally();
        throw e
    }
}
```
````
`````
## try-with-resources 字节码原理

`````col
````col-md
```java
try (FileOutputStream in = 
       new FileOutputStream("test.txt")) {
    in.write(1);
} catch (IOException e) {
    throw new RuntimeException(e);
} finally {
    System.out.println("OK");
}
```
![[JVM 字节码 2024-08-01 22.39.13.excalidraw]]
````
````col-md
```java
// 等价实现
public static void test() {
    try {
        try {
            // 0-9
            FileOutputStream in =
                new FileOutputStream("test.txt");
            try {
                // 10-12
                in.write(1);
            } catch (Throwable e) {
                try {
                    // 22-24
                    in.close();
                } catch (Throwable e2) {
                    // 30-37
                    e2.addSuppressed(e);
                    throw e2;
                }
                // 36-37
                throw e;
            }
        } catch (Throwable eee) {
            // 59-
            System.out.println("OK");
            throw eee;
        }
    } catch (IOException e) {
        // 49-58
        try {
            throw new RuntimeException(e);
        } catch (Throwable eee) {
            System.out.println("OK");
            throw eee;
        }
    }
    // 38
    System.out.println("OK");
}
```
````
`````
# 对象创建

对象创建的过程分为三步：
1. 第一次加载类时，调用 `<cinit>` 方法
2. 使用 `new` 指令创建一个对象
3. 调用 `<init>` 方法初始化对象，该方法即构造函数
## cinit 方法

`<cinit>` 是类的静态初始化方法，静态初始化块、静态变量初始化都会编译到这个方法中。该方法不可被直接调用，仅在四个指令触发时被调用：

* 创建类对象实例，包括 `new` 、反射、反序列化等
* 访问类的静态变量和静态变量方法
* 访问类的静态字段或对非 `final` 静态字段赋值
* 初始化某个类的子类

```java
private static int a = 0;
static {
    System.out.println("static block");
}
```

编译出的结果为：

```
descriptor: ()V
  iconst_0
  putstatic     #7  // a: I
  getstatic     #13 // java/lang/System.out:Ljava/io/PrintStream;
  ldc           #19 // static block
  invokevirtual #21 // java/io/PrintStream.println:(Ljava/lang/String;)V
  return
```
## 对象创建指令

一个对象创建需要三条指令：`new`，`dup`，`invokespecial <init>`。

1. `new`：创建一个类的实例引用，将该引用压入栈顶
2. 使用 `invokespecial` 指令调用 `<init>` 方法访问构造函数

`dup` 的作用是复制一份对象引用并压入栈。由于 `invokespecial` 调用构造函数后会弹出一份引用，故需要复制一份保证保留一个对象引用。
![[Pasted image 20240801224218.png]]
## init 方法

`<init>` 方法对应的是对象初始化方法，包括类构造函数、非静态变量初始化、对象初始化代码块。

```java
public class MyClass {

    // 局部变量赋值
    private int a = 10;

    // 构造函数
    public MyClass() {
        int c = 30;
    }

    // 对象初始化代码块
    {
        int b = 20;
    }
}
```

编译后，字节码为：

```
public MyClass();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
        aload_0;
        invokespecial #1;  // 调用基类构造 java/lang/Object."<init>":()V
        aload_0;
        bipush 10;
        putfield #7;       // a:I = 10
        bipush 20;
        istore_1;          // int b=20
        bipush 30;
        istore_1;          // int c=30
        return;
```

结论：尽管局部变量赋值和初始化代码块写在构造函数之外，编译到字节码中均位于构造函数之中。

因此，局部变量赋值出的异常，需要在构造函数之中抛出异常：

```java
private FileOutputStream fos = new FileOutputStream("test.txt");
public MyClass() throws IOException {
    // do something
}
```
# 方法调用

| 方法指令            | 说明                             |
| --------------- | ------------------------------ |
| invokestatic    | 调用静态方法                         |
| invokespecial   | 调用私有实例方法、构造器，使用 `super` 调用基类方法 |
| invokevirtual   | 调用非私有实例方法                      |
| invokeinterface | 调用接口方法                         |
| invokedynamic   | 调用动态方法                         |
## invokestatic

`invokestatic` 指令用于调用静态方法。该指令要求方法在编译时即确定，在运行时不会修改，属于静态绑定，不需要将对象加载到操作数栈，只需要将参数入栈即可：

```class
// 调用 Integer.valueOf("42");
ldc          #2 // "42"
invokestatic #3 // java/lang/Integer.valueOf:(Ljava/lang/String;)
```
## invokevirtual

`invokevirtual` 指令用于调用普通实例方法，目标方法在运行时根据对象实例确定，类似 C++ 虚方法，需要先将函数所在对象入栈。

```java
class Color {
    public void printColorName() {
        Color red = new Red();
        Color yellow = new Yellow();
        foo(red);
        foo(yellow);
    }

    public static void foo(Color color) {
        color.printColorName();
    }
}

class Red extends Color {
    @Override
    public void printColorName() {
        System.out.println("Red");
    }
}

class Yellow extends Color {
    @Override
    public void printColorName() {
        System.out.println("Yellow");
    }
}
```

编译后结果为：

```java
// Color.class
public void printColorName();
  descriptor: ()V
  Code:
    new           #7  // class org/example/Red 创建 Red 对象
    dup
    invokespecial #9  // Method org/example/Red.<init>: ()V 调用构造函数
    astore_1          // 将新建的 Red 对象存一下
    ...               // 此处省略创建 Yellow 对象的操作
    aload_1
    invokestatic  #13 // Method foo:(Lorg/example/Color;)V 调用函数
    aload_2
    invokestatic  #13 // Method foo:(Lorg/example/Color;)V 调用函数
    return

public static void foo(ory.example.Color);
  descriptor: (Lorg/example/Color;)V
  Code:
    aload_0
    invokevirtual #19 // Method printColorName:()V
    return
```

上面例子中，`foo` 函数调用了 `Color#printColorName` 方法，但其目标方法却不一定，需要通过对象的实际类型进行分派
## invokespecial

调用特殊的成员方法：
* 实例的构造函数 `<init>`
* `private` 修饰的私有方法
* `super` 关键字调用的基类方法

这几种方法不会被子类覆盖，因此可以在编译时确定被调用方法（类似 `static`），使用单独的指令可以提高效率。但查找方法还是需要对应实例，因此还是需要将被调函数所在对象压入栈。init方法的调用就是使用 `invokespecial` 指令。
## invokeinterface

`invokeinterface` 指令用于调用接口方法，类似 `invokevirtual` 调用继承方法，但查的表不同。

```java
private AutoCloseable resources;
public void foo() throws Exception {
    resources.close();
}
```

```
aload_0                 // this
getfield #7             // Field resources:Ljava/lang/AutoCloseable;
invokeinterface #13, 1  // Method java/lang/AutoCloseable.close:()V
return
```

可见，`invokeinterface` 指令除需要传入一个方法引用外，还需要一个编号用于 Java 接口方法分派。
### Java 方法分派原理

Java 方法分派受 C++ 影响，通过虚方法表解决方法继承重写问题，表明也叫 `vtable`

`````col
````col-md
```java
class A {
    public void method1() { }
    public void method2() { }
    public void method3() { }
}

class B extends A {
    @Override
    public void method2() { }
    public void method4() { }
}
```
````
````col-md
|index|A 方法引用|
| -------| -----------|
|1| `A/method1` |
|2| `A/method2` |
|3| `A/method3` |

<br/>

|index|B 方法引用|
| -------| -----------|
|1| `A/method1` |
|2| `B/method1` |
|3| `A/method1` |
|4| `B/method1` |
````
`````

同时，JVM 还提供了名为 `itable`（Interface method table）的表用于支持多接口。该表由偏移量表和方法表两部分组成。

`````col
````col-md
```java
interface A {
    void method1();
    void method2();
}

interface B {
    void method3();
}

class C implements A, B {

    @Override
    public void method1() { }
    @Override
    public void method2() { }
    @Override
    public void method3() { }
}

class D implements B {

    @Override
    public void method3() { }
}
```
````
````col-md
| index | C itable 方法引入 |
| ----- | ------------- |
| 1     | `method1`     |
| 2     | `method2`     |
| 3     | `method3`     |

<br/>

|index|D itable 方法引入|
| -------| -------------------|
|1| `method3` |
````
`````
## invokedynamic

`invokedynamic` 指令由 JDK 7 开始加入，对多语言在 JVM 上鸭子类型的实现提供技术支撑。

该指令直到 JDK 8 才开始在 Java 中使用，用于 lambda 表达式的调用。`invokedynamic` 的调用过程如下：

1. 首次执行 `invokedynamic` 指令时，调用引导方法 `Bootstrap Method`，根据该方法获取一个 `CallSite` 对象。
2. 从 `CallSite` 获取 `MethodType` 对象，该对象包含 MethodHandle 对象代表对一个方法的引用。
3. 在 `CallSite` 没有发生变化的情况下，`MethodHandle` 可一直使用。通过 `MethodHandle` 调用函数。

以 Groovy 编译结果为例：

```groovy
def add(a, b) {
    return a + b;
}

println add(3, 6)
println add("xxx", "yyy")
println add("xxx", 3)
```

其中，`run` 函数编译结果为：

```
aload_0
aload_0
iconst_3
bipush 6
invokedynamic #48, 0 // InvokeDynamic #1: invoke:(LMain;II)Ljava/lang/Object
invokedynamic #53, 0 // InvokeDynamic #2: invoke:(LMain;Ljava/lang/Object;)Ljava/lang/Object;
pop

aload_0
aload_0
ldc #55 // "xxx"
ldc #57 // "yyy"
invokedynamic #60, 0 // InvokeDynamic #1: invoke:(LMain;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object
invokedynamic #53, 0 // InvokeDynamic #2: invoke:(LMain;Ljava/lang/Object;)Ljava/lang/Object;
pop

aload_0
aload_0
ldc #55 // "xxx"
iconst_3
invokedynamic #63, 0 // InvokeDynamic #1: invoke:(LMain;Ljava/lang/String;I)Ljava/lang/Object
invokedynamic #53, 0 // InvokeDynamic #2: invoke:(LMain;Ljava/lang/Object;)Ljava/lang/Object;

areturn
```

该函数使用了 `invokedynamic` ，先从 `BootstrapMethods` 部分查找对应的方法

```
BootstrapMethods:
  0: #34 REF_invokeStatic org/codehaus/groovy/vmplugin/v8/IndyInterface.bootstrap:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;
    Method arguments:
      #26 runScript
      #27 0
  1: #34 REF_invokeStatic org/codehaus/groovy/vmplugin/v8/IndyInterface.bootstrap:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;
    Method arguments:
      #44 add
      #45 2
  2: #34 REF_invokeStatic org/codehaus/groovy/vmplugin/v8/IndyInterface.bootstrap:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;
    Method arguments:
      #50 println
      #45 2
  3: #34 REF_invokeStatic org/codehaus/groovy/vmplugin/v8/IndyInterface.bootstrap:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/String;I)Ljava/lang/invoke/CallSite;
    Method arguments:
      #66 plus
      #27 0
```

根据字节码可见，add 方法被导向了 `InvokeDynamic #1`，在 `BootstrapMethod` 中可见其结构：

```java
public static CallSite bootstrap(
    Lookup caller,   // 调用者
    String callType, // 调用类型 = CALL_TYPES.METHOD("invoke")
    MethodType type;
    String name;     // 方法名 = "add"
    int flags
) { ... }
```
### MethodHandle

> [!note] MethodHandle
> 方法句柄（又称方法指针），轻量级的 `Method`，表示对一个函数的引用

`MethodHandle` 可以直接使用，可以代替反射的 `Method`，具有类似原生调用的效率，使用步骤如下：

1. 创建 `MethodType` 对象，指定方法签名。每个 `MethodHandle` 对应一个 `MethodType`
2. 调用 `MethodHandles.lookup()` 获取 `Lookup` 对象，根据方法类型不同使用 `findStatic`、`findSpecial`、`findVirtual` 查找方法
3. 使用获取的 `MethodHandle` 方法句柄的 `ionvoke` 或 `invokeExact` 方法调用方法

```java
public class MyClass {

    public void foo(int i) {
        System.out.println(i);
    }
}

public static void main(String[] args) throws Throwable {
    // V(I)
    MethodType methodType = MethodType.methodType(Void.TYPE, Integer.TYPE);
    // void MyClass::foo(int)
    MethodHandle foo = MethodHandles.lookup().findVirtual(MyClass.class, "foo", methodType);
    // new MyClass().foo(10)
    foo.invoke(new MyClass(), 10);
}
```
### lambda 表达式原理

```java
public static void main(String[] args) {
    Runnable r = () -> {
        System.out.println("Hello lambda");
    };
    r.run();
}
```

```
public static void main(java.lang.String[]):
  invokedynamic   #7, 0  // InvokeDynamic #0: run: ()Ljava/lang/Runnable
  astore_1
  aload_1
  invokeinterface #11, 1 // InterfaceMethod: java/lang/Runnable.run:()V
  return
private static void lambda$main$0():
  getstatic     #15 // Field java/lang/System.out:Ljava/io/PrintStream;
  ldc           #21 // String Hello lambda
  invokevirtual #23 // Method java/io/PrintStream.println:(Ljava/lang/String;)V
  return
BootstrapMethods:
  0: #46 REF_invokeStatic java/lang/invoke/LambdaMetafactory.metafactory:(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
    Method arguments:
      #53 ()V
      #54 REF_invokeStatic org/example/Main.lambda$main$0:()V
      #53 ()V
```

在 `Main` 类中生成了一个 `lambda$main$0` 方法，该方法即 `lambda` 的代码。该函数的 `BootstrapMethod` 表示为：

```
public static CallSite metafactory(
    Lookup caller,                       // 调用者，用于查找方法的上下文
    String invokedName,                  // 调用函数名 = "run"
    MethodType invokedType,
    MethodType samMethodType,            // 函数式接口定义的方法 = ()void
    MethodHandle implMethod,             // 编译时生成的静态方法 = lambda$main$0
    MethodType instantiatedMethodType    // 一般与 samMethodType 相同或为其特例 = ()void
);
```

在 `java.lang.invoke.LambdaMetafactory` 中可以看到 `metafactory` 的源码：

```java
public static CallSite metafactory(MethodHandles.Lookup caller,
                                   String interfaceMethodName,
                                   MethodType factoryType,
                                   MethodType interfaceMethodType,
                                   MethodHandle implementation,
                                   MethodType dynamicMethodType)
        throws LambdaConversionException {
    AbstractValidatingLambdaMetafactory mf;
    mf = new InnerClassLambdaMetafactory(caller, factoryType, interfaceMethodName, 
                                         interfaceMethodType, implementation, dynamicMethodType, 
                                         false, EMPTY_CLASS_ARRAY, EMPTY_MT_ARRAY);
    mf.validateMetafactoryArgs();
    return mf.buildCallSite();
}
```

而在 `InnerClassLambdaMetafactory` 中，使用 `ASM` 生成内部类：

```java
implMethodClassName = implClass.getName().replace('.', '/');
implMethodName = implInfo.getName();
implMethodDesc = implInfo.getMethodType().toMethodDescriptorString();
constructorType = factoryType.changeReturnType(Void.TYPE);
lambdaClassName = lambdaClassName(targetClass);
// If the target class invokes a protected method inherited from a
// superclass in a different package, or does 'invokespecial', the
// lambda class has no access to the resolved method. Instead, we need
// to pass the live implementation method handle to the proxy class
// to invoke directly. (javac prefers to avoid this situation by
// generating bridges in the target class)
useImplMethodHandle = (Modifier.isProtected(implInfo.getModifiers()) &&
                       !VerifyAccess.isSamePackage(targetClass, implInfo.getDeclaringClass())) ||
                       implKind == H_INVOKESPECIAL;
cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
int parameterCount = factoryType.parameterCount();
if (parameterCount > 0) {
    argNames = new String[parameterCount];
    argDescs = new String[parameterCount];
    for (int i = 0; i < parameterCount; i++) {
        argNames[i] = "arg$" + (i + 1);
        argDescs[i] = BytecodeDescriptor.unparse(factoryType.parameterType(i));
    }
} else {
    argNames = argDescs = EMPTY_STRING_ARRAY;
}
```
# 运算符
| 运算符     | 指令       | 适用类型         |
| ------- | -------- | ------------ |
| `+`     | `<T>add` | `i, l, f, d` |
| `-`     | `<T>sub` | `i, l, f, d` |
| `/`     | `<T>div` | `i, l, f, d` |
| `*`     | `<T>mul` | `i, l, f, d` |
| `%`     | `<T>rem` | `i, l, f, d` |
| `-`（取负） | `<T>neg` | `i, l, f, d` |
| `&`     | `<T>and` | `i, l`       |
| `\|`    | `<T>or`  | `i, l`       |
| `^`     | `<T>xor` | `i, l`       |
# 类型转换

低于 `int` 的类型（boolean, char, byte, short）在字节码中也是以 `int` 方式来处理的，所以转换到 `int` 时不需要特殊的指令。

| 类型       | `int` | `long` | `float` | `double` | `byte` | `char` | `short` |
| -------- | ----- | ------ | ------- | -------- | ------ | ------ | ------- |
| `int`    |       | `i2l`  | `i2f`   | `i2d`    | `i2b`  | `i2c`  | `i2s`   |
| `long`   | `l2i` |        | `l2f`   | `l2d`    |        |        |         |
| `float`  | `f2i` | `f2l`  |         | `f2d`    |        |        |         |
| `double` | `d2i` | `d2l`  | `d2f`   |          |        |        |         |
# synchronized

> [!note] synchronized
> 定义了一个临界区，保证方法和代码块在同一时刻只有一个线程可以进入临界区。

JVM 不会使用任何特殊字节码指令处理同步方法，只会增加一个 `flag`，判断方法是否是同步的。

```java
int count = 0;
public synchronized void increase() {
    count++;
}
```

```
flags: 0x0021 (ACC_PUBLIC | ACC_SYNCHRONIZED)
  aload_0
  dup
  getfield #7 // Field: count: I
  iconst_1
  iadd
  putfield #7 // Field: count: I
  return
```

将 `synchronized` 修饰替换成等效的关键字，可以看到其中的细节：

`````col
````col-md
```java
int count = 0;
public void increase() {
    synchronized(this) {
        count++;
    }
}
```
````
````col-md
```java
// 视 monitorenter, monitorexit 作函数
public void increase() {
    monitorenter(this);
    try {
        count++;
    } finally {
        monitorexit(this);
    }
}
```
````
`````
```
Code: 
   aload_0
   dup
   astore_1
   monitorenter
   // 本段与上面的相同，省略
4  aload_0
   ...
   putfield #7
   // 相同部分结束
   aload_1
   monitorexit
16 goto 24
19 astore_2
   aload_1
   monitorexit
22 aload_2
   athrow
24 return

Exception Table:
  from  4 to 16, target 19, type any
  from 19 to 22, target 19, type any
  
```
# 反射

反射是 Java 核心特性之一，允许在运行时动态调用某个对象的方法、创建实例、获取属性等。

```java
private static int count;
public static void foo() {
    new Exception("Test #" + (count++)).printStackTrace();
}
public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Class<Main> aClass = Main.class;
    Method foo = aClass.getMethod("foo");
    for (int i = 0; i < 20; i++) {
        foo.invoke(null);
    }
}
```

我们通过每次反射调用时输出调用栈可以看到反射的调用过程：

```
java.lang.Exception: Test #0
	at org.example.Main.foo(Main.java:12)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
```

```
java.lang.Exception: Test #16
	at org.example.Main.foo(Main.java:12)
	at jdk.internal.reflect.GeneratedMethodAccessor1.invoke(Unknown Source)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at org.example.Main.main(Main.java:19)
```

可以看到，前 15 次通过 `NativeMethodAccessorImpl.invoke0` 访问，这是一个 `native` 方法。调用次数多了，触发 `inflation` 机制，由 `GeneratedMethodAccessor` 创建 `GeneratedMethodAccessor1` 类完成调用。

> [!warning] 通过 `JNI native` 调用会比动态生成类慢 20 倍左右，但动态生成类第一次生成字节码又比反射慢 3-4 倍，因此调用次数较少的情况下生成字节码反而更慢

调整 `inflation` 阈值的参数有两个
-  `sun.reflect.inflationThreshold` 默认 15，即触发的阈值
- `sun.reflect.noInflation` 默认 `false`，设置为 `true` 可以禁用 `inflation`

通过 `arthas` 的 `jad` 反编译 `GeneratedMethodAccessor1`，忽略异常处理，其结果为：

```java
package jdk.internal.reflect;

import java.lang.reflect.InvocationTargetException;
import jdk.internal.reflect.MethodAccessorImpl;
import org.example.Main;

public class GeneratedMethodAccessor1 extends MethodAccessorImpl {
    /*
     * Loose catch block
     */
    public Object invoke(Object object, Object[] objectArray) throws InvocationTargetException {
        // 忽略异常处理
        Main.foo();
        return null;
        // 忽略异常处理
    }
}
```
