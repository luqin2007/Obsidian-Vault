#java14 #java14incubator #java15 #java15incubator #java16 #java16incubator #java17 #java17incubator #java18 #java18incubator #java19 #java19preview #java20 #java20preview #java21 #java21preview #java22

Pamana 子项目，与 Java 运行时之外的代码和数据进行互操作，高效调用外部函数，安全访问外部内存
* 分配外部内存 ：`MemorySegment` 和 `SegmentAllocator`
* 操作和访问结构化的外部内存： `MemoryLayout`，`VarHandle`
* 控制外部内存的分配和释放：`Arena` 与 `SegmentScope`
* 调用外部函数：`Linker`、`FunctionDescriptor` 和 `SymbolLookup`

```java
// 初始化环境
Linker linker = Linker.nativeLinker();
// or SymbolLookup.loaderLookup();
SymbolLookup stdlib = linker.defaultLookup();
// 查找函数
// void radixsort(char* s[], int length)
MemorySegment segment = stdlib.find("radixsort").orElseThrow();
FunctionDescriptor desc = FunctionDescriptor.ofVoid(  
        MemoryLayout.paddingLayout(ValueLayout.ADDRESS.byteSize()),  
        MemoryLayout.paddingLayout(ValueLayout.JAVA_INT.byteSize())  
);
MethodHandle radixSort = linker.downcallHandle(segment, desc);
// 分配内存
String[] strs = {"mouse", "cat", "dog", "car"};
try (Arena offHeap = Arena.openConfined()) {
    MemorySegment pointers = offHeap.allocateArray(ValueLayout.ADDRESS, strs.length);
    for (int i = 0; i < strs.length; i++) {
        MemorySegment cstr = offHeap.allocateUtf8String(strs[i]);
        pointers.setAtIndex(ValueLayout.ADDRESS, i, cstr);
    }
    // 调用函数
    radixSort.invoke(pointers, strs.length, MemorySegment.NULL, '\0');
    // 将结果复制回 JVM 堆
    for (int i = 0; i < strs.length; i++) {
        MemorySegment cstr = pointers.getAtIndex(ValueLayout.ADDRESS, i);
        strs[i] = cstr.getUtf8String(0);
    }
} catch (Throwable e) {
    throw new RuntimeException(e);
}
```

* `Linker`：提供 Java 代码与外部函数之间互相访问的功能
    * `downcall` 允许 Java 链接到外部函数
    * `upcall` 允许外部函数调用 Java 方法
* `SymbolLookup`：检索一个或多个库中的符号地址
* `Arena`：竞技场范围，控制内存段的生命周期，实现了 `AutoClosable` 且关闭后不可再访问
* `MemorySegment`：提供对连续内存区域的访问
    * `heap segments`：分配于 Java 堆中
    * `native segments`：分配于 Java 堆外
* `ValueLayout`：对基本数据类型进行建模，且对 Java 原始类型和地址定义了有用的布局常量
