自 Groovy 3 起支持 Java 风格的 lambda 表达式。对于动态Groovy，lambda表达式将转换为等效的Groovy闭包。

```groovy
{ 闭包参数 ->
    // statements
}
```

- 闭包参数以逗号分割，无参数时，`->` 符号可省略
- 闭包参数规则与常规方法参数相同
- 在使用时，当省略 it 时，闭包有一个隐式的参数 it

```groovy
Closure callback = { println 'Done!' }
Closure<Boolean> isTextFile = { File file ->
    file.name.endsWith('.txt')
}
callback()
isTextFile(new File(...))
```

- 任何一个闭包都是一个 Closure 类的实例，其泛型为返回值类型
- 闭包可以直接调用，或者调用其 `call()` 方法
# 委托策略

当直接调用方法时，闭包内方法查找顺序，这些常量定义于 Closure 类中
- OWNER_FIRST：默认，owner 优先
- DELEGATE_FIRST：delegate 优先
- OWNER_ONLY：仅 owner
- DELEGATE_ONLY：仅 delegate
- TO_SELF：仅 Closure 闭包本身，用于自定义解析策略和闭包类

```groovy
// 接 delegate 部分实例
def person = new Person(name: 'abc', age: 10)
def cl = {
    println "${name.toUpperCase()}: ${getAge()}"
}
cl.delegate = person
cl.resolveStrategy = Closure.DELEGATE_FIRST
// ABC: 10
// 此时，由于策略为 delegate 优先，因此获取的是 person 的 name
cl()
```
## this

等效于 `getThisObject` 方法，对应定义闭包的对象

```groovy
@ToString
class Person {
    String name
    def dump() {
        def cl = {
            // 闭包的 this 为创建该闭包的 Person 实例类
            def msg = this.toString()
            println msg
            msg
        }
        cl()
    }
}

class Main {
    static void main(String[] args) {
        // Person(Abc)
        new Person(name: 'Abc').dump()
    }
}
```
## owner

等效于 `getOwner()` 方法，类似 this，与其区别为 当该闭包来自另一个闭包时，this 返回该闭包的最外层创建者对象或类（即非闭包），而 owner 会返回闭包

```groovy
def cl = {
    def ownerAndThis = { [owner, this] }
    ownerAndThis()
}
def ownerAndThis = cl()
// Closure cl=Main$_main_closure1@525f1e4e
println "Closure cl=${cl.toString()}"
// owner=Main$_main_closure1@525f1e4e
// owner 是闭包的直接创造者
println "owner=${ownerAndThis[0].toString()}"
// this=class Main
// this 是闭包的实际（间接）创造者
// 这里是在 Main#main 静态方法中，所以返回一个类
println "this=${ownerAndThis[1].toString()}"
```
## delegate

等效于 `getDelegate()` 方法，对应第三方对象，默认为 owner，可手动修改，可直接调用（优先级在 this 之后）

```groovy
class Person {
    String name
    int age
}

class Main {

    static void main(String[] args) {
        def person = new Person(name: 'abc', age: 10)
        def cl = {
            println "${name.toUpperCase()}: ${getAge()}"
        }
        cl.delegate = person
        // MAIN: 10
        // owner 为 class，其中包含 name，因此这里获取的是 Main.class.name
        cl()
    }
}
```
# @DelegatesTo

编译时注解，可以定义闭包的类型、委托策略等，用于注解的静态检查
- 简单委托：只定义 value 属性值某个类

  ```groovy
  void body(@DelegatesTo(BodySpec) Closure cl) {
      // ...
  }
  ```

- 授权策略：同时指委托类 value 和委托策略 strategy

  ```groovy
  void body(@DelegatesTo(strategy=Closure.DELEGATE_ONLY, value=BodySpec) Closure cl) {
      // ...
  }
  ```

- 委托参数

  ```groovy
  def exec(@DelegatesTo.Target Object target, @DelegatesTo Closure code) {
     def clone = code.rehydrate(target, this, this)
     clone()
  }
  
  class Greeter {
     void sayHello() { println 'Hello' }
  }
  def greeter = new Greeter()
  exec(greeter) {
     sayHello()
  }
  ```

- 多重闭包

  ```groovy
  class Foo { void foo(String msg) { println "Foo ${msg}!" } }
  class Bar { void bar(int x) { println "Bar ${x}!" } }
  class Baz { void baz(Date d) { println "Baz ${d}!" } }
  
  void fooBarBaz(
      @DelegatesTo.Target('foo') foo,
      @DelegatesTo.Target('bar') bar,
      @DelegatesTo.Target('baz') baz,
  
      @DelegatesTo(target='foo') Closure cl1,
      @DelegatesTo(target='bar') Closure cl2,
      @DelegatesTo(target='baz') Closure cl3) {
      cl1.rehydrate(foo, this, this).call()
      cl2.rehydrate(bar, this, this).call()
      cl3.rehydrate(baz, this, this).call()
  }
  
  def a = new Foo()
  def b = new Bar()
  def c = new Baz()
  fooBarBaz(
      a, b, c,
      { foo('Hello') },
      { bar(123) },
      { baz(new Date()) }
  )
  ```

- 委托泛型

  ```groovy
  public <T> void configure(
      @DelegatesTo.Target List<T> elements,
      @DelegatesTo(strategy=Closure.DELEGATE_FIRST, genericTypeIndex=0) Closure configuration) {
     def clone = configuration.rehydrate(e, this, this)
     clone.resolveStrategy = Closure.DELEGATE_FIRST
     clone.call()
  }
  
  class Realm {
     String name
  }
  List<Realm> list = []
  3.times { list << new Realm() }
  configure(list) {
     name = 'My Realm'
  }
  ```

  ```groovy
  class Mapper<T,U> {
      final T value
      Mapper(T value) { this.value = value }
      U map(@DelegatesTo(type="T") Closure<U> producer) {  
          producer.delegate = value
          producer()
      }
  }
  ```
# 柯里化

Groovy 中，Currying 定义为部分程序。闭包中，代表预设其中的部分参数

```groovy
def nCopy = { int times, String str, String prefix -> "$prefix${str * times}" }
// 将最左侧参数设置为 2
def twoCopy = nCopy.curry(2)
// 将最右侧参数设置为 "*2: "
def twoCopyAndPrefix = twoCopy.rcurry("*2: ")
assert nCopy(2, "Hello", "*2: ") == twoCopyAndPrefix('Hello')
// *2: HelloHello
println twoCopyAndPrefix('Hello')
```
# 记忆化

memoize 系列方法可以将闭包计算的结果缓存。当函数为纯函数，且要进行比较慢的运算或需要经常做重复运算，可以使用此方法

```groovy
def fib
fib = { long n -> n<2 ? n: fib(n-1) + fib(n-2) }
// 不使用 memoize
long start
for (i in 0..<5) {
    start = System.nanoTime()
    fib(25)
    /*
    fib100 runs 299734500
    fib100 runs 49634600
    fib100 runs 17741800
    fib100 runs 20411000
    fib100 runs 29824700
     */
    println "fib100 runs ${System.nanoTime() - start}"
}
// 使用 memoize 可以看到时间明显缩短
fib = fib.memoize()
for (i in 0..<5) {
    start = System.nanoTime()
    /*
    fib100 runs 12568600
    fib100 runs 47900
    fib100 runs 41200
    fib100 runs 40200
    fib100 runs 34500
     */
    fib(25)
    println "fib100 runs ${System.nanoTime() - start}"
}
```

- memoize：缓存所有结果
- memoizeAtMost：最多缓存 n 个结果
- memoizeAtLeast：至少缓存 n 个结果
- memoizeBetween：最少缓存 n 个结果，最多缓存 m 个结果
# 递归优化

用于避免递归调用时的 StackOverflowException 异常，返回一个 TrampolineClosure，将递归调用转换为链式调用而非堆栈填充

```groovy
def factorial
factorial = { int n, def accu = 1G ->
    if (n < 2) return accu
    factorial.trampoline(n - 1, n * accu)
}
factorial = factorial.trampoline()

assert factorial(1)    == 1
assert factorial(3)    == 1 * 2 * 3
assert factorial(1000) // == 402387260.. plus another 2560 digits
```
# SAM 类型类型转换

任何闭包可通过 as 转换为 SAM 类型的类。自 Groovy 2.2.0 开始，as 可省略

除 SAM 类型外，闭包也可以转化为任何类型，尤其是接口

```groovy
interface FooBar {
    int foo()
    void bar()
}
def impl = { println 'ok'; 123 } as FooBar
assert impl.foo() == 123
impl.bar()
```
# 方法指针

使用 &. 从方法获取闭包
