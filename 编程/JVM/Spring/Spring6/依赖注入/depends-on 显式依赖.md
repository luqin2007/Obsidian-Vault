> [!caution] `depends-on` 依赖不会通过 `parent` 属性继承

```xml
<beans ...>
  <bean id="bean1" ... />
  <bean id="bean2" ... />

  <bean ... depends-on="bean1, bean2" />
</beans>
```

有关不同生命周期之间依赖，直接将引用当成一次获取对象即可。

* `singleton` A 依赖 `prototype` B：创建 `A` 时创建一个 `B`，`A` 全程持有同一个 `B`
* `prototype` A 依赖 `singleton` B：每个 `A` 对象引用相同的 `B`

‍
