Less 的 `@import` 兼容 CSS  的 `@import` 的所有规则，但 Less 不要求 `@import` 必须在文档开头 - 编译后所有的 `@import` 都将直接移动到文档开头，并支持 `.less` 文件

```Less
.foo {
  background: #900;
}

@import "this-is-valid.less";
```

编译结果：

```CSS
@import "this-is-valid.less";

.foo {
  background: #900;
}
```

# 文件类型

Less 中，有额外的文件类型识别规则：
- 若文件扩展名为 `.css`，则被视为普通 CSS 文件导入
- 若文件没有扩展名，自动添加 `.less` 扩展名并作为 `less` 文件导入
- 若非 `.css` 扩展名，不管扩展名是啥，均作为 `less` 文件导入

# 导入选项

导入可附带导入选项，使用 `()` 引用：`@import (选项1, 选项2, ...) 文件`
- [[#reference]]：作为 Less 文件，但不立即打开文件
- [[#inline]]：导入文件，但不处理他们
- less：无论文件扩展名是什么，都作为 Less 文件导入
- css：无论文件扩展名是什么，都作为 CSS 文件导入
- [[#once]]：默认，仅导入一次文件
- [[#multiple]]：多次导入文件
- optional：当文件无效时，忽略该文件并继续编译

## reference

引入外部样式但不立即展开，当未使用到该文件的内容时（Mixin 和 extend），编译将忽略引入的文件

> [!info]
> -   **[extend](https://less.bootcss.com/features/#extend-feature)**: When a selector is extended, only the new selector is marked as _not referenced_, and it is pulled in at the position of the reference `@import` statement.
> -   **[mixins](https://less.bootcss.com/features/#mixins-feature)**: When a `reference` style is used as an [implicit mixin](https://less.bootcss.com/features/#mixins-feature), its rules are mixed-in, marked "not reference", and appear in the referenced place as normal.

## inline

尽管 Less 兼容绝大多数已知 CSS 标准，仍还有一些不支持的：
- 某些地方的注释
- 所有在不修改 CSS 的情况下的 CSS hacks

此时，可以用 `inline` 只插入 CSS 到一个文件里

## once

若存在多个 `@import` 均导入了一个文件，那该文件只导入一次

## multiple

允许多次导入同一个文件
