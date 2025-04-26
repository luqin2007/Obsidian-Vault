```cardlink
url: https://cn.vitejs.dev/
title: "Vite"
description: "Next Generation Frontend Tooling"
host: cn.vitejs.dev
favicon: https://cn.vitejs.dev/logo.svg
image: https://vite.dev/og-image.jpg
```

一种新型前端构建工具，提供开箱即用的配置
* 开发服务器：支持热更新的开发服务器
* 构建指令：使用 Rollup 打包

Vite 将应用模块分为依赖和源代码两类，对其采用不同策略
* 依赖：大多为在开发时不会变动的 JavaScript 代码，Vite 使用 Esbuild 预构建依赖
* 源代码：非直接 JavaScript 代码，需要转换（CSS、vue 组件等），Vue 以原生 ESM 提供源代码，且在浏览器请求时按需提供源代码

使用：

1. 安装，可以安装到全局，以后再创建项目不需要重新安装

    ```shell
    npm i vite -g
    ```
2. 初始化项目

    ```shell
    npm init vite <project-name>
    ```
3. 安装依赖

    ```shell
    npm i
    ```
4. 启动开发服务器

    ```shell
    npm run dev
    ```
# Error: listen EACCES: permission denied ::1:8000

> [!error] error when starting dev server:
> Error: listen EACCES: permission denied ::1:8000
>     at Server.setupListenHandle [as _listen2] (node:net:1855:21)
>     at listenInCluster (node:net:1920:12)
>     at GetAddrInfoReqWrap.doListen [as callback] (node:net:2069:7)
>     at GetAddrInfoReqWrap.onlookup [as oncomplete] (node:dns:109:8)

端口被占用。使用 `netstat -ano` 查看发现被 `PID=5152` 的进程占用

![[image-20240603000706-m5m69hk.png]]

经检查发现该进程为 `httpd.exe`，是 `Incredibuild` 自带的一个组件，将其结束并禁止自启即可。

```
  VITE v5.2.12  ready in 311 ms

  ➜  Local:   http://localhost:8000/
  ➜  Network: use --host to expose
  ➜  press h + enter to show help

```
# Pre-transform error: Failed to parse source for import analysis because the content contains invalid JS syntax.

> [!error]
> ```shell
> 12:57:54 [vite] Pre-transform error: Failed to parse source for import analysis because the content contains invalid JS syntax. Install @vitejs/plugin-vue to handle .vue files.
> ```

Vite 正常安装了 `plugin-vue`，但需要配置：

```js
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
})
```
