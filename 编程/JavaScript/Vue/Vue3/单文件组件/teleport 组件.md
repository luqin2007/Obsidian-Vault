当一个组件的 `template` 部分不属于该组件时，可以使用 `<teleport>` 标签将视图挂载到任何位置。

> [!note] 场景假设
> 相关业务逻辑封装在一个组件中，但其视图部分在某个模态框上，该模态框在 `<body>` 下，甚至都不在 `#app` 组件中

```html
<template>
<div id="teleport-container">
    <teleport to="body">
        <div>该 div 元素将被传送到 body 标签下</div>
    </teleport>
    <teleport to="#teleport-container">
        <div>该 div 元素在 id=teleport-container 的标签下</div>
    </teleport>
</div>
</template>
```