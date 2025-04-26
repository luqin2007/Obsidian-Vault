# jsonwebtoken

将 JSON 对象转化成 JWT 字符串

```javascript
import jwt from 'jsonwebtoken'

// 1. 定义 Secret 密钥
const secretKey = '...'

// 2. 生成 Token
let data = {
	user: 'username',
	...
}

let config = {
	expiresIn: '30min' // Token 有效期
}

let token = jwt.sign(data, secretKey, config)
```

# express-jwt

将 JWT 字符串还原成 JSON 对象

该插件为 express 中间件，应用后会将解析后的对象添加到 `req.user` 中

```javascript
import expressJWT from 'express-jwt'

let mw = expressJWT({secret: secretKey}) // Secret 密钥
			.unless({path: [/^\/api\//]}) // 指定不需要访问权限的接口，此处为以 /api/ 开头的路径
app.use(mw)
```

Token 解析失败的异常可以用 Express 错误中间件捕获，其特征为 `err.name === "UnauthorizedError"`
