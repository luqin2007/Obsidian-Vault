使用 System.Text.Json 可用于读写 Json 数据
# 直接序列化

```cs title:"将 Json 转换为 C# 对象"
var str = File.ReadAllText(fileName);
var data = JsonSerializer.DeserializeObject<List<Demo>>(str);
```

```cs title:"将 C# 对象转化为 Json"
var data = ...; # List<Demo>
var str = JsonSerializer.Serialize(DemoEntity);
```
## 序列化选项

在 `Serialize` 和 `Deserialize` 相关方法中可以传入一个 `JsonSerializerOptions` 对象，作为序列化和反序列化的选项

| 属性名                           | 类型                  | 说明                                                           |
| ----------------------------- | ------------------- | ------------------------------------------------------------ |
| `WriteIndented`               | `bool`              | 是否写入缩进以提高 Json 可读性                                           |
| `PropertyNamingPolicy`        | `JsonNamingPolicy`  | Json 键与对象属性名的转换                                              |
| `Encoder`                     | `JavaScriptEncoder` | 编码，使用 `JavaScriptEncoder.Create(UnicodeRanges.All)` 可避免中文被编码 |
| `IgnoreNullValues`            | `bool`              | 忽略 null 值                                                    |
| `PropertyNameCaseInsensitive` | `bool`              | 反序列化时，属性名是否忽略大小写                                             |
| `Converters`                  | `JsonConverter` 列表  | 自定义序列化、反序列化器                                                 |
## JsonConverter

对于某些对象可以使用 `JsonConverter` 自定义序列化、反序列化
