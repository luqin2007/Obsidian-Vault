# Cookie

通过 `request.cookies` 获取 cookie

通过 `response.set_cookie()` 设置 cookie

更推荐使用 Session，更安全
# Session

通过导入库中的 `session` 访问。Flask 的 Session 加密存储于 Cookie 中

```python
from flask import session

# 使用 session 需要先设定一个密钥
# 越长越安全，但加解密越慢，一般 20 字符左右
app.secret_key = '密钥'
# 设置 session 信息
session['key'] = 'value'
# 检查是否存在
v = session.get('key')
if v is None:
    # 数据不存在
    pass
# 清空 session 信息
session.clear()
```
# 全局对象

对每一个会话请求，Flask 都有一个线程上下文的全局变量 g

```python
from flask import session, g

@app.before_request
def on_before_request():
    """
    在请求被处理前从 session 中获取登录信息并保存到线程的全局上下文 
    """
    setattr(g, 'username', session.get('name'))
    setattr(g, 'email', session.get('email'))


@app.context_processor
def on_process_context():
    """
    在处理模板时将上下文的用户信息传递给模板
    """
    return {
        'username': g.username,
        'email': g.email
    }
```
