Java 编译器插件，包括元编程和语言增强功能
# 使用

1. 编译器插件

![[../../../_resources/images/Pasted image 20240917130624.png]]

2. 依赖

```groovy
repositories {
    jcenter()
    maven { url 'https://oss.sonatype.org/content/repositories/snapshots/' }
}

dependencies {
    implementation 'systems.manifold:manifold-rt:2024.1.33'
    testImplementation 'junit:junit:4.12'
    // Add manifold to -processorpath for javac
    annotationProcessor group: 'systems.manifold', name: 'manifold', version: '2024.1.33'
    testAnnotationProcessor group: 'systems.manifold', name: 'manifold', version: '2024.1.33'
}
```

# 参考

```cardlink
url: http://manifold.systems/
title: "Manifold is a Java compiler plugin, its features include Metaprogramming, Properties, Extension Methods, Operator Overloading, Templates, a Preprocessor, and more."
description: "Manifold - a Java language extension"
host: manifold.systems
image: images/manifold_ico.png
```
