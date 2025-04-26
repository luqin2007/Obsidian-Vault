# 运行时

Groovy 在运行时，对 POJO，POGO，Groovy 拦截器有特殊处理
- POJO：Java 对象，通常由 Java 或其他 JVM 语言编写
- POGO：Groovy 对象，由 Groovy 创建，实现 [groovy.lang.GroovyObject](https://docs.groovy-lang.org/2.5.9/html/gapi/index.html?groovy/lang/GroovyObject.html) 接口，在进行方法调用时，会进行更多的判断
- Groovy 拦截器：Groovy 对象，实现 [groovy.lang.GroovyInterceptable](https://docs.groovy-lang.org/2.5.9/html/gapi/index.html?groovy/lang/GroovyInterceptable.html) 接口，具有方法拦截功能

查找和执行一个 Groovy 变量或方法的一般流程如下：
![Groovy拦截](GroovyInterceptions.png)

对于 POJO，Groovy 会从 [groovy.lang.MetaClassRegistry](https://docs.groovy-lang.org/2.5.9/html/gapi/index.html?groovy/lang/MetaClassRegistry.html) 中获取 MetaClass

- GroovyObject：负责将方法调用转移到 MetaClass 上
- invokeMethod：用于GroovyInterruptable 接口或 MetaClass 拦截方法时使用

> [!warning]
> 不建议重写该方法。若要拦截未实现方法，推荐重写 methodMissing 方法

- get/setProperty：用于对方法的成员变量的 getter/setter 操作拦截，覆盖 getXxx/setXxx 方法
- get/setMetaClass：对 MetaClass 的访问，用以更改默认拦截机制
- get/setAttribute：该方法行为与 MetaClass 的具体实现有关。默认的实现为可以跳过 getter/setter 访问属性
- methodMissing：当找不到方法时，调用该方法，可在该方法中对这种情况特殊处理。
	- `$static_methodMissing`：静态成员，对应静态方法缺失

> [!tip]
> 触发该方法时，向 MetaClass（ExpandoMetaClass） 中注册一下方法，下次调用该方法将不会产生额外开销

- propertyMissing：当找不到属性时，调用该方法
	- `$static_propertyMissing`：静态成员，对应静态属性缺失
- GroovyInterceptable：若实现该接口，所有的方法将被 `invokeMethod` 方法拦截而不使用

> [!tip]
> 若要拦截所有方法，而不能实现 GroovyInterceptable 接口（如 POJO），可以重写 MetaClass.invokeMethod 方法

```groovy
class InterceptionThroughMetaClassTest extends GroovyTestCase {

    void testPOJOMetaClassInterception() {
        String invoking = 'ha'
        invoking.metaClass.invokeMethod = { String name, Object args ->
            'invoked'
        }

        assert invoking.length() == 'invoked'
        assert invoking.someMethod() == 'invoked'
    }

    void testPOGOMetaClassInterception() {
        Entity entity = new Entity('Hello')
        entity.metaClass.invokeMethod = { String name, Object args ->
            'invoked'
        }

        assert entity.build(new Object()) == 'invoked'
        assert entity.someMethod() == 'invoked'
    }
}
```
## Categories

Category 类别类，向指定上下文中注入
- [groovy.time.TimeCategory](https://docs.groovy-lang.org/2.5.9/html/gapi/index.html?groovy/time/TimeCategory.html)
- [groovy.servlet.ServletCategory](https://docs.groovy-lang.org/2.5.9/html/gapi/index.html?groovy/servlet/ServletCategory.html)
- [groovy.xml.dom.DOMCategory](https://docs.groovy-lang.org/2.5.9/html/gapi/index.html?groovy/xml/dom/DOMCategory.html)

使用时，需要使用 use 定义范围

```groovy
use(TimeCategory)  {
    // minute, hours 属性为 TimeCategory 添加
    println 1.minute.from.now
    println 10.hours.ago

    def someDate = new Date()       
    println someDate - 3.months
}
```

创建时，应当使用静态方法，第一个参数必须是被修饰类型对象

```groovy
class NumberCategory {

    static String getMeters(Number self) {
        return self + " m"
    }
}

use(NumberCategory) {
    println 1.meters // 1 m
}
```

也可以使用 Category 注解

```groovy
@Category(Number) // 向 Number 添加 getMeters，this 就是 Number
class NumberCategory {

    String getMeters() {
        return this + " m"
    }
}

use(NumberCategory) {
    println 1.meters // 1 m
}
```
## MetaClass

MetaClass 元类是 Groovy 方法解析的核心，默认为 MetaClassImpl，未找到方法使用 methodMissing，最终调用 invokeMethod
### 自定义

可将一个对象的 metaClass 设置为任何实现了 MetaClass 接口的对象，通常继承自 `MetaClassImpl`，`DelegatingMetaClass`，`ExpandoMetaClass` 或 `ProxyMetaClass`

**使用前，应当调用 MetaClass#initialize 方法**

- DelegatingMetaClass：用于修饰当前类的 MetaClass
- ExpandoMetaClass：可使用闭包方便的修改方法、变量、构造、静态方法等，使用 << 或 = 运算符添加
	- 接口方法：可将方法添加到接口上

```groovy
// 向某一对象添加方法
def book = new Book(title: "Hello Groovy")
book.metaClass.titleInUpperCase << { -> title.toUpperCase() }
// 向某一类添加方法
Book.metaClass.titleInUpperCase2 = { -> title.toUpperCase() }

// 向某一类添加属性
// 方法一 直接向 metaClass 添加
Book.metaClass.author = 'Unknown'
// 方法二 向 metaClass 添加 getter/setter
def authors = [:]
Book.metaClass.getAuthor2 = {-> authors[delegate]}
// 若只读，省略 setter
Book.metaClass.setAuthor2 = {author -> authors[delegate] = author}

// 向某一类添加构造
Book.metaClass.constructor << { String title -> new Book(title: title) }
Book.metaClass.constructor << { String title, int ver -> new Book(title: title + " Ver. " + ver) }

// 向某一类添加静态方法
Book.metaClass.static.create << { title -> new Book(title: title) }
Book.metaClass.static.show << { Book book -> println "Book: ${book.title}" }
```

```groovy
// 动态方法：使用字符串作为方法名
def methodName = "Hello"
// 自定义动态方法
Book.metaClass."changeTitleTo${methodName}" = {-> delegate.title = methodName}

Book book = new Book(title: 'Groovy')
book.changeTitleToHello()
println book.title // Hello
```

- ProxyMetaClass
### 自动更改

若存在 `groovy.runtime.metaclass.[对应要替换的类的完整包名].[要替换的类的类名]MetaClass` 类，Groovy 会将对应类的 MetaClass 替换为该 MetaClass

可用于替换 POJO 的 metaClass（测试时貌似没用）
### 扩展模块

可以将一些方法扩展到现有类型上。不管是实例方法还是静态方法，都需要在 META-INF 中标记
- `META-INF/org.codehaus.groovy.runtime.ExtensionModule`

```
moduleName=[模块名]
moduleVersion=[模块版本，用于检查是否在两个不同版本中加载同一模块]
extensionClasses=[扩展实例方法所在类，以逗号分隔]
staticExtensionClasses=[扩展静态方法所在类，以逗号分隔]
```
#### 实例方法

- 方法必须是静态方法
- 方法第一个参数是被调用对象，一般是 self

  ```groovy
  // file: Book.groovy
  class Book {
      String title
  }
  
  // file: BookExtension.groovy
  class BookExtension {
      static void displayName(Book self) {
          println self.title
      }
      static void useBook(Book self, Closure closure) {
          closure.call(self)
      }
  }
  
  static void main(String[] args) {
      Book book = new Book(title: 'Groovy')
      book.displayName() // Groovy
      book.useBook {
          println it.title // Groovy
      }
  }
  ```

#### 静态方法

- 方法必须是静态方法
- 方法不存在被扩展类中
- 方法第一个参数是被扩展类的对象，但未使用，仅用于判断

  ```groovy
  // file: BookExtensionStatic
  class BookExtensionStatic {
  
      static void display(Book self) {
          println self
          println 'Book'
      }
  }
  
  Book.display() // null Book
  ```
# 编译时

编译时元编程，又称 AST 转换，允许在编译时生成代码，修改字节码文件或生成对应字节码，类似 Java 的编译时注解+注解处理器
1. 对其他 JVM 平台语言，如 Java 等可见。运行时元编程不对其他 JVM 语言可见
2. 可将对应方法转换为类协定（如接口，抽象类等）的一部分
3. 性能更好，与运行时元编程相比不需要初始化阶段
## 函数实现

- @ToString：自动重写 toString 方法

```groovy
@ToString
class Book {
    String title
    int version
}

// Book(Groovy, 1)
println new Book(title: 'Groovy', version: 1)
```

| 属性                   | 默认值 | 作用                                                |
|:---------------------- |:------ |:--------------------------------------------------- |
| excludes               | []     | 排除的属性值                                        |
| includes               | []     | 包含的属性值。默认包含全部                          |
| includeSuper           | false  | 是否调用父类 toString 方法                          |
| includeNames           | false  | 是否包含属性名                                      |
| includeFields          | falses | 是否包含不可见属性名，如 private                    |
| includeSuperProperties | false  | 是否包含父类属性                                    |
| includeSuperFields     | false  | Should visible super fields be included in toString |
| ignoreNulls            | false  | 是否忽略 null                                       |
| includePackage         | true   | 是否使用带包名的全类名而非简写                      |
| allProperties          | true   | 是否包含所有属性。该值为 false 时仅包含 JavaBean    |
| cache                  | false  | 是否缓存 toString 结果                              |
| allNames               | false  | 是否包含带有内部名称的属性，即以 $ 开头的属性       | 

- EqualsAndHashCode：自动重写 equals 及 hashCode 方法

| Attribute     | 默认值 | 作用                   |
| :------------ | :----- | :--------------------- |
| excludes      | []     | 同上                   |
| includes      | []     | 同上                   |
| cache         | false  | 同上                   |
| callSuper     | false  | 同上                   |
| includeFields | false  | 同上                   |
| useCanEqual   | true   | 是否生成 canEqual 方法 |
| allProperties | false  | 同上                   |
| allNames      | false  | 同上                   |

- Canonical：组合 `@ToString`，`EqualsAndHashCode`，`@TupleConstructor`

- AutoImplement：为未实现的方法提供默认实现。若标有可抛出异常，则会抛出对应异常，否则返回默认值
## 构造函数

-  TupleConstructor：自动创建一个包含一个元组的构造函数

```groovy
@TupleConstructor
class Book {
    String title
    int version
    String alwaysNull = null

    private String name
}

// 生成的构造函数
@Generated
public Book(String title, int version, String alwaysNull) { ... }
@Generated
public Book(String title, int version) { ... }
@Generated
public Book(String title) { ... }
@Generated
public Book() {
    CallSite[] var1 = $getCallSiteArray();
    this((String)null, Integer.valueOf(0), (String)null);
}
```

若同时存在 @TupleConstructor 及 @PropertyOptions 注解，代表构造函数中可能存在自定义的属性处理逻辑

| 属性                   | 默认值 | 作用                                                         |
| :--------------------- | :----- | :----------------------------------------------------------- |
| excludes               | []     | 同上                                                         |
| includes               | []     | 同上                                                         |
| includeProperties      | true   | 同上                                                         |
| includeFields          | false  | 同上                                                         |
| includeSuperProperties | true   | 同上                                                         |
| includeSuperFields     | false  | 同上                                                         |
| callSuper              | false  | 同上                                                         |
| force                  | false  | 默认当存在一个对应的构造函数时，AST不会做任何事情。该属性为 true 则仍会创建对应构造函数 |
| defaults               | true   | 当该属性为 false 时，仅创建一个包含所有属性的构造函数        |
| useSetters             | false  | 默认AST会为所有的属性直接赋值。若该值为 true，则会调用其 setter 方法 |
| allNames               | false  | 同上                                                         |
| allProperties          | false  | 同上                                                         |
| pre                    | null   | 构造函数开始时调用的闭包                                     |
| post                   | null   | 构造函数结束时调用的闭包                                     |

`force=true，defaults=false` 时，可以通过多个该注解根据需求创建不同的构造函数

- MapConstructor：创建一个使用 Map 的构造函数

```groovy
@MapConstructor
class Book {
    String title
    int version
}

// 生成结果
@Generated
public Book(Map args) { ... }
```

- InheritConstructors：创建与父类对应的构造函数

| 属性                   | 默认值 | 作用                       |
| :--------------------- | :----- | :------------------------- |
| constructorAnnotations | false  | 是否继承构造函数的注解     |
| parameterAnnotations   | false  | 是否继承构造函数参数的注解 |

- Newify：用于生成其他语法样式的构造函数

```java
// python
@Newify([Tree,Leaf])
class TreeBuilder {
    Tree tree = Tree(Leaf('A'),Leaf('B'),Tree(Leaf('C')))
}
// ruby
@Newify([Tree,Leaf])
class TreeBuilder {
    Tree tree = Tree.new(Leaf.new('A'),Leaf.new('B'),Tree.new(Leaf.new('C')))
}
```

- Lazy：延迟初始值的初始化

```Java
// 生成类似这样的代码
List $myField
List getMyField() {
    if ($myField!=null) { return $myField }
    else {
        $myField = { ['a','b','c']}()
        return $myField
    }
}
```
## 类扩展

- Sortable：可排序
	- 实现 Comparable 接口
	- 实现 compareTo 方法
	- 创建 comparatorByXxx 方法创建 Comparator 对象

-  Builder：根据 builderStrategy 创建 builder
	- SimpleStrategy：默认生成 setXxx 方法，方法返回对象本身
	- ExternalStrategy：注解到一个空类上（XxxBuilder 等），使用 forClass 指定生成的类
	- DefaultStrategy：默认值。通过一个方法创建 builder 对象
	- InitializerStrategy：使用构造函数创建

若对一个类使用多个 Builder 注解，应保证 builderClassName 和 builderMethodName 的唯一性

```groovy
@Canonical
class Person {
    String first
    String last
    int born
}

@Builder(builderStrategy=ExternalStrategy, forClass=Person, includes=['first', 'last'], buildMethodName='create', prefix='with')
class PersonBuilder { }

def p = new PersonBuilder().withFirst('Johnny').withLast('Depp').create()
assert "$p.first $p.last" == 'Johnny Depp'
```

```groovy
@Builder
class Person {
    String firstName
    String lastName
    int age
}

def person = Person.builder().firstName("Robert").lastName("Lewandowski").age(21).build()
assert person.firstName == "Robert"
assert person.lastName == "Lewandowski"
assert person.age == 21
```

```groovy
@ToString
@Builder(builderStrategy=InitializerStrategy)
class Person {
    String firstName
    String lastName
    int age
}

assert new Person(Person.createInitializer().firstName("John").lastName("Smith").age(21)).toString() == 'Person(John, Smith, 21)'
```

- IndexedProperty：为 list/array 添加带有 index 的 getter/setter 方法，用于与其他 JVM 语言的 GPath 兼容
## 类设计

- BaseScript：用于脚本中使用，脚本应继承自自定义脚本基类而非Scripe类，用于 DSL
- Delegate：简化委托模式

```groovy
class Event {
    @Delegate Date when
    String title
}
```

会将 Data 中的方法添加到 Event 类中，并委托给 when 对象调用，结果为

```groovy
class Event {
    Date when
    String title
    boolean before(Date other) {
        when.before(other)
    }
    // 包括其他所有 when 的成员...
}
```

该注解可用于方法中，视为委托对象的 factory 方法

- ImmutableBase：只读类标记，将类为 final
- Immutable：简化只读类创建，相当于同时使用
	- @ToString
	- @EqualsAndHashCode
	- @TupleConstructor
	- @MapConstructor
	- @ImmutableBase
	- @ImmutableOptions
	- @PropertyOptions
	- @KnownImmutable
- PropertyOptions：允许在类创建时插入自定义的属性处理程序
	- 用于 @TupleConstructor，@MapConstructor，@ImmutableBase
	- 通常被 @Immutable 调用
- VisibilityOptions：允许定义转换器生成的样板代码的可见性
- ImmutableOptions：将目标类中某些属性标志为只读属性

| 属性                  | 默认值 | 作用           |
| :-------------------- | :----- | :------------- |
| knownImmutableClasses | []     | 对应类型为只读 |
| knownImmutables       | []     | 对应属性为只读 |

- KnownImmutable：相当于 ImmutableOptions 的 knownImmutableClasses 属性
- Singleton：创建单例模式
## 函数设计
- Memoized：标记纯函数，缓存方法结果，当输入的参数相同时，直接返回缓存的结果，不需要再次计算

```groovy
@Memoized
long longComputation(int seed) {
    // slow computation
    Thread.sleep(100*seed)
    System.nanoTime()
}

def x = longComputation(1) // returns after 100 milliseconds
def y = longComputation(1) // returns immediatly
def z = longComputation(2) // returns after 200 milliseconds
assert x==y
assert x!=z
```

- TailRecursive：将尾递归转换为对应循环版本，且返回值不能是 void，用于防止递归栈溢出
## 日志集成

为对应类添加相关变量，适配某些日志框架
- Log：变量：`java.util.logging,Loggger`，框架：JDK
- Commons：变量：`org.apache.commons.logging.Log`，框架：Apache Common Logging
- Log4j：变量：`org.apache.log4j.Logger`，框架：Apache Log4j 1.x
- Log4j2：变量：`org.apache.logging.log4j.Logger`，框架：Apache Log4j 2.x
- Slf4j：变量：`org.slf4j.Logger`，框架：Simple Logging Facade for Java (SLF4J)
## 声明式开发

- Synchronized：添加 synchronized 关键字
- WithReadLock WithWriteLock：添加 ReentrantReadWriteLock 同步锁
- AutoClone：自动生成 clone() 方法
- AutoExternalize：自动生成 readExternal/writeExternal 序列化/反序列化方法
## 脚本安全

- ThreadInterrupt：向代码的循环体、方法、闭包开头添加判断 Thread.interrupted，保证线程可以正常退出
- TimedInterrupt：当方法执行超时后触发异常，对静态方法不兼容
- ConditionalInterrupt：接受一个闭包，在循环时自动判断是否符合条件，触发 InterruptedException，防止 while(true) 无限循环
## 编译时行为
### Field

```groovy
// Script.groovy

def x

String line() {
    '=' * x
}

x = 3
assert '===' == line()
x = 5
assert '=====' == line()
```

以上代码作为脚本执行时，会抛出 MissingPropertyException 异常。这是由于所有脚本代码被编译成一个 run() 方法，x 的作用域为 run 方法体，而 line() 方法则被编译成一个单独的方法，即类似以下代码

```groovy
public class Script extends groovy.lang.Script {
    
    // ...
    
    public Object run() {
        // ...
        Object x = null
        // ...
        x = 3
        assert '===' == line()
        // ...
    }
    
   
    public String line() {
        '=' * x
    }
}
```

因此，line 方法无法读取到 x 变量值。使用该注解可以将 x 转化为类的成员变量

```groovy
import groovy.transform.Field

@Field def x

String line() {
    '=' * x
}

x = 3
assert '===' == line()
x = 5
assert '=====' == line()
```

此时，可以视为

```groovy
public class Script extends groovy.lang.Script {
    Object x;
    
    // ...
    
    public Object run() {...}
    
    public String line() {...}
}
```

脚本可以正常运行
## AnnotationCollector

创建元注解
## 编译相关

- TypeChecked：激活编译时类型检查
- CompileStatic：激活静态编译
- CompileDynamic：禁用部分静态编译特性
- DelegatesTo：用于辅助编译器类型检查或静态编译
## Swing patterns

- Bindable：根据 JavaBeans 规范，将属性转换为绑定属性，增加 PropertyChangeListener 的一系列方法
- ListenerList：用于 List，添加 ActionListener 一系列方法
- Vetoable：添加一系列 VetoableChangeListener 相关方法
## 测试

- NotYetImplemented：反转 JUnit 3/4 的测试结果
- ASTTest：在编译时某阶段测试，若接收的闭包断言失败，则无法通过编译
## Grape

依赖管理机制：Grab，GrabConfig，GrabExclude，GrabResolver，Grapes
## 自定义 AST 转换
### 编译阶段

- Initialization: 读取源文件，初始化环境
- Parsing: 根据源代码生成 token 树，用于生成语法树
- Conversion: 从 token 树产生抽象语法树（AST）
- Semantic Analysis: 执行语法无法检查的一致性检查和有效性检查，解析类
- Canonicalization: 完成 AST 构建
- Instruction Selection: 选择指令集，如 Java6 或 Java7 的字节码级别
- Class Generation: 在内存中创建类的字节码
- Output: 将类字节码写入文件
- Finalization: 清理，结束
### 本地转换

对于注解的上下文进行转换

1. 定义一个注解

```groovy
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
@GroovyASTTransformationClass("demo.WithLoggingTransform")
@interface WithLogging {}
```

2. 实现 ASTTransformation

```groovy
// demo.WithLoggingTransform.groovy
@CompileStatic // 静态编译，提高性能
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS) // 语义分析阶段
class WithLoggingTransform implements ASTTransformation {
    @Override
    void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        // 这里获取的下标为 1，下标 0 为注解节点
        def method = astNodes[1] as MethodNode
        def start = createPrint("Start $method.name")
        def end = createPrint("End $method.name")
        def statements = (method.code as BlockStatement).statements
        statements.add(0, start)
        statements.add(end)
    }
    private Statement createPrint(String message) {
        new ExpressionStatement(new MethodCallExpression(
                new VariableExpression("this"),
                new ConstantExpression("println"),
                new ArgumentListExpression(new ConstantExpression(message))
        )) // this.println(message)
    }
}
```

3. 使用

```groovy
// Start test
// Hello!!
// End test

@WithLogging
static void test() {
    println "Hello!!"
}
```
### 全局转换

应用于全局的注解，不需要显式声明，编译器会自动添加到所有可以添加的地方

- 在 META-INF 创建 org.codehaus.groovy.transform.ASTTransformation 文件，写入对应 ASTTransformation 类

  ```
  demo.GlobalASTTransform
  ```

- 创建 ASTTransformation 类

```groovy
// demo.GlobalASTTransform
@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class GlobalASTTransform implements ASTTransformation {
    @Override
    void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        sourceUnit.AST.methods.forEach({ MethodNode it ->
            def start = createPrint("Start $it.name")
            def end = createPrint("End $it.name")
            def statements = (it.code as BlockStatement).statements
            statements.add(0, start)
            statements.add(end)
        })
    }
    private Statement createPrint(String message) {
        new ExpressionStatement(new MethodCallExpression(
                new VariableExpression('this'),
                new ConstantExpression('println'),
                new ArgumentListExpression(new ConstantExpression(message))
        ))
    }
}
```
### AST API

使用 ClassCodeExpressionTransformer 辅助类 ClassHelper，GeneralUtils 等修改
### 宏

使用 macro 简化节点编程，使用 macro 创建对应 Statement 代码，使用 MacroClass 辅助创建类

```gtoovy
ClassNode classNode = astNodes[1] as ClassNode
ReturnStatement statement = macro {
    return 42
}
MethodNode methodNode = new MethodNode('getMessage', ClassNode.ACC_PUBLIC, ClassHelper.make(String), [] as Parameter[], [] as ClassNode[], statement)
classNode.addMethod(methodNode)
```

宏方法：第一个变量为 MacroContext 的静态方法，以 @Macro 注解，并在 META-INF/groovy/org.codehaus.groovy.runtime.ExtensionModule 中定义
