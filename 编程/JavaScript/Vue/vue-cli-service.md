Vue 自带的一个服务器。对于一个 Vue 项目，使用 `npm run serve` 即可启动，用于调试。

# 模式

一般情况下，Vue 内置 `development`、`production`、`test` 模式
- `development`：开发环境
	- `vue-cli-service serve`
- `production`：线上环境，使用正式域名访问正式环境
	- `vue-cli-service build`
	- `vue-cli-service test:e2e`
- `test`：测试环境，也是线上环境，使用 IP 或域名访问测试项目
	- `vue-cli-service test:unit`

使用 `--mode` 可以指定自定义模式

```shell
# 自定义的 demo 模式
vue-cli-service serve --mode demo
```

# 环境变量

在不同模式下可以使用各自不同的环境变量，位于 `.env.<模式名>` 文件中

```properties
VUE_APP_API = "/devApi"
VUE_API_TARGET = "http://v3.web-jshtml.cn/api"
```

在代码中使用 `process.env` 访问，如 `process.env.VUE_APP_API`

# 接口代理

用于接口联调，修改 `vue.config.js` 实现代理和跨域

```js
module.exports = defineConfig({
  // ...
  devServer: {
    // 允许服务器被外界访问
    // host: "0.0.0.0",
    proxy: {
      // 代码中请求的路径
      "/devApi": {
        // 目标地址
        target: "http://v3.web-jshtml.cn/api",
        // 允许跨域
        changeOrigin: true,
        // 使用 http 而非 https
        secure: false,    
        // 匹配替换字符串
        pathRewrite: {
          "^/devApi": ""
        },
        // 其他可选选项
        // websockets
        ws: false,
      },
    },
  }
});
```

在代码中，以 `/devApi` 为请求前缀

```js
const instance = axios.create({
  // ...
  baseURL: "/devApi",
});
```
