Spring AOP 基于代理，将目标对象创建一个代理对象。运行时，对目标对象的调用被代理拦截，由代理执行适用于目标方法的通知。

> [!note] 若目标对象没有实现任何接口时，使用 GCLIB 代理；否则，使用 JavaSE 代理

![[../../../../../_resources/images/Spring AOP 2024-09-09 22.15.48.excalidraw|80%]]

要开启 Spring AOP，需要创建 `<aop:aspectj-autoproxy />` 元素或存在一个 `@EnableAspectJAutoProxy` 注解

|注解属性|XML 属性|说明|
| ----------| ----------| ----------------------------------------------------------------------------------------------------------------------|
|`proxyTargetClass`|`proxy-target-class`|是否阻止生成目标对象的代理。若为 `true`，则不会使用代理替代目标对象，而是通过 `AopContext` 的 `currentProxy` 静态方法访问 AOP 代理。默认 false|
|`exposeProxy`|`expose-proxy`|是否暴露代理对象给程序，确保代理类内部调用也可以触发切片。默认 false|

例：通过 `AopContext` 拿到当前对象的代理，并手动调用代理类的方法确保内部调用时切片正常触发

```java
public class BankAccountServiceJpaDataImpl implements BankAccountService {

    @Override
    @Transactional
    public void createAccount(BankAccountDetails bankAccountDetails) {
        boolean isDuplicate =
                ((BankAccountService) AopContext.currentProxy()).isDuplicateAccount(bankAccountDetails.getAccountId());
        if (!isDuplicate) {
            bankAccountRepository.save(bankAccountDetails);
        }
    }

    @Override
    public boolean isDuplicateAccount(int accountId) {
        return bankAccountRepository.findById(accountId).isPresent();
    }
}
```

* 使用 `AopContext.currentProxy()` 获取当前方法的切面代理。代理是线程安全的。
* 使用代理调用 `isDuplicateAccount` 方法，确保切面正确调用。
