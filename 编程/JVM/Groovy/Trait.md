特征 可以看做成带有成员空间的接口，允许存在成员变量，使用 trait 关键字定义，后使用 implements 关键字像普通接口一样使用。

- 允许使用 this 表示实现该特征的类实例
	- 使用 `@SelfType` 注解修饰 Trait，可以限定实现该特征 this 的类型

- 特征可以实现接口，使用 implements 声明，且可以通过 instanceof 检查
# 特征成员

- 特征的字段会存储到实现的类中，名称中包含全类名，`.` 使用 `_` 替代，且在最终成员名添加 `__`

```groovy
package demo.traits

trait MyTrait {
    String message
}

class Main implements MyTrait {
}
```

反编译后，可以看到（简化后）：

```java
public class Main implements MyTrait {
    private String demo_traits_MyTrait__message;
    
    public String demo_traits_MyTrait__message$get() {
        return this.demo_traits_MyTrait__message;
    }

    public String demo_traits_MyTrait__message$set(String val) {
        this.demo_traits_MyTrait__message = val;
        return val;
    }

    public String getMessage(); // 省略实现
    public void setMessage(String arg1); // 省略实现
}
```

- 方法相关限制：
	- 可以包含普通方法和抽象方法，抽象方法使用 abstract 关键字声明
	- 方法可以声明为 private，不支持 protected 或 package-private
	- 方法可以被 final 修饰，但这样的方法仍可以被重写

```groovy
trait Trait {
    final def fm() {
        println "in trait"
    }
}

class Class implements Trait {
    @Override
    def fm() {
        println "in class"
    }
}

// in class
new Class().fm()
```
## 静态成员

- 具有静态成员的 trait 不能通过静态编译的类型检查
- 静态成员不会出现在特征生成的接口中
- 静态成员不属于特征，而是属于每个实现类（类似属性）

  ```groovy
  trait TestHelper {
      public static boolean CALLED = false
      static void init() {
          CALLED = true
      }
  }
  class Foo implements TestHelper {}
  class Baz implements TestHelper {}
  Foo.init()
  assert Foo.TestHelper__CALLED
  assert !Baz.TestHelper__CALLED
  ```
## 状态陷阱

注意访问的是 trait 的属性还是对象属性，使用正确的 getter/setter 方法

```groovy
trait CountPair {
    int x = 1
    int y = 2
    int sum() { x + y }
}

class CountImpl implements CountPair {
    int x = 3
    int y = 4
}

class Main {
    static void main(String[] args) {
        // 3 因为 x 和 y 都掉用的是 trait 的 x 和 y
        println new CountImpl().sum()
    }
}
```

```groovy
trait CountPair {
    int x = 1
    int y = 2
    int sum() { x + y }
}

class CountImpl implements CountPair {
    int x = 3
    int y = 4

    @Override
    int sum() {
        return x + y
    }
}

class Main {
    static void main(String[] args) {
        // 7 此时，重写后的 sum 方法调用的是实现类的 x 和 y
        println new CountImpl().sum()
    }
}
```

```groovy
trait CountPair {
    int x = 1
    int y = 2
    int sum() { getX() + getY() }
}

class CountImpl implements CountPair {
    int x = 3
    int y = 4
}

class Main {
    static void main(String[] args) {
        // 7 因为 getX 和 getY 获取的是具体实现类的对应属性
        println new CountImpl().sum()
    }
}
```
# 多继承

trait 允许多继承。当多个 trait 都实现了同一个方法或属性，默认使用最后一个特征的方法，使用类似 java8 接口的方法（`Type.super.func()`）调用其它特征的方法

当存在多继承时，super 有意义，指代实现了该特征的实例中，实现了同一个接口（该方法所对应接口）的**其他** trait；若没有实现其他 trait，则代表 this

```groovy
interface ILogger {
    void printMessage(String message)
}

trait DefaultLogger implements ILogger {
    @Override
    void printMessage(String message) {
        println 'Default Logger'
        println message
    }
}

trait ExtraLogger implements ILogger {
    @Override
    void printMessage(String message) {
        println 'Extra Logger'
        super.printMessage(message)
    }
}

class Main implements DefaultLogger, ExtraLogger {
    static void main(String[] args) {
        // Extra Logger
        // Default Logger
        // Message
        new Main().printMessage('Message')
    }
}
```

注意调用优先级

```groovy
// 由于 DefaultLogger 在 ExtraLogger 之后，因此不会执行 ExtraLogger 的代码
class Main implements ExtraLogger, DefaultLogger {
    static void main(String[] args) {
        // Default Logger
        // Message
        new Main().printMessage('Message')
    }
}
```
# SAM 类型

若特征定义了一个抽象方法，则该方法为 SAM（单一抽象方法）的候选者

```groovy
trait Greeter {
    String greet() { "Hello $name" }
    abstract String getName()
}

// 允许。此时 闭包实现 getName 方法，该方法为该对象的单一抽象方法
Greeter greeter = { 'Alice' }
void greet(Greeter g) { println g.greet() }
// { 'Alice' } 为 Greeter
greet { 'Alice' }
```
# 与 mixin 的区别

- Trait 在内部实现为一个接口+几个辅助类
- Trait 对 Java 可见
- Trait 可以符合类型检查和静态编译

```groovy
class A { String methodFromA() { 'A' } }
class B { String methodFromB() { 'B' } }
A.metaClass.mixin B
def o = new A()
assert o.methodFromA() == 'A'
assert o.methodFromB() == 'B'
assert o instanceof A
assert !(o instanceof B)
```
### 局限性

- AST 转换未完全兼容：@CompileStatic 会应用于特征而不是类，以及其他一些未知问题
- 特征内 ++ 和 -- 将不被允许，应当替换为 +=1 和 -=1
