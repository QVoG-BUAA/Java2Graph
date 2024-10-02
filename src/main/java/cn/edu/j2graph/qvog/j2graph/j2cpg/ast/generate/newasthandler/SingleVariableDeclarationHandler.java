package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.Arg;

public class SingleVariableDeclarationHandler extends Handler {
    InitialASTNode initialASTNode;

    public SingleVariableDeclarationHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public Arg build() {
        Arg arg = new Arg();
        for (InitialASTNode initialASTNode : initialASTNode.childNodeList) {
            String type = initialASTNode.type;
            if (type.equals("SimpleType")) {
                arg.argClass = new SimpleTypeHandler(initialASTNode).build();
            } else if (type.equals("SimpleName")) {
                arg.arg = new SimpleNameHandler(initialASTNode).build();
                arg.lineno = initialASTNode.startLine;
                arg.end_lineno = initialASTNode.endLine;
                arg.name = initialASTNode.label;
            } else if (type.equals("PrimitiveType")) {
                arg.argClass = new PrimitiveTypeHandler(initialASTNode).build();
            } else if (type.equals("ArrayType")) {
                arg.isArray = true;
                arg.argClass = new ArrayTypeHandler(initialASTNode).build();
            }
        }
        return arg;
    }
}
