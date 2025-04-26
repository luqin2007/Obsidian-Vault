当源码中包含中文时，Gradle 可能无法编译，这是由于默认情况下 Windows 命令行使用的是 GBK 编码，在 `build.gradle` 中指定一下编译时的编码即可。

```groovy
tasks.withType(JavaCompile) {
    options.encoding = "UTF-8"
}
```

或 properties 中

```
org.gradle.jvmargs=-Dfile.encoding=UTF-8
```