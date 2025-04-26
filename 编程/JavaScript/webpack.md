`webpack` 是一个 JS 应用程序静态模块打包工具，每个模块组合成一个或多个 `bundle` 。

> [!note] `webpack`  中每个文件称为一个模块

![[Pasted image 20240807234908.png]]
通常通过 NPM 安装 Webpack

```shell
npm install webpack --save-dev
npm install webpack-cli --save-dev
npm install webpack-dev-server --save-dev
```

*  `webpack` ：打包工具
*  `webpack-cli` ：`webpack`  命令行工具
*  `webpack-dev-server` ：一个支持热更新、接口代理的小型 Node.js Express 服务器

```cardlink
url: https://webpack.js.org/
title: "webpack"
description: "webpack is a module bundler. Its main purpose is to bundle JavaScript files for usage in a browser, yet it is also capable of transforming, bundling, or packaging just about any resource or asset."
host: webpack.js.org
favicon: https://webpack.js.org/favicon.a3dd58d3142f7566.ico
image: https://webpack.js.org/icon-pwa-512x512.934507c816afbcdb.png
```
# 基本配置

 `webpack`  的基本配置文件为项目根目录下的 `webpack.config.js`  文件。`webpack`  根据该文件生成 `bundle.js`  并在 HTML 中引用。

`````col
````col-md

|属性|说明|
| ------| --------------|
| `mode` |模式|
| `entry` |入口文件配置|
| `output` |输出文件配置|
| `module` |模块配置|
| `plugins` |插件配置|
| `devServer` | `webpack-dev-server`  配置|

````
````col-md
```javascript
module.exports = {
    mode: 'development',
    entry: './src/index.js',
    output: { ... },
    module: { ... },
    plugins: { ... },
    devServer: { ... }
}
```
````
`````
## mode

 `webpack`  模式定义了 `webpack`  的执行环境，也可以通过命令参数配置

```shell
webpack --mode=production
```

* 开发环境 `development` 
* 生产环境 `production` 
* 仅打包 `none` 
## entry

应用程序入口。`webpack`  从该文件开始查找所有依赖的模块和库。
* 单起点入口：`entry`  接收 `string`  或其数组
* 多起点入口：`entry`  接收一个对象 `{ <name>: string|string[] }` 
## output

 `webpack`  创建的输出文件位置和命名规则，默认为 `./dist` 

所有路径中，多起点入口的应用程序路径中可以使用 `[name]`  作为占位符，编译时替换为 `entry`  配置中的键名 `<name>` 

| 属性               | 类型           | 说明                                                        |
| ---------------- | ------------ | --------------------------------------------------------- |
|  `path`        |  `string`  |  `webpack`  所有文件输出路径，绝对路径                               |
|  `publicPath`  |  `string`  | 公共路径，静态资源和其他插件使用相对路径的基础路径                                 |
|  `filename`    |  `string`  | 输出的 `js`  文件，默认 `bundle.js` ，多起点常为 `[name]-bundle.js`  |
## 单起点入口打包实例

1. 创建依赖资源 `src/index.js`，后面调用文件中的方法

```reference title:src/index.js fold
file: "@/_resources/codes/node.js/webpack/spa/src/index.js"
```

2. 创建入口文件 `src/main.js` ，调用 `index.js`  的方法

```reference title:src/main.js fold
file: "@/_resources/codes/node.js/webpack/spa/src/main.js"
```

3. 创建 `webpack.config.js`  作为 `webpack`  配置

```embed-js
PATH: "vault://_resources/codes/node.js/webpack/spa/webpack.config.js"
LINES: 1-11,16
TITLE: "webpack.config.js"
```

4. 打包

在 `package.json` 中创建 `build` 用于打包，即运行 `npm run build` 时运行 `webpack`*

```embed-js
PATH: "vault://_resources/codes/node.js/webpack/spa/package.json"
LINES: 1-3,5,11
TITLE: "package.json"
```

打包生成 `dist/bundle.js` 文件

```console
$ npm run build

> build
> webpack

asset bundle.js 4.26 KiB [emitted] (name: main)
runtime modules 670 bytes 3 modules
cacheable modules 279 bytes
  ./src/main.js 48 bytes [built] [code generated]
  ./src/index.js 231 bytes [built] [code generated]
webpack 5.89.0 compiled successfully in 88 ms
```
### 使用  `webpack-dev-server`

在 `package.json` 和 `webpack.config.js` 中配置 ` webpack-dev-server `

```embed-js
PATH: "vault://_resources/codes/node.js/webpack/spa/package.json"
LINES: 1-2,4-5,11
TITLE: "package.json"
```

`webpack-dev-server` 可用参数有：
*  `--open <cmd>` ：服务器运行后执行后面的程序
*  `--host <url> --port <port>` ：地址和接口
*  `--config <config>` ：指向 `webpack-dev-server` 配置文件

```embed-js
PATH: "vault://_resources/codes/node.js/webpack/spa/webpack.config.js"
LINES: 2,12-16
TITLE: "webpack.config.js"
```

运行 `npm run dev`

```console
$ npm run dev

> dev
> webpack-dev-server --config webpack.config.js

<i> [webpack-dev-server] Project is running at:
<i> [webpack-dev-server] Loopback: http://localhost:8080/
<i> [webpack-dev-server] On Your Network (IPv4): http://192.168.199.133:8080/
<i> [webpack-dev-server] Content not from webpack is served from '../spa' directory
asset bundle.js 259 KiB [emitted] (name: main)
  ... 模块文件生成进度
webpack 5.93.0 compiled successfully in 5484 ms
```
## 多起点入口打包实例

1. 创建 `src/myindex.js` 和 `src/mymain.js` 两个入口

```reference title:src/myindex.js fold
file: "@/_resources/codes/node.js/webpack/mpa/src/myindex.js"
```

```reference title:src/mymain.js fold
file: "@/_resources/codes/node.js/webpack/mpa/src/myindex.js"
```

2. 创建 `webpack.config.js` 作为 `webpack` 配置

```reference title:webpack.config.js
file: "@/_resources/codes/node.js/webpack/mpa/webpack.config.js"
```

3. 打包运行与[[#单起点入口打包实例|单入口项目]]相同，`dist` 生成 `myindex-bundle.js` 和 `mymain-bundle.js`

```console
$ npm run build

> build
> webpack

asset myindex-bundle.js 1.43 KiB [emitted] (name: myindex)
asset mymain-bundle.js 1.3 KiB [emitted] (name: mymain)
./src/myindex.js 237 bytes [built] [code generated]
./src/mymain.js 120 bytes [built] [code generated]
webpack 5.93.0 compiled successfully in 65 ms
```

4. 创建主页 `index.html`，引入 `dist/bundle.js`即可查看效果

```reference title:index.html
file: "@/_resources/codes/node.js/webpack/mpa/index.html"
```

![[Pasted image 20240808082024.png]]
# 加载器

>[!note] 一般情况下，加载器名称命名方式为 `<type>-loader` 。

`webpack` 默认仅识别 `js`。`css` 等其他模块需要特定加载器，通过 `npm install` 安装

```shell
npm install css-loader --save-dev
npm install style-loader --save-dev
```

加载器有多种使用方法

````tabs
tab: 推荐：webpack 配置文件

```js title:webpack.config.js
module.exports = {
    module: {
        rules: [{
            test: /\.css/
            use: [
                { loader: 'style-loader' },
                { loader: 'css-loader',
                  options: { modules: true } }
            ]
        }]
    }
}
```

tab: import 引用

从右向左串联执行，使用 `!`  分隔源文件和多个加载器，`?`  表示加载器选项
<br/>

```js
import style from 'style-loader!css-loader?modules!./style.css'
```

tab: 使用命令行

例：对 `.jade` 应用 `jade-loader`，对 `.css` 文件应用 `css-loader` 和 `style-loader`
<br/>

```shell
webpack --module-bindjade-loader--module-bind'css=style-loader!css-loader'
```
````
## 常见加载器
### 普通文件
| 加载器                 | 说明                                |
| ------------------- | --------------------------------- |
| `raw-loader`        | 加载 utf-8 文本内容                     |
| `val-loader`        | 代码转换为模块执行，将 `exports` 转为 js  代码   |
| `file-loader`<br /> | 将文件本身输出到文件夹，返回 `URL`              |
| `url-loader`        | 同 `file-loader`，对小文件返回 `data URL` |
### JSON

* `json-loader`
* `json5-loader`
* `cson-loader`

> JSON5：一种 json 的方言，还未成为 ISO 标准

```cardlink
url: https://json5.org/
title: "JSON5 – JSON for Humans"
description: "JSON for Humans"
host: json5.org
```
### 脚本转译

将一种脚本转换成其他脚本或其他版本脚本

| 加载器                         | 原类型          | 目标类型 | 说明                        |
| --------------------------- | ------------ | ---- | ------------------------- |
| `script-loader`             | JS           | JS   | 在全局上下文中执行一次脚本             |
| `babel-loader`              | ES2015+      | ES5  | 使用 `Babel` 转换             |
| `buble-loader`              | ES2015+      | ES5  | 使用 `Buble` 转换             |
| `traceur-loader`<br />      | ES2015+      | ES5  | 使用 `Traceur` 转换           |
| `ts-loader`                 | TypeScript   | JS   | 像 JS 一样加载和使用 TS           |
| `awesome-typescript-loader` | TypeScript   | JS   |                           |
| `coffee-script`             | CoffeeScript | JS   | 像 JS 一样加载和使用 CoffeeScript |
### 模板

| 加载器                     | 模板类型         | 返回类型       |
| ----------------------- | ------------ | ---------- |
| `html-loader`           | 静态 `HTML`    | 字符串        |
| `pug-loader`            | `Pug` 模板     | 函数         |
| `jade-loader`           | `Jade` 模板    | 函数         |
| `react-markdown-loader` | `Markdown`   | `React` 组件 |
| `markdown-loader`       | `Markdown`   | `HTML`     |
| `posthtml-loader`       | `PostHTML`   | `HTML`     |
| `handlebars-loader`     | `Handlebars` | `HTML`     |
| `markup-inline-loader`  | `SVG/MathML` | `HTML`     |
### 样式表

* `css-loader`，`less-loader`，`postcss-loader`，`stylus-loader`
* `sass-loader`：`SASS` 和 `SCSS` 文件
* `style-loader`：将模块样式添加到 `DOM` 中
### 框架

* `vue-loader`：加载 `Vue` 单文件组件
* `polymer-loader`：使用选择预处理器 `preprocessor` 处理，并 `require()` 类似一等模块 `first-class` 的 `Web` 组件
* `angular2-template-loader`：`Angular` 模块
### 功能性加载器

| 加载器              | 作用                 |
| ---------------- | ------------------ |
| `mocha-loader`   | 使用 `mocha` 测试      |
| `coverjs-loader` | 使用 `CoverJS` 覆盖率测试 |
| `eslint-loader`  | 使用 `ESLint` 清理代码   |
| `jshint-loader`  | 使用 `JSHint` 清理代码   |
| `jscs-loader`    | 使用 `JSCS` 检查代码样式   |
## 例：加载 CSS 和图片

1. 安装相关加载器
    * `css-loader`：加载 CSS 文件
    * `style-loader`：将 CSS 样式注入到 HTML 中形成 `<style>` 标签
    * `file-loader`：加载图片
    * `url-loader`：将图片编码成 `base64` 写入页面，减少服务器请求数量

 ```shell
 npm i css-loader style-loader file-loader url-loader --save-dev
 ```

2. 创建资源文件

    * 图片 `images/test.jpg`
    * 样式表  `css/style.css`
    * 入口文件 `src/main.js`

```reference title:css/style.css fold
file: "@/_resources/codes/node.js/webpack/loaders/css/style.css"
```

```reference title:src/main.js fold
file: "@/_resources/codes/node.js/webpack/loaders/src/main.js"
```

3. 创建配置文件 `webpack.config.js`，运行生成

```reference title:webpack.config.js
file: "@/_resources/codes/node.js/webpack/loaders/webpack.config.js"
start: 10
end: 29
```

4. 创建 `index.html`

```reference title:index.html fold
file: "@/_resources/codes/node.js/webpack/loaders/index.html"
```
# 插件

插件 `Plugins`  可实现自定义的 `webpack`  功能，类似 loader，在  `module.rules.use`  中传入对应的 `loader`  对象。

某些插件需要在 `exports.plugins`  内配置插件设置，以 `MiniCssExtractPlugin`  为例：

```js
const MiniCssExtractPlugin = require('mini-css-extract-plugin')

module.exports {
    module: {
        rules: [{
            // 导入插件
            use: [MiniCssExtractPlugin.loader]
        }]
    },
    // 插件配置
    plugins: [
        new MiniCssExtractPlugin({ filename: 'common.css' })
    ]
}
```
## 例：生成 common.css

利用 `MiniCssExtractPlugin`  插件将各目录下 CSS 文件收集合并成 `common.css` 

1. 安装加载器和插件

```shell
npm install css-loader style-loader mini-css-extract-plugin --save-dev
```

2. 创建各种 CSS 文件

```reference title:src/index.css fold
file: "@/_resources/codes/node.js/webpack/plugins/src/index.css"
```

```reference title:src/style.css fold
file: "@/_resources/codes/node.js/webpack/plugins/src/style.css"
```

3. 创建入口文件 `src/main.js`

```reference title:src/main.js fold
file: "@/_resources/codes/node.js/webpack/plugins/src/main.js"
```

4. 创建配置文件 `webpack.config.js`  并构建生成 `bundle.js` 

```reference title:webpack.config.js fold
file: "@/_resources/codes/node.js/webpack/plugins/webpack.config.js"
```

5. 创建 `index.html` 

```reference title:index.html fold
file: "@/_resources/codes/node.js/webpack/plugins/index.html"
```

![[Pasted image 20240808131501.png]]
# 目录别名

Vue 中 `@` 可以指代项目源码目录，其他目录别名可以通过 `vue.config.js` 设置

```js
const { defineConfig } = require("@vue/cli-service");
const path = require('path')

module.exports = defineConfig({
  configureWebpack: {
    resolve: {
      // 支持自动添加的文件后缀
      extensions: ['.js', '.json', '.vue'],
      // 路径别名
      alias: {
        '@': path.resolve(__dirname, "./src"),
        '@u': path.resolve(__dirname, "./src/utils"),
        '@a': path.resolve(__dirname, "./src/api"),
        '@c': path.resolve(__dirname, "./src/components")
    }
  }
});
```