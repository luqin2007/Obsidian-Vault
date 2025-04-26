# 上下文环境

Melvor Mod 上下文可以通过以下几种途径获取：

* 在 `manifest.json` 中定义了 `setup` 函数

```js
export function setup(ctx) {
    // do something
}
```

* 传统 JS 配置

```js
const ctx = mod.getContext(import.meta);
```

* 从 Mod 注册函数中获取

```js
mod.register(ctx => {
    // do something
})
```

* 从生命周期函数获取

```js
onCharacterLoaded(ctx => {
    // do something
})
```

* 获取开发环境上下文，不应在实际 Mod 中使用

```js
const devCtx = mod.getDevContext();
```
# Mod 属性

| 属性        | 类型                | 说明       |
| --------- | ----------------- | -------- |
| name      | string            | Mod 名    |
| namespace | string\|undefined | Mod 命名空间 |
| version   | string            | Mod 版本   |
# 加载资源

**所有资源都应位于 Mod 包根目录**

* `getResourceUrl(path: string): string`
    * 从 Mod 包中查找可用资源
    *  `path` ：相对于 Mod 根目录的路径
    * 返回值：可用于引用该资源的 Url
* `loadModule(path: string): Promise<any>`
    * 从 Mod 包中加载 JS 模块
    *  `path` ：相对于 Mod 根目录的路径
    * 返回值：包含所有模块导出对象的 `Promise` 

````tabs
tab: my_modules.mjs
```js
export function greet(name) {
    console.log(`Hello, ${name}!`);
}
```
tab: setup.mjs
```js
export async function setup(ctx) {
    const myModule = ctx.loadModule('my_modules.mjs');
    myModule.greet('Melvor');
}
```
````

资源加载函数参数为字符串 `path`，返回一个 `Promise` 对象，`path` 为相对于 Mod 根目录的路径

| 函数               | 加载内容                             | 返回类型                      |
| ---------------- | -------------------------------- | ------------------------- |
| `loadScript`     | 加载并运行 JS 脚本                      | `Promise<void>`           |
| `loadTemplates`  | 加载所有 HTML 文件中的 `<template>`  元素 | `Promise<void>`           |
| `loadStylesheet` | 加载 CSS 样式表                       | `Promise<void>`           |
| `loadData`       | 加载 JSON 资源                       | `Promise<any>`，包含 JSON 对象 |
````tabs
tab: my_data.json
```json
{
  "something": "rock"
}
```
tab: setup.mjs
```js
export async function setup(ctx) {
    const myData = await ctx.loadData('my_data.json');
    // { something: "rock" }
    console.log(myData);
}
```
````
# 共享资源

* `share(path: string): void`
    * 将 Mod 的资源共享给其他 Mod 使用；需要 Mod 提供命名空间
    * 其他 Mod 使用时，可通过[[#加载资源]] API 获取，但 `path`  前添加 `命名空间:` 

``````tabs
tab: 资源共享 mod
`````col
````col-md
```json
// manifest.json
{
  "namespace": "helloMelvor"
}
```
````
````col-md
```js
// setup.mjs
export function setup(ctx) {
    // 共享资源给其他 Mod
    ctx.share('my_cool_image.png');
    ctx.share('Greeter.mjs');
}
```
````
`````
tab: 资源使用 mod
```js
// setup.mjs
export async function setup(ctx) {
    // 使用其他 Mod 的共享资源
    const url = ctx.getResourceUrl('helloMelvor:my_cool_image.png');
    const api = await ctx.loadModule('Greeter.mjs');
}
```
``````
# 生命周期

生命周期函数用于将代码注入游戏不同加载阶段，其函数特征为：
* `on生命周期阶段(callback): void`
* `callback: (ctx: ModContext) => void|Promise<void>`

各生命周期函数包括：
* `onModsLoaded`：当所有 Mod 加载完成后调用，此时应在角色（存档）选择界面
* `onCharacterSelectionLoaded`：当角色（存档）选择界面完全加载完成后调用
* `onInterfaceAvailable`：当玩家选择存档后，游戏界面注入之后但未初始化之前
	* 用于在 `onCharacterLoaded`  之前注入自定义技能
* `onCharacterLoaded`：当玩家选择存档后，游戏加载完成，但离线进度没有计算之前
* `onInterfaceReady`：当离线进度计算完成，且游戏界面也加载完成

# 游戏对象
## 方法

以下方法位于 `context.gameData` 对象中
* `addPackage(data: string|GameDataPackage): Promise<void>`
    * 注册一个游戏对象
    *  `data` ：一个游戏对象或包含游戏对象的 `json`  文件路径
* `buildPackage(builder: (pkgBuilder: GameDataPackageBuilder) => void): BuiltGameDataPackage`
    * 通过 `GameDataPackageBuilder`  API 创建对象
    *  `builder` ：用于将单个游戏对象添加到数据包的构建器
## 数据类型

* `BuiltGameDataPackage`：游戏数据包装器
    *  `package` ：属性，`GameDataPackage`  类型
    *  `add(): void` ：该方法用于将对象注册到游戏中

```js
const pkg = ctx.gameData.buildPackage((p) => { ... });
pkg.add();
```

# Mod 设置

**Mod 设置的存储依赖于** **`mod.io`** **，客户端应保证已订阅该 Mod**
## 方法

`section(name: string): Section` 方法位于 `context.settings` 对象中，用于获取一个设置域，若不存在则自动创建。

> [!note]
> 所有设置域都会在 Mod 的设置页面中显示
## 类型

* `Section`：Mod 设置域
    *  `add(config: SettingConfig | SettingConfig[]): void` ：添加一个选项
    *  `get(name: string): any` ：获取一个选项
    *  `set(name: string, value: any): void` ：设置一个值
    *  `type(name: string, config: SettingTypeConfig): void` ：注册一个选项类型，需要 Mod 带有命名空间
* `SettingConfig`：一个设置选项

| 属性       | 类型                                                      | 说明                                                                                                |
| -------- | ------------------------------------------------------- | ------------------------------------------------------------------------------------------------- |
| type     | string                                                  | 设置选项类型，默认提供一些类型，也可以通过 `type`  注册<br>使用其他 Mod 注册的类型时需要 `Mod命名空间:` 前缀                             |
| name     | string                                                  | 选项名，对应 `get`、`set` 方法中 `name` 属性                                                               |
| label    | string\|HTMLElement                                     | 显示标签                                                                                              |
| hint     | string\|HTMLElement                                     | 标签说明                                                                                              |
| default  | any                                                     | 默认值                                                                                               |
| onChange | `(value: any, prevValue: any): void\|boolean\|string` | 当数据发生变化时的回调函数，返回 `false` 表示设置失败，返回 `string` 类型表示设置失败并返回错误原因，该字符串将在 `.validation-message` 元素中渲染 |
自带类型有：

| 类型                 | 属性类型          | 额外属性                                                                                     | 说明    |
| ------------------ | ------------- | ---------------------------------------------------------------------------------------- | ----- |
| `text`           | `string`    | `maxLength`：最大长度                                                                       |       |
| `number`         | `number`    | `min`，`max`：范围                                                                        |       |
| `switch`         | `boolean`   |                                                                                          | 开关    |
| `dropdown`       | `any`       |  `color` ：颜色，与 bootstrap 中忽略 `btn-`  前缀的颜色相同<br>`options`：内容，类型为 `DropdownOption[]` | 下拉菜单  |
| `button`         | `undefined` |  `display: string\|HTMLElement` ：显示样式<br>`color` ：颜色<br>`onClick()` ：响应方法            | 按钮    |
| `checkbox-group` | `any[]`     | `options`：选项，类型为 `CheckboxOption`                                                     | 复选框   |
| `radio-group`    | `any`       | `options`：选项，类型为 `CheckboxOption`                                                     | 单选框   |
| `label`          | `undefined` |                                                                                          | 标签    |
| `custom`         | `any`       | `SettingTypeConfig` 类中所有属性                                                             | 自定义类型 |
````tabs
tab: SettingTypeConfig
一个设置类型，包含数据类型和 UI 展示方式
<br>
```js
{
    // 创建渲染对象
    render(name: string, onChange: () => void, config: SettingConfig) {
        // 返回 HTMLElement 类型对象
        // 若需要的话对象 id 应被设置为 name 属性值
        // 通常将 <input> 的 id 和 name 设置为 name 值
        //     将 <label> 的 for 属性也设置为 name 值
        // onChange 方法在值发生变化时调用，通常注册为 <input> 的 onchange  事件监听
        // 显示错误信息的部分应被添加 class="validation-message"
        return HTMLElement 
    },
    // 从渲染对象中获取值
    get: (root: HTMLElement) => any,
    // 将值设置到渲染对象
    set: (root: HTMLElement, value: any) => void
}
```
tab: DropdownOption
一个下拉菜单
<br>
```js
{
    // 选项返回值
    value: any,
    // 显示内容
    display: string|HTMLElement
}
```
tab: CheckboxOption
一个单选或复选框的选项
<br>
```js
{
    // 选项返回值
    value: any,
    label: string|HTMLElement,
    hint: string|HTMLElement
}
```
````
# 角色数据

**Mod 数据的存储依赖于** **`mod.io`** **，客户端应保证已订阅该 Mod**

角色数据存于 `characterStorage` 对象中，仅当 `onCharacterLoaded` 事件后可访问，每个 Mod 每个角色（存档）数据大小上限为 8k，仅能存储可被 `JSON` 序列化、反序列化的数据。

* `setItem(key: string, data: any): void`：设置数据
* `getItem(key: string): any`：获取数据
* `removeItem(key: string): void`：移除数据
* `clear()`：清空所有数据
# 账户数据

> [!warning]
> **Mod 数据的存储依赖于** ** `mod.io` ** **，客户端应保证已订阅该 Mod**

账户数据存于 `accountStorage` 对象中，仅当 `onCharacterLoaded` 事件后可访问，每个 Mod 每个账户数据大小上限为 8k，仅能存储可被 `JSON` 序列化、反序列化的数据。

* `setItem(key: string, data: any): void`：设置数据
* `getItem(key: string): any`：获取数据
* `removeItem(key: string): void`：移除数据
* `clear()`：清空所有数据
# 游戏对象修改
## 方法

* `patch(className: class, method: string): MethodPatch`
    * 获取一个方法注入点
    *  `className` ：注入类
    *  `method` ：方法名
* `patch(className: class, property: string): PropertyPatch`
    * 获取一个属性注入点
    *  `className` ：注入类
    *  `property` ：属性名（带有 getter/setter 的对象称为属性）

```js
const a = ctx.patch(Skill, 'addXP'); // MethodPatch
const b = ctx.patch(Skill, 'level'); // PropertyPatch
```

* `isPatched(className: class, name: string): boolean`：检查一个方法或属性是否被修改
## 类型

* `MethodPatch`：方法注入入口
    *  `before(hook: (...args: any) => any[]|void): void` 
        * 注入方法调用之前先调用回调
        * 输入参数为注入方法接收的参数
        * 返回值为后面调用注入方法的参数；`undefined`  表示不对参数进行修改
    *  `after(hook: (returnValue: any, ...args: any) => any|void): void` 
        * 注入方法执行完成后调用回调
        * 第一个参数为注入方法的返回值，剩余参数为注入方法的参数
        * 返回值为注入方法的新返回值；`undefined`  表示不对返回值进行修改
    *  `replace(hook: (method, ...args: any) => any): any` 
        * 替换被注入函数
        *  `method` ：原被注入函数
        *  `args` ：参数
* `PropertyPatch`：属性注入入口
    *  `get(getter: (o: () => any) => any): void` ：替代 `getter`  函数
    *  `set(setter: (o: (value: any) => void, value: any) => void): void` ：替代 `setter`  函数
# 扩展 Mod API

* `api(object)`：需要 Mod 带有命名空间。将指定对象导出到全局 `api[命名空间]` 中；多次调用可追加
# 参考

```cardlink
url: https://wiki.melvoridle.com/w/Mod_Creation/Mod_Context_API_Reference
title: "Mod Creation/Mod Context API Reference - Melvor Idle"
host: wiki.melvoridle.com
favicon: https://wiki.melvoridle.com/favicon.ico
```
