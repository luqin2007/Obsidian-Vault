>[!success] ASM 是字节码改写的事实标准，cglib 是动态代理的事实标准

使用 cglib 动态代理的步骤如下：
1. 实现 `MethodInterceptor` 接口的 `intercept` 方法，实现拦截逻辑
2. 调用 `Enhancer.create` 方法生成目标代理类的子类实例
3. 通过代理类实例调用目标方法

> [!warning] Java9+ 需要增加以下参数：`--add-opens java.base/java.lang=ALL-UNNAMED` 允许未命名模块访问 `java.lang` 包中的 `defineClass` 方法

```java
public class Person {
    public void doJob(String name) {
        System.out.println("The class is " + getClass().getName());
        System.out.println("Doing job:" + name);
    }

    public void eat() {}

    public void sleep() {}
}
```

```java
public static void main(String[] args) {
    // 1. 实现一个 MethodInterceptor 接口，用于拦截代理方法
    MethodInterceptor interceptor = (obj, method, objects, methodProxy) -> {
        System.out.println(">>>> before intercept");
        // 这里调用原对象方法
        Object o = methodProxy.invokeSuper(obj, objects);
        System.out.println(">>>> end intercept");
        return o;
    };
    // 2. 生成 Person 类子类实例
    Person person = (Person) Enhancer.create(Person.class, interceptor);
    // 3. 调用目标方法
    person.doJob("coding");
}
```

```
>>>> before intercept
The class is org.example.cglib.Person$$EnhancerByCGLIB$$2b765f74
Doing job:coding
>>>> end intercept
```

![[../../../../../_resources/images/cglib 2024-08-02 23.21.58.excalidraw]]
cglib 通过生成子类方式产生动态代理，可以通过设置将生成的类保存到磁盘中

```java
System.setProperty(
    DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,
    "./out/cglib-debug-location");
```

![[image-20240320022009-bfcjwu4.png]]

名为 `Person$$EnhancerByCGLIB$$xxxxxxxx` 类即生成的子类，`this.CGLIB$CALLBACK_0` 即创建的 `MethodInterceptor`

```java
public class Person$$EnhancerByCGLIB$$2b765f74 extends Person implements Factory {
    // ...

    public final void doJob(String var1) {
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
        if (var10000 == null) {
            CGLIB$BIND_CALLBACKS(this);
            var10000 = this.CGLIB$CALLBACK_0;
        }

        if (var10000 != null) {
            var10000.intercept(this, CGLIB$doJob$0$Method, new Object[]{var1}, CGLIB$doJob$0$Proxy);
        } else {
            super.doJob(var1);
        }
    }


    // ...
}
```

为避免调用反射拦截方法，cglib 为动态代理生成各自的 FastClass 类存于 `MethodProxy` 中，使用 `switch` 记录方法索引，根据索引调用各自的方法：

```java
public class Person$$FastClassByCGLIB$$756b1530 extends FastClass {
    // 根据方法签名获取方法的索引
    public int getIndex(Signature var1) {
        return switch (var1.toString()) {
            case "sleep()V" -> 3;
            case "eat()V" -> 1;
            case "doJob(Ljava/lang/String;)V" -> 0;
            case "main([Ljava/lang/String;)V" -> 2;
            case "equals(Ljava/lang/Object;)Z" -> 4;
            case "toString()Ljava/lang/String;" -> 5;
            case "hashCode()I" -> 6;
            default -> -1;
        };
    }

    // 根据方法名称、形参类型获取方法的索引
    public int getIndex(String var1, Class[] var2) {
        switch (var1) {
            case "toString":
                switch (var2.length) {
                    case 0: return 5;
                }
                break;
            case "equals":
                switch (var2.length) {
                    case 1: if (var2[0].getName().equals("java.lang.Object")) return 4;
                }
                break;
            case "eat":
                switch (var2.length) {
                    case 0: return 1;
                }
                break;
            case "main":
                switch (var2.length) {
                    case 1: if (var2[0].getName().equals("[Ljava.lang.String;")) return 2;
                }
                break;
            case "doJob":
                switch (var2.length) {
                    case 1: if (var2[0].getName().equals("java.lang.String")) return 0;
                }
                break;
            case "sleep":
                switch (var2.length) {
                    case 0: return 3;
                }
                break;
            case "hashCode":
                switch (var2.length) {
                    case 0: return 6;
                }
        }

        return -1;
    }

    // 通过索引避免反射
    public Object invoke(int var1, Object var2, Object[] var3) throws InvocationTargetException {
        Person var10000 = (Person) var2;
        try {
            switch (var1) {
                case 0:
                    var10000.doJob((String) var3[0]);
                    return null;
                case 1:
                    var10000.eat();
                    return null;
                case 2:
                    Person.main((String[]) var3[0]);
                    return null;
                case 3:
                    var10000.sleep();
                    return null;
                case 4:
                    return var10000.equals(var3[0]);
                case 5:
                    return var10000.toString();
                case 6:
                    return var10000.hashCode();
            }
        } catch (Throwable var4) {
            throw new InvocationTargetException(var4);
        }

        throw new IllegalArgumentException("Cannot find matching method/constructor");
    }
}
```
