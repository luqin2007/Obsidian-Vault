# Metadata 注解

Kotlin 代码最终编译成 Class 文件，但一些特性无法仅从字节码实现，因此被写入到 `@Metadata` 注解

```java
@Metadata(mv = {1, 8, 0}, k = 2, xi = 50, 
          d1 = {...},
          d2 = {"calcSHA256", "", "path", "Ljava/nio/file/Path;", "compareAndPrintFile", "", "main", "parseM3U8", "printNameNo", "renameNoteImages", "pre", "replaceAdMark", "setMarkdownImg", "untitledKt"})
```

|字段名|含义|说明|
| --------| ------------------| ---------------------------|
|k|kind| `Metadata` 注解类型|
|mv|metadata version| `metadata` 版本号|
|bv|bytecode version|Kotlin 字节码版本号|
|d1|data1|额外语法信息，`protobuf` 二进制流|
|d2|data2|额外语法信息|
|xs|extra string|额外字符串信息|
|xi|extra int|额外信息标记|
|pn|package name|全限定包名|

|xi 值|说明|
| --------| --------------------------------------------------|
|0|该 class 是 multi-file class facade 或其中一部分|
|1|该 class 由 pre-release 版本编译而成|
|2（4）|该 class 由 Kotlin 脚本（.kts）编译而成|

|k 值|含义|
| ------| ------------------------------|
|1|Class 类型|
|2|File 类型|
|3|Synthetic Class 类型|
|4|Multi-file class facade 类型|
|5|Multi-file class part 类型|
# 顶层方法

Kotlin 中，方法、变量不必要包裹在一个类中，称为顶层方法、顶层变量。其实质是编译成一个 `文件名Kt` 类的静态成员。

`````col
````col-md
flexGrow=1
===
```Kotlin
// Hello.kt
fun foo() {
    println("hello")
}
```
````
````col-md
flexGrow=2
===
```Java
public final class HelloKt {
    public static final void foo() {
        System.out.println("hello");
    }
}
```
````
`````
# object 单例

Kotlin 可以使用 `object` 关键字声明懒汉式单例对象

```kotlin
object MySingleton {
    fun foo() {
        println("hello")
    }
}
```

```java title:'等价 java 代码'
public final class MySingleton {
    public static final MySingleton INSTANCE = new MySingleton();

    private MySingleton() {
    }

    public final void foo() {
        System.out.println("hello");
    }
}
```

``` title:'编译后 class 文件' fold
MySingleton INSTANCE:
  descriptor: LMySingleton
  flags: 0x0019 ACC_PUBLIC, ACC_STATIC, ACC_FINAL
<init>():
  descriptor: ()V
  flags: (0x0002) ACC_PRIVATE
  Code:
    aload_0
    invokespecial #8 // Object.<init>()
    return
<cinit>():
  descriptor: ()V
  flags: (0x0008) ACC_STATIC
  Code:
    new           #2  // MySingleton
    dup
    invokespecial #27 // MySingleton.<init>()
    putstatic     #30 // INSTANCE
    return
void foo():
  descriptor: ()V
  flags: (0x0011) ACC_PUBLIC, ACC_FINAL
  Code:
    ldc           #13 // hello
    getstatic         // System.out
    swap
    invokevirtual #25 // PrintStream.println(Object)V
    return
```

```kotlin
fun main() {
    MySingleton.foo()
}
```

```java title:'等价 java 代码'
public final class MySingletonTestKt {
    public static final void main(String[] args) {
        main();
    }
    public static final void main() {
        MySingleton.INSTANCE.foo();
    }
}
```

``` title:'编译后 main 方法' fold
main(String[]):
  descriptor: ([Ljava/lang/String;)V
  flags: (0x1009) ACC_PUBLIC, ACC_STATIC, ACC_SYNTHETIC
  Code:
    invokestatic #18 // main()V
    return
main():
  descriptor: ()V
  flags: (0x0019) ACC_PUBLIC, ACC_STATIC, ACC_FINAL
  Code:
    getstatic     #12 // MySingleton.INSTANCE
    invokevirtual #15 // MySingleton.foo()V
    return
```
# 扩展方法

Kotlin 支持扩展方法，在字节码层面使用装饰器模式对原始类进行包装

> [!note] 当同时存在扩展方法和成员方法时，优先选择扩展方法

```kotlin
fun  String.repeat(n: Int): String {
    val sb = StringBuilder()
    repeat(n) {
        sb.append(this)
    }
    return sb.toString()
}

fun main() {
    val str = "ab".repeat(5)
    println(str)
}
```

```java title:'等价 Java 方法'
public static final String repeat(String thiz, int n) {
    checkNotNull(thiz, "<this>");
    StringBuilder sb = new StringBuilder(); // localVar[2]
    int i = 0; // localVar[3]
    while (true) {
        if (i >= n) break;
        int j = i; // localVar[4]
        int k = 0; // localVar[5] ?
        sb.append(thiz);
        i++;
    }
    String s = sb.toString();
    checkNotNull(s, "sb.toString()");
    return s;
}

public static final void main() {
    String str = repeat("ab", 5);
    System.out.println(str);
}
```

```class title:'class 文件方法片段' fold
String repeat(String, int):
  descriptor: (Ljava/lang/String;I)Ljava/lang/String;
  flags: (0x0019) ACC_PUBLIC, ACC_STATIC, ACC_FINAL
  Code:
    aload_0
    ldc           #9 // String <this>
    invokestatic  #15 // checkNotNull(Object, String)V
    new           #17 // StringBuilder
    dup
    invokespecial #21 // StringBuilder.<init>
    astore_2
    iconst_0
    istore_3
16  iload_3
    iload_1
    if_icmpge 40
    iload_3
    istore 4
    iconst_0
    istore 5
40  aload_2
    aload_0
    invokevirtual #25 // StringBuilder.append(String)SB
    pop
    nop
    iinc 3, 1
    goto 16
    aload 2
    invokevirtual #29 // StringBuilder.toString()String
    dup
    ldc           #31 // String sb.toString()
    invokestatic  #34 // checkNotNull(Object, String)V

void main():
  descriptor: ()V
  flags: (0x0019) ACC_PUBLIC, ACC_STATIC, ACC_FINAL
  Code:
    ldc           #45 // ab
    iconst_5
    invokestatic  #47 // repeat(String,I)String
    astore_0
    getstatic     #53 // System.out
    aload_0
    invokevirtual #59 // PrintStream.println(Object)V
    return
```
# 接口默认方法

Kotlin 支持接口默认方法，但实现方式与 Java 不同，是通过内部类存放方法的实现。

```kotlin
interface MyInterface {
    fun foo() {
        println("foo")
    }
}

class MyClass : MyInterface
```

```java title:'等价 Java 方法'
public interface MyInterface {
    void foo();

    public static final class DefaultImpls {
        public static foo(MyInterface thiz) {
            System.out.println("foo");
        }
    }
}

public final class MyClass implements MyInterface {
    public foo() {
        MyInterface.DefaultImpls.foo(this);
    }
}
```

``` title:class fold
interface MyInterface:
  flags: (0x0601) ACC_PUBLIC, ACC_INTERFACE, ACC_ABSTRACT
  foo():
    descriptor: ()V
    flags: (0x0401) ACC_PUBLIC, ACC_ABSTRACT
  InnerClasses:
    public static final #23= #22 of #2; // class MyInterface$DefaultImpls

class MyInterface$DefaultImpls
  flags: (0x0031) ACC_PUBLIC, ACC_FINAL, ACC_SUPER
  foo(MyInterface):
    descriptor: (LMyInterface;)V
    flags: (0x0009) ACC_PUBLIC, ACC_STATIC
    Codes:
      ldc           #8  // foo
      getstatic     #14 // System.out
      swap
      invokevirtual #20 // PrintStream.println(Object)V
      return

class MyClass implements MyInterface
  flags: (0x0031) ACC_PUBLIC, ACC_FINAL, ACC_SUPER
  foo():
    descriptor: ()V
    flags: (0x0001) ACC_PUBLIC
    Code:
      aload_0
      invokestatic #18 // MyInterface$DefaultImpls.foo(MyInterface)V
      return
```
# 默认参数

Kotlin 支持参数的默认值，即默认参数或可选参数，通过一个掩码来判断是否使用默认值。

```kotlin
class User(val name: String, val sex: Int = 0, val age: Int = 18)

fun main() {
    User("ya")
    User("ya", 1)
}
```

```java title:'等价 Java 代码'
public final class User {
    private final String name;
    private final int sex;
    private final int age;
    public User(String name, int sex, int age) {
        checkNotNullParameter(name);
        super();
        this.name = name;
        this.sex = sex;
        this.age = age;
    }
    public User(String name, int sex, int age, 
                int mask, DefaultConstructorMarker marker) {
        if (mask & 2 != 0) sex = 0
        if (mask & 4 != 0) age = 18
        this(name, sex, age);
    }
}

public class MainKt {
    public static final main() { 
        new User("ya", 0, 0, 6, null);
        new User("ya", 1, 0, 4, null);0                                    h
    }
}
```

``` title:'class' fold
class User:
  flags: (0x0031) ACC_PUBLIC, ACC_FINAL, ACC_SUPER
  name: java.lang.String, (0x0012) ACC_PRIVATE, ACC_FINAL
  sex: I, (0x0012) ACC_PRIVATE, ACC_FINAL
  age: I, (0x0012) ACC_PRIVATE, ACC_FINAL
  User():
    descriptor: (String,I,I)V
    flags: (0x0001) ACC_PUBLIC
    Code:
      aload_1
      ldc #9 // name
      invokestatic #15 // checkNotNullParameter(String)V
      aload_0
      invokespecial #18 // Object.<init>()V
      aload_0
      aload_1
      putfield #21 // name: String
      aload_0
      iload_2
      putfield #25 // sex: I
      aload_0
      iload_3
      putfield #28 // age: I
      return
  User():
    descriptor: (String,I,I,I,DefaultConstructorMarker)V
    flags: (0x1001) ACC_PUBLIC, ACC_SYNTHETIC
    Code:
      iload 4
      iconst_2
      iand
      ifeq 9
      iconst_0
      istore_2
  9   iload 4
      iconst_4
      iand
      ifeq 19
      bipush 18
      istore_3
  19  aload_0
      aload_1
      iload_2
      iload_3
      invokespecial #33 // User.<init>(String;II)V
      return

  main():
    descriptor: ()V
    flags: (0x0019) ACC_PUBLIC, ACC_STATIC, ACC_FINAL
    Code:
      new #8 // class User
      dup
      ldc #10 // ya
      iconst_0
      iconst_0
      bipush 6
      aconst_null
      invokespecial #14 // User.<init>(String;III DCMaker;)V
      pop
      new #8 // class User
      dup
      ldc #10 // ya
      iconst_1
      iconst_0
      iconst_4
      aconst_null
      invokespecial #14 // User.<init>(String;III DCMaker;)V
      pop
      return
```
# 多返回值

从原理上看，JVM 不支持多返回值；从语义上看，Kotlin 通过将返回值包装到一个对象支持多返回值，本质是返回一个对象后解构赋值

```kotlin
data class Time (val hour: Int, val minute: Int, val second: Int)

fun getTime() = Time(18, 47, 29)

fun foo() {
    val (hour, minute, second) = getTime()
}
```

```java
public static final foo() {
    Time t = getTime();
    int hour   = t.component1();
    int minute = t.component2();
    int second = t.component3();
}
```

```
foo():
  descriptor: ()V
  flags: (0x0019) ACC_PUBLIC, ACC_STATIC, ACC_FINAL
  Code:
    invokestatic  #17                 // Method getTime:()LTime;
    astore_0
    aload_0
    invokevirtual #21                 // Method Time.component1:()I
    istore_1
    aload_0
    invokevirtual #24                 // Method Time.component2:()I
    istore_2
    aload_0
    invokevirtual #27                 // Method Time.component3:()I
    istore_3
    return
```
# 协程

Kotlin 协程（`coroutine`）通过魔改的 CPS 实现

> [!note] CPS 模式
> Continuation Passing Style，函数执行完后，不通过 return 返回，而是将返回值作为参数，调用下一个任务函数（Continuation）

CPS 方法都会有一个额外的 Continuation 变量，表示完成后要执行的代码，又称回调函数

```kotlin
fun sum(a: Int, b: Int, cont: (Int) -> Unit) = cont(a + b)
fun double(x: Int, cont: (Int) -> Unit) = cont(x * 2)
fun print(x: Int, cont: (Unit) -> Unit) = cont(print(x))

fun printDoubleSum(a: Int, b: Int) {
    sum(a, b) {
        double(it) {
            print(it) {}
        }
    }
}

fun main() {
    printDoubleSum(10, 20)
}
```

Kotlin 的 `suspend` 关键字实际上就是将一个函数转化为一个 CPS 函数，在每个函数后加一个 `Continuation<T>` 类型的参数：

```kotlin
data class User(val token: String)

suspend fun getTokenByLogin(username: String, password: String) : String = "$username:$password"

suspend fun getUserInfo(token: String) : User = User(token)

fun progressUserInfo(user: User) {}

suspend fun foo() {
    val token = getTokenByLogin("Zhang", "1234")
    val user = getUserInfo(token)
    progressUserInfo(user)
}
```

```kotlin
suspend fun foo() {
    getTokenByLogin("Zhang", "1234", object : Continuation<String> {
        override fun resumeWith(result: Result<String>) {
            val token = result.getOrNull()
            getUserInfo(token, object : Continuation<User> {
                override fun resumeWith(result: Result<User>) {
                    val user = result.getOrNull()
                    progressUserInfo(user)
                }
            })
        }
    })
    val user = getUserInfo(token)
    progressUserInfo(user)
}
```

Kotlin 中每个 `suspend` 都对应一个 `Continuation` 实现，`Continuation` 通过状态机实现，编译器将 `suspend` 方法替换为状态机的一部分，`suspend` 通过不同状态间传递 `Continuation` 实现协程切换

```kotlin title:'Result 接口' fold
interface Result<out T> {
    public val value: T
    public val isSuccess        : Boolean
    public val isFailure        : Boolean
    public fun exceptionOrNull(): Throwable?
    public fun getOrNull()      : T?
    public fun getOrThrow()     : T 
}
```

```kotlin title:'Continuation 接口' fold
interface Continuation<in T> {
    public val context: CoroutineContext
    public fun resumeWith(result: Result<T>)
}
```

1. 打标签

```kotlin
suspend fun foo() {
    val token = getTokenByLogin("Zhang", "1234") // 挂起点 1
    val a = 100
    val user = getUserInfo(token)                // 挂起点 2
    progressUserInfo(user)
    println(a)
}
```

2. 实现状态机，Kotlin 协程的原理就是每个挂起点和初始起点的 `Continuation` 都会转为状态机的一种状态，协程切换只是切换一个状态机的状态，使用 CPS 传递协程上下文。

```kotlin
fun foo(cont: Continuation<String>) {
    val sm = ContinuationImpl()  // sm.label 初始值为 1
    when (sm.label) {
        0 -> {
            sm.label = 1          // 更新状态
            getTokenByLogin("Zhang", "1234", sm)
        }
        1 -> {
            sm.label = 2          // 更新状态
            val a = 100
            sm.`$a` = a           // 保存状态
            val token = sm.result // 上一个状态的结果
            getUserInfo(token)
        }
        2 -> {
            val user = sm.result
            progressUserInfo(user)
            val a = sm.`$a`       // 恢复状态
            println(a)
            return
        }
    }
}
```
