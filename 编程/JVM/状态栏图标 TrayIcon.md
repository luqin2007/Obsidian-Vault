
状态栏图标由图标和弹出菜单组成
- 图标：`Image` 对象，可通过 `Toolkit.getDefaultToolkit().getImage()` 获取
- 菜单：一个 `PopupMenu` 对象

```java
// 加载图标
URL resource = AppEnvironment.class.getResource("icon.png");
Image image = Toolkit.getDefaultToolkit().getImage(resource);
// 创建菜单
PopupMenu menu = new PopupMenu();
TrayIcon ti = new TrayIcon(image, "Tooltip 信息", menu);
ti.setImageAutoSize(true);
SystemTray.getSystemTray().add(ti);
```

> [!error] 状态栏图片显示异常
> 调用 `TrayIcon` 的 `setImageAutoSize` 方法即可
> 
> ```java
> TrayIcon ti = ...;
> ti.setImageAutoSize(true);
> ```

> [!error] 中文乱码
> ![[../../../_resources/images/20250106_263.png]]
> 需要在 java 虚拟机配置中使用 `-Dfile.encoding=GBK` 参数即可
> - idea：在运行配置的虚拟机选项中添加

![[../../../_resources/images/Pasted image 20250106030602.png]]

![[../../../_resources/images/Pasted image 20250106030548.png]]
# 消息提示

`displayMessage` 方法可以弹出一个系统消息，Windows 下即右下角的提示