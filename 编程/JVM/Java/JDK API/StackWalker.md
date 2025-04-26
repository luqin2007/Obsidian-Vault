#java9 

简单有效的遍历栈帧方法，自上而下遍历记录，高效而便利

```java
void main() {
    // RETAIN_CLASS_REFERENCE 包含对调用的 Class 类对象的引用
    // SHOW_HIDDEN_FRAMES 包含所有隐藏帧
    // SHOW_REFLECT_FRAMES 包含反射帧
    StackWalker.getInstance(StackWalker.Option.SHOW_REFLECT_FRAMES)
            .forEach(frame -> System.out.println(frame.getClassName() + "." + frame.getMethodName()));
}
```

>[!warning] `Throwable` 或 `Thread.getAllStackTraces()` 的问题：
> - 返回整个栈帧快照，效率低
> - 栈帧包含类和方法名，但不包含类引用
> - 无法查询虚拟机隐藏的栈帧（虚拟机允许省略某些帧以提升性能）
> - 调用者敏感 - 不同方法调用返回的结果不同
> - 无法简单的过滤帧
