package asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.objectweb.asm.Opcodes.*;

public class TreeAPI3 {

    public static void main(String[] args) throws Exception {
        // 加载测试类
        URL src = CoreAPI1.class.getClassLoader().getResource("MyClass.class");
        byte[] bytes = Files.readAllBytes(Path.of(src.toURI()));
        // 使用 ASM
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(0);
        ClassNode cn = new ClassNode();
        cr.accept(cn, ClassReader.SKIP_DEBUG);
        // 删除 test02()
        cn.methods.removeIf(node -> node.name.equals("test02"));
        // 删除 b
        cn.fields.removeIf(node -> node.name.equals("b"));
        // 删除构造函数中对 b 的赋值
        for (MethodNode method : cn.methods) {
            if ("<init>".equals(method.name)) {
                InsnList insnList = method.instructions;
                AbstractInsnNode n0 = insnList.get(0);
                AbstractInsnNode n1 = insnList.get(1);
                for (int i = 2; i < insnList.size(); i++) {
                    if (insnList.get(i) instanceof FieldInsnNode n
                            && n.getOpcode() == PUTFIELD
                            && n.name.equals("b")) {
                        insnList.remove(n);  // putfield b 1
                        insnList.remove(n0); // aload 0
                        insnList.remove(n1); // iconst_1
                        break;
                    }
                    n0 = n1;
                    n1 = insnList.get(i);
                }
                break;
            }
        }
        // 保存修改后的字节码
        cn.accept(cw);
        Files.write(Path.of("MyClass.class"), cw.toByteArray());
    }
}
