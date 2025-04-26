DataFixerUpper 库期初是 Minecraft 用于处理跨版本存档更新的库，后其中的 Codec 部分也被广泛用于 MC 内数据之间的互相转换。

* `Codec<T>`：描述如何在各个类型之间转换
* `DynamicOps<T>`：将数据看作一个容器，描述如何将数据与类型 T 进行交互

# DynamicOps

`DynamicOps` 可以实现从某个类型中读入和读出数据。利用 `DynamicOps` 可以实现类型的直接转化

```java
JsonElement jsonObj = NbtOps.INSTANCE.convertTo(JsonOps.INSTANCE, tagObj);
Tag tagObj = JsonOps.INSTANCE.convertTo(NbtOps.INSTANCE, jsonObj);
```

`DynamicOps` 对应的数据类型应符合以下条件：

* 具有一种方法表示空类型，对应 `empty()` 返回的结果
* 具有一种字符串的表示方法，通过 `createString` 创建，通过 `getStringValue` 获取
* 具有一种数字的表示方法，通过 `createNumeric` 创建，通过 `getNumberValue` 获取
* 具有一种键值对的表示方法，通过 `createMap` 创建，通过 `getMapValues` 获取

  * 通过 `ops.mapBuilder()` 可以创建 Map，DynamicOps 默认实现
  * 通过 `ops.mergeToMap()` 可以连接 Map，需要自己实现
* 具有一种列表的表示方法，通过 `createList` 创建，通过 `getStream` 获取

  * 通过 `ops.listBuilder()` 可以创建 List，DynamicOps 默认实现
  * 通过 `ops.mergeToList()` 可以连接 List，需要自己实现

DataFixerUpper 内置 `JsonElement` 类型的 `DynamicOps`：

* `JsonOps.INSTANCE`：Json 数据，JsonElement 对象
* `JsonOps.COMPRESSED`：压缩后的 Json 数据

Minecraft 内置 Tag 类型的 `DynamicOps`：`NbtOps.INSTANCE`  
`Codec` 是 `DataFixerUpper` 库的序列化/反序列化工具。该接口组合了 `Encoder` 与 `Decoder` 两个接口，利用 `DynamicOps` 实现不同格式之间的互相转换。

# DataResult

Codec 序列化、反序列化后产生的是一个 `DataResult` 对象，该对象允许收集中途转化失败的结果

```java
result
    .resultOrPartial(errMessage -> /* 异常处理 */)
    .ifPresent(object -> /* 处理结果 */);
```

# 序列化与反序列化

使用 `encoderStart` 方法开始序列化过程，`parse` 方法开始反序列化过程。

```java
DataResult<T> result = codec.encodeStart(ops, object);
```

* ops：`DynamicOps<T>` 对象
* object：待序列化对象

```java
DataResult<T> result = codec.parse(ops, S object);
```

* ops：`DynamicOps<S>` 对象
* object：待反序列化数据

> `Codec` 类中包含一系列自带的通用 `Codec` 对象，包括
>
> * 适用于布尔值的 `BOOL`
> * 适用于基本数字类型的 `INT`，`LONG`，`FLOAT`，`DOUBLE` 等
> * 适用于字符串的 `STRING`
> * 适用于数字列表的 `BYTE_BUFFER`，`INT_STREAM` 等
> * 适用于数值范围的 `intRange(min, max)`, `floatRange(min, max)` 等
> * 适用于 `null` 的 `EMPTY`
>
> Minecraft 内部很多存储数据的类都有对应 Codec 对象，这些对象通常作为该类的常量，如 `ResourceLocation.CODEC`，`BlockPos.CODEC`
>
> 对于方块、物品等需要注册的对象，`CODEC` 也存在于其对应的 `Registry` 中
# 创建 Codec

通过 Codec 的实现类 `RecordCodecBuilder` 可创建用于任意类型的 Codec 对象。以下例子仅以 record 类作为例子，实际上可以用任何类型

```java
// 需要序列化/反序列化的类
record MyData(String name, int value) {}

// Codec
public static final Codec<MyData> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
                // fieldOf 对应序列化后的键，forGetter 对应序列化时如何取值
                Codec.STRING.fieldOf("name").forGetter(MyData::name),
                Codec.INT.fieldOf("value").forGetter(MyData::value)
        // apply 方法用于反序列化时创建一个对象
        ).apply(instance, MyData::new));
```

# 派生 Codec

根据现有 Codec，创建新 Codec

* 装箱：将一个对象在序列化时打包成一个单独的对象，使用 `forGetter` 指定键名

  ```java
  Codec<MyData> CODEC;
  // { "name": ..., "value": ... }
  CODEC.encodeStart(JsonOps.INSTANCE, object).result().get()

  Codec<MyData> CODEC_BOXED = CODEC.fieldOf("data").codec();
  // { "data": { "name": ..., "value": ... } }
  CODEC_BOXED.encodeStart(JsonOps.INSTANCE, object).result().get()
  ```
* 常量：使用 `Codec.unit(value)` 创建一个常量 Codec。当序列化时，产生空值；反序列化时，总是传入的 value

  ```java
  Codec<Integer> CODEC_5 = Codec.unit(5);
  ```
* `List`：`Codec#listOf()` 将 `Code<T>` 转化为 `Codec<List<T>>`

  ```java
  Codec<MyData> CODEC;
  Codec<List<MyData>> CODEC_LIST = CODEC.listOf();
  ```
* `Map`：使用 `Codec.unboundedMap(CodecKey, CodecValue)` 创建 `Codec<Map<Key, Value>>` 对象，`CodecKey` 必须可以与 String 互相转换
* `Pair`：`Codec.pair(CodecA, CodecB)`，传入的 Codec 必须是一个显示的对象，如 `RecordCodecBuilder`，`unit` 或 `fieldOf` 创建的 Codec，生成合并两个对象的结果

  ```java
  // { "name": "..." }
  public static final Codec<String> CODEC_NAME = Codec.STRING.fieldOf("name").codec();  
  // { "value": ... }
  public static final Codec<Integer> CODEC_VALUE = Codec.INT.fieldOf("value").codec();
  // { "name": "...", "value": ... }
  public static final Codec<Pair<String, Integer>> CODEC_PAIR = Codec.pair(CODEC_NAME, CODEC_VALUE);
  ```
* `Either`：`Codec.either(CodecA, CodecB)`，类似 Pair，生成 `Codec<Either<A, B>>`，二选一

  ```java
  public static final Codec<String> CODEC_STRING;
  public static final Codec<Integer> CODEC_INT;
  public static final Codec<Either<String, Integer>> CODEC_EITHER = Codec.either(CODEC_STRING, CODEC_INT);
  ```
* 等效类型：在存在 `Codec<A>` 的情况下，若我们有一个类型 B，A 与 B 之间可以一对一转换，则可以通过 `Codec<A>.xmap(Function<A, B>, Function<B, A>)` 创建 `Codec<B>`

  ```cpp
  Codec<Pair<String, Integer>> CODEC_PAIR;
  Codec<MyData> CODEC_XMAP = CODEC_PAIR.xmap(
              // Function<Pair<String, Integer>, MyData>
              p -> new MyData(p.getFirst(), p.getSecond()),
              // Function<MyData, Pair<String, Integer>>
              data -> Pair.of(data.name(), data.value()));
  ```
  若 A 与 B 之间不一定能完全转换成功，如 `ResourceLocation` 可以转换为 `String`，但反过来可能无法完成转换（要求字符串必须是 `a:b` 的格式），则为部分等效类型

|转换方法|A->B 一定成功|B->A 一定成功|
| --------------| ---------------| ---------------|
|xmap|是|是|
|flatComapMap|是|否|
|comapFlatMap|否|是|
|flatXmap|否|否|
