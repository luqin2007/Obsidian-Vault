package asm;

import org.objectweb.asm.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.objectweb.asm.Opcodes.*;

public class CoreAPI3 {

    public static void main(String[] args) throws Exception {
        // 加载测试类
        URL src = CoreAPI1.class.getClassLoader().getResource("MyClass.class");
        byte[] bytes = Files.readAllBytes(Path.of(src.toURI()));
        // 使用 ASM
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new ClassVisitorModElement(cw);
        cr.accept(cv, ClassReader.SKIP_DEBUG);
        // 保存修改后的字节码
        Files.write(Path.of("MyClass.class"), cw.toByteArray());
    }
}

class ClassVisitorModElement extends ClassVisitor {

    public ClassVisitorModElement(ClassWriter cw) {
        // ASM9 指的是使用的 ASM 版本，
        super(Opcodes.ASM9, cw);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor,
                                     String signature, String[] exceptions) {
        // 删除 test02()
        if ("test02".equals(name)) return null;
        MethodVisitor method = super.visitMethod(access, name, descriptor, signature, exceptions);
        // 修改构造函数，移除对字段 b 的赋值
        if ("<init>".equals(name)) return new MethodVisitor(api, method) {
            boolean putA = false;
            @Override
            public void visitVarInsn(int opcode, int varIndex) {
                if (!putA) {
                    super.visitVarInsn(opcode, varIndex);
                }
            }
            @Override
            public void visitInsn(int opcode) {
                if (!putA || opcode != ICONST_1)
                    super.visitInsn(opcode);
            }
            @Override
            public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                if ("a".equals(name)) {
                    putA = true;
                    super.visitFieldInsn(opcode, owner, name, descriptor);
                }
            }
        };
        return method;
    }
    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        // 移除字段 b
        if ("b".equals(name)) return null;
        return super.visitField(access, name, descriptor, signature, value);
    }
}
