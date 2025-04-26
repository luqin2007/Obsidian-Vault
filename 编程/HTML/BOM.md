浏览器对象模式 BOM，最根本的对象是 window 对象，代表当前打开的浏览器

window 也是当前浏览器的顶层对象（上下文），因此调用其内容可以省略 window 对象，且声明的函数和 var 声明的变量都会存储在 window 中

BOM 一般包含以下内容：
- document：DOM 模型
- navigator：浏览器信息
- location：地址跳转相关
- history：历史记录
- screen：屏幕

# 内置函数

- 间歇函数
	- `id = setInterval(fun, delay)`：每隔 `delay` ms 调用一次 `func`，返回该定时器 id 非 0
	- `clearInterval(id)`：停止一个定时器
- 延时函数，类似 interval，但只会执行一次
	- `id = setTimeout(fun, delay)`
	- `clearTimeout(id)`
- 编解码函数，用于将非英文字符串与 URL 字符串之间的编解码操作
	- `encodeURI(str)`
	- `decodeURI(str)`

# 异步
#html5 

通过 Web Worker 利用多线程，提高了异步任务的能力
- 同步任务：执行栈
- 异步任务：任务队列 + 回调，包括普通事件，资源加载，定时器等，由 js 的宿主环境执行（浏览器，nodejs 等），并在执行完毕后将回调函数插入任务栈

事件循环：每次执行时，先执行同步任务，并将异步任务入队，循环查询任务栈内容

![[Pasted image 20230318154014.png]]

# location

- `href`：保存了当前网站的 URL
	- 跳转页面：修改 href 属性即可，**该方法跳转后不可后退**
- `search`：返回表单查询或提交部分（字符串，`?` 后的部分，包括 ?）
- `hash`：返回 `#` 后的部分，常用于路由，在不跳转页面的情况下显示不容页面，包括 `#`
- `reload([bool])`：刷新，传入 true 时强制刷新，重新下载所有资源，类似 `[CTRL+]F5`

# navigation

包含浏览器和系统平台的相关信息
- `userAgent`：UA，常用于判断平台
![[Pasted image 20230318160313.png]]

- `clipboard`：剪贴板
# history

历史记录管理
- forward ()：前进
- back ()：后退
- go (n)：前进几步，n 为负数时表示后退

# 本地存储
#html5

浏览器为每一个域名提供了大约 5MB 的 `sessionStorage` 和 `localStorage` 的存储空间，用于客户端数据持久化

`localStorage` 可在多窗口页面中共享
- `localStorage.setItem(key, value)`
- `localStorage.getItem(key)`
- `localStorage.remove(key)`
- `localStorage.clear()`

`sessionStorage` 类似 `localStorage`，但浏览器窗口关闭后数据将销毁

注意，存储时候**只能存储字符串或其他基本类型，无法存储对象**，可通过 `JSON.stringify(obj)` 转换成 json 字符串，读取时通过 `JSON.parse(str)` 转换回对象
