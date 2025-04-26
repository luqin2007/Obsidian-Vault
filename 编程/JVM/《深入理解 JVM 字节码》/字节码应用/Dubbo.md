# Dubbo

Dubbo 是阿里的一款高性能、轻量级 RPC 框架。在 Consumer 端调用 Service 接口时，实际使用的是动态代理。默认动态代理是 Javassist 的，也可以切换成 JDK 动态代理。

>[!note] RPC：Remote Procedure Call，远程调用

服务接口 UserService：

```java
public class UserService {

    String getNameById(int userId) {
        return "name#" + userId;
    }
}

```

```java
// $echo 可用于检查服务是否正常
EchoService echoService = (EchoService) UserService;
String result = echoService.$echo("hello")
assert("hello".equals(result));

```

自动生成 proxy0：

```java
public class proxy0 extends UserService implements ClassGenerator$DC, EchoService {
  
    public static Method[] method;
    private InvocationHandler handler;

    public proxy0(InvocationHandler handler) {
        this.handler = handler;
    }

    public proxy0() {
    }
  
    public String getNameById(int userId) {
        return (String) handler.invoke(this, method[0], new Object[]{userId});
    }

    @Override
    public Object $echo(Object message) {
        return handler.invoke(this, method[1], new Object[]{ message });
    }
}
```

<iframe src="/widgets/widget-excalidraw/" data-src="/widgets/widget-excalidraw/" data-subtype="widget" border="0" frameborder="no" framespacing="0" allowfullscreen="true" style="width: 1157px; height: 297px;"></iframe>

‍
