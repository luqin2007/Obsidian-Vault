`v-for` 用于遍历数组或对象：`v-for="item in list"`

# 遍历数组

第一个参数是数组对象，第二个参数是下标索引

`````col
````col-md
flexGrow=1
===
```html
<div id="hello-vue">
    <ul>
        <li v-for="(item, index) in items">
            {{index}} -- {{item}}
        </li>
    </ul>
</div>
```
````
````col-md
flexGrow=1
===
```html
<div id="hello-vue">
    <ul>
        <li v-for="user in users">
            {{user.uname}}
        </li>
    </ul>
</div>
```
````
`````

> [!note] 支持 `push`，`pop`，`unshift`，`shift`，`splice`，`sort`，`reverse` 等方法的同步更新

```reference
file: "@/_resources/codes/Vue/Vue3/hellovue/v-for-array.html"
start: 9
end: 47
```

# 遍历对象

第一个参数是属性值，第二个是属性名，第三个是索引

```html
<div id="hello-vue">
    <ul>
        <li v-for="(value, key, index) in myObject">{{++index}}. {{key}}={{value}}</li>
    </ul>
</div>
```

# 遍历数字

`v-for="i in n"` 相当于从 1 到 `n` 的遍历

```html
<div id="hello-vue">
    <ul>
        <li v-for="i in 100">{{i}}</li>
    </ul>
</div>
```

# 循环嵌套

多层循环时，外层循环的结果可以在内层循环中直接使用

```reference
file: "@/_resources/codes/Vue/Vue3/hellovue/v-for-lager-than-100.html"
start: 9
end: 28
```
