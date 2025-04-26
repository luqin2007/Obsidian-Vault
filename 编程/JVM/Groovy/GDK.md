```cardlink
url: https://groovy-lang.org/documentation.html
title: "The Apache Groovy programming language - Documentation"
host: groovy-lang.org
favicon: img/xfavicon.ico.pagespeed.ic.5MuYua7ZOt.png
```

```cardlink
url: https://groovy-lang.org/groovy-dev-kit.html
title: "The Apache Groovy programming language - The Groovy Development Kit"
host: groovy-lang.org
favicon: img/xfavicon.ico.pagespeed.ic.5MuYua7ZOt.png
```

使用 `with` 和 `tap` 方法暂时替换 `this`
## IO
### 读
- eachXxx 方法，如 `eachLine`
- withXxx 方法，如 `withReader`
- newXxx 方法，如 `newInputStream`
- collect 方法，接受一个闭包，转换为 List
- 直接 `as String[]` 返回每一行的数组
- bytes 变量 直接返回 `byte[]`
### 写
- `write` 方法
- withXxx 方法，如 `withWriter`，`withOutputStream`
- newXxx 方法，如 `newOutputStream`
- 直接给 bytes 变量赋值 `byte[]`
- 可使用 `<<` 将内容写入流
### 目录树
- 使用 `traverse` 方法遍历目录
- 使用 `eachFileMatch` 方法条件遍历目录
- 使用 `eachFileRecurse` 方法递归遍历
### 序列化对象

使用 `withDataInputStream` / `withDataOutputStream` 序列化/反序列化对象
### 执行外部命令

- 使用 String#execute() 方法执行并生成 Process 对象
- 使用 Process#in，Process#text 获取返回流或值
- 使用 pipeTo 方法或 | 运算符将前面的输出值输入到之后的 Process 中
# 集合
## List

- 使用 `[]` 创建列表
  ```groovy
  def list = [1, 2, 3]
  ```

- 使用 `<<` 可以增加元素，相当于 `add`
  ```groovy
  def list = []
  list << 5 // 等同于 list.add(5)
  ```

- 使用 `==` 判断每个元素是否相同
  ```groovy
  def list1 = ["hello"]
  def list2 = [] << 'hello'
  assert list1 == list2
  assert list1 === list2 // false
  ```

- 使用 `clone` 方法复制列表
- 使用 `[]` 访问元素，相当于 `get` / `getAt`
  ```groovy
  def list = [1, 2, 3]
  assert list[0] == 1
  assert list[-1] == 3
  ```

- 可以使用 `set` 方法修改元素并返回原值
- 在 boolean 判断时，列表可直接用于判断，使用 isEmpty
```groovy
def list1 = []
assert !list1
def list2 = [false]
assert list2
```
### 遍历

- 使用 `each` / `eachWithIndex` 进行遍历
- 使用 `collect` 或 `*` 执行 `map` 操作
  ```groovy
  assert [1, 2, 3].collect {it*2} == [2, 4, 6]
  assert [1, 2, 3]*.multiply(2) == [2, 4, 6]
  ```

- 使用 `collect` 还可以将 `map` 的结果放入其他 `list`
  ```groovy
  def list = [0]
  assert [1, 2, 3].collect(list) {it*2} == [0, 2, 4, 6]
  assert list == [0, 2, 4, 6]
  ```
### 添加删除
- `empty`：该属性相当于 isEmpty() 方法
- `<<`：leftShift 方法，向列表最后添加元素并返回该 list
- `+`，`+=`：plus 方法，将原列表复制，向复制的新列表添加元素并返回新列表（+= 会赋值到原变量），会自动 unzip
- `add` / `addAll` 方法：增加了一个重载，传入一个对象，插入到指定对象之前
- `-` / `-=`：相当于 remove/removeAll，返回列表
- remove/removeElement/removeAt
- `*/multiply`：复制元素

  ```groovy
  assert [1, 2]*3 == [1,2,1,2,1,2]
  assert Collections.nCopy(2, [1, 2]) == [[1,2], [1,2]]
  ```
## Map

使用 `[:]` 创建

```groovy
def emptyMap = [:]
def map = [
    name: "Groovy",
    version: "3.0.2",
    id: 0
]
```

Map中，key 不加任何符号则将其转化为 String。若为一个引用的对象，使用括号

```groovy
def a = "name"
def map = [a: "Groovy"]
assert map.a == "Groovy"
assert map["name"] == null
def map2 = [(a): "Groovy"]
assert map.name == "Groovy"
```

可使用 `get`，`[]` 或者 `.` 访问对应元素；如果需要访问 Map 自身的 get 方法，需要使用完整的方法名

```groovy
map = [1     : 'a',
     (true) : 'p',
     (false): 'q',
     (null) : 'x',
     'null' : 'z']
assert map.containsKey(1) // 1 is not an identifier so used as is
assert map.true == null
assert map.false == null
assert map.get(true) == 'p'
assert map.get(false) == 'q'
assert map.null == 'z'
assert map.get(null) == 'x'

def map = [name: "Groovy", id: 1234]
assert map.class == null
assert map.get("class") == null
assert map.getClass() == LinkedHashMap
```

list.groupBy ==> map
## Range

- 3..5 即 3, 4, 5，from\=\=3，to\=\=5
- ‘a'..'d' 即 a, b, c, d
## GPath

对于map列表，可直接转化为对应的 list

```groovy
def maps = [['a': 11, 'b': 12], ['a': 21. 'b':22], null]
assert maps.a == [11, 21]
assert maps*.a == [11, 21, null]
assert maps*.a == maps.collect {it?.a}
```
## 传播运算符

在 map 创建时，使用 `*` 添加 map，行为类似 putAll

```groovy
def list1 = [
    'z': 900,
    *: ['a': 100, 'b': 200],
    'a': 300
]
assert list1 == ['a': 300, 'b': 200, 'z': 900]

def f = { [1: 'u', 2: 'v', 3: 'w'] }
assert [*: f(), 10: 'zz'] == [1: 'u', 2: 'v', 3: 'w', 10: 'zz']
```

也可以对 list 使用，返回 list

```groovy
f = {m, i, j, k -> [m, i, j, k]}
assert f('e': 100, *[4, 5], *: ['a': 10, 'b': 20, 'c': 30], 6)
   == [["e": 100, "b": 20, "c": 30, "a": 10], 4, 5, 6]
```
## `*.` 运算符

类似于 Stream#map 方法

```groovy
assert ['a', 'aaa', 'aaaaa']*.size() == [1, 3, 5]、
```
## 下标切片

```groovy
def list = [10, 11, 12, 13, 14, 15]
assert list[3..5] == [13, 14, 15]
assert list[5..3] == [15, 14, 13]
assert list[1, 3..5, 2] == [11, 13, 14, 15, 12]
// 同样可应用于 String
def str = 'hello'
assert str[0, 3] == 'hell'
```
# Date/Calendar

对 Date/Calendar 可直接使用下标访问

```groovy
def cal = instance
println "YEAR = ${cal[YEAR]}" // YEAR = 2020
println "MONTH = ${cal[MONTH]}" // MONTH = 3
println "DAY_OF_YEAR = ${cal[DAY_OF_YEAR]}" // DAY_OF_YEAR = 93
println "DAY_OF_MONTH = ${cal[DAY_OF_MONTH]}" // DAY_OF_MONTH = 2
println "DAY_OF_WEEK = ${cal[DAY_OF_WEEK]}" // DAY_OF_WEEK = 5
```

对 Date 可进行 +- 操作

```groovy
def utc = TimeZone.getTimeZone('UTC')
def date = Date.parse('yyyy-MM-dd HH:mm:ss', "2020-01-01 12:00:00", utc)
def prev = date - 1
def next = date + 1
// Prev=Tue Dec 31 20:00:00 CST 2019
println "Prev=$prev"
// Next=Thu Jan 02 20:00:00 CST 2020
println "Next=$next"
// Day Diff: 2
println "Day Diff: ${next - prev}"
```
# 便捷方法

## ConfigSlurper

用于读写配置文件

```groovy
def config = new ConfigSlurper().parse('''
    app.date = new Date()
    app.age = 42
    app {
        name = "Test${42}"
    }
''')
assert config.app.date instanceof Date
assert config.app.age == 42
assert config.app."age" == 42
assert config.app.name == 'Test42'
assert config.app instanceof groovy.util.ConfigObject
assert config.test != null // 虽然 config 中没有，但的确返回一个 ConfigObject

// 构造中可选择分支
def config = new ConfigSlurper('development').parse('''
  environments {
       development {
           app.port = 8080
       }

       test {
           app.port = 8082
       }

       production {
           app.port = 80
       }
  }
''')
assert config.app.port == 8080

// 使用 registerConditionalBlock 添加别名
def slurper = new ConfigSlurper()
slurper.registerConditionalBlock('myProject', 'developers')   
def config = slurper.parse('''
  sendMail = true

  myProject {
       developers {
           sendMail = false
       }
  }
''')
assert !config.sendMail
```

**使用 config.toProperties() 可以将 ConfigSlurper 转化为 java.util.Properties**
## Expando

使用 Expando 创建动态对象

```groovy
def expando = new Expando()
expando.name = 'John'
expando.toString = { -> 'John' }
expando.say = { String s -> "John says: ${s}" }

assert expando.name == 'John'
assert expando as String == 'John'
assert expando.say('Hi') == 'John says: Hi'
```
## 可观察集合

```groovy
def list = [1, 2, 3] as ObservableList
list.addPropertyChangeListener {
    println $it
}
```
