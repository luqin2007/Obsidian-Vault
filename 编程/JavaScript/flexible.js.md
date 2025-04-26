```HTML
<script src="http://g.tbcdn.cn/mtb/lib-flexible/0.3.4/??flexible_css.js,flexible.js"></script>
```

> [!error] *已过时：使用 vw 单位可完美替代 rem 单位，且大多数浏览器已经支持*

官方文档：[使用Flexible实现手淘H5页面的终端适配 · Issue #17 · amfe/article (github.com)](https://github.com/amfe/article/issues/17)

作用：
- 为 HTML 标签动态添加以下属性：
	- font-size：HTML 根标签的 font-size，等价于 1rem 单位长度，大小为屏幕宽度的 1/10
		- 不拆分成 100 份（vw）：部分浏览器不支持 12px 以下的字体大小
	- data-dpr：设备像素比，`dpx=物理像素/设备独立像素（逻辑分辨率）`
		- 可通过 `<meta name="flexible" content="initial-dpr=n"/>` 手动将 dpr 设置为 n
		- 默认下，仅 iPhone 有 dpr其他设备都是 1
		- 可在 JavaScript 中使用 `window.devicePixelRatio` 获取
		- 可用于媒体查询（仅用于 `webkit` 内核）
			- `-webkit-device-pixel-ratio`
			- `-webkit-max-device-pixel-ratio`
			- `-webkit-min-device-pixel-ratio`
- 自动添加 meta：`<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">`
	- 兼容 Retina 屏：scale=0.5
# 参考

```cardlink
url: https://github.com/amfe/lib-flexible
title: "GitHub - amfe/lib-flexible: 可伸缩布局方案"
description: "可伸缩布局方案. Contribute to amfe/lib-flexible development by creating an account on GitHub."
host: github.com
favicon: https://github.githubassets.com/favicons/favicon.svg
image: https://opengraph.githubassets.com/5f9dd38aa7a52747584214623e8fe85451e0ece11958c1ecf0fbcd61672e1a3d/amfe/lib-flexible
```
