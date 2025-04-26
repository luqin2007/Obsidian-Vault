- HTML：使用 `a` 替代 `img`

```HTML
<div class="logo">
    <h1><a href="...">{{Title}}</a></h1>
</div>
```

- CSS：使用背景图片

```CSS
.logo {
    width: {{img_width}};
    height： {{img_height}};
}

.logo h1 a {
    display: block;
    width: {{img_width}};
    height： {{img_height}};
    background-image: url({{img_path}});
    background-size: contain;
    font-size: 0;
}
```
