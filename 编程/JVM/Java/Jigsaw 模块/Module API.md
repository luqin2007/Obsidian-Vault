
| 类                                  | 描述                                                                   |
| ---------------------------------- | -------------------------------------------------------------------- |
| Module                             | 表示运行时模块。                                                             |
| ModuleDescriptor                   | 表示模块描述。 这是不可变类。                                                      |
| ModuleDescriptor.Builder           | 用于以编程方式构建模块描述的嵌套构建器类。                                                |
| ModuleDescriptor.Exports           | 表示模块声明中的`exports`语句的嵌套类。                                             |
| ModuleDescriptor.Opens             | 表示模块声明中的`opens`语句的嵌套类。                                               |
| ModuleDescriptor.Provides          | 表示模块声明中的`provides`语句的嵌套类。                                            |
| ModuleDescriptor.Requires          | 表示模块声明中的`requires`语句的嵌套类。                                            |
| ModuleDescriptor.Version           | 表示模块版本字符串的嵌套类。 它包含一个从版本字符串返回其实例的`parse(String v)`工厂方法。               |
| ModuleDescriptor.Modifier          | 枚举类，其常量表示在模块声明中使用的修饰符，例如打开模块的`OPEN`。                                 |
| ModuleDescriptor.Exports.Modifier  | 枚举类，其常量表示在模块声明中用于`exports`语句的修饰符。                                    |
| ModuleDescriptor.Opens.Modifier    | 枚举类，其常量表示在模块声明中的`opens`语句上使用的修饰符。                                    |
| ModuleDescriptor.Requires.Modifier | 枚举类，其常量表示在模块声明中的`requires`语句上使用的修饰符。                                 |
| ModuleReference                    | 模块的内容的引用。 它包含模块的描述及其位置。                                              |
| ResolvedModule                     | 表示模块图中已解析的模块。 包含模块的名称，其依赖关系和对其内容的引用。 它可以用于遍历模块图中模块的所有传递依赖关系。         |
| ModuleFinder                       | 用于在指定路径或系统模块上查找模块的接口。 找到的模块作为`ModuleReference`的实例返回。 它包含工厂方法来获取它的实例。 |
| ModuleReader                       | 用于读取模块内容的接口。 可以从`ModuleReference`获取`ModuleReader`。                   |
| Configuration                      | 表示解析模块的模块图。                                                          |
| ModuleLayer                        | 包含模块图（`Configuration`）以及模块图中的模块与类加载器之间的映射。                           |
| ModuleLayer.Controller             | 用于控制`ModuleLayer`中的模块的嵌套类。 `ModuleLayer`类中的方法返回此类的实例。                |
