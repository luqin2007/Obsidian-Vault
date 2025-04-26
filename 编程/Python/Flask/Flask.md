
Python 的 Web 开发微框架

```python
from flask import Flask

app = Flask(__name__)


@app.route('/')
def hello_world():  # put application's code here
    return 'Hello World!'


if __name__ == '__main__':
    app.run()

```

Flask 为入口类，传入的参数：

* `import_name`：可用于 Log 及根据相对路径查找资源

> [!note]  
> 在运行参数中加入 `--host=0.0.0.0` 可以允许除本地外其他设备访问 Flask 服务器  
> 在运行参数中加入 `--port=xxx` 可以将监听端口设置为 xxx（默认 5000）  
> 多个运行参数使用空格分割
>
> ![[Pasted image 20230730085139.png]]

笔记中默认创建的 Flask 对象名为 `app`

# Debug mode

Debug 模式下，允许我们在文件变化并保存后自动重载，不需要重新启动程序即可看到修改后的结果。

在 Pycharm 中，可以直接配置 Debug Mode 开启：

![[Pasted image 20230730084421.png]]

实际上，这是通过环境变量实现的

```shell
# windows
set FLASK_DEBUG=1
# linux
export FLASK_DEBUG=1
```

当 `FLASK_ENV` 环境变量为 `development` 时默认开启调试模式，但 `FLASK_ENV` 默认为 `production`
# 目录

%% Begin Waypoint %%
- [[插件]]
- [[路由]]
- [[模板]]
- [[修饰器]]
- [[Session]]

%% End Waypoint %%