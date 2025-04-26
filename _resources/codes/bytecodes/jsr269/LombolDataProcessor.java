package jsr269;

import javax.annotation.processing.*;
        import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.naming.Context;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("jsr269.TestAnnotation")
public class LombolDataProcessor extends AbstractProcessor {

    private JavacTrees javacTrees;
    private TreeMaker treeMaker;
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        javacTrees = JavacTrees.instance(context);
        treeMaker = TreeMaker.instance(context);
        names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        // do something
        roundEnv.getElementsAnnotatedWith(Data.class).forEach(element -> {
            JCTree tree = javacTrees.getTree(element);
            tree.accept(new TreeTranslator() {
                @Override
                public void visitClassDef(JCTree.JCClassDecl classDecl) {
                    classDecl.defs.stream()
                            .filter(it -> it.getKind().equals(Tree.Kind.VARIABLE))
                            .map(it -> (JCTree.JCVariableDecl) it)
                            .forEach(it ->
                                    classDecl.defs = classDecl.defs.prepend(genGetterMethod(it)).prepend(genSetterMethod(it)));
                    super.visitClassDef(classDecl);
                }
            });
        });
        return true;
    }

    private JCTree.JCMethodDecl genGetterMethod(JCTree.JCVariableDecl field) {
        // return this.xxx
        JCTree.JCReturn jcReturn = treeMaker.Return(treeMaker.Select(
                treeMaker.Ident(names.fromString("this")),
                field.sym));
        ListBuffer<JCTree.JCStatement> listBuffer = new ListBuffer<>();
        listBuffer.append(jcReturn);

        // 修饰符
        JCTree.JCModifiers modifier = treeMaker.Modifiers(Flags.PUBLIC);
        // 方法名
        char f = field.name.toString().charAt(0);
        if (Character.isLowerCase(f)) f = Character.toUpperCase(f);
        Name name = names.fromString("get" + f + field.name.toString().substring(1));
        // 返回值类型
        JCTree.JCExpression returnType = field.vartype;
        // 方法体
        JCTree.JCBlock body = treeMaker.Block(0, listBuffer.toList());
        // 泛型列表
        List<JCTree.JCTypeParameter> genericList = List.nil();
        // 参数值
        List<JCTree.JCVariableDecl> params = List.nil();
        // 异常列表
        List<JCTree.JCExpression> thrown = List.nil();

        return treeMaker.MethodDef(modifier, name, returnType, genericList, params, thrown, body, null);
    }

    private JCTree.JCMethodDecl genSetterMethod(JCTree.JCVariableDecl field) {
        // return this.xxx = xxx
        JCTree.JCVariableDecl decl = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER, List.nil()),
                field.getName(), field.vartype, null);
        ListBuffer<JCTree.JCStatement> listBuffer = new ListBuffer<>();
        listBuffer.append(decl);

        // 修饰符
        JCTree.JCModifiers modifier = treeMaker.Modifiers(Flags.PUBLIC);
        // 方法名
        char f = field.name.toString().charAt(0);
        if (Character.isLowerCase(f)) f = Character.toUpperCase(f);
        Name name = names.fromString("set" + f + field.name.toString().substring(1));
        // 返回值类型
        JCTree.JCExpression returnType = treeMaker.Type(new Type.JCVoidType());
        // 方法体
        JCTree.JCBlock body = treeMaker.Block(0, listBuffer.toList());
        // 泛型列表
        List<JCTree.JCTypeParameter> genericList = List.nil();
        // 参数值
        List<JCTree.JCVariableDecl> params = List.of(field);
        // 异常列表
        List<JCTree.JCExpression> thrown = List.nil();

        return treeMaker.MethodDef(modifier, name, returnType, genericList, params, thrown, body, null);
    }
}

