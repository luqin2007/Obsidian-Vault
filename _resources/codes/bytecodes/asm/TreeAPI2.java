package asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.objectweb.asm.Opcodes.*;

public class TreeAPI2 {

    public static void main(String[] args) throws Exception {
        // 加载测试类
        URL src = CoreAPI1.class.getClassLoader().getResource("MyClass.class");
        byte[] bytes = Files.readAllBytes(Path.of(src.toURI()));
        // 使用 ASM
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(0);
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.SKIP_DEBUG);
        // public static final String xyz;
        FieldNode xyz = new FieldNode(ACC_PUBLIC | ACC_STATIC | ACC_FINAL,
                "xyz", "Ljava/lang/String;", null, null);
        cn.fields.add(xyz);
        // public void foo(int, String);
        MethodNode foo = new MethodNode(ACC_PUBLIC,
                "foo", "(ILjava/lang/String;)V", null, null);
        InsnList insnList = foo.instructions;
        // sb = new StringBuilder();
        insnList.add(new TypeInsnNode(NEW, "java/lang/StringBuilder"));
        insnList.add(new InsnNode(DUP));
        insnList.add(new MethodInsnNode(INVOKESPECIAL,
                "java/lang/StringBuilder", "<init>", "()V", false));
        // sb.append(i);
        insnList.add(new VarInsnNode(ILOAD, 1));
        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                "(I)Ljava/lang/StringBuilder;", false));
        // sb.append(s);
        insnList.add(new VarInsnNode(ALOAD, 2));
        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false));
        // sb.toString();
        insnList.add(new MethodInsnNode(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString",
                "()Ljava/lang/String;", false));
        // System.out.println(s)
        insnList.add(new FieldInsnNode(GETSTATIC,
                "java/lang/System", "out", "java/io/PrintStream"));
        insnList.add(new InsnNode(SWAP));
        insnList.add(new MethodInsnNode(INVOKEVIRTUAL,
                "java/lang/PrintStream", "println", "(Ljava/lang/String;)V", false));
        cn.methods.add(foo);
        // 保存修改后的字节码
        cn.accept(cw);
        Files.write(Path.of("MyClass.class"), cw.toByteArray());
    }
}
