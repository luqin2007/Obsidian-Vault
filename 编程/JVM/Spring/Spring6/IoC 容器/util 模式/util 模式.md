> [!note] xmlns 与 xsi

```reference
file: "@/_resources/codes/spring/ioc-util/src/main/resources/applicationContext.xml"
start: 4
end: 5
```

简化配置 `bean`，直接在容器中直接创建对应对象

> [!note] `util` 模式是 [[FactoryBean]] 的快捷方式

| 元素                                    | 描述                     | FactoryBean 类型               |
| ------------------------------------- | ---------------------- | ---------------------------- |
| [[list\|<list>]]                   | `java.util.List`       | `ListFactoryBean`            |
| [[map\|<map>]]                     | `java.util.Map`        | `MapFactoryBean`             |
| [[set\|<set>]]                     | `java.util.Set`        | `SetFactoryBean`             |
| [[constant\|<constant>]]              | `public static` 字段     | `FieldRetrievingFactoryBean` |
| [[property-path\|<property-path>]] | `bean` 属性              | `PropertyPathFactoryBean`    |
| [[properties\|<properties>]]       | `java.util.Properties` | `PropertiesFactoryBean`      |

通用属性：

|属性|说明|
| ------| -----------------------|
|`scope`|暴露 `bean` 范围，默认 `singleton`|
