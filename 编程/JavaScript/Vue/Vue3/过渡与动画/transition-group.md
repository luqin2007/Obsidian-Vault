`<transition-group>` 标签用于一组元素之间的动画，如列表等

* 不同于 `<transition>`，默认以 `<span>` 元素渲染
* 不切换特有元素，因此过渡模式不可用
* 内部元素需要带有 `key` 属性
* CSS 过渡动画类应用于内部元素而不是组件本身

`<transition-group>` 用法与 `<transition>` 类似，只是多了一个 `v-move` 类用于进行列表元素的移动动画

---

# 例：插入、删除元素

```reference fold
file: "@/_resources/codes/Vue/Vue3/hellovue/transition-group-in-out.html"
```

# 例：列表平滑过渡

添加 `.list-move` 类到 `.list-enter-active,.list-leave-active` 块，实现元素移动时的平滑过渡

```reference hl=13
file: "@/_resources/codes/Vue/Vue3/hellovue/transition-group-move.html"
start: 7
end: 24
```

`.list-leave-active` 使用绝对定位，可实现元素移除时剩余元素的平滑移动

```reference hl=13,19-21
file: "@/_resources/codes/Vue/Vue3/hellovue/transition-group-translate.html"
start: 7
end: 28
```

Vue 通过名为 FLIP 的简单动画序列实现 `.list-move` 平滑过渡到新位置，但其中的元素不能为 `display:inline`，可用 `display:inline-block` 替代

`v-move` 动画也可以用于多维列表的过渡

```reference fold
file: "@/_resources/codes/Vue/Vue3/hellovue/transition-group-grid.html"
```