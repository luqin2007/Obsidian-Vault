DFU 库的序列化、反序列化指的是从普通 Java 数据对象与一类特定 Java 输出对象之间的互相转化。

给定 Java 输出对象类型是一类便于与纯文本互相转化的类型，是序列化的目标类型，在 Minecraft 游戏中体现为 NBT 数据和 Gson JsonElement。这类对象通常有以下特点：

* 所有数据都被一层特定类型包装，该类在 DFU 中表示为只读
* 基本类型只有字符串、数字、字符、布尔值和空值等
* 复合数据类型只有列表（数组）或记录（Map）存在
* 列表的元素类型和记录的键值对类型都是（或都可以转换为）之前提到的包装类型
* 记录的键可被列举出来，通常都是字符串

以 Gson JsonElement 为例：

* 所有数据都实现 `JsonElement` 类
* 基本数据类型为 `JsonPrimitive`，可接受数字，字符串，布尔值，字符四种类型
* 存在 `JsonNull` 类型表示 null 值
* 复合数据类型为 `JsonObject` 和 `JsonArray` 分别表示记录和列表，列表的键为字符串

另外，还要实现序列化类与普通 Java 数据对象之间的互操作性。DFU 本身是一个函数式的数据操作库，可通过 `map`、`flatMap`、`group` 等操作实现。为了与对象构造相连接，DFU 还包括了最高 16 个参数的 Function 接口。

# 核心接口

序列化、反序列化的核心接口主要包括以下几个：

* 序列化接口：从普通 Java 数据对象转换到序列化目标类型对象

  * `Encoder<A>`：将 Java 数据对象 A 转换为目标类型对象
  * `MapEncoder<A>`：将 Java 对象 A 转换为一条目标类型对象的记录
* 反序列化接口：从序列化目标类型对象还原回 Java 数据对象

  * `Decoder<A>`：将序列化目标类型对象转换为 Java 数据对象 A
  * `MapDecoder<A>`：将序列化目标类型记录对象转换为 Java 数据对象 A
* 组合接口：组合序列化接口与反序列化接口

  * `Codec`：组合 `Encoder` 与 `Decoder`，具体实现在 `serialization.codecs` 包中并在 `Codec` 中留有快捷方式

    * `RecordCodecBuilder`：这是最常用的 Codec，详见 [[Codec#创建 Codec]]
  * `MapCodec`：组合 `MapEncoder` 与 `MapDecoder`
* 操作类：针对序列化目标对象的操作类

  * `DynamicOps<T>`：序列化目标类 T 的操作类，包括以下功能：

    * 创建空对象、数字、字符串、布尔等对象
    * 创建空列表、空记录
    * 从目标对象 T 中读字符串、数字、布尔等数据
    * 操作列表和记录，包括连接、读写、与流的互相转换等
  * `Dynamic<T>`：一个组合了目标类型 T 与操作类 `DynamicOps<T>` 的组合

    * `OptionalDynamic<T>`：可空的 Dynamic 对象
* 工具类

  * `Keyable`：表示一个可列出所有键的对象，`MapEncoder<A>` 与 `MapDecoder<A>` 实现此接口

    * `CompressorHolder`：键可压缩的带有键的对象，压缩后键从一个字符串表示为一个数字
    * `KeyCompressor<T>`：键压缩器，内部使用 Map 和 List 存储键与数字之间的映射关系
  * `ListBuilder<T>`：用于构建列表目标对象的工具类
  * `RecordBuilder<T>`：用于构建记录目标对象的工具类
* 实现类

  * `JsonOps`：针对 `JsonElement` 的 `DynamicOps` 实现类
