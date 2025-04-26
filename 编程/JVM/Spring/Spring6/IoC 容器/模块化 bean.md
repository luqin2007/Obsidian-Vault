使用 `<import>` 在一个 XML 配置文件中引入其他 XML 配置

```xml
<beans ...>
  <import resource="xml 文件" />
</beans>
```

Spring 允许不同 `XML` 文件中定义的 `bean` 存在依赖关系。
