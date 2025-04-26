Maven Publish 是一个使 gradle 构建的产物自动发布到 `maven` 仓库的插件。在 `build.gradle` 中添加以下内容即可。

```groovy
plugins {
    id 'maven-publish'
}
```
# Task

下面 `[PubName]` 表示项目名，`[RepoName]` 表示仓库名
- `generatePomFileFor[PubName]Publication`：为项目创建一个 POM 文件，默认位于 `build/publications/[pubName]/pom-default.xml`
- `publish[PubName]PublicationTo[RepoName]Repository`：将 PubName 项目发布到 RepoName 仓库。RepoName 默认为 Maven
- `publish[PubName]PublicationToMavenLocal`：将 PubName 项目发布到本地缓存，一般为 UserHome/.m2/
- `publish`：对于所有项目执行 `publish[PubName]PublicationTo[RepoName]Repository` 任务
- `publishToMavenLocal`：对于所有项目执行 `publish[PubName]PublicationToMavenLocal`
# Publications

配置发布

```groovy
publishing {
    publications {
        mavenJava(MavenPublication) {
            // [A-Za-z0-9_\\-.]+
            groupId = 'org.gradle.sample'
            // [A-Za-z0-9_\\-.]+
            artifactId = 'project1-sample'
            // 所有有效 ASCII 字符
            version = '1.1'
            // from 加载 org.gradle.api.component.SoftwareComponent
            // artifact 加载自定义工件（MavenArtifact）
            from components.java
            // pom 自定义 pom 文件
            pom {
                name = 'My Library'
                description = 'A concise description of my library'
                url = 'http://www.example.com/library'
                properties = [
                    myProp: "value",
                    "prop.with.dots": "anotherValue"
                ]
                licenses {
                    license {
                        name = 'The Apache License, Version 2.0'
                        url = 'http://www.apache.org/licenses/LICENSE-2.0.txt'
                    }
                }
                developers {
                    developer {
                        id = 'johnd'
                        name = 'John Doe'
                        email = 'john.doe@example.com'
                    }
                }
                scm {
                    connection = 'scm:git:git://example.com/my-library.git'
                    developerConnection = 'scm:git:ssh://example.com/my-library.git'
                    url = 'http://example.com/my-library/'
                }
            }

        }
    }
}
```
# Repositories

```groovy
publishing {
    repositories {
        maven {
            // url：必填
            url = "$buildDir/repo"
            // name：选填
        }
    }
}
```