```reference
file: "@/_resources/codes/spring/autowire-constructor/src/main/java/com/example/mybank/service/CustomerRegistrationServiceImpl.java"
start: 13
end: 16
```

```reference
file: "@/_resources/codes/spring/autowire-constructor/src/main/resources/applicationContext.xml"
start: 6
end: 8
```

注意：

* 若某个属性对应类型在 Context 中不存在对应类型的 bean，Spring 不会设置该属性，也不会产生异常
* 若某个属性对应类型在 Context 中存在多个 bean 实例，而其中有一个 bean 存在 `primary=true` 属性，则会使用该 bean 初始化
* 若某个属性对应类型在 Context 中存在多个 bean 实例，且没有 `primary=true` 或有多个该属性，Spring 会产生异常
