package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.Arg;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.ClassInstanceCreation;

public class ClassInstanceCreationHandler extends Handler {
    public InitialASTNode initialASTNode;

    public ClassInstanceCreationHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public ASTNode build() {
        ClassInstanceCreation classInstanceCreation = new ClassInstanceCreation();
        for (int index = 0; index < initialASTNode.childNodeList.size(); index++) {
            if (index == 0) {
                if (initialASTNode.childNodeList.get(index).type.equals("SimpleType")) {
                    classInstanceCreation.className = new SimpleTypeHandler(initialASTNode.childNodeList.get(index)).build();
                } else {
                    classInstanceCreation.className = "";
                }
            } else {
                Arg arg = new Arg();
                if (initialASTNode.childNodeList.get(index).type.equals("StringLiteral")) {
                    arg.arg = new StringLiteralHandler(initialASTNode.childNodeList.get(index)).build();
                    arg.argClass = "String";
                    arg.name = initialASTNode.childNodeList.get(index).label;
                    arg.lineno = initialASTNode.childNodeList.get(index).startLine;
                    arg.end_lineno = initialASTNode.childNodeList.get(index).endLine;
                    classInstanceCreation.args.args.add(arg);
                } else {
                    classInstanceCreation.args.args.add(arg);
                }
            }
        }
        classInstanceCreation.args.lineno = initialASTNode.startLine;
        classInstanceCreation.args.end_lineno = initialASTNode.endLine;
        classInstanceCreation.name = initialASTNode.label;
        classInstanceCreation.lineno = initialASTNode.startLine;
        classInstanceCreation.end_lineno = initialASTNode.endLine;
        return classInstanceCreation;
    }

}
