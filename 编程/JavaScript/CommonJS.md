CommonJS 是一个 JavaScript 的模块化规范，其主要内容为
- 每个 JS 文件是一个模块，模块内定义的成员只能被模块本身访问
- 每个模块内有一个 `module` 变量代表当前模块
- 每个模块内有一个 `exports` 变量指向 `module.exports` 属性，该属性用于模块对外共享成员
- 使用 `require(path)` 方法加载模块，`path` 为模块名或 JS 路径，该函数执行整个 JS 文件并返回 `module.exports` 对象
	- 当 `path` 为 js 文件路径时，必须使用 `./` 或 `../` 开头以免混淆
	- 当 `path` 为 js 文件路径时，`.js` 扩展名部分可以省略，会自动搜索 `.js` 和 `.json` 文件

CommonJS 一开始为后端提出，但从 ES6 开始，JavaScript 从语法层面上支持了 Module 功能，可用于取代 CommonJS 成为模块化的通用解决方案
