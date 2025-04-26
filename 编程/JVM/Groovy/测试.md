Groovy 为代码测试提供了很多支持
# assert

Java 中，assert 可以通过 -ea 和 -da 开关。

Groovy 中，assert 也被称为 power assert statement，无法被关闭。当 assert 判断为 false 时，会输出详细的问题所在，及其值

```groovy
def x = 1
assert x == 2

// Output:
//
// Assertion failed:
// assert x == 2
//        | |
//        1 false
```

```groovy
def x = [1,2,3,4,5]
assert (x << 6) == [6,7,8,9,10]

// Output:
//
// Assertion failed:
// assert (x << 6) == [6,7,8,9,10]
//         | |     |
//         | |     false
//         | [1, 2, 3, 4, 5, 6]
//         [1, 2, 3, 4, 5, 6]
```

自定义错误输出

```groovy
assert expression1[boolean] : expression2[String]
```
# TranslationService

类似动态代理，可以创建具有特定行为的对象

- 使用 Map 控制，map 的 key-value 作为方法名-闭包代码代替对象的原方法

```groovy
class TranslationService {
    String convert(String key) {
        return "test"
    }
}

def service = [convert: { String key -> 'some text' }] as TranslationService
assert 'some text' == service.convert('key.text')
```

- 使用闭包控制

```groovy
def service = { String key -> 'some text' } as TranslationService
assert 'some text' == service.convert('key.text')
```

# 隐式 SAM 强制校验

```groovy
abstract class BaseService {
    abstract void doSomething()
}

BaseService service = { -> println 'doing something' }
service.doSomething()
```
# MockFor & StubFor

```groovy
class Person {
    String first, last
}

class Family {
    Person father, mother
    def nameOfMother() { "$mother.first $mother.last" }
}
```

MockFor：

```groovy
def mock = new MockFor(Person)
mock.demand.getName { 'dummy' }
mock.use {
    def person = new Person(name: 'Person')
    def f = new Family(mother: person)
    println f.mn() // dummy。这里 mock 替换了 person 的 getName
}
mock.expect.verify()
```

StubFor：

```groovy
def stub = new StubFor(Person)
stub.demand.with {
    getName { 'dummy' }
}
stub.use {
    def person = new Person(name: 'Person')
    def f = new Family(mother: person)
    println f.mn() // dummy
}
stub.expect.verify()
```
# ExpandoMetaClass

删除修改：GroovySystem.metaClassRegistry.setMetaClass(被修改类的 Class, null)

或注册 MetaClassRegistryChangeEventListener 跟踪
