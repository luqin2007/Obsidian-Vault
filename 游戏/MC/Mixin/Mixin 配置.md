Mixin 配置文件位于 `resources/[modid].mixins.json` 中

```json
// resources/[modid].mixins.json
{
  "required": true,
  // 最低 mixin 版本
  "minVersion": "0.8",
  // mixin 类所在包
  // 该包内所有类不应被 mod 直接访问
  "package": "mod.example.mixin",
  // 适用于的 Java 版本
  "compatibilityLevel": "JAVA_17",
  // 同时应用于客户端与服务端的 Mixin 类
  "mixins": [
    // 指 mod.example.mixin.BlockEntityTypeMixin 类
    "BlockEntityTypeMixin"
  ],
  // 仅应用于客户端的 Mixin 类
  "client": [
    // 指 mod.example.mixin.MinecraftMixin 类
    "MinecraftMixin"
  ],
  // 仅应用于服务端的 Mixin 类
  "server": [
    // 指 mod.example.mixin.MinecraftServerMixin 类
    "MinecraftServerMixin"
  ],
  "injectors": {
    "defaultRequire": 1
  }
}

```
