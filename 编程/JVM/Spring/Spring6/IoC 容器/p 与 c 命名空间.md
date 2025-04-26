为简化 `bean` 配置，Spring 提供 **p 命名空间** 和 **c 命名空间**。

# p 命名空间

p 命名空间的属性可以将简单的 `<property>` 标签简化成 `<bean>` 的一个属性

>[!summary] xmlns：`http://www.springframework.org/schema/p`

p 命名空间属性有两种模式：`<property-name>` 表示注入的属性名，`<property-value>` 表示注入的值，`<property-ref>` 表示注入的对象引用。

```xml
<bean ... p:<property-name>="<property-value>" />
<!-- 等效于 -->
<bean ...>
    <property name="<property-name>" value="<property-value>"
</bean>
```

```xml
<bean ... p:<property-name>-ref="<property-ref>" />
<!-- 等效于 -->
<bean ...>
    <property name="<property-name>" ref="<property-ref>"
</bean>
```

# c 命名空间

c 命名空间属性可以将简单的 `<constructor-arg>` 标签简化成 `<bean>` 的一个属性。

> [!summary] xmlns：`http://www.springframework.org/schema/c`

c 命名空间属性支持参数按名称注入和按位置注入，与 `ref` 模式共组成 4 种形式：

* 按名称注入：

  ```xml
  <bean ... c:<constructor-arg-name>="<constructor-arg-value>" />
  <!-- 相当于 -->
  <bean ...>
    <constructor-arg name="<constructor-arg-name>" 
                     value="<constructor-arg-value>" />
  </bean>
  ```

  ```xml
  <bean ... c:<constructor-arg-name>-ref="<constructor-arg-ref>" />
  <!-- 相当于 -->
  <bean ...>
    <constructor-arg name="<constructor-arg-name>" 
                     ref="<constructor-arg-ref>" />
  </bean>
  ```

* 按位置注入：
	-  `<constructor-arg-name>`：构造函数（或工厂方法）参数名
	- `<n>`：参数位置（从 0 开始）
	- `<constructor-arg-value>`：直接注入的一个值
	- `<constructor-arg-ref>`：注入的引用 `bean id`

  ```xml
  <bean ... c:_<n>="<constructor-arg-value>" />
  <!-- 相当于 -->
  <bean ...>
    <constructor-arg index="<n>" value="<constructor-arg-value>" />
  </bean>
  ```

  ```xml
  <bean ... c:_<n>-ref="<constructor-arg-ref>" />
  <!-- 相当于 -->
  <bean ...>
    <constructor-arg index="<n>" ref="<constructor-arg-ref>" />
  </bean>
  ```



‍
