# 初始化项目

在一个空目录中执行 `gradle init`，根据提示即可创建一个项目。本次测试项目为：

![[Pasted image 20240211140102.png]]

生成的目录结构为：

![[Pasted image 20240211140315.png]]

- `app` 目录为项目 `app` 的目录。*`app` 项目实际为根项目的一个子项目*
	- `app/src` 为源码目录，包括源码和测试源码
	- `app/build.gradle.kts` 为 `app` 项目的构建脚本

```kotlin
// 导入 application 插件，添加对 Java CLI 应用的构建支持
plugins {
    application
}

// 查找依赖的仓库为 mavenCentral
// 后面统一换成阿里云的仓库镜像，central 国内访问慢
repositories {
    mavenCentral()
}

// 测试和运行的依赖项
// 依赖版本在 gradle/libs.versions.toml 中定义
dependencies {
    // 依赖 JUnit 进行测试
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // 程序依赖 guava
    implementation(libs.guava)
}

// 定义 Java 工具链版本，使用 Java17
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

// 指定应用程序主类，将在 manifest 中定义
application {
    mainClass.set("com.demo.App")
}

// 配置 test 任务，使用 JUnit 平台测试
tasks.named<Test>("test") {
    useJUnitPlatform()
}
```

- `gradle` 目录为 Gradle Wrapper 所在目录
- `gradlew` 文件为 Gradle Wrapper 可执行文件，`GRADLE_USER_HOME` 下的对应版本可执行文件的脚本
- `settings.gradle.kts` 文件为当前 Gradle 项目配置文件

```kotlin
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

// 设置根项目的名称
rootProject.name = "tutorial"
// 设置包含 app 子项目
include("app")
```

`application` 插件依赖于 `java`，因此可直接通过 `test`，`build`，`run` 等任务测试、构建、运行了。

使用 `--scan` 参数可以查看每个任务执行时间等信息。
# 构建生命周期

执行 Gradle 任务时，主要有三个阶段：
- 初始化：执行 `settings.gradle.kts`，确定项目有哪几个子项目
- 项目配置：执行每个项目的 `build.gradle.kts`，配置每个项目的 `Project` 对象
- 执行构建：执行选定的任务

![[Pasted image 20240211232710.png]]
# 多项目构建

Gradle 中，多项目生成主要依赖于 `settings.gradle.kts` 和每个子项目的 `build.gradle.kts`
## 子项目

添加一个子项目，可以模仿 `app` 项目的目录结构创建。以创建一个依赖库子项目为例：

1. 创建项目目录，如 `lib`
2. 创建 `lib/build.gradle.kts`
3. 创建 `lib/src/main/java` 用于存放源码
4. 创建 `lib/src/main/test` 用于存放测试源码
5. 编辑 `lib/build.gradle.kts` 和 `settings.gradle.kts`

```kotlin
// lib/build.gradle.kts
// 内容基本与 app/build.gradle.kts 一致
// 区别在于，使用 java 插件，因为是一个库没有主类
plugins {
    id("java")
}

repositories {
    // 阿里云镜像库
    maven {
        setUrl("https://maven.aliyun.com/repository/public/")
    }
    maven {
        setUrl("https://maven.aliyun.com/repository/central/")
    }
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation(libs.guava)
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
```

```kotlin
// settings.gradle.kts
// ...
include("lib")
```

由于我们想让应用 `app` 依赖于 `lib`，因此还要修改 `app/build.gradle.kts` 修改其依赖项：

```kotlin
// ...
dependencies {
    // ...
    implementation(project(":lib"))
}
// ...
```
## 复合构建

复合构建允许在一个 Gradle 项目中执行其他 Gradle 项目，可以在项目中创建多个二进制 Gradle 插件并使用。以创建一个插件，更新许可证为例：

1. 在 `gradle` 目录下创建一个目录用于存放插件，目录名随意，这里为 `license-plugin`
2. 在插件目录运行 `gradle init`，在向导中的生成类型选择 `4: Gradle plugin`，其他随意。此时该目录为一个单独的 Gradle 项目

![[Pasted image 20240211235526.png]]

3. 在主项目 `settings.gradle.kts`（注意不是 `gradle/插件/settings.gradle.kts`）中使用 `includeBuild` 导入插件项目

```kotlin
// settings.gradle.kts
// ...
includeBuild("gradle/license-plugin")
```
# settings.gradle.kts

该文件为一个 Gradle 项目构建的入口，初始化一个 Settings 对象，主要用于配置包含哪些项目。当然这是一个 Kotlin 脚本，可以任意编写内容。
# build.gradle.kts

该文件在配置阶段执行，用于初始化一个 `Project` 对象。该文件的主要目的是注册任务，应用插件，检索依赖等。当然这是一个 Kotlin 脚本，可以任意编写内容。

以前面 Gradle 插件的 `build.gradle.kts`（`gradle/license-plugin/plugin/build.gradle.kts`）为例

```kotlin
// 应用插件
plugins {
    // 核心插件：Gradle 自带的插件，应用时不需要提供版本
    // 添加 java-gradle-plugin 插件，添加对 Gradle 插件的支持
    `java-gradle-plugin`
    // 社区插件：需要提供 id 和版本号
    // 添加 kotlin 插件，添加对 Kotlin 的支持
    id("org.jetbrains.kotlin.jvm") version "1.9.0"
}

// 仓库，表示从哪里取得依赖项，这里使用的是 Maven 中央仓库
repositories {
    mavenCentral()
}

// 依赖，表示项目使用的库，Kotlin JUnit 集成
dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

// 扩展配置块，来自 java-gradle-plugin 插件
gradlePlugin {
    // Define the plugin
    val greeting by plugins.creating {
        id = "com.demo.license"
        implementationClass = "com.demo.LicensePlugin"
    }
}

val functionalTestSourceSet = sourceSets.create("functionalTest") {}

// Additional lines //

// 其他测试任务
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
    useJUnitPlatform()
}

// Additional lines //
```

之后，在外部项目 `build.gradle.kts` 激活插件。复合构建创建的插件类似核心插件，不需要指定插件版本

```kotlin
// app/build.gradle.kts
plugins {
    application
    // 使用复合构建产生的插件
    id("com.demo.license")
}
```

使用 `gradle tasks --all` 可以查看所有任务。
# 自定义任务

- 使用 `tasks.register` 创建一个新任务
- 使用 `tasks.named` 查找一个已存在任务，用于配置现有任务

自定义任务之后有一个闭包，为具体操作。

```kotlin
tasks.register("task1") {
    println("Register Task 1: Executed during configuration phase")
}

tasks.named("task1") {
    println("Named Task 1: Executed during configuration phase")

    doFirst {
        println("Named Task 1 - doFirst: Executed during execution phase")
    }

    doLast {
        println("Named Task 1 - doLast: Executed during execution phase")
    }
}
```

![[Pasted image 20240212003652.png]]

`doFirst` 与 `doLast` 在任务执行期间运行，外部代码在配置阶段（即 `build.gradle.kts` 脚本中执行）。且所有 `doFirst` 块都在 `doLast` 之前执行。

自定义任务类应继承自 `DefaultTask` 类：

```kotlin
abstract class LicenseTask: DefaultTask() {

    @Input
    val fileName = "${project.rootDir}/license.txt"

    @TaskAction
    fun action() {
        val licenseText = File(fileName).readText(Charset.defaultCharset())
        project.rootDir.walk()
            .filter { it.extension == ".java" }
            .map { Pair(it, it.readText()) }
            .forEach { (src, content) -> src.writeText(licenseText + content) }
    }
}
```

之后，在插件中注册该任务：

```kotlin
// gradle/license-plugin/.../LicensePlugin.kt
class LicensePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        // 注册一个名为 license 的任务，类型为 LicenseTask
        project.tasks.register("license", LicenseTask::class.java) {
            // 任务描述
            it.description = "add a license header to source code"
            // 任务组
            it.group = "from License"
        }
    }
}
```
# 参考

```cardlink
url: https://docs.gradle.org/8.5/userguide/partr1_gradle_init.html
title: "Part 1: Initializing the Project"
host: docs.gradle.org
```
