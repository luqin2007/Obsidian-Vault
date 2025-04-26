> [!note] GLSL
> OpenGL Shader Language，OpenGL 着色器语言。类似 C 语言，额外提供针对向量和矩阵的操作。

```cpp
#version 460 core

layout (location = 0) in vec3 pos;

void main() {
    gl_Position = vec4(pos, 1);
}
```

* 版本声明：使用 `#version` 声明的版本，应与程序使用的 OpenGL 版本相匹配
* 配置变量：所有变量可以使用 `in`、`out`、`uniform` 进行修饰
  * `in`：输入变量，顶点着色器的 `in` 变量来自代码通过 VAO 传入，其他着色器来自前一个着色器的同名同类型 `out` 类型变量
    * `in` 未使用 `location` 指定位置，可通过两种方式获取：
      * `glGetAttribLocation(变量名)`
      * 着色器编译时，使用 `glBindAttribLocation` 绑定，相当用指定 location
    * 设备支持的最大 `in` 类型变量个数由 `glGetInteger(GL_MAX_VERTEX_ATTRIBS)` 获取，但至少支持 16 个包含 4 个分量的属性
  * `out`：输出变量，输出到下一阶段，由下一阶段的 `in` 变量接收，片元着色器输出到颜色空间，支持多个输出
  * `uniform`：共享数据，由程序传入，但对着色器只读。不同着色器同名同类型的 `uniform` 变量值相同
    1. 激活对应着色器程序（`glUseProgram`）
    2. 获取 Uniform 变量位置（location 属性，或 `glGetUniformLocation`）
    3. 使用 `glUniformXxx` 赋值，其中 Xxx 为类型
# 数据类型

GLSL 支持各种基本类型、结构体，并额外原生支持向量和矩阵

* 基本类型：`int`、`uint`、`float`、`double`、`bool`
* 结构体
* 向量 `[T]vec[N]`，矩阵 `[T]mat[N]x[M]`
    * T：内部元素类型，省略表示 float，b 为 bool，i 为 int，u 为 uint，d 为 double
    * N、M：元素个数，取值范围 $[1, 4]$
* 采样器：`sampler2D`、`sampler3D`

向量各分量可以通过 `rgba`、`xyzw` 或 `stpq` 访问，但不能混合使用

```cpp
vec4 v4 = vec4(1, 2, 3, 4);
vec3 v3rgb = v4.rgb;
vec3 v3xyz = v4.xyz;
bool b1 = v3rgb == v3xyz; // true
vec3 v3rba = v4.rba;      // 正确
vec3 v3 = rgx;            // 编译错误
```

通过向量的构造，可以将各向量互相组合

```cpp
vec3 color = vec3(1, 0.5f, 1);
vec4 colorRGBA = vec4(color, 1); // 1 0.5 1 1
```

矩阵也可以拆分为向量，或者由向量通过构造函数创建。
# 内建变量

OpenGL 在着色器中有一些内建变量，完整版详见 [[../../../_resources/documents/OpenGL/Built-in Variable (GLSL)]]
* 顶点着色器
  * `gl_Position`：顶点着色器输出，记录顶点坐标
  * `gl_PointSize`：点作为图元时绘制的大小，当且仅当 `glEnable(GL_PROGRAM_POINT_SIZE)` 时可写
  * `gl_VertexID`：只读，渲染索引（；）
    * 使用 `glDrawElements` 系列方法进行渲染时表示该顶点对应的索引
    * 使用 `glDrawArrays` 系列方法进行渲染时表示已处理定点数量
* 片元着色器
  * `gl_FragCoord`：只读，顶点坐标
    * x、y 表示窗口坐标（`glViewport` 设置）
    * z 表示深度值
  * `gl_FrontFacing`：只读，bool 类型，该面是否为正面。
    * 当开启面剔除时无意义，因为背面已经剔除过
  * `gl_FragDepth`：深度值
    * OpenGL 4.2 之前的版本只要修改了此值，提前深度测试便会禁用，GL42 后可配置

```cpp
layout(depth [condition]) out float gl_FragDepth;
```

* `[condition]` 为 `any` 时，一旦修改，关闭所有提前深度测试
* `[condition]` 为 `greater` 时，深度值比当前值大时不会关闭提前测试
* `[condition]` 为 `less` 时，深度值比当前值小时不会关闭提前测试
* `[condition]` 为 `unchanged` 时，深度值只读
# 接口块

用于两个着色器之间传递复杂数据，类似 struct

* 仍使用 in 或 out 声明
* 接口块仅要求块名相同即可互相匹配，实例名可互不相同
* 实例名可以不存在，可用于解决命名冲突

`````col
````col-md
```glsl
in VS_OUT {
    vec2 TexCoords;
} fs_in;
```
````
````col-md
```glsl
in VS_OUT {
    vec2 TexCoords;
} fs_in;
```
````
`````

![[Pasted image 20230912204548-20240513170134-jlsigd1.png]]
# uniform 缓冲对象

一种特殊缓冲区对象，绑定后为着色器提供 Uniform 块的值

* 块布局
  * `binding`：OpenGL 4.2 后可用，显示指定一个绑定点
  * 布局规则：内存布局规则
    * 默认：共享布局
    * `packed`：紧凑布局，具体布局依赖于硬件实现
    * `std140`
      * 标量 int，bool，float 基准对齐量为 4 字节
      * 向量基准对齐量为 8 或 16 字节，矩阵以列向量形式存储
      * 数组内每个元素的基准对齐量为 16
      * 结构体根据规则计算，并将结果扩展到 16 的倍数

  ```cpp
  layout (std140, binding=2) uniform ExampleBlock {
                       //基本对齐量   对齐偏移量
      float value;     //        4            0
      vec3  vector;    //       16           16
      mat4  matrix;    //     16x4  32 48 64 80
      float values[3]; //     16x3   96 112 128
      bool  boolean;   //        4          144
      int   integer;   //        4          148
  }
  ```

* Uniform 缓冲区对象：在代码中访问 Uniform 块

  ```cpp
  // 创建和初始化缓冲区
  GLuint ubo;
  glGenBuffers(1, &ubo);
  glBindBuffer(GL_UNIFORM_BUFFER, ubo);
  glBufferData(GL_UNIFORM_BUFFER, ...);
  glBindBuffer(GL_UNIFORM_BUFFER, 0);
  // 绑定 Uniform 缓冲
  GLuint index = glGetUniformBlockIndex(program, "ExampleBlock");
  glUniformBlockBinding(program, index, 2);
  glBindBufferBase(GL_UNIFORM_BUFFER, 2, ubo);
  ```

 之后，直接操作缓冲区即可
  ![[Pasted image 20230912211648-20240513170333-gf6hawy.png]]
