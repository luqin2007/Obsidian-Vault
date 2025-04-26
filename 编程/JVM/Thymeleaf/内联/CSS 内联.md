# CSS 内联

```html
<style th:inline="css">
    .[[${classname}]] {
      text-align: [[${align}]];
    }
</style>
```

```html
<style th:inline="css">
    .main\ elems {
      text-align: center;
    }
</style>
```

CSS 内联也支持自动类型转换和自然模板

```html
<style th:inline="css">
    .main\ elems {
      text-align: /*[[${align}]]*/ left;
    }
</style>
```
