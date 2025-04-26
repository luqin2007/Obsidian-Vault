# Eval

使用 Eval 动态执行 Groovy 代码

```groovy
// 1089
println Eval.me('33 * 33')
// sssss
println Eval.me('"s" * 5')
// Hello World
// null
println Eval.me('println "Hello World"')
```
# 脚本

Groovy 除了使用 JVM 的标准程序语言结构（main 函数作为入口），也可以作为脚本使用，脚本从第一行开始，向下逐行执行，直接运行即可。

可在脚本中定义方法。脚本中定义的方法会作为普通方法编译到脚本类中，其他内容作为 run 方法的内容

脚本中的变量不必要显式定义，可直接使用，
- 若在脚本中声明，则变量是位于 run 方法体中的局部变量，其他方法无法访问
- 若在脚本中直接使用，则这些变量会经过脚本绑定，对方法可见，且可以通过 Binding 共享数据

```groovy
x = 1
y = 2
assert x * y == 2
```
# 执行脚本

当 Groovy 脚本作为单独的脚本执行时，直接运行即可。

Groovy 的脚本被编译为类，一般继承自 `groovy.lang.Script`，在代码中可用 GroovyShell 执行脚本，脚本与代码之间可以使用 Binding 交换数据

```groovy
def binding = new Binding()
def shell = new GroovyShell(binding)
binding.setVariable('x', 1)
binding.setVariable('y', 3)
shell.evaluate 'z = x * 2 + y'
println shell.getVariable('z') // 5
```
## 共享数据

使用 Binding 共享数据，可向脚本中写入或读出变量值，注意 Binding 线程不安全

```groovy
def data = new Binding()
def shell = new GroovyShell(data)
// 写入
data.setProperty('text', 'I am shared data')
data.setProperty('date', new Date())
// At Sat Apr 04 15:34:18 CST 2020, I am shared data
println shell.evaluate('''
    result = "At $date, $text"
    result
''')
// 读出
// At Sat Apr 04 15:34:18 CST 2020, I am shared data
println data.getProperty('result')
```

若需要对同一个脚本应用不同的参数，可使用 parse 方法

```groovy
def data1 = new Binding(x: 3)
def data2 = new Binding(x: 4)
def shell = new GroovyShell()
def script = shell.parse 'y = x * x'
script.binding = data1
script.run()
script.binding = data2
script.run()
// data1.y=9, data2.y=16
println "data1.y=${data1['y']}, data2.y=${data2['y']}"
```

此时，这两个 run 方法是运行在一个线程上的，并不是多线程执行。若需要多线程，则需要创建多个 Script 实例及 Binding 实例，保证线程安全

```groovy
def script = '''
    thread = Thread.currentThread().name
    y = x * x
'''
def data1 = new Binding(x: 3)
def data2 = new Binding(x: 4)
def shell = new GroovyShell()
def script1 = shell.parse script
def script2 = shell.parse script
script1.binding = data1
script2.binding = data2
def thread1 = Thread.start { script1.run() }
def thread2 = Thread.start { script2.run() }
[thread1, thread2]*.join()
// script1: y = 9 at Thread-1
println "script1: y = ${data1['y']} at ${data1['thread']}"
// script2: y = 16 at Thread-2
println "script2: y = ${data2['y']} at ${data2['thread']}"
```

不推荐这么做。这么做可以直接用 GroovyClassLoader
## 自定义脚本类

- 创建自定义脚本类 CustomScript 继承自 `groovy.lang.Script`
- 创建 `CompilerConfiguration` 实例 config，将 `scriptBaseClass` 值指向 CustomScript
- 使用 config 创建 `GroovyShell`

```groovy
package demo

// 自定义脚本类
abstract class CustomScript extends Script {
    String name
    void greet() {
        println "Hello $name"
    }
}

def config = new CompilerConfiguration()
config.scriptBaseClass = "demo.CustomScript"
def shell = new GroovyShell(this.class.classLoader, config)
// Hello Groovy
shell.evaluate '''
    setName 'Groovy'
    greet()
'''
```

可在 Script 中实现 run 方法，然后创建一个抽象方法，返回 Object，以替代 run 方法
### @BaseScript

可使用 BaseScript 注解在脚本中指定脚本基类
- 该注解可以应用到一个变量上，这个变量会赋值脚本转化为的 Script 对象

```groovy
import demo.CustomScript
import groovy.transform.BaseScript

@BaseScript CustomScript baseScript
// class demo.CustomScript
println baseScript.class.superclass
setName 'Groovy'
// Hello Groovy
greet()
```

- 该注解可以应用到 import 上

```groovy
@BaseScript(CustomScript)
import demo.CustomScript
import groovy.transform.BaseScript

setName 'Groovy'
// Hello Groovy
greet()
```
### GroovyClassLoader

使用 GroovyClassLoader 替代 GroovyShell，可以在运行时动态创建 class

GroovyClassLoader 会保持他创建的 Class 的引用，请注意防止内存泄漏

```groovy
def classCode = '''
    class Foo {
        static int i = 0
        
        int call() {
            i++
            return i * i
        }
    }
'''
def gcl = new GroovyClassLoader()
def myClass = gcl.parseClass(classCode)
println myClass.newInstance().call() // 1
println myClass.newInstance().call() // 4
```

注意：使用 parseClass 重复生成 Class 时，若源为 String，则产生多个 Class 对象；若源为 File 时，则产生同一个 Class 对象

```groovy
String classCode = '...'
def gcl = new GroovyClassLoader()
def c1 = gcl.parseClass(classCode)
def c2 = gcl.parseClass(classCode)
assert c1 != c2
File classFile = new File('...')
def c3 = gcl.parseClass(classCode)
def c4 = gcl.parseClass(classCode)
assert c3 == c4
```
### GroovyScriptEngine

使用 GroovyScriptEngine 允许依赖管理

```groovy
def url = new File('D:\\projects\\Demos\\Groovy\\src').toURI().toURL()
def binding = new Binding()
def engine = new GroovyScriptEngine([url] as URL[])
def before = ''
def exit = false
while (!exit) {
    def greeter = engine.run('GreeterTest.groovy', binding)
    def hello = greeter.sayHello()
    if (hello != before) {
        before = hello
        println hello
    }
    exit = greeter.exit()
}

// GreeterTest.groovy
class Greeter {
    String sayHello() {
        'Hello, Groovy'
    }

    boolean exit() {
        true
    }
}
new Greeter()
```

可以修改 GreeterTest.groovy 的内容，在应用不退出的情况下，修改输出值 
### CompilationUnit

不推荐，通过 org.codehaus.groovy.control.CompilationUnit 在编译期执行操作
## JSR 223

javax.script API，可以在 Java 环境下调用 Groovy 脚本

```java
ScriptEngineManager manager = new ScriptEngineManager();
ScriptEngine engine = manager.getEngineByName("groovy");
// 执行脚本
int sum = (int) engine.eval("(0..100).sum()");
// sum: 5050
System.out.println("sum: " + sum);
// 共享变量
engine.put("a", "HELLO");
engine.put("b", "world");
engine.eval("c = a.toLowerCase() + ' ' + b.toUpperCase()");
String c = (String) engine.get("c");
// parameter: hello WORLD
System.out.println("parameter: " + c);
// 方法调用
String func = "def factorial(n) { n == 1 ? 1 : n * factorial(n - 1) }";
engine.eval(func);
int factorial = (int) ((GroovyScriptEngineImpl) engine).invokeFunction("factorial", 10);
// func factorial: 3628800
System.out.println("func factorial: " +factorial);
```
