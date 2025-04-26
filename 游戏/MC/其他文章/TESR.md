## 动画状态机

动画状态机（IAnimationStateMachine）也是由 TESR 实现的

## armatures

该文件定义了动画的基本信息，包括运动的材质是材质的哪一部分，动画有几个状态，每个状态是如何运动的，触发什么事件等

```json
// assets/[MODID]/armatures/[block/item]/xxx.json，xxx.json 与材质名同名
{
  "joints": {
    "joint_default": {"5": [1.0]}
  },
  "clips": {
    "move": {
      "loop": true,
      "joint_clips": {
        "joint_default": [
          {
            "variable": "offset_y",
            "type": "uniform",
            "interpolation": "linear",
            "samples": [0, 10]
          }
        ]
      },
      "events": {}
    }
  }
}
```



### joints：动画中运动的基本单位

由 models 的 elements 数组的一部分或几部分组成

```json
"joints": {
  "[joint_name]": {"[index]": [1.0]}
}
```

- joint_name：一组运动的 joints 名，用于 clips 调用
- index：运动元素在 elements 中的序列索引，从 0 开始
- 数组：1.0 代表该元素贡献度为 1。若为简单动画，每一部分均设为1即可



### clips：动画运动的方式

类似一个个封装好的执行动画的方法，供 asm 调用

```json
  "clips": {
    "[clip_name]": {
      "loop": true,
      "joint_clips": {
        "[joint_name]": [
          {
            "variable": "[variable]",
            "type": "uniform",
            "interpolation": "[interpolation]",
            "samples": [0, 10]
          }
        ]
      },
      "events": {
          "[event_time]": "[event_value]"
      }
    }
  }
```
- clip_name：当前 clip 名，用于 asm 文件调用
- variable：动画运动的方式。
  - offset_x，offset_y，offset_z：平移
  - scale，scale_x，scale_y，scale_z：缩放
  - axis_x，axis_y，axis_z：绕轴旋转
  - angle：旋转角度
  - origin_x，origin_y，origin_z：绕起点旋转
- type：目前尚无意义，但必须为 uniform
- interpolation：插值器，将 samples 转化为连续值，作用于 variable
  - nearest：根据输入值确定。当输入值 <0.5 则使用第一个值，否则使用第二个值
  - linear：线性插值
- samples：动画播放的值，该值被 interpolation 处理后作用于 variable
- event_time：事件触发的时间，通常位于0-1之间。
- event_value：事件值，文本
  - 普通事件可被 ASM 的 pastEvents 回调处理，位于客户端。
  - 特殊事件：使用 !event_type 开头，目前只有 !event_type:transition，提供当前状态



该文件必须位于 armatures 文件夹下且与 models 文件同名

## asms

该文件定义了动画的控制信息，包括控制动画进行的变量，控制动画跳转，定义动画起始状态等

```json
{
  "parameters": {
    "anim_cycle": ["/", "#cycle_length"]
  },
  "clips": {
    "default": ["apply", "forgedebugmodelanimation:block/rotatest@default", "#anim_cycle" ]
  },
  "states": [
    "default"
  ],
  "transitions": {},
  "start_state": "default"
}
```

### 参数 parameters

```json
  "parameters": {
    "[parameter_name]": <parameter_value>
  },
```

参数表示将输入值 运算为其他量。用于播放动画时对输入量的预处理

实际类似函数

在代码中，参数表示为 TimeValue，在 ASM 创建时添加进 ImmutableMap

parameter_value：参数值，可选四种类型

- IdentityValue：字符串 #identity

- ParameterValue：引用其他参数，使用 #parameter_name

- ConstValue：数字常量

- SimpleExprValue：数学表达式，[ regex("[+\\-*/mMrRfF]+"), <parameter_definition>, ... ]

  | 操作 | 含义                                                  |
  | :--- | :---------------------------------------------------- |
  | `+`  | `输出 = 输入 + 参数`                                  |
  | `-`  | `输出 = 输入 - 参数`                                  |
  | `*`  | `输出 = 输入 * 参数`                                  |
  | `/`  | `输出 = 输入 / 参数`                                  |
  | `m`  | `输出 = min(输入, 参数)`                              |
  | `M`  | `输出 = max(输入, 参数)`                              |
  | `r`  | `输出 = floor(输入 / 参数) * 参数` (向下取整)         |
  | `R`  | `输出 = ceil(输入 / 参数) * 参数` (向上取整)          |
  | `f`  | `输出 = 输入 - floor(输入 / 参数) * 参数` (取余)      |
  | `F`  | `输出 = ceil(输入 / 参数) * 参数 - 输入` (参数减余数) |

  ```json
  // 输入值+4
  [ "+", 4 ]
  // 输入值/5+1
  [ "/+", 5, 1]
  // 输入值+2+other
  [ "++", 2, "#other" ]
  // 输入值+other+（compose(cycle, 3)），compose 在后面有说明
  [ "++", "#other", [ "compose", "#cycle", 3] ]
  ```

- CompositionValue：连接。将两个参数作为输入，输出 value1(value2(输入))

  ​                                    [ "compose", <parameter_definition>, <parameter_definition> ]

  ```json
  // cycle(3)
  [ "compose", "#cycle", 3]
  // test(other(输入))
  [ "compose", "#test", "#other"]
  // 3+other(输入)
  [ "compose", [ "+", 3], "#other"]
  // other2(other3(other(输入)))
  [ "compose", [ "compose", "#other2", "#other3"], "#other"]
  ```

  

### 剪辑 clips

接收输入（默认为当前时间），转化为动画的输入

指定某一状态需要做什么，是动画的实际执行者

```json
  "clips": {
    "clip_name": <clip_value>
  }
```

clip_value：实际要执行的动画操作

- IdentityClip：字符串 #identity

- ClipReference：引用其他剪辑，使用 #clip_name

- ModelClip：使用 armatures 中的剪辑。格式为 model资源名@armatures剪辑名

- TimeClip：自定义剪辑输入而不是使用默认输入。默认输入为当前世界秒数（float，考虑到tick）

  格式：[ "apply", <clip_definition>, <parameter_definition> ]

  - clip_definition：要引用的另一个剪辑
  - parameter_definition：使用的新输入。将以当前时间为默认参数输入该 parameter

  ```json
  // mymod:block/animated_thing@moving(#cycle_time)
  ["apply", "mymod:block/animated_thing@moving", "#cycle_time"]
  // mymod:block/animated_thing@moving(#cycle + 3)
  ["apply", [ "apply", "mymod:block/animated_thing@moving", [ "+", 3 ] ], "#cycle"]
  ```

- TriggerClip：触发事件 [ "trigger_positive", <clip_definition>, <parameter_definition>, "<event_text>"]

  类似 TimeClip，且当 parameter_definition>0 时会触发 <event_text>

  ```json
  // end_cycle>0 时触发 !transition:moving 事件，切换为 moving 状态
  [ "trigger_positive", "#default", "#end_cycle", "!transition:moving" ]
  [ "trigger_positive", "mymod:block/animated_thing@moving", "#end_cycle", "boop" ] 
  ```

  

- SlerpClip：混合两个剪辑 [ "slerp", <clip_definition>, <clip_definition>, <parameter_definition>, <parameter_definition> ]

  将一个剪辑平滑的转化为另一个剪辑，前两个输入为两个剪辑，第三个为传入参数，最后一个为 process，介于0-1，为混合中的距离

  ```json
  // 将“关闭”剪辑混合到“打开”剪辑中，为两个剪辑提供未更改的时间作为输入并混合进度#progress
  [ "slerp", "#closed", "#open", "#identity", "#progress" ]
  // 当输入参数mover给定结束剪辑时，将移动剪辑的结果混合，其中未改变的时间作为混合进度#progress的输入。
  [ "slerp", [ "apply", "#move", "#mover"], "#end", "#identity", "#progress" ]
  ```

  



### 状态 states

动画播放的状态，所有可能剪辑状态的列表



### 转换 translations

对某一状态允许进入的其他状态进行定义。可空

```json
"transitions": {
    "[state_name]": ["[state_allowed_name]", "[state_allowed_name]", "[state_allowed_name]"],
    "[state_name]": ["[state_allowed_name]", "[state_allowed_name]", "[state_allowed_name]"],
    "[state_name]": ["[state_allowed_name]", "[state_allowed_name]", "[state_allowed_name]"]
},
```





## 代码

注册、控制动画进行

### 获取ASM

```java
@SideOnly(Side.CLIENT) // ASM 只用于 Client 端
public IAnimationStateMachine load(String location) {
    // location 必需为完整路径
    // 比如 这里指向的就是 assets/asm/block/test.json
    // ImmutableMap 可以添加 VariableValue，可通过 ASM.setValue 设置值，但不能读取
    return ModelLoaderRegistry.loadASM(new ResourceLocation(MODID, "asm/block/test.json"), ImmutableMap.of())
}
```



### 方块

使用 FastTESR 实现，因此必须有一个 TileEntity，且提供 ANIMATION_CAPABILITY

```java
@Override
default boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityAnimation.ANIMATION_CAPABILITY;
}

@Nullable
@Override
default <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityAnimation.ANIMATION_CAPABILITY 
        && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
        return capability.cast((T) load());
    }
    return null;
}
```



该 TileEntity 需要绑定一个 AnimationTESR，重写 handleEvents 方法，用于处理动画事件。在注册 TileEntity 时注册

```java
ClientRegistry.bindTileEntitySpecialRenderer(tileEntityClass, animationTESRObject);
```





方块必须具备 BlockState，包含 Properties.StaticProperty 和 Properties.AnimationProperty。前者决定是否使用静态渲染，动画为 false；后者保存动画状态

```java
@Override
protected BlockStateContainer createBlockState() {
    return new ExtendedBlockState(this,
            new IProperty[]{Properties.StaticProperty},
            new IUnlistedProperty[]{Properties.AnimationProperty});
}
```



### 物品

物品动画使用 ANIMATION_CAPABILITY 实现

```java
private static class ItemAnimationHolder implements ICapabilityProvider
{
    private final VariableValue cycleLength = new VariableValue(4);

    private final IAnimationStateMachine asm = proxy.load(new ResourceLocation(MODID.toLowerCase(), "asms/block/engine.json"), ImmutableMap.<String, ITimeValue>of(
        "cycle_length", cycleLength
    ));

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityAnimation.ANIMATION_CAPABILITY;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if(capability == CapabilityAnimation.ANIMATION_CAPABILITY)
        {
            return CapabilityAnimation.ANIMATION_CAPABILITY.cast(asm);
        }
        return null;
    }
}
```



### 实体

实体动画使用 AnimationModelBase 实现

为了使用动画API为实体设置动画，实体的渲染器必须将`AnimationModelBase`作为其模型。 该模型的构造函数采用两个参数，即实际模型的位置（如JSON或B3D文件的路径，而不是方块状态引用）和`VertexLighter`。 

可以使用`new VertexLighterSmoothAo(Minecraft.getMinecraft().getBlockColors())`创建`VertexLighter`对象。 

实体还必须提供`ANIMATION_CAPABILITY`，它可以通过传递ASM以`.cast`方法创建。

`handleEvents()`回调位于`AnimationModelBase`类中，如果你想使用该事件，你必须继承`AnimationModelBase`类。 回调有三个参数：正在渲染的实体，当前时间(tick)，以及已发生事件的可迭代对象

```java
ResourceLocation location = new ModelResourceLocation(new ResourceLocation(MODID, blockName), "entity");
return new RenderLiving<EntityChest>(manager, new net.minecraftforge.client.model.animation.AnimationModelBase<EntityChest>(location, new VertexLighterSmoothAo(Minecraft.getMinecraft().getBlockColors()))
{
    @Override
    public void handleEvents(EntityChest chest, float time, Iterable<Event> pastEvents)
    {
        chest.handleEvents(time, pastEvents);
    }
}, 0.5f) {

// ... getEntityTexture() ...

};
```