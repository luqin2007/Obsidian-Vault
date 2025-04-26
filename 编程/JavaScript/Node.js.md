基于 Chrome V8 引擎的运行环境
- 直接在交互式环境中运行 js 命令
- 使用 `node js文件` 执行已存在 js 文件

# 模块化

按一定规则，将大文件拆分成多个独立且相互依赖的小文件

NodeJS 的模块遵循 [[CommonJS]]  的模块化规范；自 NodeJS 13.2 开始，NodeJS 全面支持 ES6 Module 规范可用于替代 CommonJS，只需要在 `package.json` 中添加 `"type": "module"` 即可

# 内置模块

## fs

`const fs = require('fs')`，NodeJS 用于操作文件的模块
- `fs.readFile(path[, options], (err, data) => {})`：读文件
	- `options`：编码等，不指定则以二进制形式输入，返回一个 `Buffer`
	- `err`：`message`, `errno`, `code`, `path` 等
	- `data`：文件内容，是一个 `Buffer` 或 `string`
- `fs.writeFile(path, data[, options = 'utf-8'], (err) => {})`：写文件

带有 `Sync` 后缀的方法表示阻塞执行，没有回调函数参数

注意：路径使用相对路径时，起始路径为 node 命令执行时的目录
- `__dirname`：当前文件所在目录
	- ES Module 环境使用 `import.meta.url`，使用 `url` 模块的 `fileURLToPath` 转换成目录
- `__filename`：当前文件所在地址
	- ES Module 环境使用 `path` 和 `url` 共同获取

```javascript
import {dirname} from 'path'
import {fileURLToPath} from 'url'

const __filename = fileURLToPath(import.meta.url)
const __dirname = dirname(__filename)
```

## path

`const path = require('path')`，用于进行路径处理的模块
- `string path.join(...paths: string)`：路径拼接
- `path.basename(path[, ext])`：获取文件路径中最后一部分
	- 带有 `ext` 扩展名时返回的路径不带扩展名
- `path.extname(path)`：获取文件扩展名

## http

`const http = require('http')`，该模块用于创建 Web 服务器，本地 ip 地址为 `localhost` 或 `127.0.0.1`
- `http.createServer()`：创建服务器，返回一个服务器实例

### 创建服务器

1. 创建服务器对象
2. 绑定 `request` 事件，监听客户端
3. 启动服务器

```javascript
const http = require('http')

let server = http.createServer()
server.on('request', (req, res) => {
    // 响应请求
})

server.listen(8000, () => console.log("Server is running..."))
```

### 响应请求

- req：请求
	- `req.url`，`req.method`
- res：响应
	- 响应头：`res.setHeader(name, value)`
		- 解决中文乱码：`res.setHeader('Content-Type', 'text/html; charset=utf-8')`
	- 向客户端传递数据：`res.end(data)`

# npm

npm 用于查找和导入第三方包：
[npm (npmjs.com)](https://www.npmjs.com/)

## 包结构

npm 管理的项目通常包含以下部分：
- `package.json`：当前项目的包管理配置文件，记录了当前包的信息和依赖的其他包
	- `name`：包名称
	- `version`：版本号
	- `main`：入口文件
- `node_modules`：存放下载的包
- `package-lock.json`：保存下载的包的信息

使用 `npm init -y` 可快速创建一个配置文件，`node_modules` 和 `package-lock.json` 则会由 npm 自动生成和管理

## 管理包

包分为项目包和全局包两种，全局包安装到 `C:\Users\用户名\AppData\Roaming\npm\node_modules` 目录中

当使用 npm 对一个项目进行包管理后，会自动创建：
- `node_modules`
- `package-lock.json`
- `package.json` => `dependencies` 或 `devDependecies` 属性

包管理常用命令有
- 安装：使用 `npm install 包名[@版本]` 或 `npm i 包名[@版本]`，并自动添加到 `dependencies` 节点
	- `npm i` 或 `npm install` 可根据 `package.json` 一次性安装完成项目的所有依赖
	- 安装依赖可以有很多参数
		- `--save-dev`，`-D`：添加到 `devDependencies` 而非 `dependencies` 节点，表示包只会在开发阶段使用
		- `-g`：包作为全局包将安装到全局中，而非当前项目
- 卸载：使用 `npm uninstall 包名` 可直接卸载包
- 发布：先通过 `npm login` 登录，再通过 `npm publish` 发布
	- 撤回：`npm unpublish 包名 --force`，只能撤回 72h 之内的包

## 下载源

可通过以下命令切换下载源

```shell
npm config get registry
npm config set registry=新下载源
# 校验是否切换成功
npm config get registry
```

也可以使用 nrm 快速切换下载源

```shell
# 安装到全局
npm i nrm -g

# 查看所有下载源
nrm ls
# 切换下载源
nrm use 源名称
```

## 加载顺序

- 优先从缓存中加载
- 优先加载内置模块
- 加载 js 文件
	- 不以 `./` 或 `../` 开头，识别为模块名而非 js 文件名
	- 扩展名补全：确切文件名，文件名.js，文件名.json，文件名.node
- 加载文件夹
	- 查找 `package.json` 文件，取其 `main` 属性指定文件
	- 无 `package.json`，尝试加载 `index.js` 文件
- 加载项目
	- 从当前项目的 `node_modules` 文件夹中加载
	- 逐级向上，查找父文件夹的 `node_modules` 文件夹

## NPM 全局工具

### i5ting_toc

该工具可用于将 markdown 文件转换为 HTML

### nodemon

用于在修改 node 项目后自动重启项目

当运行项目时，使用 `nodemon` 而不是 `node` 即可

# 参考

```cardlink
url: https://nodejs.org/en
title: "Node.js — Run JavaScript Everywhere"
description: "Node.js® is a JavaScript runtime built on Chrome's V8 JavaScript engine."
host: nodejs.org
favicon: https://nodejs.org/static/images/favicons/favicon.png
image: https://nodejs.org/zh-cn/next-data/og?title=Node.js%20%E2%80%94%20Run%20JavaScript%20Everywhere&type=announcement
```
