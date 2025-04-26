# 目录结构

Gradle 目录分为两部分 - Gradle 用户目录和项目目录。前者存放 Gradle 本体需要的公共配置文件和缓存文件，后者为被 Gradle 管理的项目。
![[Pasted image 20240202164330.png]]
## Gradle 用户目录

Gradle 用户目录由 `GRADLE_USER_HOME` 环境变量配置，默认为 `~/.gradle`

| 名称                   | 类型 | 说明                          |
| ---------------------- | ---- | ----------------------------- |
| caches                 | 目录 | 版本无关的缓存                |
| caches/版本号          | 目录 | 用于特定 Gradle 版本的缓存    |
| caches/jars-n          | 目录 | 共享缓存（依赖项等），n 为数字          |
| caches/modules-n       | 目录 | 共享缓存（依赖项等），n 为数字          |
| daemon                 | 目录 | Gradle 守护进程的注册表和日志 |
| init.d | 目录 | 保存全局初始化脚本                |
| jdks                   | 目录 | 由工具链下载的 JDK            |
| wrapper/dists          | 目录 | 下载的 Gradle Wrapper 发行版  |
| gradle.property                       | 文件     | 全局 Gradle 配置                              |
### 自动缓存清理

默认每天由 Gradle 守护进程运行一次清理程序，包括：
- `cache/版本号` 目录中，正式版 30 天不使用则清除，快照版 7 天不使用则清除
- `cache/jars-n` 等共享缓存，远程 30 天未使用删除，本地生成的缓存 7 天删除
- `wrapper/dists` 发行版未被使用时删除

8.0 之后，通过 `init.d/cache-settings.gradle` 可以设置缓存清理策略：

```groovy
beforeSettings { settings ->
    settings.caches {
        // 未使用正式版 Wrapper 缓存删除日期
        releasedWrappers.removeUnusedEntriesAfterDays = 45
        // 未使用快照版 Wrapper 缓存删除日期
        snapshotWrappers.removeUnusedEntriesAfterDays = 10
        // 未使用共享缓存（如依赖项等）删除日期，如依赖库等
        downloadedResources.removeUnusedEntriesAfterDays = 45
        // 未使用共享缓存（构建时生成）删除日期，如工件转换等
        createdResources.removeUnusedEntriesAfterDays = 10
        // 缓存清理程序运行配置
        //   DEFAULT: 默认定期在后台执行（24h 每次）
        //   DISABLED: 不自动执行
        //   ALWAYS: 每次回话后执行
        cleanup = Cleanup.DISABLED
    }
}
```

>[!note]
>由于缓存清理策略仅 8.0 之后存在，可以考虑使用 `GradleVersion.current() >= GradleVersion.version(8.0)` 进行判断，然后导入该策略。其他对版本有要求的设置也可以这样进行。
>
>1. 将缓存清理策略设置文件放在非 `init.d` 根目录中，如 `init.d/8.0/cache-settings.gradle`
>2. 在 `init.d/cache-settings` 中将其导入：
>
>```groovy
>if (GradleVersion.current() >= GradleVersion.version(8.0)) {
>    apply from: '8.0/cache-settings.gradle'
>}
>```
### 缓存标记

Gradle 8.1 开始支持使用 `CACHEDIR.TAG` 文件设置缓存标记，详见[[Cache Directory Tagging Specification|缓存标记规范]]。

若需要移除标记，使用 `markingStrategy` 设置

```groovy
// /init.d/cache-settings.gradle
beforeSettings { settings ->
    settings.caches {
        markingStrategy = MarkingStrategy.NONE
    }
}
```
## 项目目录

Gradle 项目可以包含一个根项目和若干子项目。一个 Gradle 管理的项目目录的典型结构如下：

```dirtree
- .gradle: 用于特定项目的缓存目录
  - 4.8: 用于特定 Gradle 版本的缓存目录
  - ...
- build: 构建目录，build 任务产生的文件
- gradle
  - wrapper: Gradle Wrapper 及其配置
- gradle.properties: 特定项目的 Gradle 配置属性
- gradlew: 特定项目的 Gradle 执行脚本，可用于启动 gradle
- gradlew.bat: 特定项目的 Gradle 执行脚本（Windows）
- settings.gradle: 项目配置文件，可定义子项目
- subproject-one: 子项目目录
  - build.gradle: 子项目构建脚本
- subproject-two: 子项目目录
  - build.gradle: 子项目构建脚本
```

一个项目可以通过 `gradle init` 初始化，Gradle 自动创建对应的单项目结构。
# 多项目构建

Gradle 支持多项目构建，又称多模块构建，允许一个大型项目中包含多个子项目、子模块。Gradle 多项目组织结构为：
![[Pasted image 20240206154720.png]]
Gradle 社区对多项目文件结构有两种标准：

![[Pasted image 20240206155945.png]]

- 使用 `buildSrc` 的多项目结构：`buildSrc` 目录结构类似一个普通项目，其中包含所有构建逻辑
- 复合构建：`build-logic` 目录是一个构建目录，包含所有构建逻辑

在多项目的项目文件中，根项目目录下 `settings.gradle` 用于配置子项目。
## 项目路径

通过 `:` 分割，可以导航到对应的项目：

```groovy
:sub-project-1
```

使用 `gradle projects` 可以查看项目结构

![[Pasted image 20240206161842.png]]
## 任务

- 直接调用 `gradle 任务名` 执行的是所有项目中同名的所有任务
- 使用完全限定名 `:项目:子项目...:任务名` 可以调用指定子项目的任务
## 构建与测试

- `build`：编译与构建完整或单个项目
- `buildNeeded`，`buildDependents`：按依赖关系构建多项目
# 生命周期
## 任务图

![[Pasted image 20240206163842.png]]
Gradle 在执行任何任务时，都会创建任务图。任务图是一种有向无环图（DAG），反映了任务之间的依赖关系和输入、输出关系。如图，左侧是某个项目的任务图，右侧为 Java 插件 `build` 任务的任务图。
## 任务执行

Gradle 执行任务步骤为：

1. 初始化：检查 `settings.gradle` 创建 `Settings` 对象，评估哪些项目将被构建，创建每个项目的 `Project` 对象
2. 配置：执行每个部分的 `build.gradle` 文件，为必要的任务创建任务图
3. 执行：根据依赖关系确定执行顺序和是否并行执行

![[Pasted image 20240206164515.png]]
# settings.gradle

> [!note]
> `settings.gradle` 实际上是一个 Groovy 脚本（如果使用 Kotlin 脚本，则为 `settings.gradle.kts`），包含整个语言的运行时上下文，可以包含包括循环、判断等复杂逻辑，应对其视为脚本而不是配置文件。
## Settings 对象

![[Pasted image 20240206165553.png]]

`settings.gradle` 是构建的入口，用于生成 `Settings` 对象，确认全局配置尤其是确认所有项目。

有关 `Settings` 对象详见[Settings - Gradle DSL Version 8.5](https://docs.gradle.org/8.5/dsl/org.gradle.api.initialization.Settings.html)。全局上下文即 settings 对象。

```groovy
// 设置 rootProject 对象的 name 属性为 root_project
rootProject.name = "root_project"
```

一些常用的属性和方法有：

| 名称          | 类型 | 说明                             |
| ------------- | ---- | -------------------------------- |
| `buildCache`  | 属性 | 构建 cache 设置                  |
| `plugins`     | 属性 | 应用到 Settings 的插件容器       |
| `rootDir`     | 属性 | 构建根目录                       |
| `rootProject` | 属性 | 根任务                           |
| `settings`    | 属性 | 返回当前 Settings 对象本身       |
| `include()`   | 方法 | 设置当前构建的项目（包括子项目） |
| `includeBuild()`              | 方法     | 设置复合构建包含的构建                                 |
## 脚本结构
### 插件

```groovy
// 设置插件仓库位置
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

// 应用 Settings 插件
// 这个插件是 Gradle 自带的，可以自动下载 JDK
plugins {
    id 'org.gradle.toolchains.foojay-resolver-convention' version '0.5.0'
}

// 插件配置
```

应用到 Settings 的插件有两种：
- 仅能应用到 Settings 的插件，如示例中的 `foojay-resolver-convention`
- 需要在各个项目之间统一版本、共享配置的插件
### 项目

```groovy
// 根项目配置
rootProject.name = 'gradle_demo'

// 设置子项目公共的仓库位置
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

// 设置包含的子项目
include('sub-project-a')
include('sub-project-b')
include('sub-project-c')
```

- `rootProject` 表示根项目的配置，类型为 Project，必须设置 `rootProject.name` 根项目名称
- `dependencyResolutionManagement.repositories` 为所有子项目依赖的公共仓库
- 使用 `include` 引入子项目
# build.gradle

> [!note]
> `build.gradle` 实际上是一个 Groovy 脚本（如果使用 Kotlin 脚本，则为 `build.gradle.kts`），包含整个语言的运行时上下文，可以包含包括循环、判断等复杂逻辑，应对其视为脚本而不是配置文件。

`build.gradle` 用于配置一个项目的 Project 对象。每个 `build.gradle` 对应一个 `Project` 对象。有关 `Project` 的具体信息详见 [Project - Gradle DSL Version 8.5](https://docs.gradle.org/8.5/dsl/org.gradle.api.Project.html)。一些常见的属性和方法有：

| 名称 | 类型 | 说明 |
| ---- | ---- | ---- |
| `name` | `String` | 项目目录名 |
| `path` | `String` | 项目目录的完整限定名 |
| `description` | `String` | 项目说明 |
| `plugins` |  | 插件 |
| `dependencies` | `DependencyHandler` | 项目依赖配置 |
| `repositories` | `RepositoryHandler` | 依赖仓库配置 |
| `layout` | `ProjectLayout` | 提供项目的几个重要的位置 |
| `group` | `Object` | 项目的组 |
| `version` | `Object` | 项目版本 |
| `uri()` | 方法 | 解析 URI 路径，相对 `path` |
| `task()` | 方法 | 在当前项目中创建一个任务 |
## 插件

```groovy
// 应用插件
plugins {
    id 'org.jetbrains.kotlin.jvm' version '1.9.22'
    id 'application'
}

// 插件配置
application {
    mainClass = "com.example.Main"
}
```

Gradle 内部插件（如 application，java）可以直接使用 `id` 添加，其他插件应当同时使用 `id` 和 `version` 应用。
## 依赖

```groovy
// 依赖仓库
repositories {
    maven {
        url 'https://maven.aliyun.com/repository/public/'
    }

    maven {
        url 'https://maven.aliyun.com/repository/gradle-plugin'
    }
    mavenCentral()
}

// 依赖库
dependencies {
    implementation 'com.google.guava:guava:32.1.1-jre'
    testImplementation 'org.jetbrains.kotlin:kotlin-test'
}
```

使用 `repositories` 属性管理依赖仓库，使用 `dependencies` 管理依赖库。
## 任务

任务是 Gradle 实际执行的基本工作，包括但不限于：
- 编译
- 单元测试
- 打包成 Jar 或 War 等

任务通常是通过插件定义，但也可以在 `build.gradle` 中创建，通过 `TaskContainer#register` 方法注册。`Project` 的 `tasks` 属性即项目的 `TaskContainer#register`。*应避免使用 `create` 添加任务*

```groovy
tasks.register("zip-reports", Zip) {
    from 'Reports'
    include '*'
    archiveBaseName = 'Reports.zip'
    destinationDirectory = file('/dir')
}
```

也可以通过 `named` 查找已有任务并修改。

```groovy
tasks.named('test') {
    useJUnitPlatform()
}

tasks.named('javadoc').configure {
    exclude 'app/Internal*.java'
    exclude 'app/internal/*'
    exclude 'app/internal/*'
}
```
## 额外属性

Gradle 对象，包括 `Project`，`Task`，`SourceSets`，都支持附加额外属性用于保存一些自定义数据，通过 `ext` 属性（Kotlin 为 `extra`，使用代理）进行访问。

```groovy
ext {
    springVersion = '3.1.0'
    emailNotification = 'build@email'
}

println springVersion
```

```kotlin
val springVersion by extra("3.1.0")
val emailNotification by extra { "build@email" }

println(extra["springVersion"])
```
## 自动导入

Gradle 隐式导入 `org.gradle` 包下大多数类
# 自定义任务

![[Pasted image 20240208001445.png]]
任务大多来自插件（`plugins`），部分来自 `gradle` 构建脚本。任务一般分为两类：*（？？？）*
- 可操作任务（Actionable task）：包含一系列活动，在构建时运行，如 `compileJava`
- 生命周期任务（Lifecycle task）：没有绑定的活动，通常生命周期任务依赖于一个或多个可操作任务，如 `assemble`，`build`

可以直接通过 `gradle` 或 `gradlew` 运行任务。
- 运行 `gradle tasks` 或 `.\gradlew tasks` 可以查看所有任务
- 运行 `gradle 任务名` 或 `.\gradlew 任务名` 可以直接运行某个任务

可以通过 `tasks.register(name)` 在构建脚本（`build.gradle`）中添加任务。自定义任务的创建和注册详见[[#build.gradle#任务]]。

```groovy
tasks.register('hello') {
    doLast {
        println 'Hello World!'
    }
}
```

`doLast`，`doFirst` 为任务执行的具体代码，先运行 `doFirst` 后运行 `doLast`，多次同名调用之间按顺序执行（`doFirst` 是倒序的）

```groovy
tasks.register('taskExec') {
    doLast { println 'last 1' }
    doLast { println 'last 2' }
    doFirst { println 'first 1' }
    doFirst { println 'first 2' }
    doLast { println 'last 3' }
    doFirst { println 'first 3' }
}
```

![[Pasted image 20240210224830.png]]
## 任务描述

通过设置 `group` 属性表示任务的任务组，`description` 属性表示任务的说明。任务组和任务说明与执行无关，仅在 `gradle tasks` 等命令查找任务时显示。

```groovy
tasks.register('hello') {
    group = "Custom Task"
    description = "This is a task print \"Hello World\""
    doLast {
        println 'Hello World!'
    }
}
```

```shell
.\gradlew tasks
```

![[Pasted image 20240210163445.png]]

另外，可以通过 `.\gradlew help --task 任务名` 查看任务的具体信息和使用方法

![[Pasted image 20240210163655.png]]
## 任务依赖

通过 `dependsOn` 可以设置任务之间的依赖关系。后面既可以接一个 `Task` 的名称（字符串），也可以接收一个 `Task` 对象。该对象可以通过 `register` 返回值获取，也可以通过 `tasks.任务名` 获取。

```groovy
tasks.register('hello') {
    doLast {
        println 'Hello World!'
    }
}

tasks.register('afterHello') {
    dependsOn 'hello'
    doLast {
        println 'After hello~~'
    }
}
```

```shell
./gradlew afterHello
```

![[Pasted image 20240210164416.png]]

甚至可以在运行时通过 `named` 动态修改依赖。
## 任务类型

所有 Gradle 任务都是 `Task` 类的对象。使用 `tasks.register` 时可以设置 `Task` 的类型。

自定义任务类可以继承 `DefaultTask` 类，并使用 `@TaskAction` 注解标记任务代码入口（函数名随意）
- `@Input`：任务输入，标记一个返回值为[[Gradle 托管属性]]或普通属性的方法
- `@OutputFile`，`@OutputFiles`：输出文件（列表），标记一个返回文件类型的[[Gradle 托管属性]]或普通对象的方法

```groovy
abstract class HelloTask extends DefaultTask {

    @TaskAction
    void hello() {
        println 'Hello from HelloTask'
    }
}

tasks.register('helloTask', HelloTask)
```

![[Pasted image 20240210225636.png]]

若任务存在输入输出，可以使用 `@Input`，`@OutputFile` 等注解标记。Gradle 默认开启**增量编译**，即前后两次编译中，若输入完全相同，则跳过该任务，直接使用上次生成的结果。

```groovy
abstract class HelloTask extends DefaultTask {

    @Input
    abstract Property<String> getMessage();

    @OutputFile
    abstract RegularFileProperty getOutputFile();

    @TaskAction
    void hello() {
        def file = getOutputFile().getAsFile().get()
        if (!file.isFile()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        file.write(getMessage().get())
        println "Write [${getMessage().get()}] to [${file}]"
    }
}

tasks.register('helloTask', HelloTask) {
    message = "Hello from HelloTask"
    outputFile = file("/myfile.txt")
}
```

![[Pasted image 20240210231149.png]]
## 内置任务

- GroovyDoc
- Zip
- Jar
- Copy
- JacocoReport
- Sign
- Delete
# 插件

Gradle 本身只是一个可执行框架，通过插件（`plugin`）扩展其功能，如实现 Java 的编译等。插件一般具有以下功能：
- 扩展 Gradle 模型，如扩展和增加新的 DSL 配置
- 根据约定配置项目，如新任务、默认配置等
- 应用特定配置，如添加组织依赖，标准等

根据开发和维护主体不同，插件分为以下三种：
- 核心插件，由 Gradle 开发和维护
- 社区插件，社区开发并上传到 Maven 或 Gradle Plugin 的插件
- 本地插件，用户创建的本地插件项目

根据实现形式，插件分为以下三种：
- 二进制插件，实现 `Plugin` 接口或在 Groovy/Kotlin DSL 中声明的插件，这类插件通常会打包成 jar，相当于运行其 `apply()` 方法
- 脚本插件，额外的构建脚本，通常是本地文件
## 应用插件

使用 `plugins` 代码块应用插件。该块的上下文为[PluginDependenciesSpec](https://docs.gradle.org/8.5/javadoc/org/gradle/plugin/use/PluginDependenciesSpec.html)对象，且：
- 只能在 `build.gradle`，`settings.gradle` 中使用
- 必须在所有其他块之前，`buildscript` 块与 `import` 导入除外
- 插件名、版本号必须是字符串常量或字面量
### 核心插件

直接在 `plugins` 块中使用插件名或 `id(插件名)` 即可。

```groovy
plugins {
    application // 等效于 id 'application'
    id 'java'   // 等效于 java
}
```
### 社区二进制插件

类似核心插件，但需要手动指定版本

```groovy
plugins {
    id 'com.jfrog.bintray' version '1.8.5'
}
```

如果有多个子项目，通常要求依赖的版本相同。此时可以在外部 `build.gradle` 中进行配置；如果某个依赖不会在所有子项目中使用，可以使用 `apply false` 设置其不会被应用。在使用时，直接使用 id 即可

```groovy
// settings.gradle
// 设置所有子项目的插件
include 'hello'
include 'goodbye'

// build.gradle
// 配置插件版本，但并不应用
plugins {
    id 'com.example.hello' version '1.0.0' apply false
    id 'com.example.goodbye' version '1.0.0' apply false
}
```

```groovy
// hello/build.gradle
// 应用 hello 插件，版本为外部 build.gradle 配置的版本
plugins {
    id 'com.example.hello'
}

// goodbye/build.gradle
// 应用 goodbye 插件，版本为外部 build.gradle 配置的版本
plugins {
    id 'com.example.goodbye'
}
```
### buildSrc 插件项目

在当前项目中，可以创建一个 `buildSrc` 模块作为构建插件。应用时，只使用 `id` 即可

```groovy
// buildSrc/build.gradle
// 插件配置
gradlePlugin {
    plugins {
        myPlugin {
            id = 'my-plugin'
        }
    }
}

// build.gradle
// 应用插件
plugins {
    id 'my-plugin'
}
```
### buildscript 代码块

该代码块主要作用有：
- 设置用于插件的依赖和仓库
- 声明当前文件中哪些插件可用

```groovy
buildscript {
    repositories {  // this is where the plugins are located
        mavenCentral()
        google()
    }
    dependencies { // these are the plugins that can be used in subprojects or in the build file itself
        classpath group: 'commons-codec', name: 'commons-codec', version: '1.2' // used in the task below
        classpath 'com.android.tools.build:gradle:4.1.0' // used in subproject
    }
}
```
### 脚本插件

使用 `apply plugin:插件类` 导入。假设有脚本插件：

```groovy
// other.gradle
// 这个文件中存在一个脚本插件
public class Other implements Plugin<Project> {
    @Override
    void apply(Project project) {
        // ...
    }
}
```

```groovy
// build.gradle
// 应用插件

// 1. 将 other.gradle 文件导入
apply from: 'other.gradle'
// 2. 应用插件
apply plugin: Other
```
### 旧版方式

传统的 Gradle 插件应用方式仍然可用，但不推荐了
- `apply plugin: '插件id`
- `apply plugin: 插件主类`

```groovy
// build.gradle
apply plugin: 'java'
// 等效于
apply plugin: JavaPlugin
```
## 插件管理

插件管理块 `pluginManagement` 必须在初始化时使用，可用于管理全局插件

- `settings.gradle` 中，且必须为其第一个块（`import` 除外）

```groovy
// settings.gradle
pluginManagement {
    plugins {
        // 这里可以用于设置所有项目及子项目的版本号
        // helloPluginVersion 在 gradle.properties 中
        id 'com.example.hello' version "${helloPluginVersion}"
    }
    repositories {
    }
    dependencies {
    }
}
```

- `init.gradle` 中，且位于初始化脚本中

```groovy
settingsEvaluated { settings ->
    settings.pluginManagement {
        plugins {
        }
        repositories {
        }
        dependencies {
        }
    }
}
```
### repositories

插件仓库

```groovy
pluginManagement {
    repositories {
        // 插件仓库
        maven { url ... }
        gradlePluginPortal()
        ivy { url ... }
    }
}
```
### plugins

插件版本配置，此处只会配置版本号，不会直接应用到项目中。具体项目中直接使用 `id` 应用。

```groovy
pluginManagement {
    plugins {
        // 这里可以用于设置所有项目及子项目的版本号
        // helloPluginVersion 在 gradle.properties 中
        id 'com.example.hello' version "${helloPluginVersion}"
    }
}

// build.gradle
// 在项目中直接使用 id 应用
plugins {
    id 'com.example.hello'
}
```
### resolutionStrategy

解析规则，可以自定义插件解析方式。

```groovy
pluginManagement {
    resolutionStrategy {
        // 遍历每个插件
        eachPlugin {
            // 若插件 namespace 为 com.example
            if (requested.id.namespace == 'com.example') {
                // 应用 com.example:sample-plugins:1.0.0 插件
                useModule('com.example:sample-plugins:1.0.0')
            }
        }
    }
}
```
## 版本目录

允许使用别名引入插件

```toml
// gradle/libs.versions.toml
[versions]
intellij-plugin = "1.6"

[plugins]
jetbrains-intellij = { id="org.jetbrains.intellij", version.ref="intellij-plugin" }
```

```groovy
// build.gradle
plugins {
    alias(libs.plugins.jetbrains.intellij)
}
```
## 编写插件

只要实现了 `Plugin<Project>` 接口的类便可以作为插件的入口。该类可以是抽象的。

```groovy
// 自定义任务
abstract class CreateFileTask extends DefaultTask {
    @Input
    abstract Property<String> getFileText();

    @Input
    String getFileName() {
        return 'myfile.txt'
    }

    @OutputFile
    File getMyFile() {
        return new File(fileName)
    }

    @TaskAction
    void action() {
        myFile.createNewFile()
        myFile.write(fileText.get())
    }
}

// 自定义插件
abstract class MyPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.tasks.register('createFileTask', CreateFileTask) {
            group = 'From MyPlugin'
            description = "Create myfile.txt in the current directory"
            fileText = "HELLO FROM MY PLUGIN"
        }
    }
}

// 应用插件
apply plugin: MyPlugin
```

详见[[自定义 Gradle 插件]]
# 参考

```cardlink
url: https://docs.gradle.org/8.5/userguide/userguide.html
title: "Gradle User Manual"
host: docs.gradle.org
```
