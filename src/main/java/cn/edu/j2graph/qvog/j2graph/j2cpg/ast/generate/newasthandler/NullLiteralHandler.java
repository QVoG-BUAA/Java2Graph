package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;

public class NullLiteralHandler extends Handler {
    InitialASTNode initialASTNode;

    public NullLiteralHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public Object build() {
        return null;
    }
}
