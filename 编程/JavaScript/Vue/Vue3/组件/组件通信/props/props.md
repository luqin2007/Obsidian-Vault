父组件可以通过标签属性的形式向子组件传递信息

1. 使用子组件属性的 `props` 属性声明可接受数据名，可以是字符串、对象或数组。

> [!note] 使用对象或对象数组时，可对传入的参数进行[[数据验证]]

```reference
file: "@/_resources/codes/Vue/Vue3/hellovue/props.html"
lang: "js"
start: 37
end: 42
```

2. 在子组件中，像访问 `data` 数据一样访问 `props` 属性

```reference
file: "@/_resources/codes/Vue/Vue3/hellovue/props.html"
start: 16
end: 18
```

3. 在父组件中，使用标签属性传递数据

> [!attention] HTML 属性不区分大小写，`props` 中使用驼峰式命名的属性，标签属性应使用 `-` 连接

```reference
file: "@/_resources/codes/Vue/Vue3/hellovue/props.html"
start: 11
end: 11
```

可以使用 `v-bind="object"` 的形式将一个对象直接传递给组件，对象将按其属性名展开传递给子组件

```reference
file: "@/_resources/codes/Vue/Vue3/hellovue/props.html"
start: 13
end: 13
```
