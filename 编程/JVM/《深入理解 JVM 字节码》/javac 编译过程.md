java 编译分为前端编译和后端编译，javac 负责前端编译。

![[../../../../_resources/images/javac 编译过程 2024-08-02 08.03.34.excalidraw]]

`javac` 本身由 Java 编写，没有 YACC、Lex 等生成工具，代码精简高效。

> [!note] 可以在 OpenJDK 下载其源码并调试。

`javac` 编译分为 7 个阶段：

1. parse：词法分析及语法分析
2. enter：生成符号表
3. process：处理注解
4. attr：检查语义合法性、常量折叠
5. flow：数据流分析
6. desugar：去除语法糖
7. generate：生成字节码
# parse 词法分析&语法分析

> [!note] 扫描
> 词法分析（`lexical analyze`）将源代码拆分成词法记号 `Token`，这个过程称为扫描 `scan`，完成对符号的拆分，并将空格、注释等无用代码排除。

`com.sun.tools.javac.parser.Scanner` 类可以在 Java8 下使用 `com.perfma.wrapped:com.sun.tools` 库

> [!note] 词法分析由 `com.sun.tools.javac.parser.Scanner` 完成

> [!note] 语法分析由 `com.sun.tools.javac.parser.JavacParser` 完成

`````col
````col-md
flexGrow=2.5
===
```java
ScannerFactory factory = ScannerFactory.instance(new Context());
Scanner scanner = factory.newScanner("int k = i + j;", false);

scanner.nextToken();
while (scanner.token().kind != Tokens.TokenKind.EOF) {
    System.out.println(scanner.token().kind);
    scanner.nextToken();
}
```
````
````col-md
flexGrow=1
===
```java
int
token.identifier
=
token.identifier
+
token.identifier
';'
```
````
`````

词法分析后是语法分析（`syntax analyze`），在词法分析的基础上分析记号之间的关系，构造抽象语法树（`AST`，`Abstract Syntax Tree`）。AST 的每个节点是一个语法单元，是后面语义分析、语法校验、代码生成的基础。

![[../../../../_resources/images/javac 编译过程 2024-08-02 08.12.39.excalidraw]]
# enter 生成符号表

* 解析和填充符号表（`symbol table`）：遍历 AST 信息存储到符号表中，便于快速查询
* 没有自定义构造时，添加 `<init>` 默认构造方法

> [!note] 符号表：由标识符、标识符类型、作用域等信息构成的信息表

Enter 阶段由 `com.sun.tools.javac.comp.Enter` 和 `com.sun.tools.javac.comp.MemberEnter` 完成

符号由 `Symbol` 类表示，每个符号必然有以下几个属性：
* `name`：符号名，如 `x`，`y` 等变量名
* `kind`：符号类型，是一个 `Kind` 枚举，如 `Kinds.VAR` 表示变量，`Kinds.MTH` 表示方法符号
* `type`：符号类型，指的是符号变量的变量类型，如 `int`，`char` 等，方法的类型是 `null`

> [!note] 作用域
> 作用域由 `Scope` 类表示，可以嵌套。查找符号时，先从当前作用域查找，找不到则向外层作用域查找，直到顶层作用域。
# process 处理注解

由 `com.sun.tools.javac.processing.JavacProcessingEnvironment` 类完成，负责自定义注解处理。

通过注解处理生成 class，其效率比反射有明显提升。
# attr 语义分析

`attr` 阶段是语义分析的一部分，主要完成检查变量类型、方法返回值类型是否合法，是否有重复的变量、类定义等

Enter 阶段由 `com.sun.tools.javac.comp.Attr` 完成
1. `com.sun.tools.javac.comp.Check`
    * `checkType`：检查方法返回值与方法声明的返回值类型一致
    * `checkUnique`：检查是否有重复定义
2. `com.sun.tools.javac.comp.Resolve`
    * 检查变量、方法、类访问是否合法（`private` 等）
    * 为重载方法调用选择具体方法
3. `com.sun.tools.javac.comp.ConstFold` 
    * 编译期常量合并，如字符串常量相加，常量整数运算运算等
4. `com.sun.tools.javac.comp.Infer`
    * 推导泛型方法的参数类型
# flow 数据流分析

`flow` 阶段用来处理数据流分析，由 `com.sun.tools.javac.comp.Flow` 完成

很多编译时校验在这个阶段完成
* 非 void 方法是否所有分支都有返回值
* 受检异常（checked exception）是否被捕获或抛出
* 局部变量使用前是否完成初始化
* `final` 变量是否有重复赋值
* 是否有不可达代码
# desugar 解语法糖

该步骤用于解语法糖，如泛型、内部类、try-with-resources、for-each、自动拆装箱、字符串和枚举的 switch、++、--、变长参数等

`com.sun.tools.javac.comp.TransTypes`：泛型擦除和根据泛型的类型转换

`````col
````col-md
```java
List<Long> idList = new ArrayList<>();
idList.add(1L);
long firstId = idList.get(0);
```
````
````col-md
```java
List idList = new ArrayList();
idList.add(Long.valueOf(1L));
long firstId = ((Long) idList.get(0)).longValue();
```
````
`````

除泛型外的语法糖由 `com.sun.tools.javac.comp.Lower` 完成

* 去除逻辑死代码（不可访问的 if 块等）

`````col
````col-md
```java
if (false) {
    System.out.println("String #1 false");
} else {
    System.out.println("String #2 true");
}
```
````
````col-md
```java
System.out.println("String #2 true");
```
````
`````

* String、枚举类的 `switch`

`````col
````col-md
对于字符串，详见[[JVM 字节码#switch-case 字节码原理]]
```java
String s;
switch(s) {
    case "A": ...
    case "B": ...
}
```
````
````col-md
```java
String s;
switch(s.hashCode()) {
    case "A".hashCode(): {
        if ("A".equals(s)) ...
    }
    case "B".hashCode(): {
        if ("B".equals(s)) ...
    }
}
```
````
`````

对于枚举，编译器会生成一个中间类，维护一个 `SwitchMap` 数组，记录 `ordinal` 与递增整数序列的映射关系

> [!warning] 为了提供更好的性能，`ordinal()` 值不一定是连续的

  ```java
  enum Color { RED, BLUE }
  Color color;
  switch (color) {
      case RED: ...
      case BLUE: ...
  }
  ```

  ```java
  class Outer$0 {
      synthetic static final int[] $SwitchMap$Color = 
                                       new int[Color.values().length];
      static {
          $SwitchMap$Color[Color.RED.ordinal()] = 1;
          $SwitchMap$Color[Color.BLUE.ordinal()] = 2;
      }
  }

  Color color;
  switch(Outer$0.$SwitchMap$Color[color.ordinal()]) {
      case 1: ...
      case 2: ...
  }
  ```

# generate 生成 class

`generate` 阶段主要用于遍历 AST 生成最终 Class 文件

> [!info] `generate` 阶段由 `com.sun.tools.javac.jvm.Gen` 完成

* 初始化块代码并收集到 `<cinit>` 与 `<init>` 中

`````col
````col-md
```java
public class MyInit {
    {
        System.out.println("hello");
    }
    int a = 100;
}
```
````
````col-md
```java
public class MyInit {
    int a;
    public MyInit() {
        System.out.println("hello");
        a = 100;
    }
}
```
````
`````

* 将字符串拼接语句转化为 `StringBuilder.append()` 方法

  ```java
  public void foo(String x, String y) {
      System.out.println(x + y);
  }
  ```

  ```java
  public void foo(String x, String y) {
      String str = new StringBuilder().append(x).append(y).toString();
      System.out.println(str);
  }
  ```

* 为 `sychronized` 关键字生成异常表，保证 `monitorenter` 与 `monitorexit` 成对调用

* 选择 `tableswitch` 和 `lookupswitch` 指令

  ```java
  // nlabels case 个数
  // hi      case 最大值
  // lo      case 最小值
  long table_space_cost = 4 + (hi - lo + 1);
  long table_time_cost = 3;
  long lookup_space_cost = 3 + 2 * nlabels;
  long lookup_time_cost = nlabels;
  // cost = space_cost + 3 * time_cost
  long table_cost = table_space_cost + 3 * table_time_cost;
  long lookup_cost = lookup_space_cost + 3 * lookup_time_cost;
  int op = nlabels > 0 && table_cost <= lookup_cost ? TABLESWITCH : LOOKUPSWITCH;
  ```

‍
