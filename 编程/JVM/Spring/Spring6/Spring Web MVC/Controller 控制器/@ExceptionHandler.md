注解注册中，使用 WebMvcConfigurer 配置，使用 `@ExceptionHandler` 注解一个方法，可用于处理当前控制器中的异常信息。

也可以使用注解或使用 XML 配置一个 `ExceptionHandlerExceptionResolver` 实例

* 方法的 `value` 属性可以标记几个可接受的异常类型，否则使用方法参数的异常类型

  ```java
  @ExceptionHandler
  public String handleException(Exception e) {
      return "error";
  }
  ```

* 方法的形参列表为一个 `Exception` 的子类，也可以没有参数

  ```java
  @ExceptionHandler(IOException.class)
  public String handleException() {
      return "error";
  }
  ```

* 返回值可以是 `ModelAndView`，`View`，`String`，`Model` 和 `void` 等


