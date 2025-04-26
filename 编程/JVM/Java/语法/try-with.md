`try-with` 可以在结束时自动释放 `AutoCloseable` 接口管理的资源， `try` 后的括号内可以放一条或多条语句，使用 `;` 分割

`````col
````col-md
```java
try(AutoCloseable resource = /*...*/) {
    // do something
}
```
````
````col-md
```java
AutoCloseable resource = /*...*/;
try {
    // do something
} finally {
    // ...
    // 省略一次异常处理
    resource.close();
}
```
````
`````
`try-catch` 也是可以作为一个正常的 `try` 块使用的

```java
try(AutoCloseable resource = /*...*/) {
    // do something
} catch (IOException e) {
    // do something while occur IOException exception
}
```

#java9 允许 `try` 括号中使用变量。

```java
AutoCloseable resource = /*...*/;

// 允许直接使用变量而不是语句
try(resource) {
    // do something
}
```
