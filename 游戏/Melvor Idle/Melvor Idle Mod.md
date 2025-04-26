# 环境构建

一个 Mod 至少含有以下两个文件：

`manifest.json`：Mod 加载入口，至少包含一个 `setup` 或 `load` 属性

```json title:manifest.json
{
    "setup": "setup.mjs"
}
```

| 属性          | 值类型                    | 说明                                                                                                  |
| ----------- | ---------------------- | --------------------------------------------------------------------------------------------------- |
| `namespace` | `string?`              | Mod 命名空间，一些工具需要使用，不能以 `melvor` 开头                                                                  |
| `icon`      | `string?`              | Mod 图标，位于 Mod 根目录，最大 38px 的 png 或 svg 图片                                                            |
| `setup`     | `string?`              | （`setup`、`load`二选一）JS Mod 模块入口，优先级高于 `load`                                                      |
| `load`      | `string?` 或 `string[]` | （`setup`、`load`二选一）按顺序加载资源。不推荐，推荐在 `setup` 中定义的入口里加载。可用资源包括：js（JS文件）, mjs（JS模块）, css, json, html |

`setup.mjs`：前面 `manifest.json` 中 `setup` 属性定义的入口文件，至少导出一个 `setup` 方法

```js title:setup.mjs
export function setup(ctx) {
    // do something
    // Mod 入口
}
```

# Mod 样例

一个简单的双倍经验 Mod，修改 Skill 类达到获取所有双倍

```js title:setup.mjs
export function setup(ctx) {
    ctx.patch(Skill, 'addXP').before(function(amount, masteryAction) {
        return [amount * 2, masteryAction];
    });
}
```

# 设置选项

通过 `ctx.settings` 创建和访问设置菜单

* `ctx.settings.section`：获取或创建设置菜单，通过 `add` 添加选项，通过 `get` 获取选项

```js title:setup.mjs
export function setup(ctx) {
    // 设置菜单
    ctx.settings.section('General').add({
        type: 'number',
        name: 'xp-multiplier',
        label: 'XP Multiplier',
        hint: 'Multiply all XP gains by this amount',
        default: 2
    });

    // Mod 内容
    ctx.patch(Skill, 'addXP').before(function(amount, masteryAction) {
        const xpMultiplier = ctx.settings
                                .section('General')
                                .get('xp-multiplier');
        return [amount * xpMultiplier, masteryAction];
    });
}
```

其中，`add` 方法可以接受一个对象或对象数组，`type` 表示类型，可选有：`Text`，`Number`，`Switch`，`Dropdown`，`Button`，`Checkbox Group`，`Radio Group`，`Label`

# 发布

将 `manifest.json` 和所有 JavaScript 代码打包到一个 `zip` 包根目录，上传到 [Mod](mod.io) 即可。

# 使用资源

## 使用模块

通过 `ctx.loadModule`  导入 JS 模块文件

````tabs
tab: setup.mjs
```js
export async function setup(ctx) {
    const configService = await ctx.loadModule('configService.mjs');
    // do something
}
```
tab: configService.mjs
```js
const ctx = mod.getContext(import.meta);

// do something
```
````
## 使用脚本

通过 `ctx.loadScript` 导入 JS 脚本文件

````tabs
tab: setup.mjs
```js
export async function setup(ctx) {
    await ctx.loadScript('hello-melvor-script.js');
    // do something
}
```
tab: hello-melvor-script.js
```js
mod.register(ctx => {
    // do something
})
```
````

使用 `manifest.json` 的 `load` 属性也能引入 `js` 文件，与 `ctx.loadScript` 作用相同

## 其他资源

`context` 有各种方法可以导入不同资源：

* `loadTemplates`：导入 `html` 文件中的所有 `<template>` 标签
* `loadStylesheet`：导入一个 `css` 文件到当前页面
* `loadData`：导入一个 `json` 对象（返回该对象）
* `getResourceUrl`：导入图片、声音和其他类型资源，返回一个可以引用该资源的 url

# 修改游戏

## 生命周期

游戏允许在不同情况下注入不同回调代码

````tabs
tab: 生命周期函数
|生命周期函数|说明|
| --------------| ----------------------------------------------------|
| `onModsLoaded` |所有 Mod 加载完成|
| `onCharacterSelectionLoaded` |角色选择界面加载完成|
| `onCharacterLoaded` |角色选择完成，所有游戏对象创建，但离线收益还未计算|
| `onInterfaceReady` |离线收益计算完成，游戏内 UI 可以被安全修改|
tab: 示例 setup.mjs
```js
export function setup(ctx) {
    ctx.onModsLoaded(ctx => {
        // do something
    });
}
```
````
## 游戏对象

### 通过 json 添加游戏对象

普通游戏对象可以通过 json 定义

优点
* 方便添加
* 易于分离游戏逻辑与数据
* 通过 Json Schema 允许某些编辑器支持代码提示  
  缺点
* 有些游戏对象无法通过该方法创建

```json
{
  "$schema": "https://melvoridle.com/assets/schema/gameData.json",
  "data": {
    // ...
  }
}
```

将定义好的 json 文件放入项目中，在游戏中加载即可

* 通过 `load` 属性加载

```json title:manifest.json
{
  "namespace": "mod 命名空间",
  "load": [
    "物品 json 文件.json"
    // ...
  ],
  // ...
}
```

* 通过 `setup` 代码加载

```js title:setup.mjs
export async function setup(ctx) {
    await ctx.gameData.addPackage('物品 json 文件');
}
```

### 运行时定义

通过 `ctx.gameData.buildPackage`  创建

* 优点
  * 可以注册任意类型游戏对象
  * 允许动态创建对象
* 缺点
  * 更复杂
  * 没有代码提示

```js title:setup.mjs
export function setup(ctx) {
  ctx.gameData.buildPackage((package) => {
    package.items.add({ ... })
  }).add();
}
```

## 自定义侧边栏

使用全局变量 `sidebar` 设置侧边栏，侧边栏结层级按从外向内依此为：

`Sidebar` -> `Categories` -> `Items` -> `Subitems`

* `category(name)`，`item(name)`，`subitem(name)` 方法可以在对应层级获取或创建一个新项目
* `remove()` 方法可以删除该项目
* `category(name, ops)`，`item(name, ops)`，`subitem(name, ops)` 可以创建或修改对应项目
* `categories()`，`items()`，`subitems()` 可以获取该层级中所有子项目

## 自定义组件

可以创建自定义组件添加到游戏中

* 创建一个 HTML 模板文件，其中根标签使用 `<template>`，变量使用 `{{ 变量名 }}` 代替

```html title:template.html
<template>
  <span class="text-light">{{ count }}</span>
  <button class="btn btn-secondary" @click="inc">+</button>
</template>
```

* 在 `manifest.json` 或 `setup()` 中加载（二选一）

````tabs
tab: manifest.json
```json
{
  "load": ["template.html"]
}
```
tab: setup.mjs
```js
export async function setup(ctx) {
  ctx.loadTemplates('template.html')
}
```
````

* 定义控件并创建 UI 对象

```js
function Counter(props) {
  return {
    $template: "#counter-component",
    count: props.count,
    inc() {
      this.count++;
    }
  };
}

ui.create(Counter({ count: 0 }), document.getElementById('woodcutting-container'));
```
## 游戏数据

* 角色数据存储在 `ctx.charactterStorage` 中
* 账号数据存储在 `ctx.accountStorage` 中

> [!warning]
> * 数据存储大小限制为 8k
 >   * 每个角色数据每个 Mod 可存储空间为 8k
 >   * 每个账号数据总可存储空间为 8k
> * 可存储类型为可以被 Json 序列化的类型（仅限 JS 数据对象等，方法等无法保存）

### 数据监听

游戏数据对象变化可以通过 `patch(object, functionName)` 方法进行监听和修改。

* 在数据变化之前或之后执行操作
* 获取和修改变动值

可监听对象包括 `Player`，`Enemy`，`CombatManager`，`Woodcutting`，`Skill` 等。支持注入的方法及其参数（一个回调函数）信息如下：

* `before(callback)`
    * 触发：被注入方法调用前触发
    * 参数：被注入方法传入的参数
    * 返回：一个数组，该数组将作为参数传入被注入参数

```js title:setup.mjs
export function setup(ctx) {
    // 在获取经验之前对原始经验值进行处理，达到双倍经验
    ctx.patch(Skill, 'addXP').before((amount, masterAction) => {
        return [amount * 2, masterAction];
    });
}
```

* `after(callback)`
    * 触发：被注入方法调用后触发
    * 参数：可选，被注入方法的返回值
    * 返回：可选，该返回值将替代原返回值作为新的返回值

```js title:setup.mjs
export function setup(ctx) {
    // 当 rollToHit 函数返回值为 false 时打印 log，
    // 并将的返回值强行修改为 true，达成 100% 命中
    ctx.patch(Player, 'rollToHit').after((willHit) => {
        if (!willHit)
            console.log('A miss? I think not!');
        return true;
    });
}
```

* `replace(callback)`
    * 触发：调用被注入方法调用时，替换被注入方法（但仍会受 `before` ，`after`  注入的回调影响）
    * 参数：第一个参数是原始函数，剩下的是实际传入的参数
    * 返回：实际需要的返回值

```js title:setup.mjs
export function setup(ctx) {
    ctx.patch(Skill, 'addXP').replace(function(o, amount, masterAction) {
        // prevent any woodcutting XP
        if (this.id == 'melvorD:Woodcutting') return;
        // double any mining XP
        if (this.id == 'melvorD:Mining')
            return o(amount * 2, masterAction);
        // grant all other XP as normal
        return o(amount, masterAction);
    });
}
```

# API 依赖

API 之间可以互相依赖，一个 Mod 可以给其他 Mod 提供 API 方法。

向其他 Mod 提供 API 需要为 Mod 设置一个 `namespace`

```json title:manifest.json
{
  "namespace": "Mod命名空间"
}
```

然后，通过 `ctx.api(object)` 公开 API；多次调用 `ctx.api` 可以不断追加 API

```js title:setup.mjs
export function setup(ctx) {
    ctx.api({ ... });
}
```

如果需要调用其他 Mod 的 API，也可以通过 `ctx.api` 获取

```js title:setup.mjs
export function setup(ctx) {
    // 另一个 mod 提供的 api
    const api1 = ctx.api.提供API的Mod的命名空间;
    const api2 = ctx.api['提供API的Mod的命名空间'];
}
```

# 参考

```cardlink
url: https://wiki.melvoridle.com/w/In-game_Functions
title: "In-game Functions - Melvor Idle"
host: wiki.melvoridle.com
favicon: https://wiki.melvoridle.com/favicon.ico
```
