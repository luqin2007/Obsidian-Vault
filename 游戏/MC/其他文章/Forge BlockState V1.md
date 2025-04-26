
```json
<blockstate> == {
  "forge_marker": 1,
  "defaults": <variant>, // 可选 添加到所有 variants 中
  "variants": {
    "<property>": {
        "<value>": <variant> // variant 定义属性的给定值; 变量可指定多个值.
     },
    "<variant name>": <variant>, // 完整变量字符串定义
    "<variant name>": [<variant1>, ...], // 完整变量数组定义 -- 结果将为随机变量
  }
}

<variant> == {
  "model": "<model location>",
  "textures": {  // 将 <from> 纹理映射到 <to>; 可定义多映射
    "<from>": "<to>"
  },
  "x": <angle-90>, // 原版旋转兼容; 只支持 90 的倍数, 参考原版规范
  "y": <angle-90>,
  "transform": <root-transform>, // 转换
  "uvlock": <bool>, // 参考原版规范
  "weight": <int>, // 随机选择权重, 参考原版规范
  // submodels: 指定的所有内容都将合并到基本 model 中. 若该变量为根变量且无 model - 基本 model 将会选择一个 submodels.
  "submodel": "<model location>",
  "submodel": <variant>,
  "submodel": [<variant>, ...],
  "custom": { "<key>": <value> } // model 可用的自定义数据
}

<root-transform> == <transform>

<root-transform> == {
  "thirdperson": <transform>,
  "firstperson": <transform>,
  "gui": <transform>,
  "head": <transform>,
  // 或其他任何 <transform> 键
}

<transform> == "<builtin string>"
currently supported builtin strings:
  "identity" - identity transformation
  "forge:default-block" - default block transformation (example: stone)
  "forge:default-item" - default 2d item transformation (example: bucket)
  "forge:default-tool" - default 2d tool transformation (example: pickaxe)
This may be expanded to something more generic in the future.

<transform> == <matrix>
<transform> == { "matrix": <matrix> }

<matrix> == [
  [<number>, <number>, <number>, <number>],
  [<number>, <number>, <number>, <number>],
  [<number>, <number>, <number>, <number>]
]
4x3 matrix (3x3 affine part + translation column)

<transform> == {
  // 所有的键都是可选的
  "translation": [<number>, <number>, <number>],
  "rotation": <rotation>,
  "scale": [<number>, <number>, <number>], // per-axis scale
  "scale": <number>, // uniform scale
  "post-rotation": <rotation>
}

<rotation> == [<number>, <number>, <number>, <number>]
Quaternion(x, y, z, w)

<rotation> == {"<axis>": <number>}
Rotation around the coordinate axis, in degrees. Value is unrestricted

<rotation> == [{"<axis1>": <number>}, ...]
Composition of rotations around multiple axes, in the specified order
```



```json
// https://gist.github.com/RainWarrior/0618131f51b8d37b80a6
<blockstate> == {
  "forge_marker": 1,
  "defaults": <variant>, // optional, added to all variants
  "variants": {
    "<property>": {
    "<value>": <variant> // variant definition for the specified value of this property; variants for multiple values can be specified.
  },
    "<variant name>": <variant>, // variant definition for the full variant string
    "<variant name>": [<variant1>, ...], // array of definitions for the full variant - result will be the random variant
  }
}

<variant> == {
  "model": "<model location>",
  "textures": {  // remaps the <from> texture in the model to <to>; multiple remappings can be specified
    "<from>": "<to>"
  },
  "x": <angle-90>, // vanilla rotation compatibility; only multiples of 90 degrees are supported, see vanilla specification
  "y": <angle-90>,
  "transform": <root-transform>, // transformations
  "uvlock": <bool>, // see vanilla specification
  "weight": <int>, // random weight, see vanilla specification
  // submodels: all stuff specified will be merged into the base model. If this is the root variant without the model - base will be chosen from one of the submodels.
  "submodel": "<model location>",
  "submodel": <variant>,
  "submodel": [<variant>, ...],
  "custom": { "<key>": <value> } // custom data that models can use
}

<root-transform> == <transform>

<root-transform> == {
  "thirdperson": <transform>,
  "firstperson": <transform>,
  "gui": <transform>,
  "head": <transform>,
  // or any of the <transform> keys
}

<transform> == "<builtin string>"
currently supported builtin strings:
  "identity" - identity transformation
  "forge:default-block" - default block transformation (example: stone)
  "forge:default-item" - default 2d item transformation (example: bucket)
  "forge:default-tool" - default 2d tool transformation (example: pickaxe)
This may be expanded to something more generic in the future.

<transform> == <matrix>
<transform> == { "matrix": <matrix> }

<matrix> == [
  [<number>, <number>, <number>, <number>],
  [<number>, <number>, <number>, <number>],
  [<number>, <number>, <number>, <number>]
]
4x3 matrix (3x3 affine part + translation column)

<transform> == {
  // all keys are optional
  "translation": [<number>, <number>, <number>],
  "rotation": <rotation>,
  "scale": [<number>, <number>, <number>], // per-axis scale
  "scale": <number>, // uniform scale
  "post-rotation": <rotation>
}

<rotation> == [<number>, <number>, <number>, <number>]
Quaternion(x, y, z, w)

<rotation> == {"<axis>": <number>}
Rotation around the coordinate axis, in degrees. Value is unrestricted

<rotation> == [{"<axis1>": <number>}, ...]
Composition of rotations around multiple axes, in the specified order
```