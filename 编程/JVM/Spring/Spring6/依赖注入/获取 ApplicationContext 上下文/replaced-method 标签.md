使用 `<replaced-method>` 标签可以重新实现某个方法。

1. 创建 `bean` 类，`getMyBean` 方法即要重写的方法

```reference
file: "@/_resources/codes/spring/applicationcontext-replaced-method/src/main/java/com/example/mybank/ReplacedMethodBean.java"
start: 3
end: 8
```

2. 创建代理类，实现 `MethodReplacer` 接口，重写 `reimplement` 方法

> [!note] 代理类也是一个普通 `bean` 类，因此也支持 `ApplicationContextAware` 等生命周期接口

```reference
file: "@/_resources/codes/spring/applicationcontext-replaced-method/src/main/java/com/example/mybank/MyMethodReplacer.java"
start: 10
end: 20
```

3. 在 `XML` 中注册

```reference
file: "@/_resources/codes/spring/applicationcontext-replaced-method/src/main/resources/applicationContext.xml"
start: 8
end: 12
```

除了根据名称选择 `MethodReplacer`，我们还可以通过参数选择函数重载

```xml
<bean id="replacedMethod" class="com.example.mybank.bean.ReplacedMethodBean">
    <!-- 选择重载: getMyBean(String, String) -->
    <replaced-method name="anotherMethod" replacer="myMethodReplacer">
        <arg-type>java.lang.String</arg-type>
        <arg-type>java.lang.String</arg-type>
    </replaced-method>
</bean>
```
