# JTA 支持

当事务涉及到多个事务资源时，可以使用 JTA 事务管理，Spring 提供 `JtaTransactionManager` 类管理 JTA 事务。

> 大部分情况下 `JtaTransactionManager` 可以满足需求，Spring 也提供了一些额外的实现类：
>
> * 适用于 WebLogic 应用程序服务器的 `WebLogicJtaTransactionManager`
> * 适用于 WebSphere 应用程序服务器的 `WebSphereUowTransactionManager`

使用 `<tx:jta-transaction-manager>` 可以自动根据应用程序上下文类型创建 `JtaTransactionManager`
