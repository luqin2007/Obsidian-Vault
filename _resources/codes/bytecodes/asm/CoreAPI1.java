package asm;

import org.objectweb.asm.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class CoreAPI1 {

    public static void main(String[] args) throws Exception {
        // 加载测试类
        URL src = CoreAPI1.class.getClassLoader().getResource("MyClass.class");
        byte[] bytes = Files.readAllBytes(Path.of(src.toURI()));
        // 使用 ASM
        ClassReader cr = new ClassReader(bytes);
        ClassVisitor cv = new MyClassVisitor();
        cr.accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG);
    }
}

class MyClassVisitor extends ClassVisitor {

    public MyClassVisitor() {
        // ASM9 指的是使用的 ASM 版本，
        super(Opcodes.ASM9);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor,
                                   String signature, Object value) {
        // 输出字段名
        System.out.println("Field: " + name);
        return super.visitField(access, name, descriptor, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor,
                                     String signature, String[] exceptions) {
        // 输出方法名
        System.out.println("Method: " + name);
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
