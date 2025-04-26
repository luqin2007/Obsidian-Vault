高度包容、快速而极简的 Node.js Web 框架，封装了 `http` 模块

# 路由

使用路由可快速匹配请求 URL 和处理函数：`app.MATHOD(PATH, FUNC)`，通过 `listen` 方法开启服务器

```javascript
app.get('/', (req, res) => { ... })
app.post('/', (req, res) => { ... })

app.listen(8000, () => {})
```

- `req.query` 对象可获取客户端发送的查询参数（`?xxx=xxx` 部分），不存在查询参数则为 `{}`
- `res` 使用 `send` 方法向客户端发送数据
- 通过 `:` 的形式匹配动态参数，如 `app.get('/user/:id/:name', ...)`，在 `req.params` 中获取动态参数

路由的匹配过程为按定义顺序自上而下进行匹配

## 模块化路由

为方便管理，Express 不建议直接将路由挂载到 app 中，而是推荐将路由单独创建为路由模块
1. 创建模块对应 js 文件
	1. 使用 `express.Router()` 创建路由对象
	2. 向路由对象挂载具体路由
	3. 使用 `module.exports` 共享路由对象
2. 使用 `app.use()` 注册路由
	1. 使用 `app.use(route)` 挂载路由
	2. 使用 `app.use('/xxx', route)` 为路由增加统一访问前缀

```javascript
// router.js (ES Module) 模块化路由实现
import {Router} from 'express'

const router = Router()

router.get('/xxx', (req, res) => { ... })
router.post('/xxx', (req, res) => { ... })

export {router}
```

```javascript
// app.js (ES Module) 服务器实现
import express from 'express'
import {router} from './router.js'

const app = express()
app.use(router)
app.listen(8000, () => { ... })
```

## 静态资源

使用 `express.static(path)` 可创建静态目录的路由

```javascript
import express from 'express'

const app = express()

app.use(express.static('/web')) // 直接取 web 目录下的对应路径作为输出资源
app.use('/res', express.static('/resources'))
```

# 中间件

中间件 Middleware，指业务流程的中间处理环节，用于对请求的预处理

![[Pasted image 20230425004106.png]]

Express 中间件本质是一个函数，函数形参列表中必须包含一个 `next` 变量（函数），并在处理后调用 `next()` 函数，表示把流传关系转交给下一个环节（中间件或路由）

```javascript
function mw1(req, res, next) {
	...
	next()
}

function mw2(req, res, next) {
	...
	next()
}

// 可直接按顺序填入中间件
app.get('/xxx', mw1, mw2, (req. res) => {
	// ...
})

// 或将中间件打包成一个数组
app.post('/xxx', [mw1, mw2], (req. res) => {
	// ...
})

// 定义全局中间件
app.use(function(req, res, next) { ... })
```

- 一定要在路由之前注册中间价
- 一个请求可通过多个中间件处理，多个中间件和路由共享 `req`，`res` 对象
- 中间件一定要调用 `next()`；调用 `next()` 后不要再进行其他代码操作

## 应用级别中间件

通过 `app.use`，`app.get`，`app.post` 等方法绑定到 `app` 实例上的中间件

## 路由级别中间件

通过 `route.use`，`route.get`，`route.post` 等方法绑定到路由上的中间件

## 错误级别中间件

专门用于捕获项目中发生的异常错误，防止项目因异常而崩溃的中间件，其处理函数参数列表为：

```javascript
function errMiddleware(err, req, res, next) {
    // ...
    next()
}
```

**错误级别中间件必须注册在路由之后**

## Express 内置中间件

- `express.static()`：快速托管静态资源
- `express.json()`：解析 json 请求体数据
	- 如果不添加，对于 json 类型的请求体，`req.data` 为 `undefined`
- `express.urlencoded()`：解析 url-encorded 请求体数据
	- 如果不添加，对于 url-encorded 类型的请求体，`req.data` 为 `undefined`

## 第三方中间件

- body-parser：用于解析表单数据
- cors：允许服务器跨域 Ajax 请求响应解除浏览器跨域访问限制
	- 若服务器同时使用 CORS 和 JSONP，应当先注册 JSONP 处理接口后注册 CORS 插件，以防冲突
- express-jwt：用于全局，允许服务器使用 JWT：[[JWT 相关#express-jwt]]
- express-session：用于全局，允许服务器使用 Session，为 `req` 增加 `session` 对象

```javascript
import session from 'express-session'

app.use(session({
	secret: "任意字符串",
	resave: false,
	saveUninitialized: true
}))
```

之后就可以在 `req` 上操作 `session` 了

```javascript
app.post('/login', (req, res) => {
    // 身份校验
    let pass = false
    // ...

	if (pass) {
		// 保存 Session 信息
		req.session.user = req.body
		req.session.islogin = true
	}
	res.send({ ... })
})

app.get('/username', (req, res) => {
	// 读取 Session 信息
	if (req.session.islogin) {
		return res.send({ ... , username = req.session.user })
	}
})

app.get('/logout', (req, res) => {
	// 销毁 Session 信息
	req.session.destory()
	res.send({ ... })
})
```

## 自定义中间件

data 事件：客户端发送给服务端的数据。当数据较大无法一次性发送完毕时，会触发多次事件

```javascript
let str = '';
req.on('data', chunk => str = str + chunk);
```

end 事件：当客户端数据发送完毕时触发

```javascript
req.on('end', () => req.body = str)
```

汇总：

```javascript
function mw1(req, res, next) {
	let str = ''
	req.on('data', chunk => str = str + chunk)
	req.on('end', () => req.body = str)
	next()
}
```

- 若字符串格式符合查询字符串格式，可使用 NodeJS 的 `querystring` 模块的 `parse` 函数解析

# 参考

```cardlink
url: http://expressjs.com/zh-cn/
title: "Express - Node.js Web 应用程序框架"
host: expressjs.com
favicon: https://expressjs.com/images/favicon.png
image: https://expressjs.com/images/og.png
```
