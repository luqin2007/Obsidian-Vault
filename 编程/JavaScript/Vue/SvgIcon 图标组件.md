# 批量导入 Svg

使用 `require.conetxt` 导入指定目录下的所有 `svg` 图标并注册为组件

```js
const context = require.context(<path>, <subdir>, <regex>);
```

`require.context` 可遍历指定目录文件并自定导入，接收三个参数：
- `<path>`：字符串，待搜索目录
- `<subdir>`：布尔，是否搜索子目录
- `<regex>`：正则表达式，对目录中的文件进行过滤

获取的 `RequireContext` 对象可获取导入的文件
- `keys()`：数组，获取所有加载的文件
- `RequireContext` 本身也是一个函数，输入加载的文件，返回实际加载的路径名

```ts
const context = require.context("./icons", false, /\.svg$/);

const requireAll = (requireContext: any) => {
  console.log("Context Keys", requireContext.keys());
  return requireContext.keys().map(requireContext);
};

const svgIcons = requireAll(context);
console.log("Svg Icons", svgIcons);
```

![[../../../_resources/images/Pasted image 20240921155458.png]]

# 解析 Svg

需要将 `svg` 文件解析为 `<symbol>` 标签才能使用，借助 `svg-sprite-loader` 插件处理

```shell
npm install svg-sprite-loader --D
```

配置 `vue.config.js`：

```js title:vue.config.js
module.exports = defineConfig({
  chainWebpack: (config) => {
    // svg 图标所在目录
    const iconPath = path.resolve("./src/components/svgIcon/icons");
    // 排除默认 svg 加载器加载图标
    config.module
      .rule("svg")
      .exclude.add(iconPath)
      .end();
    // 使用 svg-sprite-loader 加载图标
    config.module
      .rule("svg-icons")
      .test(/\.svg$/)
      .include.add(iconPath)
      .end()
      .use("svg-sprite-loader")
      .loader("svg-sprite-loader")
      // 配置 symbol 元素 id
      // 对应 SvgIcon.svg 中 <use> href 属性值
      .options({ symbolId: "icon-[name]" })
      .end();
  }
});
```

# 使用图标

在页面中使用 `svg` 元素的 `<use>` 标签引用 `<symbol>`

```html
<svg>
  <!-- 加载 home.svg 图标 -->
  <use href="#icon-home"></use>
</svg>
```

最后封装成插件

```html title:SvgIcon.vue fold
<template>
  <svg class="svg-class" :class="svgClassName" aria-hidden="true">
    <!-- 加载 home.svg 图标 -->
    <use :href="svgIconName"></use>
  </svg>
</template>

<script>
import { ref, computed } from 'vue';

export default {
  name: "SvgIcon",
  props: {
    // iconName：图标类型
    iconName: {
      type: String,
      default: "",
    },
    // className：直接绑定到 svg 的样式
    className: {
      type: String,
      default: "",
    },
  },
  setup(props) {
    // 不直接使用 className
    // 防止父对象变化，class 未变时，图标重渲染
    const svgClassName = ref(props.className);
    const svgIconName = computed(() => `#icon-${this.iconName}`);
    return { svgClassName, svgIconName };
  },
}
</script>

<style lang="scss" scoped>
.svg-class {
  width: 1em;
  height: 1px;
  fill: currentColor;
}
</style>

```
