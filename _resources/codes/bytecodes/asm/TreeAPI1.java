package asm;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class TreeAPI1 {

    public static void main(String[] args) throws Exception {
        // 加载测试类
        URL src = CoreAPI1.class.getClassLoader().getResource("MyClass.class");
        byte[] bytes = Files.readAllBytes(Path.of(src.toURI()));
        // 使用 ASM
        ClassReader cr = new ClassReader(bytes);
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.SKIP_DEBUG | ClassReader.SKIP_CODE);
        for (FieldNode field : cn.fields) {
            System.out.println("Field: " + field.name);
        }
        for (MethodNode method : cn.methods) {
            System.out.println("Method: " + method.name);
        }
    }
}
