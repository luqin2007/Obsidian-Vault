`@RequestMapping` 注解适用于控制类和方法。注解控制类时，可用于指定基路径

```reference
file: "@/_resources/codes/spring-web/requestmapping/src/main/java/com/example/mybank/controller/FixedDepositControllerImpl.java"
start: 22
end: 24
```

|`@RequestMapping` 参数|说明|
| -----------------------| --------------------------------------------------------------------------------------------------|
|`value`、`path`|子路径，允许使用通配符，`**` 表示匹配所有子路径（带有 `/`）|
|`method`|请求方式，也可以简化成 `@GetMapping`、`@PostMapping` 等|
|`params`|查询参数：<br />`params="x"`，`params="!x"`：参数中包含或不包含 x<br />`params="x=a"`，`params="x!=a"`：参数 x 等于或不等于 a <br />|
|`consumes`|根据请求的 Content-Type 请求头判断<br />`consumes="application/json"`：请求数据类型包含 `application/json`<br />`consumes="!application/json"`：请求数据类型不包含 `application/json`<br />|
|`produces`|根据响应可接受的 Content-Type 判断，规则与 `consumes` 相同|
|`headers`|根据请求头判断<br />`Content-Type=application/json` 表示 `ContentType` 值为 `application/json`<br />`Content-Type!=application/json` 表示 `ContentType` 值非 `application/json`<br />`From` 表示包含 `From` 头<br />`!Cache-Control` 表示不包含 `Cache-Control` 头<br />|

被 `@RequestMapping` 注解的函数参数可以包含：
* `HttpServletRequest`：可获取访问请求
* `HttpServletResponse`：返回的响应，可通过此向响应写入任意信息
* `HttpSession`：Session 会话
* `SessionStatus`：会话缓存控制
* `java.security.Principal`
* `BindingResult`、`Error`：数据绑定和校验的结果
* `org.springframework.ui.Model` 用于收集模型数据，相当于 `ModelMap` 部分
* 通过 `@RequestParam` 注解标注的[[请求参数]]
* 通过 `@PathVariable` 和 `@MatrixVariable` 注解标注的参数，可以从访问路径中解析[[路径参数]]

被 `@RequestMapping` 注解的函数返回值可以是：
* `ModelAndView`：视图名称和模型数据。
	* 模型数据可以使用 `ModelMap`
	* 当值为普通类时，`key` 为首字母小写的类名
	* 若为集合或数组时，`key` 附加 `List` 后缀
	* 视图名将通过 `ViewResolver` 解析为一个视图资源文件

```reference
file: "@/_resources/codes/spring-web/requestmapping/src/main/java/com/example/mybank/controller/FixedDepositControllerImpl.java"
start: 33
end: 44
```

* 字符串：视图名称
	* · `redirect:` 开头的字符串，调用后重定向到后面的地址
    * 任意字符串：指定视图名。此时可以使用传入 `Model` 参数指定模型数据； `redirect` 表示跳转到某个路由路径

```reference
file: "@/_resources/codes/spring-web/requestmapping/src/main/java/com/example/mybank/controller/FixedDepositControllerImpl.java"
start: 46
end: 50
```

* `org.springframework.web.servlet.View`
* `Callable`、`ListenableFuture`、`DeferredResult`：[[异步处理]]的结果，异步处理的结果应为前面几个类型。
* `void`：通过参数直接写入 `HttpServletResponse` 时不需要再返回任何值