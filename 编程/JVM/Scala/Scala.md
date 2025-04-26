Scala 是一个 JVM 平台的强类型语言，在保持了静态语言的基础上具有更大的可扩展性和语法上的灵活性。

> [!warning] 随着 Java 的不断改善，其优势逐渐淡化，而 Scala 庞大的基础库又带来了大量的负担。

* 表达式即可以计算的语句

  ```scala
  println("Hello scala")
  ```

* 变量使用 `var` 声明，常量使用 `val` 声明

  ```scala
  var a = 1 // 这里使用了自动编译器的类型推算，可以不必显式声明 Int
  var b: Int = 2
  a = 3

  val c = 1
  val d: String = "scala"
  c = 2 // 异常：error: reassignment to val
  ```

* 使用 `{}` 组合的表达式称为代码块，最后一个表达式即为该代码块的返回值

  ```scala
  println({
      val x = 1 + 2
      "代码块返回：" + (x + 1)
  })
  ```

JVM 平台需要实现的一个入口方法，接受一个 `Array[String]` 参数

```scala
object Main {
    def main(args: Array[String]): Unti = {
        // ....
        println("Hello, Scala developer[main]")
    }
}
```

- [[类型结构]]
- [[函数]]
- [[类]]
- [[Trait]]
- [[隐式转换]]
- [[包]]
- [[泛型]]
- [[注解]]
- [[循环]]
- [[模式匹配]]
- [[异常捕获]]
- [[actor 并发模型]]