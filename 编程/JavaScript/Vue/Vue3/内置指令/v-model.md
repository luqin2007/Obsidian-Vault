`v-model` 可以在 `<input>`、`<textarea>` 和 `<select>` 元素上完成双向绑定
* 对应元素忽略 `value`、`checked`、`selected` 值，使用 Vue 实例作为初始数据
* 双向绑定：对应元素数据改变时将直接反映到 Vue app 属性值，反之亦然

> [!note] v-model 实际触发 `update:modelValue` 事件，并携带 `modelValue` 类型数据


`````col
````col-md
flexGrow=1
===
```embed-html
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/v-model.html"
LINES: "9-49"
TITLE: "v-model.html"
```
````
````col-md
flexGrow=1
===
```embed-js
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/v-model.html"
LINES: "52-69"
TITLE: "v-model.html"
```
````
`````

# 修饰符

| 修饰符      | 说明                                |
| -------- | --------------------------------- |
| `lazy`   | 输入框不会立即同步到变量，而是在 `change` 事件发生后同步 |
| `number` | 自动将输入框内值转换为数字                     |
| `trim`   | 自动去除输入的前后空格                       |
