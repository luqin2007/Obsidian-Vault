JaCoCo（Java Code Coverage）是一个代码覆盖率统计工具
* 指令级覆盖率：通过检查字节码指令是否被使用
* 分支覆盖率：常见 if，switch 等分支选择的覆盖率检查
* 圈复杂度：又称条件复杂度或循环复杂度，衡量模块判定结构的复杂程度，覆盖所有可能情况时使用的最少调试用例数
* 代码行覆盖率：一行代码是否被执行
* 方法：非抽象方法任意一条指令执行表示这个方法被覆盖
* 类：一个类中有一个方法被执行，则认为这个类被覆盖

JaCoCo 通过字节码注入探针代码实现覆盖率统计，具有 Offline、On-The-Fly 两种模式，在各个监测点前后插入探针字节码，运行到探针表示代码被覆盖
* Offline：生成目标文件之前对字节码文件进行插桩，执行时通过插桩后的字节码生成测试覆盖率报告
* On-The-Fly：通过 `javaagent` 启动 `Instrumentation` 代理程序，加载前利用 ASM 对字节码进行修改，JVM 执行修改过的字节码文件生成测试覆盖率报告，无需提前插桩

在底层，JaCoCo 使用 `$jacocoInit` 的 `boolean` 数组存储探针。探针不会改变程序任何行为，只会在执行到某处是把那处的值置为 `true`。

```flowchart
a=>operation: a()
cond=>operation: cond()
ifeq=>condition: IFEQ
b=>operation: b()
c=>operation: c()
goto=>operation: GOTO
d=>operation: d()
ret=>operation: RETURN

p1=>operation: 探针
p2=>operation: 探针
p3=>operation: 探针
p4=>operation: 探针
p5=>operation: 探针
p6=>operation: 探针

a->p1->cond->ifeq
ifeq(yes)->p2->b->p3->goto->p4->d->p5->ret
ifeq(no)->c->p6->p4
```
