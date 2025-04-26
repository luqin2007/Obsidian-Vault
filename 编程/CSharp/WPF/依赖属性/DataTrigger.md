根据绑定目标的特定属性更改元素属性，用于在 XAML 中根据已有控件的属性设置值

1. 在要设置的控件中添加对应的 `Style` 组件，内部可以通过 `Setter` 设置默认值

```xml
<Image Name="Icon">
    <Image.Style>
        <!-- 默认情况下 Visibility=Visible -->
        <Setter Property="Visibility" Value="Visible" />
    </Image.Style>
</Image>
```

2. 创建 `Style.Triggers` 触发器元素，在内部添加可以添加 `DataTrigger` 元素

```xml
<Image Name="Icon">
    <Image.Style>
        <Style TargetType="Image">
            <Setter Property="Visibility" Value="Visible" />
            <Style.Triggers>
                <DataTrigger ...>
                <!-- ... -->
                </DataTrigger>
            </Style.Triggers>
        </Style>
    </Image.Style>
</Image>
```

3. `DataTrigger` 中，使用 [[数据绑定|Binding]] 设置关联的属性，`Value` 表示当关联属性为某个值时应用属性 

```xml
<Image Name="Icon">
    <Image.Style>
        <Style TargetType="Image">
            <Setter Property="Visibility" Value="Visible" />
            <Setter Property="Height" Value="45" />
            <Setter Property="Margin" Value="10" />
            <Style.Triggers>
                <!-- 数据绑定到当前元素的 Source 属性 -->
                <!-- 目标值为 null -->
                <DataTrigger 
                    Binding="{Binding Path=Source, RelativeSource={RelativeSource Self}}" 
                    Value="{x:Null}">
                    <!-- ... -->
                </DataTrigger>
            </Style.Triggers>
        </Style>
    </Image.Style>
</Image>
```

4. 在 `DataTrigger` 内创建 `Setter` 元素设定属性集

```xml
<Image Name="Icon">
    <Image.Style>
        <Style TargetType="Image">
            <Setter Property="Visibility" Value="Visible" />
            <Style.Triggers>
                <DataTrigger Binding="{Binding Path=Source, RelativeSource={RelativeSource Self}}" Value="{x:Null}">
                    <!-- 当 Source=null 时，设置 Visibility=Hidden -->
                    <Setter Property="Visibility" Value="Hidden" />
                </DataTrigger>
            </Style.Triggers>
        </Style>
    </Image.Style>
</Image>
```
# MultiDataTrigger

`DataTrigger` 仅能根据一个属性或数据源判断，使用 `MultiDataTrigger` 针对多个数据源判断

```xml
<Window.Resources>
  <Style TargetType="ListBoxItem">
    <Style.Triggers>、	
      <MultiDataTrigger>
        <MultiDataTrigger.Conditions>
          <Condition Binding="{Binding Path=Name}" Value="Portland" />
          <Condition Binding="{Binding Path=State}" Value="OR" />
        </MultiDataTrigger.Conditions>
        <Setter Property="Background" Value="Cyan" />
      </MultiDataTrigger>
    </Style.Triggers>
  </Style>
```