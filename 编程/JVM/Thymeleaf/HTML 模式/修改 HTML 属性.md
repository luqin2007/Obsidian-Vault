# 设置属性值

通过 `th:attr` 或 `data-th-attr` 属性设置标签的某个属性：`th:attr="属性=值"`

```html
<!-- <form action="/subscribe"> -->
<form action="subscribe.html" th:attr="action=@{/subscribe}">
```

若要同时设置多个属性，使用 `,` 分隔，下面的

```html
<!-- <img src="/images/gtvglogo.png" title="[logo]" alt="[logo]"> -->
<img src="../../images/gtvglogo.png" th:attr="src=@{/images/gtvglogo.png},title=#{logo},alt=#{logo}" />
```

可以直接使用 `th:属性` 或 `data-th-属性` 的形式直接设置属性值

```html
<img src="../../images/gtvglogo.png" th:src="@{/images/gtvglogo.png}" th:title="#{logo}" th:alt="#{logo}" />
```

若多个属性值相同，可以使用 `-` 连接属性名一次设置：

```html
<img src="../../images/gtvglogo.png" th:src="@{/images/gtvglogo.png}" th:title-alt="#{logo}" />
```

# 设置前缀或后缀

`th:attrappend` 与 `th:attrprepend` 可以为属性添加后缀或前缀，同样可以同时设置多个属性，以 `,` 分隔

```html
<input type="button" value="Do it!" class="btn" th:attrappend="class=${' ' + cssStyle}" />
```

可以使用 `th:属性append` 或 `th:属性prepend` 为指定属性添加后缀或前缀

```html
<tr th:each="prod : ${prods}" class="row" th:classappend="${prodStat.odd}? 'odd'">
```
