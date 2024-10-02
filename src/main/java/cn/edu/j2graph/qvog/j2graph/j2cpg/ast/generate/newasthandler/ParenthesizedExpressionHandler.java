package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;

public class ParenthesizedExpressionHandler extends Handler {
    public InitialASTNode initialASTNode;

    public ParenthesizedExpressionHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public ASTNode build() {
        ASTNode binaryOperator;
        if (initialASTNode.childNodeList.get(0).type.equals("InfixExpression")) {
            binaryOperator = new InfixExpressionHandler(initialASTNode.childNodeList.get(0)).build();
        } else {
            binaryOperator = new UnconvertedASTNodeHandler().build();
        }
        return binaryOperator;
    }

}
