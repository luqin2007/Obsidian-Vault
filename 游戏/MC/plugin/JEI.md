# 引用

```groovy
// build.gradle

```

# 创建

## 入口

```java
@JEIPlugin
public class JeiPlugin implements IModPlugin {

    // todo something
}
```

## 加载过程

### registerItemSubtypes

> 注册使用 NBT 或者 Capability 而不是 meta 区分子类型的物品

- useNbtForSubtypes
- registerNbtInterpreter
- registerSubtypeInterpreter
- getSubtypeInfo
- hasSubtypeInterpreter

### registerCategories

> 注册分类

### registerIngredients

> 向 JEI 注册新 Ingredient 类型，而非基础的 ItemStack 和 FluidStack

### register

> 注册物品及合成表
>
> - 合成表：定义一个成分或者信息之间的过程或关系

### onRuntimeAvailable

> 运行时与 JEI 通信，通过 IJeiRuntime 

# Ingredient

> Ingredient：几乎所有东西都可以作为 Ingredient ，一般是 ItemStack/FluidStack
>
> Ingredient List：物品，流体，以及任何绘制在屏幕 JEI 一侧的东西组成的列表

## 添加物品

### 自动加载

- 创造模式物品栏
- 注册的物品
- Block 物品（Item#getItemFromBlock 获得）

### 手动加载

- 将物品正确注册到游戏
- 若子类型丢失，确保其正确显示到创造模式物品栏
- 若物品位于创造模式物品栏，仍未出现在 JEI，可能是 JEI 认为其是同一物品，解决方法详见 物品子类型

## 隐藏 Ingredient

> 位于 register 方法中
>
> 添加到黑名单的物品仍会显示在合成表中

```java
// 将需要隐藏的物品添加到黑名单 
@Override
public void register(IModRegistry registry) {
    IIngredientBlacklist blacklist =
        registry.getJeiHelpers().getIngredientBlacklist();
    blocklist.addIngredientToBlacklist(...);
}
```



## 物品子类型

- 问题：使用 NBT 和 Capabilities 系统可以表示几乎无限的物品。合成时，可能要关注其中的某些数据

- 解决：使用 ISubtypeRegistry

  ```java
  @Override
  public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {
      subtypeRegistry.registerSubtypeInterpreter(
          MyItems.ingotRedstone,
          // 返回对关注的 meta，NBt等数据的描述
          // 都不关心返回 NONE
          itemStack -> "xxx");
  }
  ```

## 注册其他类型

> 注册其他类型
>
> 位于 registerIngredients

# 合成表

## 原版合成表

支持导入：

- 有序合成：ShapedOreRecipe, ShapedRecipes,
- 无需合成：ShapelessOreRecipe, ShapelessRecipes
- 酿造：VanillaBrewingRecipe，AbstractBrewingRecipe, BrewingRecipe, BrewingOreRecipe
- 药水：PotionHelper.isReagent
- 熔炉：Smelting，注册在 FurnaceRecipes 的其他物品
- 燃料i：Fuels，TileEntityFurnace.isItemFuel

## Mod 合成表

- Recipe Categories

  > 合成分类 已存在 Crafting，Smelting，Fuel，Brewing，Information
  >
  > 内置分类 Uid 存于 VanillaRecipeCategoryUid
  >
  > 用于绘制一类合成的界面布局

  ```java
  public static class MyCategory implements IRecipeCategory {
      private IDrawable mIcon;
      private IDrawable mBackground;
      public MyCategory(IGuiHelper helper) {
          // IGuiHelper 有一些用于绘制的方法
          mIcon = helper.createDrawableIngredient(MyItems.ingotRedstone);
          mBackground = helper.createDrawable(new ResourceLocation(MyMod.MODID, "texture/gui/jei.png"),
                  0, 0, 116, 54);
      }
      public static final String UID = 
          MyMod.MODID + ".jei.category";
      // uid
      @Override
      public String getUid() {
          return UID;
      }
      // 本地化标题
      // 显示在合成表 GUI 顶部
      @Override
      public String getTitle() {
          return I18n.format(
              MyMod.MODID + ":jei.category.title");
      }
      // Mod 名或 id，显示于合成表 Tooltip
      @Override
      public String getModName() {
          return MyMod.MODID;
      }
      // 显示背景
      @Override
      public IDrawable getBackground() {
          return mBackground;
      }
      // 图标
      @Nullable
      @Override
      public IDrawable getIcon() {
          return mIcon;
      }
      // 绘制
      @Override
      public void setRecipe(
          IRecipeLayout recipeLayout, 
          IRecipeWrapper recipeWrapper, 
          IIngredients ingredients) {
          // do something
          // 实例见那些 XxxCategory 类
      }
  }
  ```

  

- Recipe Wrappers

  > 显示单个配方的方式，确定输入输出
  >
  > 用于显示一类合成的信息，绘制合成步骤，项目描述
  >
  > 处理点击次数和按钮功能

- Recipe Handlers

  > 将合成表与合成分类关联，将该合成转换为 Wapper，并检查合成合法性

- 注册

  ```java
  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
      // 注册 Category
      IDrawable drawable = registry.getJeiHelpers().getGuiHelper().createDrawableIngredient(MyItems.redstoneApple);
      registry.addRecipeCategories(new MyCategory(drawable));
  }
  
  @Override
  public void register(IModRegistry registry) {
      // 注册 Handler
      registry.handleRecipes(
          RedstoneApple.class, 
          recipe -> ingredients -> {/*do something*/}, 
          MyCategory.UID);
      // 注册合成
      registry.addRecipes(collection, MyCategory.UID);
  }
  ```

  

## Mod 其他合成方式

> 向 JEI 添加其他合成方式或信息

```java
@Override
public void register(IModRegistry registry) {
    registry.addIngredientInfo(
        MyItems.redstoneApple, 
        () -> RedstoneApple.class,
        // 描述文本在语言文件中的 id，使用 \n 换行
        "my_mod:jei.description.redstone_apple"
    );
}

// en_us.lang
my_mod:jei.description.redstone_apple=A Redstone Apple
```

# 其他功能

- 指定类别合成工具

  > 在合成表左侧显示的合成工具

  ```java
  @Override
  public void register(IModRegistry registry) {
      // 注册合成工具
      registry.addRecipeCatalyst(Ingredient, Uids);
  }
  ```

- 合成表物品转移

  > 合成表右下角 "+" 按钮行为

  ```java
  @Override
  public void register(IModRegistry registry) {
      // 转移行为
      registry.getRecipeTransferRegistry().addRecipeTransferHandler(/* ... */);
  }
  ```

- 从其他 GUI 打开合成表

  > 从其他 GUI 点击打开合成表

  ```java
  @Override
  public void register(IModRegistry registry) {
      registry.addRecipeClickArea(
          /*gui class*/, 
          /*click area*/,
          /*uid*/);
  }
  ```

  