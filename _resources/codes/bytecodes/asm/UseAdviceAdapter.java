package asm;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class UseAdviceAdapter {

    public static void main(String[] args) throws Exception {
        // 加载测试类
        URL src = CoreAPI1.class.getClassLoader().getResource("Foo.class");
        byte[] bytes = Files.readAllBytes(Path.of(src.toURI()));
        // 使用 ASM
        ClassReader cr = new ClassReader(bytes);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = new ClassVisitor(Opcodes.ASM9, cw) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor method = super.visitMethod(access, name, descriptor, signature, exceptions);
                if ("foo".equals(name)) {
                    return new AdviceAdapter(api, method, access, name, descriptor) {
                        // 声明几个类型描述符
                        static final String System = "java/lang/System";
                        static final String PrintStream = "java/io/PrintStream";
                        static final String PrintStreamDesc = "Ljava/io/PrintStream;";
                        static final String StringDesc = "Ljava/util/String;";
                        static final String printlnDesc = "(" + StringDesc + ")V";

                        private final Label start = new Label();

                        @Override
                        protected void onMethodEnter() {
                            super.onMethodEnter();
                            // System.out.println("enter foo");
                            mv.visitFieldInsn(GETSTATIC, System, "out", PrintStreamDesc);
                            mv.visitLdcInsn("enter " + name);
                            mv.visitMethodInsn(INVOKEVIRTUAL, PrintStream, "println", printlnDesc, false);
                            // [start] {
                            mv.visitLabel(start);
                        }
                        @Override
                        protected void onMethodExit(int opcode) {
                            super.onMethodExit(opcode);
                            // System.out.println("normal exit foo");
                            if (opcode != ATHROW) onFinally(opcode);
                        }
                        @Override
                        public void visitMaxs(int maxStack, int maxLocals) {
                            Label end = new Label();
                            mv.visitTryCatchBlock(start, end, end, null);
                            mv.visitLabel(end);
                            // } [end]
                            // System.out.println("err exit foo");
                            onFinally(ATHROW);
                            // throw e;
                            mv.visitInsn(ATHROW);
                            super.visitMaxs(maxStack, maxLocals);
                        }

                        private void onFinally(int opcode) {
                            mv.visitFieldInsn(GETSTATIC, System, "out", PrintStreamDesc);
                            String str = opcode == ATHROW ? "err exit " + name : "normal exit " + name;
                            mv.visitLdcInsn(str);
                            mv.visitMethodInsn(INVOKEVIRTUAL, PrintStream, "println", printlnDesc, false);
                        }
                    };
                }
                return method;
            }
        };
        cr.accept(cv, 0);
        // 保存修改后的字节码
        Files.write(Path.of("Foo.class"), cw.toByteArray());
    }
}
