Gradle 托管属性用于构建脚本任务、插件等的可配置属性配置，其类型包括：
- `Property<T>`
- `RegularFileProperty`
- `DirectoryProperty`
- `ListProperty<T>`
- `SetProperty<T>`
- `MapProperty<K, V>`
- `ConfigurableFileCollection`
- `ConfigurableFileTree`
- `NamedDomainObjectContainer<T>`
	- 以 String 为键，由容器负责创建和配置元素，并提供生成脚本用于定义和配置元素 DSL：`register(name) { ... }`
	- `ObjectFactory.domainObjectContainer()`
- `ExtensiblePolymorphicDomainObjectContainer<T>`
	- 类似 `NamedDomainObjectContainer`，允许为不同类型定义不同实例化策略
	- `ObjectFactory.polymorphicDomainObjectContainer()`
- `DomainObjectSet<T>`
	- 类似 `NamedDomainObjectContainer`，但没有键值对，不负责创建元素
	- `ObjectFactory.domainObjectSet()`
- `NamedDomainObjectSet<T>`
	- 类似 `NamedDomainObjectContainer`，但不负责创建元素
	- `ObjectFactory.namedDomainObjectSet()`
- `NamedDomainObjectList<T>`
	- 类似 `NamedDomainObjectContainer`，但不负责管理元素
	- `ObjectFactory.namedDomainObjectList()`
- 嵌套属性：没有字段，所有方法都是托管属性的接口或抽象类

在自定义任务或配置中，可变属性只要声明一个抽象 `getter` 方法即可。若需要只读属性，则需要创建一个非抽象 `getter` 方法。

```java
public abstract class MyTask extends DefaultTask {
    @Input
    public abstract Property<String> getLocation();

    @Internal
    public Property<URI> getUri() {
        return getLocation().map(l -> URI.create("https://${l}"));
    }

    @Nested
    public abstract Resource getResource(); 
}

public interface Resource {
    Property<String> getHostName();
    Property<String> getPath();
}
```
