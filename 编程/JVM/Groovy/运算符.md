# 算术运算符

大部分运算符与 Java 相同，新增 `**` 和 `**=` 用于乘方
# Elvis 运算符

三元运算符的简写，`a ?: b` 等效于 `a ? a : b`
# 对象运算符
## 安全导航符

`?.`：若对象非空，则执行后面的运算，否则返回 null

```groovy
def a
// ...
def str = a?.toString()
// 等同于
def a
// ...
if (a != null) {
    str = a.toString()
} else {
    str = null
}
```
## 直接访问符

`@.`：跳过 getter 方法直接访问属性本身

```groovy
class User {
    String name
    
    String getName() {
        "Name: $name"
    }
}

def user = new User(name: 'a')

assert user.name == "Name: a"
assert user@.name == 'a'
```
## 方法指针符

`.&`：获取一个方法的指针，该方法只与方法名绑定，调用时可调用其重载方法，即调用的方法是在运行时确定的

```groovy
def str = 'example of method reference'
// 获取方法指针
def fun = str.&toUpperCase
def upper = fun()
assert upper == str.toUpperCase()
// 重载
def doSomething(String str) { str.toUpperCase() }
def doSomething(Integer x) { 2*x }
def reference = this.&doSomething
assert reference('foo') == 'FOO'
assert reference(123)   == 246
```
# 正则运算符

- 使用 ~ 创建一个 java.util.regex.Pattern 对象

```groovy
def p = ~/foo/
assert p instanceof Pattern
```

- 使用 =~ 获取 Matcher 对象

```groovy
def text = "some text to match"
def m = text =~ /match/          
assert m instanceof Matcher
if (!m) {
    throw new RuntimeException("Oops, text not found!")
}
```

- 使用 `==~` 获取 boolean 类型匹配结果

```groovy
m = text ==~ /match/
assert m instanceof Boolean
if (m) {
    throw new RuntimeException("Should not reach that point!")
}
```
# Spread 运算符

`*`，用于对 Iterable 中的每一项进行展开
## 传播调用

执行调用操作，并将结果重新收集到 List，也可以使用 `collectNested` 方法，类似于 `Stream.map.toList()`

该运算符是空安全的，即容器中的元素为 null 时，结果为 null，不会触发 NPE

```groovy
class Car {def make}
def cars = [new Car(make: 'a'), ...]
// 获取
def makes = cars*.make
assert makes == ['a', ...]
```
## 传播参数

将容器的每个元素作为方法参数，可与普通参数混合使用

```groovy
int func(int x, int y, int z) { x * y + z }
def args = [1, 2, 3]
assert func(*args) == func(1, 2, 3)
// 混合调用
def args2 = [1, 2]
assert func(*arg2, 3) == func(1, 2, 3)
```
## 传播元素

用于 list 或 map 构建时，可以插入已存在的 list 或 map 的元素

```groovy
def list = [1, 2, 3]
assert [4, *list, 5] == [4, 1, 2, 3, 5]

def map = [a: 1, b: 2]
assert [c: 3, *map, d: 4] == [c: 3, a: 1, b: 2, d: 4]
```
# Range 运算符

使用 `..` 获取一个 `groovy.lang.Range` 范围对象，该对象实现 `List` 接口，支持从所有实现 Comparable 接口并具有 next 和 previous 方法的对象创建，常见的有：
- 长度为 1 的 String
- int 等整数

```groovy
def range = 0..5
assert (0..5).collect() == [0, 1, 2, 3, 4, 5]
assert (0..<5).collect() == [0, 1, 2, 3, 4]
assert (0..5) instanceof List
assert (0..5).size() == 6
```
# Spaceship 运算符

`a <==> b` 等同于 `a.compareTo(b)`
# Subscript运算符

`[]` 运算符等同于 getAt 和 putAt 方法，也可用于切片

```groovy
def list = [0, 1, 2]
list[0] == list.getAt(0)
list[0] = 1 // list.putAt(0, 1)
list[0..1] == [0, 1]
```
## 多重赋值

多重赋值主要依赖于 getAt 方法（即 `[]` 运算符），当给定变量对应位置不存在时赋值为 null

```groovy
// i=1, j=2, k=3
def (i, j, k) = [1, 2, 3]
// i=1, j=2, k=null
def (i, j, k) = [1, 2]
// i=1, j=2, k=3
def (i, j, k) = [1, 2, 3, 4]
// i=1, j=2，依赖于 getAt 方法
class Values {
    int a = 1
    int b = 2
    int getAt(int idx) {
        if (idx == 0) a
        else if (idx == 1) b
        else throw new Exception("Wrong coordinate index, use 0 or 1")
    }
}
def (i, j) = new Values()
```
# Membership运算符

`in` 相当于调用 `isCase` 方法，在 List 中也等效于 `contains` 方法

```groovy
def list = [0, 1, 2]
assert 0 in list
```
# Indentity 运算符

groovy 中，`==` 对应 java 的 equals 方法，is 等同于 java 的 `==` 运算符
# Coercion运算符

`as` 运算符强调从一种类型转换成另一种类型，且这两种类型并不相同，为强制转换的一个变体。当强转失败时，调用 `asType` 方法进行转换

```groovy
class Identifiable { String name }
class User {
    Long id
    String name
    def asType(Class Target) {
        if (target == Identifiable) {
            return new Identifiable(name: name)
        }
        throw new ClassCastException("User cannot be coerced into $target")
    }
}

def u = new User(name: 'a')
assert !(p instanceof Identifiable)
assert (u as Identifiable) instanceof Identifiable
```
#  Diamond 运算符

`<>` 用于兼容 Java 的 `<>`。当仅使用 Groovy 时是不必要的，因为静态类型检查时 Groovy 类型检查器会自动进行类型推断
# Call 运算符

`()` 用于调用 call 方法（可以有参数，不一定需要实现 Callable 接口，有点类似于 C++ 的仿函数）
# 优先级

- 创建对象（new）优先级最高
- 方法调用，闭包等其次
- 成员访问、安全引用、Spread 第三
- 正则运算符在逻辑运算符之后
- 其他算术、逻辑、赋值运算符按一般优先级排列，表格省略
# 运算符重载

运算符被映射到了不同方法，通过重载指定方法实现运算符重载，相关方法可查文档
