#java12 #java12preview #java13 #java13preview #java14 

当只有一行语句时，可以使用 `->` 省略 `break`

```java
switch (day) {
  case MONDAY:
  case FRIDAY:
  case SUNDAY:
      System.out.println(6);
      break;
  case TUESDAY:
      System.out.println(7);
      break;
  case THURSDAY:
  case SATURDAY:
      System.out.println(8);
      break;
  case WEDNESDAY:
      System.out.println(9);
      break;
}
```

```java
switch (day) {
    case MONDAY, FRIDAY, SUNDAY -> System.out.println(6);
    case TUESDAY                -> System.out.println(7);
    case THURSDAY, SATURDAY     -> System.out.println(8);
    case WEDNESDAY              -> System.out.println(9);
}
```

若只有一行赋值，则可以将 `switch` 作为一个表达式使用

`````col
````col-md
```java
int dayNum;
switch (day) {
    case MONDAY: dayNum = 1;break;
    case FRIDAY: dayNum = 2;break;
    case SUNDAY: dayNum = 3;break;
    case TUESDAY: dayNum = 4;break;
    default: dayNum = 0;
}
```
````
````col-md
```java
int dayNum = switch (day) {
    case MONDAY -> 1;
    case FRIDAY -> 2;
    case SUNDAY -> 3;
    case TUESDAY -> 4;
    default -> 0;
};
```
````
`````

若有多行还有返回值，使用 `yield` 作为表达式返回

```java
String dayOfWeek = switch (i) {
    case 1 -> {
        String day = "Monday";
        yield day;
    }
    case 2 -> {
        String day = "Tuesday";
        yield day;
    }
    default -> "Unknown";
};
```
# 模式匹配
#java17 #java17preview #java21  

- 类型模式：在 `case` 中直接进行类型匹配
	- 可以检查是否覆盖了密封类的所有子类
- 空值处理：直接处理 `null` 值

```java
static String formatterPatternSwitch(Object o) {
    return switch (o) {
        case null      -> "null"
        case Integer i -> String.format("int %d", i);
        case Long l    -> String.format("long %d", l);
        case Double d  -> String.format("double %f", d);
        case String s  -> String.format("String %s", s);
        case default   -> o.toString();
    };
}
```

- 守卫模式：使用 `when` 做进一步条件检查

```java
Effect effect;
Guitar guitar;

switch(effect) {
    case Tuner tu when !guitar.isInTune() -> ... ;
    case Tuner tu                         -> ... ; 
}
```

* 模式变量：可以在模式匹配中声明一个变量来引用被匹配对象
* `switch` 表达式和语句中支持推断 `record` 模式类型参数，因此可以使用 `var`
* 当一个模式匹配失败时，抛出 `MatchException`
