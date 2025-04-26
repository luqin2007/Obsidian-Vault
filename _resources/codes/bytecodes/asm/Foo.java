package asm;

public class Foo {

    public void foo() {
        System.out.println("Step 1");
        int a = 1 / 0;
        System.out.println("Step 2");
    }
}
