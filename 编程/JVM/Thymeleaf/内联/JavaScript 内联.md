# JavaScript 内联

`javascript` 模式的内联会自动为字符串添加 `""` 并处理转义

```html
<script th:inline="javascript">
    ...
    var username = [(${session.user.name})];
    ...
</script>
```

```html
<script th:inline="javascript">
    ...
    var username = "Sebastian \"Fruity\" Applejuice";
    ...
</script>
```

同时，还支持自然模板形式 -- 使用 `/*[[]]*/` 或 `/*[()]*/` 包围模板，并在后面接一个合法值，方便调试。Thymeleaf 会自动忽略注释后分号前的内容。

```html
<script th:inline="javascript">
    ...
    var username = /*[[${session.user.name}]]*/ "Gertrud Kiwifruit";
    ...
</script>
```

事实上，`javascript` 模式的内联不仅支持字符串，还支持其他几种常用类型：数字，布尔，数组，集合，Map，只有属性的对象等。使用自然模板时根据后面的值判断类型。（通过 `IStandardJavaScriptSerializer` 类完成序列化）

```html
<script th:inline="javascript">
    ...
    var user = /*[[${session.user}]]*/ null;
    ...
</script>
```

```html
<script th:inline="javascript">
    ...
    var user = {"age":null,"firstName":"John","lastName":"Apricot",
                "name":"John Apricot","nationality":"Antarctica"};
    ...
</script>
```

单独的 js 文件适用于文本内联的规则。
