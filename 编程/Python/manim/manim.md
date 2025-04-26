Manim 是一个 Python 动画引擎，常用于数据可视化
1. 创建一个类，继承自 `Scene`，重写 `construct` 方法在 `Scene` 中创建动画
2. 使用 `manim -pql <py 文件> <类名>` 编译动画，在 `media/videos` 中生成动画

![[../../../_resources/images/Pasted image 20240918084734.png]]

# 形状

使用 `scene.play` 渲染一个图形，`Create` 表示一个创建图形的动画

```reference
file: "@/_resources/codes/manim/CreateCircle.py"
start: 4
end: 8
```

# 变换

`Transform` 表示一个图形变换的动画，每个 `play` 表示一段动画

```reference
file: "@/_resources/codes/manim/SquareToCircle.py"
start: 4
end: 18
```

> [!attention] `Transform` 和 `ReplacementTransform`
> 二者视觉效果相同，`Transform` 仅替换两个图形的所有属性，`ReplacementTransform` 则是替换图形对象

```reference fold
file: "@/_resources/codes/manim/SquareToCircle.py"
start: 21
end: 41
```

# 基本定位

初始位置在屏幕中央，使用 `LEFT`、`RIGHT`、`UP`、`DOWN` 分别表示四个方向的一个单位
- 使用 `next_to` 进行相对定位
- `play` 可以同时接受多个动画

```reference
file: "@/_resources/codes/manim/SquareAndCircle.py"
start: 4
end: 14
```

# `.animate` 动画

`Mobject.animate` 可以创建一个从给定形状变换的动画

```reference
file: "@/_resources/codes/manim/AnimatedSquareAndCircle.py"
start: 4
end: 12
```

# 参考

```cardlink
url: https://www.manim.community/
title: "Manim Community"
description: "Manim is a community-maintained Python library for creating mathematical animations."
host: www.manim.community
favicon: https://www.manim.community/favicon-32x32.png
image: https://www.manim.community/banner.png
```

- [[场景]]
- [[模块]]
- [[配置]]
# 'latex' is not recognized as an internal or external command, operable program or batch file.

> [!error] 'latex' is not recognized as an internal or external command, operable program or batch file.

本地电脑需要安装 `latex` 应用，Windows 安装 `MikTex` 即可