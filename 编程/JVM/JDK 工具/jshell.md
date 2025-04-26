#java9 

`````col
````col-md
flexGrow=2
===
![[Pasted image 20240806131511.png]]
````
````col-md
flexGrow=1
===

| 命令      | 说明                   |
| ------- | -------------------- |
| help    | 查看 JShell 命令的帮助      |
| exit    | 退出 jshell            |
| vars    | 列出所有变量               |
| list    | 查看已输入过的代码片段          |
| drop    | 移除已执行的代码             |
| edit    | 打开一个 GUI，可以批量执行代码    |
| imports | 查看所有的导入（`import` 语句） |
| save    | 保存代码                 |
| open    | 导入代码                 |

````
`````

JShell 是 Java 的一个 REPL 命令行环境，位于 `${jdk安装目录}$/bin/jshell.exe` ，并使用 `--start` 指定启动参数
* `--start DEFAULT`：仅导入默认的几个 import
* `--start PRINTING`：额外静态导入 `System.out.print`, `System.out.println`, `System.out.printf`

JShell 环境下，执行 Java 语句不必要输入 `;`

# API

通过 JShell API，可以模拟在 JShell 中执行一段代码的结果，代码片段是 String 字符串

| 类            | 获取                     | 作用             |
| ------------ | ---------------------- | -------------- |
| JShell       | `JShell.create()`      | 表示一个 JShell 会话 |
| SnippetEvent | `JShell#eval`          | 一次执行事件         |
| Snippet      | `SnippetEvent#snippet` | 一个代码片段         |

```java
import jdk.jshell.*;

void main() {
    // JShell 表示一个会话
    try (JShell shell = JShell.create()) {
        for (SnippetEvent event : shell.eval("int i = 100;")) {
            // Snippet 表示一个代码片段
            Snippet snippet = event.snippet();
            // Snippet: Snippet:VariableKey(i)#1-int i = 100;
            System.out.println("Snippet: " + snippet);
            // Kind: VAR
            System.out.println("Kind: " + snippet.kind());
            // Sub-Kind: VAR_DECLARATION_WITH_INITIALIZER_SUBKIND
            System.out.println("Sub-Kind: " + snippet.subKind());
            // Previous Status: NONEXISTENT
            System.out.println("Previous Status: " + event.previousStatus());
            // Current Status: VALID
            System.out.println("Current Status: " + event.status());
            // Value: 100
            System.out.println("Value: " + event.value());
        }
    }
}
```
