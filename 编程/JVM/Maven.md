Maven 是一款 Java 依赖管理、构建、打包工具
- 依赖管理（核心）
	- 以配置文档形式管理依赖
	- 自动下载依赖的依赖
	- 确保依赖无冲突
- 构建管理：提供编译、测试、打包、部署过程的独立任务
# 项目结构


一个基本的 Maven 项目根目录包含一个 `pom.xml` 和 `src` 目录。

`````col
````col-md
flexGrow=1
===
![[Pasted image 20230922122320.png]]
````
````col-md
flexGrow=1
===
```dirtree
- pom.xml: Maven 配置文件
- src: 项目源代码文件
  - main: 主项目
    - resources: 资源文件
    - java: Java 项目源码
    - webapp: Web 项目源码
      - WEBINF/web.xml
  - test: 测试项目
```
````
`````

`pom.xml` 是 Maven 的核心配置文件，是一个 XML 类型文档。项目信息、依赖管理、打包任务等均在该文件中配置。该文档以 `<project>` 标签为跟标签，表示一个项目。

```xml title:pom.xml fold
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.lq2007.test</groupId>
    <artifactId>maventest</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
    </properties>

</project>
```

# 项目信息

`<project>` 中记录了项目基本属性
- `<groupId>`：组织标志，通常为 `com.公司或BU.业务线[.子业务线]`
- `<artifactId>`：包 id，通常为 `项目名`，或 `项目名-模块名`
- `<version>`：版本号，通常为 `主版本号.次版本号.修订号[-快照后缀]`
	- 主版本号：产生了不向下兼容的 API 变化
	- 次版本号：产生了向下兼容的 API 变化
	- 修订号：bug 修订
- `<packaging>`：打包方式
	- `jar`：默认
	- `war`：web 项目，生成 `.war` 文件
	- `pom`：不打包

# 依赖管理

 `<dependencies>` 标签中，`<dependency>` 声明一个依赖项，使用 `<groupId>`，`<artifactId>`，`<version>` 三个标签定位依赖

`<dependency>` 的 `<scope>` 标签定义包的适用范围，可省略，默认为 `compile`
- `compile`：默认，打包和运行时均可用（`src/main`，`src/test` 均可用）
- `test`：仅测试时可用（仅 `src/test` 可用，如 JUnit）
- `runtime`：仅运行时可用（`src/main`，`src/test` 均不可用，但运行时使用，如 JDBC 相关）
- `provide`：仅编程时可用（`src/main`，`src/test` 均可用，但运行时不可用，通常在运行时由外部环境提供，如 `HttpServerlet` 由 tomcat 提供）

```xml
<project>
    <dependencies>
        <dependency>
            <groupId>依赖 GroupID</groupId>
            <artifactId>依赖包</artifactId>
            <version>依赖版本号</version>
            <scope>作用域</scope>
        </dependency>
        
        <dependency>...</dependency>
        
        <dependency>...</dependency>
    </dependencies>
</project>
```

> [!note]- 依赖传递
> 当导入了一个依赖时，会自动导入该依赖的所依赖的所有依赖
> - 实现了自动依赖管理
> - 避免重复依赖
> - 保证依赖版本的正确性

> [!Note]- 依赖冲突
> 在依赖传递的过程中，当发生重复的依赖导入时，自动终止依赖传递，避免依赖冲突。
> 
> 若多个依赖包中包含相同依赖但其版本不同，根据以下原则选择：
> 1. 依赖链短的优先
> 2. 依赖链长度相同时，`<dependency>` 出现位置靠前的优先

## 本地缓存

Maven 本地缓存默认在 `~/.m2/repository/` 目录下，可以在 `%M2_HOME%/conf/settings.xml` 中进行配置

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 
   http://maven.apache.org/xsd/settings-1.0.0.xsd">
      <localRepository>本地缓存路径</localRepository>
</settings>
```

# 属性

标签中多个相同的值可以考提取成变量统一管理，在 `<properties>` 标签中声明。

`<properties>` 每个子标签表示一个变量，标签名即变量名，使用时为 `${变量名}`

```xml
<project>
    <properties>
        <some_dependency.version>1.0</some_dependency.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>依赖 GroupID</groupId>
            <artifactId>依赖包1</artifactId>
            <!-- 相当于 <version>1.0</version> -->
            <version>${some_dependency.version}</version>
        </dependency>
        <dependency>
            <groupId>依赖 GroupID</groupId>
            <artifactId>依赖包2</artifactId>
            <!-- 相当于 <version>1.0</version> -->
            <version>${some_dependency.version}</version>
        </dependency>
    </dependencies>
</project>
```

# 相关网站

[Maven Repository](https://mvnrepository.com/)

[Fetching Data#aaio](https://mvnrepository.com/)
