向 Less 编译时插入 [[JavaScript]] 插件

```Less
@plugin "插件名"
```

每个插件对应一个 JS 文件，查找的文件包括：
- less-plugin-插件名.js
- 插件名.js
- npm://less-plugin-插件名
- npm://less-plugin-插件名.js
- npm://插件名
- npm://插件名.js
当带有前缀（`.`，`..` 等相对路径）时，只会查找以下文件：
- 插件名.js
- npm://插件名
- npm://插件名.js

# 插件 Hello World

```Javascript
registerPlugin({  
    install: function (less, pluginManager, functions) {  
        functions.add("pi", () => Math.PI);  
    }  
})
```

```Less
@plugin "../js/my-less-plugin";  
  
.test-plugin {  
  value: pi();  
}
```

编译成 CSS 后：

```CSS
.test-plugin {  
  value: 3.141592653589793;  
}
```

当然，除了 `registerPlugin`，还可以使用 `module.exports` 暴露初始化对象

```Javascript
// 该 JS 与前面 registerPlugin 那个作用一致
module.exports = {  
    install: function (less, pluginManager, functions) {  
        functions.add("pi", () => Math.PI);  
    }  
}
```

关键在于那个对象，注册方法两种都行

```Javascript
// 二选一：
registerPlugin({初始化对象})

module.exports = {初始化对象}
```

# 作用域

插件有自己的作用域，其有效域在于所在的大括号，因此可以在不同的域中选择不同的带有同名函数的插件

```Javascript
// my-less-plugin1.js
module.exports = {  
    install: function (less, pluginManager, functions) {  
        functions.add("value", () => 1);  
    }  
}

// my-less-plugin2.js
module.exports = {  
    install: function (less, pluginManager, functions) {  
        functions.add("value", () => 2);  
    }  
}
```

```Less
.test-plugin1 {  
  @plugin "../js/my-less-plugin1";  
  value: value();  
}  
  
.test-plugin2 {  
  @plugin "../js/my-less-plugin2";  
  value: value();  
}
```

编译成 CSS 后：

```CSS
.test-plugin1 {  
  value: 1;  
}  
.test-plugin2 {  
  value: 2;  
}
```

# 参数与返回值

插件是一些函数，当然支持参数，但传入的参数是被包装后的，需要通过 `value` 取得其值

```Javascript
module.exports = {  
    install: function (less, pluginManager, functions) {  
        functions.add("value", (n) => Math.PI * n.value);  
    }  
}
```

```Less
.test-plugin-calc {  
  @plugin "../js/my-less-plugin";  
  value3: value(3);  
  value2: value(2);  
  value1: value(1);  
}
```

编译成 CSS 后：

```CSS
.test-plugin-calc {  
  value3: 9.42477796076938;  
  value2: 6.283185307179586;  
  value1: 3.141592653589793;  
}
```

插件可以返回任何值，但直接返回数值，是无法在 Less 中参与计算的。需要返回一个 `tree.Dimension` 下的对象：

```Javascript
module.exports = {  
    install: function (less, pluginManager, functions) {  
        functions.add("value", () => new tree.Dimension(Math.PI));  
    }  
}
```

```Less
.test-plugin-calc {  
  @plugin "../js/my-less-plugin";  
  
  value: value();  
  value_m3: value() * 3;  
}
```

编译成 CSS 后：

```CSS
.test-plugin-calc {  
  value: 3.14159265;  
  value_m3: 9.42477796;  
}
```

当然，插件也可以没有返回值，此时应当返回 false

```Javascript
module.exports = {  
    install: function (less, pluginManager, functions) {  
        functions.add("func", () => {  
            // do something  
            return false;  
        });  
    }  
}
```

# 初始化对象

初始化对象指的是 `registerPlugin()` 传入的对象或 `module.exports` 的对象。其内容主要包括：

```Javascript
module.exports = {  
    /* 当插件第一次导入时立即调用，仅调用一次 */
    install: function (less, pluginManager, functions) {  
    },  
    
    /* 每次使用 @plugin 引入插件时调用 */
    use: function (context) {  
    },  
    
    /* 每次在规则展开时使用 @plugin 引入插件时调用 */
    eval: function (context) {  
    },  
    
    /* 插件导入时允许传入一些选项，类似于 @import 那种写法，@plugin (args) "plugin"，这里用于处理这些选项 */
    setOptions: function (argumentString) {  
    },  
    
    /* 最低兼容 Less 版本，可用数组表示 */
    minVersion: ['3.0'],  
    
    /* 在使用 lessc 时在控制台展示的参数选项 */
    printUsage: function () {  
    },  
}
```

