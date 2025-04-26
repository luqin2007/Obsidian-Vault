# 行列初始化

在 `XAML` 中，添加 `Grid.ColumnDefinitions` 和 `Grid.RowDefinitions` 元素，有几行/几列就添加几个

```XML
<Grid>
    <!-- 定义三行三列 -->
    <Grid.ColumnDefinitions>
        <ColumnDefinition />
        <ColumnDefinition />
        <ColumnDefinition />
    </Grid.ColumnDefinitions>
    <Grid.RowDefinitions>
        <RowDefinition />
        <RowDefinition />
        <RowDefinition />
    </Grid.RowDefinitions>
</Grid>
```

每行、每列属性在 `ColumnDefinition` 和 `RowDefinition` 中添加

Width，Height，MaxWidth 等宽高度可选值为：
- `n*`：按比例均分所有剩余空间
	- `*`：`n=1` 时省略 1，默认
- `auto`：根据内部元素大小决定
- `n`，`npx` 等：固定长度

```xml
<!-- 行高匹配内部元素高度 -->
<RowDefinition Height="Auto"/>

<!-- 两个列按 1:4 均分（空白）空间 -->
<Grid.ColumnDefinitions>
    <ColumnDefinition Width="1*"/>
    <ColumnDefinition Width="4*"/>
</Grid.ColumnDefinitions>
```
# 设置元素位置

设置位置使用 `Grid` 类提供的静态变量

```csharp
// 向 Grid 中添加 widget 组件
grid.Children.Add(widget);
// 设置位于第 n 列
Grid.SetColumn(widget, n);
// 设置位于第 n 行
Grid.SetRow(widget, n);
```