在 `fxml` 文档中使用 `fx:controller` 指定一个 Controller 类。

在类中，可以直接引用 JavaFX 控件
- `public` 变量可以省略 `@FXML` 注解

```xml
<GridPane fx:controller="com.example.javahotkeynotify.ui.TestFormController">
    <Label fx:id="title" GridPane.columnIndex="1" text="{}"/>  
    <Label fx:id="process" GridPane.columnIndex="1" GridPane.rowIndex="1"/>  
    <Label fx:id="path" GridPane.columnIndex="1" GridPane.rowIndex="2"/>  
    <Label fx:id="keys" GridPane.columnIndex="1" GridPane.rowIndex="3"/>
</GridPane>
```

```java
public class TestFormController {
    public Label title;
    public Label process;
    public Label path;
    @FXML private Label keys;
}
```

使用 `@FXML` 注解的 `initialize()` 方法将在 fxml 文件加载完成后自动调用，用于初始化