`@ControllerAdvice` 注解是一个 `@Component` 方式注解，即注入到 bean 类中。使用该注解表示为控制器提供支持。

在该类中定义的 `@InitBinder`，`@ModelAttribute`，`@ExtensionHandler` 方法将应用于所有控制器。
