#java16 

`instanceof` 时可直接附加强转操作

```java
Object o = "This is a string";
if (o instanceof String str) {
    // str 已经是 String 类型了
    System.out.println(str.length());
}
```
