# `PropertySourcesPlaceholderConfigurer`

实现配置文件模板化。在属性中，将 `${<property_name>}` 替换为 `locations` 属性配置的文档中的对应值

```reference
file: "@/_resources/codes/spring/bean-propertysourcesplaceholderconfigurer/src/main/resources/applicationContext.xml"
start: 6
end: 14
```

配置一个 `PropertySourcesPlaceholderConfigurer` 对象，并在 `locations` 属性中设置一个列表，内包含所有加载的属性文件

```reference
file: "@/_resources/codes/spring/bean-propertysourcesplaceholderconfigurer/src/main/resources/database.properties"
```

```reference
file: "@/_resources/codes/spring/bean-propertysourcesplaceholderconfigurer/src/main/resources/web-service.properties"
```

之后，便可以在 `bean` 中引用对应的属性值

```reference
file: "@/_resources/codes/spring/bean-propertysourcesplaceholderconfigurer/src/main/resources/applicationContext.xml"
start: 16
end: 25
```

`PropertySourcesPlaceholderConfigurer` 可用的属性有：

|属性|类型|说明|
| ------| ------| ------------------------------------------------|
|`locations`|`List<String>`|加载的配置文档文件相对路径|
|`localOverride`|`boolean`|本地属性优先，即可以使用 `properties` 的配置覆盖配置文件|
|`properties`|`Properties`|当 `localOverride=true` 时允许使用该属性的配置覆盖配置文件的配置|

`context:property-placeholder` 是  `PropertySourcesPlaceholderConfigurer` 的一个快捷方式

```reference
file: "@/_resources/codes/spring/bean-context-property-placeholder/src/main/resources/applicationContext.xml"
start: 2
end: 8
```

# `PropertyOverrideConfigurer`

`PropertyOverrideConfigurer` 类似 `PropertySourcesPlaceholderConfigurer`
* 不能用于构造函数注入，只能用于属性
* 允许使用默认值

`PropertyOverrideConfigurer` 配置方法与 `PropertySourcesPlaceholderConfigurer` 相同，但会根据 `<bean-id>.<property-name>` 格式自动替换对应 `bean` 的属性值

```reference
file: "@/_resources/codes/spring/bean-propertyoverrideconfigurer/src/main/resources/applicationContext.xml"
start: 6
end: 24
```

`context:property-override` 是 `PropertyOverrideConfigurer` 的一个快捷方式

```reference
file: "@/_resources/codes/spring/bean-context-property-override/src/main/resources/applicationContext.xml"
start: 2
end: 8
```
