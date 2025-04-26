Vue CLI 是一个基于 Vue.js 进行快速开发的系统
* `@vue/cli` 实现交互式项目脚手架
* `@vue/cli` + `@vue/cli-service-global` 实现零配置原型开发
* `@vue/cli-service` 可升级依赖，基于 `webpack` 并带有合理配置文件
* 丰富的官方插件平台
* 创建和管理 `Vue.js` 项目用户界面

使用 Vue CLI 初始化项目管理步骤为：

1. 全局安装 `@vue/cli`

    ```shell
    npm i -g @vue/cli
    ```

    升级：

    ```shell
    npm update -g @vue/cli
    ```

2. 打开图形化界面，通过图形化界面引导创建流程

    ```shell
    vue ui
    ```

在创建后的项目中，使用 `npm run serve` 可以启动服务器，通过 `localhost:8080` 即可访问项目网页。
