物体的颜色即物体反射给人眼的颜色，是被物体吸收部分颜色后的太阳光照颜色，GLSL 中将两个颜色向量相乘即可。

>[!note] GLSL 向量相乘，每个分量分别相乘，相当于一种颜色将另一种颜色的一个分量按比例吸收。

光照早期在顶点着色器中计算，现如今大多数都在片元着色器中计算。

> [!note] Gouraud 着色：在顶点着色器中实现光照计算  
  优点：处理顶点数少，运算量小，效率高  
  缺点：只计算了当前一个顶点的色值，不进行插值计算，效果差

> [!note] 全局照明算法：
>
> * 光通常不来自同一个光源，而是在周围分散
> * 光可以发散反射，达到较远的点
> * 光能在一个表面反射，对物体产生间接影响

冯氏光照模型 Phong Lighting Model 描述物体反射到人眼的光颜色，包括三部分：
* 环境光 Ambient：即使是黑暗环境下，物体颜色也并非完全黑色。该部分表示物体永远具有的颜色
* 漫反射 Diffuse：方向性影响，视觉效果最显著的分量，越正对光源越明亮
* 镜面反射 Specular：光泽物体上的亮斑，颜色更倾向于光源颜色
# 环境光

使用简化的全局光照模型，即一个很小的光照颜色分量（一般为常量），没有光源的情况下物体也会发出一些颜色。

环境光实现起来比较简单，使用 $光照颜色\times环境因子\times物体颜色$ 即可，环境因子通常很小。

```glsl title:环境光计算
// 光照颜色
uniform vec3 lightColor;

// 环境因子
float ambientStrength = 0.1;
// 物体颜色
vec3 objectColor;

vec3 ambient = ambientStrenght * lightColor;
vec3 ambientColor = ambient * objectColor;
```
# 漫反射

模拟从环境光中获取的平行光光源，如太阳光等。

为了计算漫反射颜色，通常需要向着色器提供以下信息：
* 在顶点属性中指定顶点的法向量信息和世界坐标
* 在着色器 uniform 数据中记录光源位置，法向量变换矩阵等信息
    * 法向量变换矩阵：可选，仅在对模型进行不等比缩放时生效，使用模型矩阵可计算

```glsl title:法向量变换矩阵
mat4 normalMatrix = mat4(transpose(invert(modelMatrix)));
```

```glsl title:漫反射计算
vec3 vNormal;
vec3 position;

uniform mat4 normalMatrix;
uniform vec3 lightPos;
uniform vec3 lightColor;

// 顶点位置的世界坐标
vec3 vPosition = vec3(modelMatrix * vec4(position, 1));
// 漫反射颜色
vec3 norm = normalize(normalMatrix * vNormal);
vec3 lightDir = normalize(lightPos - vPosition);
float diff = max(dot(norm, lightDir), 0);
vec3 diffuseColor = diff * lightColor;
```
# 镜面反射

实现高光效果，表示表面反射特性，与视线位置相关。

镜面反射需要的数据通常为 `uniform` 数据：
* 视线位置（摄像机位置） `viewPos`
* 反光度 `shininess` ，与材料有关，越大反射光越强，散射（光斑大小）越小
* 镜面强度 `specularStrength`，取值范围 $(0,1]$，越大越亮

```glsl title:镜面反射
// 镜面强度
float specularStrength;

uniform vec3 viewPos;

// 来自漫反射计算
vec3 vPosition;
vec3 norm;

// 方向向量
vec3 viewDir = normalize(viewPos - vPosition);
vec3 reflectDir = reflect(-lightDir, norm);

// 计算反射光颜色
float spec = pow(max(dot(viewDir, reflectDir), 0), shininess);
vec3 specular = specularStrength * spec * lightColor;
```
# 光照着色器

物体的最终颜色即光照颜色 `(ambient + diffuse + specular) * objectColor`

在顶点着色器中输出顶点坐标和法线向量，在片元着色区中计算光照

````tabs
tab: 顶点着色器
```glsl title:顶点着色器
#version 460 core

layout(location = 0) in vec3 position; // 顶点坐标
layout(location = 1) in vec3 normal;   // 法线向量

out vec3 vPosition; // 顶点坐标
out vec3 vNormal;   // 法线向量

// 模型矩阵、观察矩阵、投影矩阵
uniform mat4 modelMatrix, viewMatrix, projectionMatrix;

void main() {
    vec4 mPos = modelMatrix * vec4(position, 1);
    vPosition - mPos.xyz;
    vNormal = normal;
    gl_Position = projectionMatrix * viewMatrix * mPos;
}
```

tab: 片元着色器
```glsl title:片元着色器
#version 460 core

in vec3 vPosition;
in vec3 vNormal;

out vec4 FragColor;

uniform vec3 lightColor;    // 光照颜色
uniform vec3 objectColor;   // 物体颜色
uniform vec3 lightPos;      // 光源位置
uniform vec3 viewPos;       // 视线位置（摄像机位置）
uniform vec3 normalMatrix;  // 法线变换矩阵 = transpose(invert(modelMatrix))

uniform float ambientStrength;   // 环境光强度
uniform float specularStrength;  // 漫反射强度
uniform float specularStrength;  // 镜面反射强度
uniform int shininess;           // 反光度

void main() {
    // 环境光
    vec3 ambient = ambientStrength * lightColor;
    // 漫反射
    vec3 norm = normalize(normalMatrix * vNormal);
    vec3 lightDir = normalize(lightPos - vPosition);
    float diff = max(dot(norm, lightDir), 0);
    // 镜面反射
    vec3 viewDir = normalize(viewPos - vPosition);
    vec3 reflectDir = reflect(-lightDir, norm);
    float spec = pow(max(dot(viewDir, reflectDir), 0), shininess);
    vec3 specular = specularStrength * spec * lightColor;

    FragColor = vec4((ambient + diffuse + specular) * objectColor, 1);
}
```
````
# 光照材质

不同材料对光的反应不同，反光度也不同，需要对不同的光设置不同的颜色。

定义 `Material` 结构体，之后在使用 `lightColor` 的地方与对应的参数相乘即可。`lightColor` 在这里代表入射光，一般使用 `vec3(1.0)` 即白光

```glsl title:Material
struct Material {
    vec3 ambient, diffuse, specular;
    float shininess;
}

uniform Material material;
```

* 使用 `glGetUniformLocation("material.ambient")` 获取每个分量的 `location`
* 不同材料的参数可以直接查询 [[../../../_resources/documents/OpenGL/OpenGL or VRML Materials]]，**注意反光度需要扩大 128 倍**

另一种表现材质的光照效果为光照贴图。当填充纹理时，采样的结果即当前点的颜色，可直接用于光照颜色。

>[!note] 通常采样的颜色可以作为环境光 `ambient` 和漫反射光 `diffuse` 的颜色，而镜面反射 `specular` 可采用一个单独的图片，黑色填充不反光部分，反光度越低颜色越黑。
