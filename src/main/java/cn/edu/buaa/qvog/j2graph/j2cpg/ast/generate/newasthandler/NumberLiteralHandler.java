package cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;

public class NumberLiteralHandler extends Handler {
    InitialASTNode initialASTNode;

    public NumberLiteralHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public Integer build() {
        return Integer.parseInt(initialASTNode.label);
    }

}
