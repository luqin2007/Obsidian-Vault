ES6 Module 语法在语言标准层面上支持了模块功能
- 静态加载：编译阶段按需加载
- 默认使用严格模式

# export

`export` 关键字用于规定模块的对外接口，除 `export` 修饰的变量外，其他成员都无法在模块外（文件外）访问到。`export` 有以下两种使用方法：

- 直接修饰变量，函数或类
```javascript
export let sharedVariable = 123
export function add(x, y) {
	return x + y
}
export class AClass {}
```

- 批量导出现有变量和函数
```javascript
let a = 10;
let b = "xxx";
function add(x, y) {
	return x + y;
}
function sub(x, y) {
	return x - y;
}

export {
	a, add,                // 直接以原名导出
	b as name, sub as fsub // 以别名导出
}
```

# import

使用 `import` 加载其他模块中 `export` 的成员

```javascript
import { ... /* 成员名 */ } from '模块名或 js 文件'
import { 成员名 as 别名 } from '模块名或 js 文件'
import '模块名或 js 文件' // 只执行所加载脚本，不导入
import * from '模块名或 js 文件' // 导入所有 export 的成员
```

- `import` 导入的变量都是只读的，不允许在加载模块的脚本中改写接口
- `import` 后如果跟模块名，则必须有对应配置文件，用于查找 `js` 文件
- `import` 命令具有提升效果，所有 `import` 的语句都会提升到文档开头
- `import` 是静态执行的，因此不能使用表达式和变量，只能用字符串字面量
- 对于相同的两条 `import` 语句，只会执行一次

# 其他方法

- 默认接口

```javascript
let x = 10
export default x 
```

`exprot default` 暴露的变量只能有一个，该导出的变量可以用任意变量接收，用于接收的变量不需要 `{}` 包围，如：

```javascript
import y from '模块名'
```

**注意：每个模块中只能 `export default` 一次**

- 转发：将 `import` 和 `export` 合并

```javascript
export { foo, bar } from 'other_module'
```

以上写法等同于：

```javascript
import { foo, bar } from 'other_module'
export { foo, bar }
```

转发同样支持默认接口和 `*`

```javascript
export { default } from `foo`
export { es6 as default } from `bar`
export * as otherIdentifier from 'emmmm' // ES2020 后支持
```

# 动态加载
#es2020

使用 `import` 函数引入模块并返回 `Promise`，类似 NodeJS 的异步 `require` 方法

```javascript
import('模块名或 js 文件')
	.then (module => { ... })
	.catch (err => { ... })
```

