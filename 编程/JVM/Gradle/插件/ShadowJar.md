ShadowJar 是一个辅助 java/groovy 打包设置的 Gradle 插件，可以方便的控制 `jar` 任务打包时的行为，常用于以下情况：
- 创建可执行的 Jar 包
	- 包含所有依赖
	- MANIFEST 中包含可执行主类
- 将依赖包包含进 Jar 中防止依赖冲突
- 可选的将其他内容打入包中

导入插件详见 [Gradle - Plugin: com.github.johnrengelman.shadow](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)。应用后，`ShadowJar` 将自动添加（修改）以下内容：

- `shadowJar` 任务：
	- 自动包含 `main` 源代码集
       - 自动绑定所有 `runtimeClasspath` 配置的依赖
	- 默认 `classifier` 属性为 `'all'`
	- `Manifest` 默认包含
		- 所有 `jar` 任务中定义的 `manifest`
		- 根据 `shadow` 配置增加 `Class-Path`
		- 排除以下任何 `Jar` 索引或加密签名
			- `META-INF/INDEX.LIST`
			- `META-INF/*.SF`
			- `META-INF/*.DSA`
			- `META-INF/*.RSA`
- 增加 `shadow` 配置块
- 注册 `shadow` 组件，可用于 `maven-publish`
# 基础配置

- 输出文件名：默认 `destinationDir`，`archiveBaseName`，`appendix`，`archiveVersion` 和 `extension` 与 `Jar` 任务一致，而 `archiveClassifier` 默认为 `all`

```groovy
// build/lib/shadow.jar
shadowJar {
    archiveBaseName.set('shadow')
    archiveClassifier.set('')
    archiveVersion.set('')
}
```

- 运行时依赖：一般来说，可执行 jar 包默认包含所有依赖；若需要依赖外部包，需要在 `MANIFEST.MF` 中指定。`ShadowJar` 提供 `shadow` 依赖配置以提供该功能

```groovy
dependencies {
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    // implementation("com.google.code.gson:gson:2.9.0")
    shadow "com.google.code.gson:gson:2.9.0"
}
```

将会生成 `META-INF/MANIFEST.MF`

```
Class-Path: gson-2.9.0.jar
```

- `Manifest`：除了 `shadow` 依赖配置外，`shadowJar` 还包含其他方式修改 `MANIFEST.MF`

`shadowJar` 继承于 `jar`，任何在标准 `jar` 任务中配置的 `manifest` 都将继承
    
```groovy
jar {
    manifest {
        attributes 'Class-Path': '/libs/a.jar'
    }
}
```
    
也可以从其他继承自 `Jar` 的任务中获取，利用 `inheritFrom` 方法
    
```groovy
task testJar(type: Jar) {
  manifest {
    attributes 'Description': 'This is an application JAR'
  }
}

shadowJar {
  manifest {
    // 使用 testJar 任务的 manifest 配置
    inheritFrom project.tasks.testJar.manifest
  }
}
```
# 内容过滤

通过多条 `exclude` 和 `include` 添加/删除打包内容，`exclude` 优先级更高，可使用 ANT 风格的模式匹配多个文件
# 依赖配置

默认下，`runtimeClasspath` 配置引入的依赖将全部打包到输出

```groovy
dependencies {
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    // gson 2.9.0 将全部添加到输出的 jar 包中
    implementation("com.google.code.gson:gson:2.9.0")

    // https://mvnrepository.com/artifact/org.objenesis/objenesis
    // objenesis 不会加入
    testImplementation("org.objenesis:objenesis:3.2")
}
```

可在 `shadowJar` 中配置其他类型

```groovy
shadowJar {
    configurations = [project.configurations.compileClasspath]
}

dependencies {
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    // gson 2.9.0 将全部添加到输出的 jar 包中
    implementation("com.google.code.gson:gson:2.9.0")

    // https://mvnrepository.com/artifact/org.objenesis/objenesis
    // objenesis 也会加入
    compileOnly("org.objenesis:objenesis:3.2")
}
```

通过 `shadowJar.dependencies` 可将导入的依赖排除，但不适用于依赖传递

```groovy
dependencies {
    implementation 'org.apache.logging.log4j:log4j-core:2.11.1'
}

shadowJar {
    dependencies {
        // log4j-core 将被排除
        exclude(it.dependency('org.apache.logging.log4j:log4j-core:2.11.1'))
    }
}
```

可通过模式匹配或省略部分来匹配

```groovy
shadowJar {
    dependencies {
        exclude(it.dependency('org.apache.logging.log4j:log4j-core:.*')) // olk
        exclude(it.dependency('org.apache.logging.log4j:log4j-core')) // no version, ok
        exclude(it.dependency(':log4j-core:2.11.1')) // ok
        exclude(it.dependency('org.apache.logging.log4j::')) // ok
        exclude(it.dependecy {
            it.moduleGroup == 'org.apache.logging.log4j' // ok
        }
    }
}
```
# Jar 合并

Shadow 允许自定义 Jar 输出过程。Shadow 会在输出 Jar 时，将文件流流经一系列 `Transformer`，在 `shadowJar` 中使用 `transform` 设置

```groovy
import com.github.jengelman.gradle.plugins.shadow.transformers.Transformer
import com.github.jengelman.gradle.plugins.shadow.transformers.TransformerContext
import org.apache.tools.zip.ZipOutputStream

class MyTransformer implements Transformer {

    @Override
    boolean canTransformResource(FileTreeElement element) { true }

    @Override
    void transform(TransformerContext context) {}

    @Override
    boolean hasTransformedResource() { true }

    @Override
    void modifyOutputStream(ZipOutputStream os, boolean preserveFileTimestamps) {

    }
}

class MyTransformer2 implements Transformer {

    String parameter

    @Override
    boolean canTransformResource(FileTreeElement element) { true }

    @Override
    void transform(TransformerContext context) {}

    @Override
    boolean hasTransformedResource() { true }

    @Override
    void modifyOutputStream(ZipOutputStream os, boolean preserveFileTimestamps) {

    }
}

shadowJar {
    // 接受一个 Transformer 对象或 Transformer 实现类
    transform(MyTransformer)
    transform(MyTransformer2) {
        parameter = 'Param1'
    }
    transform(new MyTransformer2(parameter: 'param2'))
}
```

其中，内置合并类 `ServiceFileTransformer` 可用于当多个依赖使用相同的服务名时，将其提取出来放置到项目 jar 包中

```groovy
// Merging Service Files
shadowJar {
    // 等同于 transform(ServiceFileTransformer.class)
    mergeServiceFiles()
}
```

但是由于 Groovy 的描述语法与标准 Java 服务描述文件不同，`ServiceFileTransformer` 会忽略 Groovy 扩展模块描述文件。

这些文件位于 `META-INF/services/org.codehaus.Groovy.runtime.ExtensionModule`，可使用 `mergeGroovyExtensionModules` 添加

```groovy
shadowJar {
    mergeServiceFiles()
    // groovy extension
    mergeGroovyExtensionModules()
}
```

默认 Groovy 扩展服务文件保存于 `META-INF/services`，可通过修改 `mergeServiceFiles` 的 `path` 属性更改

```groovy
// Merging Service Files
shadowJar {
    mergeServiceFiles() {
        path = 'META-INF/custom_services'
    }
}
```

同时，可以使用 `exclude` 筛选合并内容

```groovy
// Merging Service Files
shadowJar {
    mergeServiceFiles() 
        exclude 'META-INF/services/com.acme.*'
    }
}
```

使用 `append` 可以向对应文件追加某些内容

```groovy
shadowJar {
    append 'test.properties'
}
```

对于 XML，可能需要特殊的转换器 `XmlAppendingTransformer` 用于解析和追加内容

```groovy
// Appending a XML File
import com.github.jengelman.gradle.plugins.shadow.transformers.XmlAppendingTransformer

shadowJar {
    transform(XmlAppendingTransformer.class) {
        resource = 'properties.xml'
    }
}
```
# 导入重定位

ShadowJar 通过 ASM，可以将一个依赖包的包名更改，并同步修改任何包名和导入语句，防止依赖冲突

```groovy
shadowJar {
     relocate 'junit.framework', 'shadow.junit'
}
```

同时允许使用 `include`, `exclude` 对重定位类进行配置，支持 Ant 路径匹配或正则（使用 `%regex[]` 包围）

```groovy
// Configuring Filtering for Relocation
shadowJar {
   relocate('junit.textui', 'a') {
       exclude 'junit.textui.TestRunner'
   }
   relocate('junit.framework', 'b') {
       include 'junit.framework.Test*'
   }
   relocate('org.foo', 'a') {
       include '%regex[org/foo/.*Factory[0-9].*]'
   }
}
```

ShadowJar 支持自动重定位包，但为了清晰起见，已在 4.0.0 中移除。若要使用任务自动重定义，可定义一个 `ConfigureShadowRelocation` 类型任务

```groovy
import com.github.jengelman.gradle.plugins.shadow.tasks.ConfigureShadowRelocation

task relocateShadowJar(type: ConfigureShadowRelocation) {
    target = tasks.shadowJar
    prefix = 'myapp' // default is 'shadow'
}

tasks.shadowJar.dependsOn tasks.relocateShadowJar
```
# 最小化包

通过 `minimize`，ShadowJar 可以移除未使用的依赖，尽可能地减少包体积

```groovy
shadowJar {
    minimize()
}
```

允许使用 `exclude` 排除移除的依赖，使其打包进 Jar 中

```groovy
shadowJar {
    minimize {
        exclude(it.dependency('org.scala-lang:.*:.*'))
        exclude(it.project(':api'))
    }
}
```
# 可重复构建

ShadowJar 支持 Gradle 生成 byte-to-byte 的 Jar

```groovy
tasks.withType(AbstractArchiveTask) {
    preserveFileTimestamps = false
    reproducibleFileOrder = true
}
```

副作用是，所有文件拥有一致的时间戳
# 自定义任务

允许自定义 `ShadowJar` 类型任务，常用于对不同包选择不同依赖

```groovy
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

task testJar(type: ShadowJar) {
    from sourceSets.test.output
    archiveClassifier.set('test')
    configurations = [project.configurations.testImplementations]
}
```