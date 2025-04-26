>[!note] NPM：Node Package Manager，Node.js 的默认包管理器
# NPM 状态

* `npm -v`：显示 NPM 版本，用于检查 NPM 是否正常安装
* `npm help`：查看帮助
    *  `npm help json` ：访问 NPM 的 `json`  文件，会打开一个网页
# 包管理

|命令|简写|参数|说明|
| ---------------------------------| ---------------------------------| -----------------| --------------------------------------------------------------------|
|`npm install`<br />|`npm i`<br />||根据项目 `package.json` 记录的信息安装项目依赖模块|
|||`<包名>...`|安装一个或多个包|
|||`-g`|将模块安装到全局|
|||`--save`|将模块安装到当前项目并写入 `package.json` 的 `dependencies` 配置中|
|||`--save-dev`|将模块安装到当前项目并写入 `package.json` 的 `devDependencies` 配置中，该包仅在开发环境下存在|
|||`--registry=...`|使用镜像，如淘宝为 `https://registry.npm.taobao.org`|
|`npm -ls`<br />|||查看当前项目安装的包|
|||`-g`|查看全局安装的包|
|||`<包名>`|查看依赖包版本号|
|`npm view <包名>`<br />|||查看包的 `package.json`|
|||`dependencies`|查看包的依赖关系|
|||`repository.url`|查看包源文件地址|
|||`engines`|查看包依赖的 Node.js 版本|
|`npm outdated`|||查看所有已过时的包|
|||`-g --depth=0`|查看所有需要更新的包|
|`npm update`<br />||`<包名>`|更新特定软件包|
|||`-g`|更新全局软件包|
|`npm uninstall`<br />||`<包名>`|卸载指定软件包|
|||`-g <包名>`|卸载全局软件包|
|`npm search`||`<包名>`|查找包|
|`npm unpublish`||`<包名> <版本>`|撤销自己发布的某版本包代码|
# 项目管理

* `npm init`：生成项目文件 `package.json`，该文件用来描述项目信息和用到的模块
* `npm cache clear`：清空 NPM 缓存
    *  `npm cache clean -f` ：清除缓存
* `npm rebuild <包名>`：重建包内容
‍
```cardlink
url: https://www.npmjs.com/
title: "npm | Home"
host: www.npmjs.com
favicon: https://static-production.npmjs.com/b0f1a8318363185cc2ea6a40ac23eeb2.png
image: https://static-production.npmjs.com/338e4905a2684ca96e08c7780fc68412.png
```

