`BeanPostProcessor` 可以在 bean 初始化之前、之后执行额外的函数，直接在 `XML` 中创建对应 `bean` 即可生效

> [!attention] `BeanPostProcessor` 不会检查其他 `BeanPostProcessor` 类型的 bean 对象

> [!note] 内置 `BeanPostProcessor` 默认没有注入到 `ApplicationContext` 中

* `DestructionAwareBeanPostProcessor`：表示一个 bean 定义
* `CommonAnnotationBeanPostProcessor`：启用 JSR250 注解支持

实现 `Order` 接口可以控制不同 `processor` 的执行顺序

```java title:BeanPostProcessor接口定义 fold
public interface BeanPostProcessor {
	@Nullable
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	@Nullable
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
}
```

* `postProcessBeforeInitialization`：在 `bean` 初始化方法被调用之前触发
* `postProcessAfterInitialization`：在 `bean` 初始化方法被调用之后触发

使用 `FactoryBean` 时，无论 `isSingleton()` 返回值，`FactoryBean` 是 `singleton` 的，`BeanPostProcessor` 和其他生命周期接口调用有以下特点：

* 对于 `FactoryBean`，`Before` 和 `After` 方法均生效
* 对于 `FactoryBean#getObject`，只会调用 `After` 方法
* 类似 `InitializingBean` 的生命周期接口只会在 `FactoryBean` 调用一次

可以使用 `BeanFactoryPostProcessor`，该接口可以获取所有 `bean` 的信息。
- [[内置 BeanFactoryPostProcessor]]

| 方法                       | 说明                 |
| ------------------------ | ------------------ |
| `getBeanDefinitionNames` | 获取所有 `bean` 对象的 id |
| `getBeanDefinition`      | 根据名称获取某个 `bean` 对象 |

| 类                | 说明             |
| ---------------- | -------------- |
| `BeanDefinition` | 表示一个 `bean` 定义 |

---

- 例：[[实现 InstanceValidator 接口 bean 的自查]]