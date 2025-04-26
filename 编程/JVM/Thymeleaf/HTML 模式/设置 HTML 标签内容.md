* `th:text` 或 `data-th-text` 可用于替换 HTML 标签的内容，其中的值会自动进行转义

```html
<!-- home.welcome=Welcome to our <b>fantastic</b> grocery store! -->

<!-- p>Welcome to our &lt;b&gt;fantastic&lt;/b&gt; grocery store!</p -->
<p th:text="#{home.welcome}">Welcome to our grocery store!</p>
```

* `th:utext` 或 `data-th-utext` 可用于不进行自动转义的替换，因此其中的 `<>` 标签可以生效

```html
<!-- home.welcome=Welcome to our <b>fantastic</b> grocery store! -->

<!-- p>Welcome to our <b>fantastic</b> grocery store!</p -->
<p th:utext="#{home.welcome}">Welcome to our grocery store!</p>
```

还可以直接在标签内替换，使用 `[[]]`，即内联

```html
<div>Welcome [[${user.name}]]</div>
```

‍
