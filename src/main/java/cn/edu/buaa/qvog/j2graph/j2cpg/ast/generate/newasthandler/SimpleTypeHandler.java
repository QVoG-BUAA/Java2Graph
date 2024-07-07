package cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;

public class SimpleTypeHandler extends Handler {
    InitialASTNode initialASTNode;

    public SimpleTypeHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public String build() {
        String string = "";
        for (InitialASTNode initialASTNode : initialASTNode.childNodeList) {
            String type = initialASTNode.type;
            if (type.equals("SimpleName")) {
                string = new SimpleNameHandler(initialASTNode).build();
            }
        }
        return string;
    }
}
