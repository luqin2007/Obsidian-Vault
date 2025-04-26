> [!note] StrictMath 与 Math 的区别
> - StrictMath 严格保持各个平台结果的一致性，严格符合 IEEE 754 标准与 fdlibm
> - Math 可以使用平台特定硬件和算法优化，结果可能产生略微差异，但性能更好
# Java9
#java9 

* `floorDiv`：小于或等于 $x\div y$ 代数商最大长度

```java
import static java.lang.StrictMath.floorDiv;

void main(String[] args) {
    System.out.println("Using StrictMath.floorDiv(long, int):");
    System.out.printf("  floorDiv(20L, 3) = %d%n", floorDiv(20L, 3));
    System.out.printf("  floorDiv(-20L, -3) = %d%n", floorDiv(-20L, -3));
    System.out.printf("  floorDiv(-20L, 3) = %d%n", floorDiv(-20L, 3));
    System.out.printf("  floorDiv(Long.Min_VALUE, -1) = %d%n", floorDiv(Long.MIN_VALUE, -1));
}
```

* `floorMod`：最小模数，$x-(floorDiv(x,y) * y)$  

```java
import static java.lang.StrictMath.floorMod;
  
void main(String[] args) {
    System.out.println("Using StrictMath.floorMod(long, int):");
    System.out.printf("  floorMod(20L, 3) = %d%n", floorMod(20L, 3));
    System.out.printf("  floorMod(-20L, -3) = %d%n", floorMod(-20L, -3));
    System.out.printf("  floorMod(-20L, 3) = %d%n", floorMod(-20L, 3));
}
```

* `fma`：IEEE 754-2008 `fusedMultiplyAdd`（$a\times b+c$）

```java
import static java.lang.StrictMath.fma;

void main() {
    System.out.println("Using StrictMath.fma(double, double, double):");
    System.out.printf("  fma(3.337, 6.397, 2.789) = %f%n", fma(3.337, 6.397, 2.789));
}
```

* `multiplyExact`：乘

```java
void main() {
    System.out.println("Using StrictMath.multiplyExact(long, int):");
    System.out.printf("  multiplyExact(29087L, 7897979) = %d%n", StrictMath.multiplyExact(29087L, 7897979));
    try {
        System.out.printf("  multiplyExact(Long.MAX_VALUE, 5) = %d%n", StrictMath.multiplyExact(Long.MAX_VALUE, 5));
    } catch (ArithmeticException e) {
        System.out.println("  multiplyExact(Long.MAX_VALUE, 5) = " + e.getMessage());
    }
}
```

* `multiplyFull`：确切乘

```java
void main() {
    System.out.println("Using StrictMath.multiplyFull(int, int):");
    System.out.printf("  multiplyFull(29087, 7897979) = %d%n", StrictMath.multiplyFull(29087, 7897979));
}
```

* `multiplyHigh`：长度是两个 64 位参数的 128 位乘积的最高有效 64 位。当乘以两个 64 位长的值时，结果可能是 128 位值。因此，该方法返回 `significant (high)` 64 位。

```java
import static java.lang.StrictMath.multiplyHigh;

void main() {
    System.out.println("Using StrictMath.multiplyHigh(long, long):");
    System.out.printf("  multiplyHigh(29087L, 7897979L) = %d%n", multiplyHigh(29087L, 7897979L));
    System.out.printf("  multiplyHigh(Long.MAX_VALUE, 8) = %d%n", multiplyHigh(Long.MAX_VALUE, 8));
}
```
