通过 `JavaScript` 事件控制过渡和动画

* 动画事件，事件处理函数第一个参数为 `el`，第二个参数为 `done`：
    * `before-enter`，`enter`，`after-enter`，`enter-cancelled`
    * `before-leave`，`leave`，`after-leave`，`leave-cancelled`
* `done` 参数是一个函数，当动画完成后必须调用 `done()` 通知 Vue 完成

`JS` 控制的动画和 CSS 过渡动画可以同时存在
* 同时使用 CSS 过渡动画和 JS 动画时，JS 事件中 `done` 可以不调用，CSS 动画完成后自动完成
* `:css="false"` 属性表示禁用 CSS 动画，可以防止 CSS 动画参数对 JS 动画的影响

```embed-html
PATH: "vault://_resources/codes/Vue/Vue3/hellovue/transition-js.html"
LINES: "25-33,34-35,41-57,59"
TITLE: ""
```
