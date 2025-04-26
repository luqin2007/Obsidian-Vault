Fastjson 内置了部分 ASM 库，只保留必要部分。

```java
public class MyBean {
    public String id;
    public String name;
    public int score;
}
// MyBean{id=1, name=张三, score=100}
// {"id":"1","name":"张三","score":100}
```

```java
public static void main(String[] args) {
    String json = "{\"id\":\"1\",\"name\":\"张三\",\"score\":100}\n";
    MyBean bean = JSON.parseObject(json, MyBean.class);
    System.out.printf("MyBean{id=%s, name=%s, score=%d}", bean.id, bean.name, bean.score);
    String beanJson = JSON.toJSONString(bean);
    System.out.println(beanJson);
}
```

Fastjson 使用 ASM 为对应 Bean 生成对应类，规避反射操作。

> [!warning] 思考：真的有必要吗？相比 jackson 或 gson 确实快了，但应该无感

可通过 `arthas` 导出查看。

```reference fold
file: "@/_resources/codes/bytecodes/fastjson/com.alibaba.fastjson2.reader.ORG_1_3_MyBean.java"
```

```reference fold
file: "@/_resources/codes/bytecodes/fastjson/com.alibaba.fastjson2.writer.OWG_1_3_MyBean.java"
```
