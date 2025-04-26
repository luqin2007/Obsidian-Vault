#java12

针对不同地区格式化数字

```java
import static java.text.NumberFormat.Style;  
import static java.text.NumberFormat.getCompactNumberInstance;  
import static java.util.Locale.*;

void main() {  
    int value = 1_234_554_321;
    System.out.println(getCompactNumberInstance().format(value));
    System.out.println(getCompactNumberInstance(SIMPLIFIED_CHINESE, Style.LONG).format(value));
    System.out.println(getCompactNumberInstance(SIMPLIFIED_CHINESE, Style.SHORT).format(value));
    System.out.println(getCompactNumberInstance(TRADITIONAL_CHINESE, Style.SHORT).format(value));
    System.out.println(getCompactNumberInstance(ENGLISH, Style.LONG).format(value));
    System.out.println(getCompactNumberInstance(ENGLISH, Style.SHORT).format(value));
}
```
