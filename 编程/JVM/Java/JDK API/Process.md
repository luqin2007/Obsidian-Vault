`Process` 表示由 JVM 启动的本地进程
# 丢弃输出
#java9

在使用 `Process` 执行命令行时，可以直接将输出丢弃

```java
void main() throws Exception {
    System.out.println("Using Redirect.INHERIT");
    new ProcessBuilder("java", "--version")
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start().waitFor();
    System.out.println();
    System.out.println("Using Redirect.DISCARD");
    // 无输出
    new ProcessBuilder("java", "--version")
            .redirectOutput(ProcessBuilder.Redirect.DISCARD)
            .redirectError(ProcessBuilder.Redirect.DISCARD)
            .start().waitFor();
}
```
# ProcessHandler
#java9 

# ProcessHandler

`ProcessHandler` 类表示一个任意的本地进程，允许查询进程状态并管理进程。

Process 可通过 `toHandler()` 方法转换为 `ProcessHandler`

```java
void main() {
    ProcessHandle handle = ProcessHandle.current();
    ProcessHandle.Info info = handle.info();
    System.out.println(STR."""
CurrentProcess:
    pid=\{handle.pid()}
    cmd=\{info.command().orElse("<null>")}
    args=\{info.arguments().map(Arrays::toString).orElse("[null]")}
    line=\{info.commandLine().orElse("<null>")}
    start=\{info.startInstant().map(Instant::toString).orElse("<null>")}
    user=\{info.user().orElse("<null>")}""");
}
```
