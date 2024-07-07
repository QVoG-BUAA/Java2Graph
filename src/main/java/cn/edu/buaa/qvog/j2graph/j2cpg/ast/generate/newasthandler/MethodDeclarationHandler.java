package cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.FunctionDef;

public class MethodDeclarationHandler extends Handler {
    public InitialASTNode initialASTNode;

    public MethodDeclarationHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public FunctionDef build() {
        FunctionDef functionDef = new FunctionDef();
        functionDef.lineno = initialASTNode.startLine;
        functionDef.end_lineno = initialASTNode.endLine;
        functionDef.name = initialASTNode.label;
        for (InitialASTNode initialASTNode : initialASTNode.childNodeList) {
            String type = initialASTNode.type;
            if (type.equals("Modifier")) {
                functionDef.modifier.add(new ModifierHandler(initialASTNode).build());
            } else if (type.equals("PrimitiveType")) {
                functionDef.primitiveType = new PrimitiveTypeHandler(initialASTNode).build();
            } else if (type.equals("SimpleName")) {
                functionDef.name = new SimpleNameHandler(initialASTNode).build();
            } else if (type.equals("SingleVariableDeclaration")) {
                functionDef.args.args.add(new SingleVariableDeclarationHandler(initialASTNode).build());
                functionDef.args.lineno = initialASTNode.startLine;
                functionDef.args.end_lineno = initialASTNode.endLine;
                functionDef.args.name = initialASTNode.label;
            } else if (type.equals("Block")) {
                functionDef.body.addAll(new BlockHandler(initialASTNode).build());
            }
        }
        return functionDef;
    }
}
