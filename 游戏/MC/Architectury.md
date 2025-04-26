# 基本结构

从 [architectury/architectury-templates: Downloads to template mods to set-up architectury mods. (github.com)](https://github.com/architectury/architectury-templates) 下载模板，对应平台代码放置在不同模块中：

```dirtree
- common: 平台无关代码
  - <Mod主类名>.java: 平台无关的公共代码
  - <Mod主类名>Platform.java: 平台相关的公共代码
- forge: Forge 平台代码
- fabric: Fabric 平台代码
- neoforge: Neoforge 平台代码
```

`common` 模块中，使用 `@ExpectPlatform` 注解标记的 `static` 方法 Architectury 会自动查找具体平台实现。

对应平台目录中，至少需要包含以下内容：

* 各自平台的 Mod 主类，按各自平台的要求写（forge：`@Mod` 注解，fabric：`ModInitializer` 接口）
* `common` 模块中的具体实现。使用 `@ExpectPlatform` 注解标记的方法的具体实现在 `包名.平台名.类名Impl` 类中的同名函数，必须使用 `public static` 修饰

````tabs
tab: common 包

```java
// net.examplemod.ExampleExpectPlatform
public class ExampleExpectPlatform {
    @ExpectPlatform
    public static Path getConfigDirectory() {
        // Just throw an error
        throw new AssertionError();
    }
}
```

tab: forge 包

```java
// net.examplemod.forge.ExampleExpectPlatformImpl
public class ExampleExpectPlatformImpl {
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
```

tab: fabric 包

```java
// net.examplemod.fabric.ExampleExpectPlatformImpl
public class ExampleExpectPlatformImpl {
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
```
````
