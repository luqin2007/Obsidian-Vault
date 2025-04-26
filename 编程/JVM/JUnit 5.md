Java 平台的测试框架，JUnit 4 与 5 之间有一次很大的更新。

新版的 JUnit 5 分为三部分：
* JUnit Platform：启动框架，定义了 TestEngine API，还包括了一个单独的控制台启动器
* JUnit Jupiter：编写测试和扩展的编程模型和扩展模型，提供 TestEngine 实现
* JUnit Vintage：提供兼容 JUnit 3 和 JUnit 4 的 TestEngine 实现

Maven 配置参考 [junit5-samples/junit5-maven-consumer at r5.0.2 · junit-team/junit5-samples (github.com)](https://github.com/junit-team/junit5-samples/tree/r5.0.2/junit5-maven-consumer)

Gradle 配置参考 [junit5-samples/junit5-gradle-consumer at r5.0.2 · junit-team/junit5-samples (github.com)](https://github.com/junit-team/junit5-samples/tree/r5.0.2/junit5-gradle-consumer)

执行 Maven 或 Gradle 的 `test` 命令，或类似 `*.*Test*` 的任务即可执行。执行 `test` 可以检查测试平台是否正常运行：

![[Pasted image 20230918011822.png]]
任何包含 `@Test` 注解方法的类都可以作为测试类

```java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author luqin2007
 */
public class FirstJUnit5Tests {

    @Test
    void myFirstTest() {
        assertEquals(2, 1 + 1);
    }
}
```

# 注解配置

JUnit Jupiter 注解用于配置和扩展测试

* 测试方法：标记测试方法。测试方法不一定是 private 的，且可被继承
    * `@Test`：标注该方法为测试方法，**不能有返回值**
    * `@ParameterizedTest`：标注该方法用于参数化测试，**不能有返回值**
    * `@RepeatedTest(value, name)`：标注该方法用于重复测试，**不能有返回值**
    * `@TestTemplate`：标注该方法是测试用例的模板，**不能有返回值**
    * `@TestFactory`：标注该方法用于动态测试的工厂方法，详见[[#动态测试]]
* 测试生命周期，对应注解都可以被继承
    * `@TestInstance(Lifecycle)`：为被测试类配置测试实例的生命周期
        * `PER_CLASS`：同一个测试类只创建一个实例。使用该类型时可以在 `@Nested` 修饰的类上使用 `@BeforeAll`，`@AfterAll`
        * `PER_METHOD`：默认，为每一个测试方法（包括模板方法，工厂方法等）创建单独类
        * 修改默认类型：两种方法
            * JVM 参数：`-Djunit.jupiter.testinstance.lifecycle.default=per_class`
            * `src/test/resources/junit-platform.properties`：`junit.jupiter.testinstance.lifecycle.default = per_class`
    * `@BeforeAll`：在所有测试方法之前执行，**不能有返回值且是静态的**
    * `@AfterAll`：在所有测试方法之后执行，**不能有返回值且是静态的**
    * `@BeforeEach`：在每个测试方法之前执行，**不能有返回值**
    * `@AfterEach`：在每个测试方法之后执行，**不能有返回值**
* 测试注释：用于运行时展示特定内容
    * `@DisaplayName(name)`：用于测试类或测试方法的自定义名称，默认为类名或方法名
* 其他
    * `@Nested`：被注解的类是一个嵌套非静态类。不能使用 `@BeforeAll` 和 `@AfterAll`，不被继承
    * `@Tag(String)`：在测试类或方法上声明标签，用于过滤测试

        * 可在类级别继承，不在方法级别继承
        * 可有多个
        * 不能为空白，不能有空格、控制符、保留字符（`,()&|!`）
    * `@Disabled`：用于禁用对应测试类或测试方法，不能被继承
    * `@ExtendWith`：自定义扩展
* 组合注解：JUnit Jupiter 支持的注解都可以用作元注解，因此可以创建自定义的组合注解

凡是可以用于测试类的注解也可以用于接口，大多数用于测试的注解都是可以继承的

# 函数参数

测试方法和测试类的构造函数可以包含以下类型参数：
* `TestInfo`
* `RepeatedTest`：用于 `@RepeatedTest`，`@BeforeEach` 或 `@AfterEach`
* `TestReporter`：使用 `publishEntry` 方法输出当前测试运行的额外数据

# 断言与假设

* 断言：`org.junit.jupiter.Assertions` 类的静态方法，仅当断言通过时测试通过
    * 值校验：`assertTrue`，`assertEquals`，`assertNotNull`
    * 时间校验：`assertTrue`
    * 校验组：`assertAll`

```java
int a = 2;
// 当且仅当 a=2 时测试通过
assertEquals(a, 2);
```

* 假设：`org.junit.jupiter.Assumptions` 类的静态方法，仅当假设通过时进行后面的测试

```java
@Test
void testOnlyOnCI() {
    // 当且仅当在持续构建服务器时运行该测试
    assumeTrue("CI".equals(System.getenv("ENV")));
    // ...
    // do some tests
}

@Test
void testPartOnCI() {
    // do some tests
  
    assumingThat("CI".equals(System.getenv("ENV")), () -> {
        // 当且仅当在持续构建服务器时运行的测试
        // ...
        // do some tests
    });
}
```

* JUnit 5 仍可以使用 JUnit 4 的 `org.junit.Assert` 的方法
* JUnit 支持 AssertJ，Hamcrest，Truth 等三方断言库，可直接使用

```java
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

@Test
void assertWithHamcrestMatcher() {
    assertThat(2 + 1, is(equalTo(3)));
}
```

# 重复测试

使用 `@RepeatedTest(value, name)` 注解的方法用于重复测试

* value：重复运行 value 次，每次测试都相当于一个 `@Test` 方法，有完整的生命周期
* name：测试名，允许以下占位符：
    * `{displayName}`：方法名，或 `@DisplayName` 设置的名称
    * `{currentRepetition}`：已运行次数
    * `{totalRepetition}`：总重复次数

# 参数化测试

使用 `@ParameterizedTest` 注解标记的类型作为参数化测试方法。参数化测试允许对同一方法使用不同参数进行测试，需要添加对 `junit-jupiter-params` 的依赖

## 参数来源

* `@ValueSource`：多组 Java 注解可直接支持的类型数组
* `@EnumSource`：使用枚举值；`name` 支持正则
* `@MethodSource`：指定一个或多个工厂方法
    * `PER_METHOD` 时必须是静态的
    * 返回一个 `Stream`，`Iterable` 或 `Iterator` 对象
    * 支持 `DoubleStream`，`IntStream`，`LongStream`
    * 若测试函数有多个参数，使用 `@MethodSource`，且返回 `Stream<Arguments>`，可以使用 `Arguments#of` 创建 `Arguments` 对象

```java
@ParameterizeTest
@MethodSource("stringProvider")
void testWithMethodSource(String argument) { ... }

static Stream<String> stringProvider() { ... }
```

* `@CsvSource`：将参数表示为 `,` 分割的值（字符串），传入一个 `String[]`
    * `@CsvSourceFile`：从 CSV 文件加载
    * 隐式转换：字符串可以隐式转换为数字，字符，枚举，时间与日期等类型
    * 显示转换：使用 `@ConvertWith` 修饰某个参数，指定一个继承自 `SimpleArgumentConverter` 的类
* `@ArgumentsSource`：指定一个实现 `ArgumentsProvider` 接口的类

## 显示名

使用 `@ParameterizedTest` 注解的 `name` 参数指定，可使用占位符：

* `{index}`：调用的是第几组数据，下标从 1 开始
* `{arguments}`：完整的逗号分隔的参数列表
* `{0}`, `{1}`, ...：第 1，2，... 个参数

# 模板测试

与 `TestTemplateInvocationContextProvider` 搭配使用

# 动态测试

使用 `@TestFactory` 工厂方法在运行时生成测试方法的测试称为动态测试。

`@TestFactory` 修饰的方法必须满足以下几点：
* 不能是 `private` 或 `static` 的
* 返回值必须是 `DynamicNode` 及其子类的 `Stream`，`Collection`，`Iterable` 或 `Iterator`
* JUnit 内置的 DynamicNode 的实现类有 DynamicContainer，DynamicTest 等
