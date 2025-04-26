`@InitBinder` 注解 `Controller` 类中的一个方法，用于初始化 `WebDataBinder`，可以接受一个 `WebDataBinder` 参数，且返回值为 `void`。

`@InitBinder` 注解的函数的参数除 `WebDataBinder` 外，还可以接受大部分 `@RequestMapping` 函数<span data-type="text" parent-style="color: var(--b3-card-info-color);background-color: var(--b3-card-info-background);">可以接受的参数</span>，除了模型特性和 `BindResult`、`Error` 等

此时，可以通过 `registerCustomEditor` 方法注册 `PropertyEditor`

通常，可以将 `WebRequest` 或 `Locale` 实例传递给该方法，便于本地化

```java
@Controller
public class FixedDepositController {

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.registerCustomEditor(Date.class, 
                new CustomDateEditor(new java.text.SimpleDateFormat("yyyy-MM-dd"), false));
    }
```


