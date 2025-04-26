
```reference
file: "@/_resources/codes/spring/autowire-bytype/src/main/java/com/example/mybank/service/CustomerRegistrationServiceImpl.java"
start: 6
end: 17
```

```reference
file: "@/_resources/codes/spring/autowire-bytype/src/main/resources/applicationContext.xml"
start: 6
end: 8
```

注意：

* 若某个属性对应类型在 Context 中不存在对应类型的 bean，Spring 不会产生异常
* 若某个属性对应类型在 Context 中存在多个 bean 实例，而其中有一个 bean 存在 `primary=true` 属性，则会使用该 bean 初始化
* 若某个属性对应类型在 Context 中存在多个 bean 实例，且没有 `primary=true`，Spring 会产生异常
