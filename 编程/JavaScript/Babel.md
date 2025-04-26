一个 JavaScript 转义器，将新版本的 `JavaScript` 代码转译成较低版本 JS 的代码，以保证浏览器的兼容性。

Babel 需要 npm 环境，安装到项目目录下即可，无需安装到全局。

> [!note] 原理：将代码解析成 AST，替换其中 ES6 的内容，最后重新生成 JS 代码。

- 核心模块
	- `babel-core`：提供转义 API，用于转义代码
	- `babylon`：JS 解析器，默认使用 ES2017
	- `babel-traverse`：用于查找 AST 树
	- `babel-generator`：转义最后一步，用于根据 AST 生成代码
- 功能包
	- `babel-types`：用于对 AST 的校验、构建和修改
	- `babel-template`：为 `babel-types` 构建 AST 树提供辅助
	- `babel-code-frames`：生成错误信息，指出错误位置
	- `babel-helpers`：包含一系列 `babel-template` 函数，提供给 `plugin` 使用
	- `babel-plugin-xxx`：转义中使用的插件
	- `babel-preset-xxx`：转义中使用的插件（针对 ES 标准）
	- `babel-polyfill`：用于构建一个完整的 ES6+ 环境
	- `babel-runtime`：类似 `babel-polyfill`，但不会污染全局作用域
- 工具包
	- `babel-cli`：命令行工具
	- `babel-register`：用于转义 `require` 引用的 JS 文件

# 配置

配置内容可以包含在 `package.json` 中，但更常见的是在项目中创建一个名为 `.babelrc` 的文件，之后再向内添加配置内容，配置文件的格式是 `json` 格式

```json
{
"presets": [
  ["env",
  {
    "modules": false,
    "targets": {
      "browsers": ["> 1%", "last 2 versions", "not ie <= 8"]
    }
  }],
  "stage-2"
],

"plugins": ["transform-vue-jsx", "transform-runtime"]
}
```

`presets` 配置当前 ES 版本信息。
- 第一个 `env` 表示使用最新的 ES 标准并向下兼容
- 第二个对象为配置
	- `modules` 表示是否对项目中的依赖应用 Babel
	- `targets` 表示当前项目支持的目标，`browsers` 表示应用于浏览器，其参数由 `browserslist` 插件提供，示例中的属性表示：
		- 用户数量 >1%
		- 兼容最新的两个版本
		- 不兼容 IE 8 及以下的版本
- 第三个是兼容 JS 规范的制作阶段，`stage-2` 代表 `babel-preset-stage-2` 插件
	- stage0：TC 39 的讨论项目
	- stage1：正式化提案
	- stage2：草案已有规范，并以补丁形式向浏览器推送
	- stage3：候选推荐
	- stage4：测试实现，通过后将在下一个 ES 版本中正式发布

`plugin` 则用于向 Babel 中添加插件，其中 `transform-vue-jsx` 用于 Vue.js 支持 JSX 的语法

# 使用

1. 在项目中安装 Babel

```shell
npm install --save-dev babel-cil
```

2. 执行转换

```shell
babel src -d lib
```

以上语句表示将 src 目录的所有 JS 文件转义后保存到 lib 目录中

3. 集成到项目中

```json
// package.json
{
// 忽略其他内容
"script": {
    "build": "babel src -d lib"
}
// 忽略其他内容
}
```

之后就可以直接构建了。

```shell
npm run build
```

