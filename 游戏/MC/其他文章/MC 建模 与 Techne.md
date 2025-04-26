# MC 建模 与 Techne

## 基本代码建模

### MC 建模基础

#### 坐标系

- MC 游戏中1米（一个方块的长度）相当于16像素

#### 渲染类

所有模型文件都必须继承于 ModelBase 类，并重写 render 方法。

每一帧，都会调用该方法。应当在该方法中调用所有子渲染 ModelRenderer 类对象

```java
public class RenderBase extends ModelBase {

    ModelRenderer body;

    public RenderBase() {
        // texOffX/texOffY 为材质偏移，相当于 u v
        body = new ModelRenderer(this, 18, 4);
        // 添加一个盒子。需要指定相对于 ModelRender 的材质偏移（offXYZ），长宽高，缩放
        body.addBox(-6f, -10f, -7f, 12, 18, 10, 0f);
        // 设置旋转中心
        body.setRotationPoint(0f, 5f, 2f);
        // 旋转弧度
        body.rotateAngleX = -0.7854f;
    }

    @Override
    public void render(Entity entityIn, 
                       float limbSwing, float limbSwingAmount, float ageInTicks, 
                       float netHeadYaw, float headPitch, float scale) {
        super.render(...);
        setRotationAngles(...);
        body.render(scale);
    }
}
```

### ModelRenderer

用于具体渲染

```java
// texOffX/texOffY 为材质偏移，相当于 u v
body = new ModelRenderer(this, 18, 4);
// 添加一个盒子。需要指定相对于 ModelRender 的材质偏移（offXYZ），长宽高，缩放
body.addBox(-6f, -10f, -7f, 12, 18, 10, 0f);
// 设置旋转中心
body.setRotationPoint(0f, 5f, 2f);
// 旋转弧度
body.rotateAngleX = -0.7854f;

// 另一种 ModelRenderer 创建，使用子 box
// 使用 setTextureOffset 设置 offset，"." 用以分割 renderer 与 b
// 共享 offset scale rotation
// 不太了解
setTextureOffset("main.a", 0, 0);
setTextureOffset("main.b", 1, 1);
main = new ModelRenderer(this, "main");
main.addBox("a", 0, 0, 0, 0, 0, 0);
main.addBox("b", 0, 0, 0, 0, 0, 0);
```



## 动画

## 块与子模型