package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;

public class PrimitiveTypeHandler extends Handler {
    InitialASTNode initialASTNode;

    public PrimitiveTypeHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public String build() {
        return initialASTNode.label;
    }
}
