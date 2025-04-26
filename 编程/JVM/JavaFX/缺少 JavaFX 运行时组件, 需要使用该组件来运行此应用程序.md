> [!error] 错误: 缺少 JavaFX 运行时组件, 需要使用该组件来运行此应用程序

先下载对应 Java 版本的 JavaFX SDK

```cardlink
url: https://gluonhq.com/products/javafx/
title: "JavaFX - Gluon"
description: "Roadmap Release GA Date Latest version Minimum JDK Long Term Support Extended or custom support Details 24 March 2025 early access 21 no 23 September 2024 23.0.1 (October 2024) 21 no upon request details 22 March 2024 22.0.2 (July 2024) 17 no upon request details 21 September 2023 21.0.5 (October 2024) 17 yes upon request […]"
host: gluonhq.com
image: https://i0.wp.com/gluonhq.com/wp-content/uploads/2015/01/gluon_logo.png?fit=781%2C781&ssl=1
```

解压后配置虚拟机选项

![[../../../_resources/images/Pasted image 20250105224456.png]]

在虚拟机选项中填入：`--module-path "<javafx-sdk>\lib" --add-modules javafx.controls,javafx.fxml`