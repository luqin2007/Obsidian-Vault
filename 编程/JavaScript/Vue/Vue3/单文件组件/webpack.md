`webpack` 使用 `vue-loader` 加载器加载 `.vue` 单文件组件，所需要的组件有：
* `vue-loader`：用于 `.vue` 文件的加载器
* `vue-template-compiler`：将 Vue 模板预编译成 JS 函数

> [!attention] `Vue`、`vue-template-compiler` 要求版本相同

`Vue3.x` 使用 `@vue/compiler-sfc` 取代了 `vue-template-compiler`，同样需要保持与 `vue` 版本相同（通常最新的都是相同的）

```shell
npm i vue @vue/compiler-sfc --save-dev
```

# 例：`vue-loader` 加载单文件组件

1. 配置 `webpack.config.js`

    ```js
    const path = require('path')
    const { VueLoaderPlugin } = require('vue-loader')

    module.exports = {
        mode: 'development',
        entry: {
            main: './main.js'
        },
        output: {
            path: path.join(__dirname, 'dist'),
            publicPath: './dist/',
            filename: 'bundle.js'
        },
        module: {
            rules: [{
                test: /\.vue$/,
                loader: 'vue-loader'
            }]
        },
        plugins: [ new Plugin() ]
    }
    ```

2. 创建 `app.vue` 组件文件

    ```html
    <template>
        <div>你好，{{ vname }}</div>
    </template>

    <script>
    export default {
        data() {
            return {
                vname: 'Vue'
            }
        }
    }
    </script>
    ```

3. 创建入口文件 `main.js`

    ```js
    import { cerateApp } from 'vue'
    import App from '/app.vue'

    cerateApp(App).mount('#app')
    ```

4. 构建，创建 `html` 文件并使用

    ```html
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Vue-loader</title>
        <style>
            div {
                color: red;
                font-size: 40pt;
            }
        </style>
    </head>
    <body>
        <div id="app">Hello Webpack!</div>
        <script src="/dist/bundle.js"></script>
    </body>
    </html>
    ```

‍
