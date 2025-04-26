# 基本类型

- 自动装箱：可以在基本类型变量和常量上直接调用方法，Groovy 自动装箱

```groovy
int a = 10
// class java.lang.Integer
println a.class
// class java.lang.Integer
println 100.class
```
# 字符串
## GString

Groovy 字符串包含 String 和 GString，二者隐式转化
- 所有接收 String 类型变量的地方，都可以接收 GString，会隐式调用 toString() 方法
- 当内容相同时，String 与 GString 的 hashCode 是不同的，应当避免在依赖 hashCode 的地方（如 HashMap）使用 GString

一般情况下，字面量中带有字符串插值的地方会使用 GString，其余使用 String：
- 使用 `''`创建的字符串为 String，不支持插值；`'''` 类似 `'`，但可跨行
	- 要创建 char 类型变量，可对单个字符串进行强转成 char 或显示声明

```groovy
def str1 = 'Hello World'
def str2 = '''
Line 0
Line 1
Line 2
'''

// 创建 char 类型字符
char c1 = 'c' // 显式声明 char 类型
def c2 = 'c' as char // 强转
```

- 使用 `""` 声明时，若有插值，为 GString，否则为 String；`"""` 类似 "，支持多行

```groovy
def a = "hello"
// class java.lang.String
println a.class

def b = "Hello, ${a}"
// class org.codehaus.groovy.runtime.GStringImpl
println b.class
```

- 可以使用 `/` 创建 GString 字符串。该字符串可插值，且被认为是 rawText 无转义，因此很适合写正则
	- 不能使用 `/` 创建空字符串，因为 `//` 被认为是注释

  ```groovy
  def color = 'blue'
  def interpolatedSlashy = /a ${color} car/
  ```

- 可使用 stripIndent stripMargin 等方法移除缩进和空格
## 插值

使用 `${}` 向字符串中添加占位符，使用时自动替换其值；当仅仅调用一个值，或该值的某个属性，`{}` 可省略

```groovy
def name = 'Lq2007'
def greeting = "Hello $name"
def greeting2 = "Hello ${name.reverse()}"
```

内插闭包：当需要惰性求值时，可使用闭包插值。当该闭包没有参数时，应当返回一个 Object，否则该闭包可以包含一个参数（java.io.StringWriter 类型），无需返回值

```groovy
def number = 1
def str1 = "Number = $number"
def str2 = "Number = ${ -> number }"
def str3 = "Number = ${w -> w << number}"
println str1 // Number = 1
println str2 // Number = 1
println str3 // Number = 1 闭包参数

number = 2
println str1 // Number = 1
println str2 // Number = 2 惰性求值
println str3 // Number = 2
```
## 连接

- 字符串使用 + 连接，与 java 相同

```groovy
assert 'ab' == 'a' + 'b'
```

- 定义字符串时，可以使用 \ 分割长文本，替换 +

  ```groovy
  def str = '这一行非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常\
          非常非常非常非常非常非常非常非常非常非常非常非常非常非常\
          非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常非常长'
  ```

- 可以用于三引号定义的字符串，常用于其第一行，消除第一行

  ```groovy
  def strippedFirstNewline = '''\
  line one
  line two
  line three
  '''
  ```
# 集合

- 使用 `[]` 创建列表，默认创建 ArrayList 无泛型，可通过显式指定列表类型或 as 指定使用的 List
	- 可使用 << 插入元素（相当于 add 方法）
	- 可使用 `[index]` 访问，index 为负数时为倒数第 index 个元素
- 使用 `[:]` 创建 Map，默认为 LinkedHashMap
