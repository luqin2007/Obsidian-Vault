#java9 

| 类型          | 方法            | 说明                                                   |
| ----------- | ------------- | ---------------------------------------------------- |
| Stream 方法   | `dropWhile`   | 丢弃第一个不满足给定条件之前的元素                                    |
| Stream 方法   | `takeWhile`   | 丢弃第一个不满足给定条件及之后的元素                                   |
| Stream 静态方法 | `ofNullable`  | 取非 null 值                                            |
| Stream 静态方法 | `iterate`     | 根据迭代生成流 `for(e=seed;hasNext;next) { /*provide e*/ }` |
| 收集器         | `filtering`   | 过滤并收集                                                |
| 收集器         | `flatMapping` | 扁平化元素                                                |
|             |               |                                                      |

```java
void main() {  
    System.out.println("--- Stream.dropWhile");  
    Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)  
            .dropWhile(i -> i <= 5)  
            .map(i -> String.format(" %d", i))  
            .forEach(System.out::print); // 6789  
    System.out.println();  
  
    System.out.println("--- Stream.takeWhile");  
    Stream.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)  
            .takeWhile(i -> i <= 5)  
            .map(i -> String.format(" %d", i))  
            .forEach(System.out::print); // 012345  
    System.out.println();  
  
    System.out.println("--- Stream.ofNullable");  
    List<String> list = new ArrayList<>();  
    list.add("item1");  
    list.add("item2");  
    list.add("item3,next is null");  
    list.add(null);  
    list.add("item5");  
    System.out.println(String.join("//", list));  
    System.out.println(list.stream()  
            .flatMap(Stream::ofNullable)  
            .collect(Collectors.joining("//")));  
  
    System.out.println("--- Stream.iterate");  
    Stream.iterate(1, n -> n <= 10, n -> n + 1)  
            .map(n -> String.format("%d ", n))  
            .forEach(System.out::print); // 1 2 3 4 5 6 7 8 9 10  
    System.out.println();  
  
    System.out.println("--- Collectors.filtering");  
    list = Stream.of("a", "b", "c", "d", "ee", "ff", "gg", "h")  
            .collect(Collectors.filtering(  
                    s -> s.length() == 1,  // 元素过滤器  
                    Collectors.toList())); // 下级集器  
    System.out.println(String.join(" ", list)); // a b c d h  
  
    System.out.println("--- Collectors.flatMapping");  
    List<? extends Serializable> list2 = Map.of("a", 1, "b", 2, "c", 3).entrySet().stream()  
            .collect(Collectors.flatMapping(  
                    e -> Stream.of(e.getKey(), e.getValue()), // 扁平化元素  
                    Collectors.toList()));                    // 下级收集器  
    list = list2.stream().map(Object::toString).toList();  
    System.out.println(String.join(" ", list)); // c 3 b 2 a 1  
}
```

`Collectors.teeing()` 聚合两个收集器的结果
