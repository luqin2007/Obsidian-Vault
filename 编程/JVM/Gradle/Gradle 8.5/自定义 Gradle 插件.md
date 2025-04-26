Gradle 插件编写教程。插件是封装好的构建逻辑
# 创建插件

插件可以位于其他项目之内，也可以单独创建一个项目。位于其他项目时可以位于：
- build 脚本中，此时当前脚本仅用于该脚本
- `buildSrc` 子项目：`projectDir/buildSrc/src/main`，当前项目内可见，需要配置 
	- 插件子项目中，应当在 `projectDir/buildSrc` 中创建 `build.gradle`

Gradle 插件的入口是一个实现了 `Plugin<Project>` 的类。当 Gradle 任务启动，加载该插件时，会调用 `apply` 方法。

```groovy
// 应用插件
apply plugin: GreetPlugin

// 创建插件
class GreetPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('hello').doLast {
            println "Hello from the GreetingPlugin"
        }
    }
}
```

```shell
# Hello from the GreetingPlugin
gradle -q hello
```
## 脚本配置

`Project` 对象带有一个 `extension` 属性，包含了所有插件的配置。我们可以创建一个[[Gradle 托管属性]]的接口作为属性；也可以直接声明一个返回普通类型的函数，但必须有实现，且在配置脚本中只读。

```groovy
// 包含 greeter，message 两个字符串属性
interface GreetPluginExtension {
    Property<String> getGreeter()
    Property<String> getMessage()
}

class GreetPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // 注册和获取脚本中 greeting {} 的配置，类型为 GreetPluginExtension
        // 后可以接一个 Action，可用于设置配置默认值
        GreetPluginExtension ext = 
            project.extensions.create("greeting", GreetPluginExtension)
        
        // 使用属性
        project.task('hello').doLast {  
            println "${ext.message.get()} from ${ext.greeter.get()}"  
        }
    }
}
```

然后就可以添加配置了

```groovy
greeting {
    greeter = "Gradle"
    message = "Hi"
}
```

```shell
# Hi from Gradle
gradle -q hello
```
## 独立项目配置

不管是 `buildSrc` 还是单独的项目，创建 `build.gradle`，配置依赖 `gradleApi()` 并配置 `gradlePlugin` 属性即可。

使用 `java-gradle-plugin` 插件会自动配置 Java 插件并引入 `gradleApi()` 依赖。在生成的 Jar 包中，也会自动插入插件的工件 id 和实现类信息

```groovy
plugins {
    id "java-gradle-plugin"
}

gradlePlugin {
    plugins {
        simplePlugin {
            id = "org.gradle_plugin.greeting"
            implementationClass = "org.gradle_plugin.greeting.GreetingPlugin"
        }
    }
}
```

- `simplePlugin`：插件名称
- `id`：工件 `id`，不允许使用 `org.gradle` 和 `org.gradleware` 开头
### 发布

可使用 Ivy 或 Maven 发布插件。可以发布到 [Gradle 插件](http://plugins.gradle.org/)
### 本地使用

可以在另一个项目中使用插件。为确定插件位置，需要设置 `pluginManagement` 配置本地插件所在仓库位置，之后即可像普通插件一样通过 `plugins` 引用插件。

*项目内 `buildSrc` 包内的插件不需要配置 `pluginManagement`*

```groovy
// settings.gradle
pluginManagement {
    repositories {
        maven {
            url = uri(repoLocation)
        }
    }
}
```
## 预编译脚本插件

Gradle 允许在 `src/main/groovy` 或 `src/main/kotlin` 编写脚本（`*.gradle` 或 `*.gradle.kts`）作为预编译插件使用于插件中。预编译插件将编译后与项目打包到一个 jar 包中。
- 预编译插件 id 不能以 `org.gradle` 开头
- 预编译插件 id 不能与内置插件同名

应用预编译插件需要知道插件 id，即被提供的文件名去掉 `.gradle` 及后面的东西。
- `src/main/groovy/java-library-convention.gradle`：id=java-library-convention
- `src/main/groovy/my.java-library-convention.gradle`：id=my.java-library-convention
## 测试

像编写普通单元测试一样创建测试类，`Project` 对象可以通过 `ProjectBuilder.builder().build()` 创建。
# 设计插件

- 结构
	- 使用二进制插件：需要重用或在独立项目共享的插件应使用二进制插件而不是脚本插件
	- 对构建的影响：注意插件在构建生命周期中的性能影响，可尝试使用 `gradle build --scan` 分析构建性能
	- 约定重于配置：使用合理的约定作为默认值，使大多数用户都不要进行额外的配置
	- 功能与约定分离：以 Java 插件为例，
		- `java base` 插件实现了一组独立的功能和通用概念，如 `SourceSet` 概念
		- `java` 插件使用了 `Java Base` 插件，划分了 `main`、`test` 等源代码集，并将 `base` 的功能组合成了 `classes`，`jar`，`javadoc` 等任务
		- `java` 插件的约定实现可以满足大部分人的要求，若不符合可退回到 `java base` 插件
- 技术
	- 使用静态语言实现插件：`Java`，`Kotlin` 等静态语言效率和静态 `Groovy` 比动态 `Groovy` 等动态语言更高
		- 插件实现与测试语言无关，可使用动态 `Groovy` 进行测试
	- 尽可能使用公共 API：`gradleApi()` 依赖包含完整 Gradle 运行时，应仅使用公共 API，不使用内部 API，因为内部 API 可能随时发生变化
	- 尽量减少外部库依赖
# 实现插件
## 使用开发插件

Gradle 插件项目的 `build.gradle` 可以直接使用 `java-gradle-plugin` 插件，样板代码详见[[#创建插件#独立项目配置]]。
## 使用自定义任务类型

自定义任务时尽量使用增强任务而不是简单的临时任务，即使用带自定义任务类型，借助属性公开配置的自定义任务。

```java
public abstract class LatestArtifactVersion extends DefaultTask {

    @Input
    public abstract Property<String> getCoordinates();

    @Input
    public abstract Property<String> getServerUrl();

    @TaskAction
    public void resolvedLatestVersion() {
        // do something
    }
}
```

之后在实际项目中使用：

```groovy
// build.gradle
tasks.register("latestVersionMavenCentral", LatestArtifactVersion) {
    coordinates = 'commons-lang:commons-lang'
    serverUrl = 'http://repo1.maven.org/maven2'
}
```

对于依赖输入和输出的任务，应尽量使用增量任务，即对任何一个输入和输出都使用对应注解标注。这样，当输入没有变化时，Gradle 可以跳过该任务。

可以在插件中注册任务，并使用外部的配置设置。

```java
// 额外配置
public interface BinaryRepositoryExtension {

    Property<String> getCoordinates();

    Property<String> getServerUrl();
}

// 在 apply 中注册任务
BinaryRepositoryExtension repo = project.getExtensions().create("binaryRepo", BinaryRepositoryExtension.class);
project.getTasks().register("latestArtifactVersion", LatestArtifactVersion.class, task -> {
    task.getServerUrl().set(repo.getServerUrl());
    task.getCoordinates().set(repo.getCoordinates());
});
```

```groovy
// build.gradle
binaryRepo {
    coordinates = 'commons-lang:commons-lang'
    serverUrl = 'https://repo1.maven.org/maven2'
}
```
## DSL

- 使用 `@Nest` 声明嵌套输入
- 使用 `Action` 声明接收代码段

```java
public abstract class SiteExtension {

    public abstract RegularFileProperty getOutputDir();

    @Nested
    public abstract CustomData getCustomData();

    public void customData(Action<? super CustomData> action) {
        action.execute(getCustomData());
    }
}
```
### DSL 容器

DSL 容器即使用户可以定义多个类型相同的命名数据对象，如 `java` 插件中的 `sourceSets` 内可以设置 `main`，`test` 等不同名称的 SourceSet 对象。

这类容器使用 `NamedDomainObjectContainer` 类型，通过 `ObjectFactory#domainObjectContainer` 创建。

1. 定义容器内元素的类型

```java
public abstract class ServerEnvironment {

    private final String name;

    @Inject
    public ServerEnvironment(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract Property<String> getUrl();
}
```

2. 在 `apply` 中创建 `NamedDomainObjectContainer` 并注册，并使用

```java
// 创建、注册容器
ObjectFactory objects = project.getObjects();
// NamedDomainObjectContainer<ServerEnvironment>
var environments = objects.domainObjectContainer(ServerEnvironment.class,
        name -> objects.newInstance(ServerEnvironment.class, name));
project.getExtensions().add("environments", environments);

// 使用容器
environments.all(environment -> {
    String env = environment.getName();
    String taskName = "deployTo" + env.substring(0, 1).toUpperCase() + env.substring(1);
    // Deploy.class 是一个自定义任务，内含一个 Property<String> getUrl()
    project.getTasks().register(taskName, Deploy.class, 
        task -> task.getUrl().set(environment.getUrl()));
});
```

3. 在 `build.gradle` 中使用

```groovy
// build.gradle
environments {
    firstServer {
        url = "www.123.com"
    }

    secondServer {
        url = "www.456.com"
    }

    lastServer {
        url = "www.789.com"
    }
}
```

```shell
# 执行任务
gradle deployToSecondServer
```
## 与其他插件交互

`project.getPlugins()` 可以获取 `PluginContainer` 对象，包含项目使用的插件
- `PluginContainer#apply(JavaPlugin.class)`
	- 强制要求项目应用 `java` 插件
	- 类似函数还有 `PluginContainer#apply(String id)`
- `PluginContainer#withType(JavaPlugin.class, javaPlugin -> { ... })`
	- 当项目依赖于 `java` 插件时执行后面的 Action 代码
	- 类似函数还有 `PluginContainer#withId(String id, Action)`
- `PluginContainer#hasPlugin`：当前项目是否包含某插件
- `PluginContainer#findPlugin`
	- 根据类或 id 查找插件，若不存在则返回 `null`
	- `getPlugin`，`getAt`：若不存在则抛出异常
## 与构建交互

允许插件访问构建中的功能状态，检查用户是否请求了特定 Gradle 功能并检查其是否开启。
- 报告或统计信息中生成状态信息
- 检查与实验性 Gradle 功能的兼容性并启用

只需要向插件注入 `BuildFeatures` 对象即可：（Gradle 8.5）

```java
public abstract class MyPlugin implements Plugin<Project> {

    @Inject
    protected abstract BuildFeatures getBuildFeatures();

    @Override
    public void apply(Project project) {
        // do something
    }
}
```

- `requested`：报告功能状态
- `active`：开启、禁用不兼容功能
# 测试插件

手动测试可以使用 Gradle 复合构建测试，有两种方法
- `--include-build` 参数
- `settings.gradle` 中使用 `includeBuild` 方法

单元测试、集成测试、功能测试都可以使用任何基于 JVM 的对应测试框架实现。
## 自动化测试

自动测试目录结构基本为

```dirtree
- src
  - functionalTest
    - groovy: 功能测试
  - integrationTest
    - groovy: 集成测试
  - main
  	- java: 插件代码
  - test
    - groovy: 单元测试
```

在 `build.gradle` 中创建特定代码集，Gradle 将自动创建编译任务，只需要再创建一个测试任务即可

```groovy
// build.gradle
dependencies {
    integrationTestImplementation(project)
}

repositories {
    maven {
        name = "Nexus tencentyun"
        url = "https://mirrors.cloud.tencent.com/nexus/repository/maven-public/"
    }
    mavenCentral()
}

def integrationTest = sourceSets.create('integrationTest')

def integrationTestTask = tasks.register('integrationTest', Test) {
    description = 'Run the integration test'
    group = 'verification'
    testClassesDirs = integrationTest.output.classesDirs
    classpath = integrationTest.runtimeClasspath
    mustRunAfter(tasks.named('test'))
}

tasks.named('check') {
    dependsOn(integrationTestTask)
}
```

配置测试框架。Gradle 没有内置任何测试框架，也不限制使用测试框架。

```groovy
// build.gradle
dependencies {
    integrationTestImplementation platform('org.spockframework:spock-bom:2.2-groovy-3.0')
    integrationTestImplementation 'org.spockframework:spock-core'
    integrationTestRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.withType(Test).configureEach {
    useJUnitPlatform()
}
```
# 发布插件

发布到 Gradle Plugin：

1. 在 [Gradle - Login](https://plugins.gradle.org/user/login) 注册账号
2. 登录后，生成 `API Keys`

![[Pasted image 20240202140202.png]]

3. 将 API 配置到本地 Gradle 中： `HOME_DIR/.gradle/gradle.properties`

```
gradle.publish.key=...
gradle.publish.secret=...
```

4. 配置项目：通过 `plugin-publish` 插件发布

```groovy
// build.gradle
plugins {
    id "com.gradle.plugin-publish" version "1.2.1"
}

group = "org.gradle_plugin_greeting"
version = "1.0"

gradlePlugin {
    website = "项目网站"
    vcsUrl = "源代码仓库网站"

    // 配置插件
    plugins {
        插件名 {
            id = "id"
            implementationClass = "主类"
            displayName = "插件显示名称"
            description = "插件说明"
            tags.set(["插件", "Tags", "列表"])
        }
    }
}
```

![[Pasted image 20240202141112.png]]
5. 发布

```shell
# 直接发布
./gradlew publishPlugins
# 发布验证
./gradlew publishPlugins --validate-only
# 手动设置 API Key
./gradlew publishPlugins -Pgradle.publish.key=<key> -Pgradle.publish.secret=<secret>
```
## shadow 依赖

`plugin-publish` 插件默认发布为 Fat jar，若要使用 `shadow` 依赖需要应用 `com.github.johnrengelman.shadow` 插件
## 发布仓库

通过 `maven-publish` 插件可以自定义发布地址，可用于发布到本地仓库

```groovy
// build.gradle
plugins {
    id "maven-publish"
}

publishing {
    repositories {
        maven {
            name = 'localPluginRepository'
            url = '../local-plugin-repository'
        }
    }
}
```
# 参考

```cardlink
url: https://docs.gradle.org/8.5/userguide/custom_plugins.html
title: "Developing Custom Gradle Plugins"
host: docs.gradle.org
```
