package asm;

import org.objectweb.asm.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.objectweb.asm.Opcodes.*;

public class CoreAPI2 {

    public static void main(String[] args) throws Exception {
        // 加载测试类
        URL src = CoreAPI1.class.getClassLoader().getResource("MyClass.class");
        byte[] bytes = Files.readAllBytes(Path.of(src.toURI()));
        // 使用 ASM
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new ClassVisitorAddElement(cw);
        cr.accept(cv, ClassReader.SKIP_DEBUG);
        // 保存修改后的字节码
        Files.write(Path.of("MyClass.class"), cw.toByteArray());
    }
}

class ClassVisitorAddElement extends ClassVisitor {

    public ClassVisitorAddElement(ClassWriter cw) {
        // ASM9 指的是使用的 ASM 版本，
        super(Opcodes.ASM9, cw);
    }

    @Override
    public void visitEnd() {
        // public static final String xyz;
        FieldVisitor xyz = visitField(ACC_PUBLIC | ACC_STATIC | ACC_FINAL,
                "xyz", "Ljava/lang/String;", null, null);
        xyz.visitEnd();
        // public void foo(int, String);
        MethodVisitor foo = visitMethod(ACC_PUBLIC, "foo", "(ILjava/lang/String;)V", null, null);
        // sb = new StringBuilder();
        foo.visitTypeInsn(NEW, "java/lang/StringBuilder");
        foo.visitInsn(DUP);
        foo.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder",
                "<init>", "()V", false);
        // sb.append(i);
        foo.visitVarInsn(ILOAD, 1);
        foo.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder",
                "append", "(I)Ljava/lang/StringBuilder;", false);
        // sb.append(s);
        foo.visitVarInsn(ALOAD, 2);
        foo.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder",
                "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        // s = sb.toString();
        foo.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder",
                "toString", "()Ljava/lang/String;", false);
        // System.out.println(s)
        foo. visitFieldInsn(GETSTATIC, "java/lang/System",
                "out", "Ljava/io/PrintStream");
        foo.visitInsn(SWAP);
        foo.visitMethodInsn(INVOKEVIRTUAL, "java/lang/PrintStream",
                "println", "(Ljava/lang/String;)V", false);
        foo.visitEnd();
        super.visitEnd();
    }
}
