`ToolProvider` 用于在程序运行时对源代码进行编译，或生成其文档。
# JavaCompiler

## 源文件集合

源文件集合是任意 `JavaFileObject` 的可迭代对象，其中 `JavaFileObject` 表示 Java 源文件
* 重写 `getCharContent` 方法用于获取源代码文本
* 若使用字符串作为源码，URI 使用 `string:///` 协议
* 文件路径是包名（`.` 替换成 `/`）+类名+扩展名，扩展名可以使用 `JavaFileObject.Kind.SOURCE`

```java
// 准备源码
String classPath = "org/example/test/Test";
String source = createClassSource();
// 创建源文件路径，这里使用 string 协议表示使用字符串源码
JavaFileObject sourceFile = new SimpleJavaFileObject(
        URI.create("string:///" + classPath + JavaFileObject.Kind.SOURCE.extension),
        JavaFileObject.Kind.SOURCE) {
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return source;
    }
};
// 创建源代码集合
List<? extends JavaFileObject> compilationUnits = List.of(sourceFile);
```
## 编译源代码

使用 `JavaCompiler#getTask` 创建编译任务，`task.call()` 执行并返回编译是否成功。

`JavaCompiler#getTask` 接收方法的参数如下：

|参数|说明|
| --------------------| --------------------------------------------------|
|out|警告、错误信息的输出位置，`null` 表示使用默认 `System.err` |
|fileManager|文件管理器，可以通过 `compile.getStandardFileManager` 方法获取|
|diagnostics|接收警告、异常等信息的具体内容，可以使用 `DiagnosticCollector` 对象|
|options|编译器参数，`null` 表示无参数|
|classes|待编译类名，`null` 表示全部源码类|
|compilationUnits|前面准备的源文件集合，也是编译单元集合|
|diagnosticListener|异常信息（诊断信息）侦听器|

`StandardJavaFileManager` 对象是 Java 编译器的一部分，它提供了文件管理器的标准实现，可以通过他获取编译结果。通常使用 `compile.getStandardFileManager` 方法获取，各个参数都可以为 `null`，表示使用默认值。

```java
JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
Boolean compileSucceed = task.call();
```
## 编译结果

`task.call()` 返回编译是否成功。若编译成功，编译后的字节数组可以通过 `fileManager` 获取；若编译失败，异常信息可以通过 `diagnostics` 获取。

```java
if (task.call()) {
    // 编译成功，获取编译结果
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    FileObject output = fileManager.getFileForOutput(null, "org/example/test", "Test.class", null);
    output.openInputStream().transferTo(bos);
    byte[] bytes = bos.toByteArray();
}

// 编译错误，检查异常
// 也包括其他 WARN，INFO 等信息
for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
    // 诊断错误
}
```
# DocumentationTool

从源代码和注释中提取文档。生成的文件仍需要通过 `StandardJavaFileManager` 获取

```java
// 创建源代码集合
List<? extends JavaFileObject> compilationUnits = List.of(sourceFile);
// 生成文档
DocumentationTool documentationTool = ToolProvider.getSystemDocumentationTool();
StandardJavaFileManager fileManager = documentationTool.getStandardFileManager(null, null, null);
DocumentationTool.DocumentationTask task = documentationTool.getTask(null, fileManager, null, null, null, compilationUnits);
Boolean generateSucceed = task.call();
if (generateSucceed) {
    FileObject output = fileManager.getFileForOutput(null, "org/example/test", "Test.html", null);
    // do something
}
```
