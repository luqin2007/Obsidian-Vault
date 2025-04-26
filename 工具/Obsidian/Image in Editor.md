- 在编辑模式下预览图片、`iframe` 块、PDF、Excalidraw 块，不需要切换到预览模式
- 支持来自本地和网络的图片，支持 `file:///` 协议和 `app://local/` 协议
- 使用 Markdown 语法调整图片显示效果，替换下面的 `<ALT_TEXT>` 即可

```markdown
![<ALT_TEXT>](image)
[[image|<ALT_TEXT>]]
```

# 图片显示

- `800x600`：调整图片大小为宽 800 px，高 600px，也可以使用百分比
- `800`：调整图片宽为 800px，长宽等比缩放，也可以使用百分比
- `#small`，`#x-small`，`#xx-small`
- `#invert`：反转颜色

# PDF

```markdown
![[PDF文件#page=页数]]
![](PDF文件#page=页数)
```