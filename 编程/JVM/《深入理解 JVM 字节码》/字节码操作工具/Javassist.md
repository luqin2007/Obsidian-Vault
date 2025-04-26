> [!note] Maven 库：`org.javassist:javassist`

![[../../../../../_resources/images/Javassist 2024-08-02 22.17.02.excalidraw]]

* 新增方法

  ```java
  ClassPool cp = ClassPool.getDefault();
  // 载入 MyClass.class 路径
  cp.insertClassPath("org.example.MyClass.class");
  CtClass ct = cp.get("org.example.MyClass");
  // 创建 public void foo(int, int)
  CtMethod foo = new CtMethod(voidType, "foo", new CtClass[] {intType, intType}, ct);
  foo.setModifiers(Modifier.PUBLIC);
  // 设置函数体 sout("hello")
  foo.setBody("System.out.println(\"hello\");");
  ct.addMethod(foo);
  // 输出
  ct.writeFile("./out");
  ```

* 修改方法：`CtMethod` 类可以用于修改方法
    * `setBody`：将一段 Java 代码翻译成字节码作为函数体
    * `insertBefore`，`insertAfter` 系列在方法开头和结尾插入语句
    * `instrument` 方法可以按某些方式替换字节码

参数引用使用 `$`，常用的有：

|符号|说明|foo(符号) 表达的内容|
| ---------------| ------------------------------------| ----------------------|
| `$0` | `this`，静态方法不可用| `foo(this)` |
| `$1`，`$2`，...|当前局部变量表第 1，2，...个参数| `foo(第 i 个参数)` |
| `$args` |一个 `Object[]`，包含所有参数| `foo(new Object[]{第一个参数, 第二个参数, ...})` |
| `$$` |将所有参数展开，即表示为 `第一个参数, 第二个参数, ...` 的形式| `foo(第一个参数, 第二个参数, ...)`<br />|
| `$cflow(函数名)` |某函数递归调用的深度||
| `$_` |在 `insertAfter` 用，表示返回值||
还有其他的但用得不多，如 `$r` 表示返回值类型，`$w` 用于包装器类型，`$sig` 表示类型为 `Class` 的参数类型数组，`$type` 表示返回值 Class，`$class` 表示修改类的 Class

‍
