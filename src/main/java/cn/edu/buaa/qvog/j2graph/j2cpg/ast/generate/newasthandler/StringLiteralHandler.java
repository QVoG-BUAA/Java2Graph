package cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;

public class StringLiteralHandler extends Handler {
    InitialASTNode initialASTNode;

    public StringLiteralHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public String build() {
        return initialASTNode.label;
    }
}
