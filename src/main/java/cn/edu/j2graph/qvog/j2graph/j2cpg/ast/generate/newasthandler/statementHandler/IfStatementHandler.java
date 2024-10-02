package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.statementHandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.BlockHandler;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.Handler;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.InfixExpressionHandler;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.MethodInvocationHandler;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement.IfStatement;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement.Statement;

public class IfStatementHandler extends Handler {
    InitialASTNode initialASTNode;

    public IfStatementHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public Statement build() {
        IfStatement ifStatement = new IfStatement(initialASTNode.endLine, initialASTNode.startLine, initialASTNode.label);
        for (int index = 0; index < initialASTNode.childNodeList.size(); index++) {
            if (index == 0) {
                if (initialASTNode.childNodeList.get(index).type.equals("MethodInvocation")) {
                    ifStatement.test = new MethodInvocationHandler(initialASTNode.childNodeList.get(index)).build();
                } else if (initialASTNode.childNodeList.get(index).type.equals("InfixExpression")) {
                    ifStatement.test = new InfixExpressionHandler(initialASTNode.childNodeList.get(index)).build();
                } else {
                    return new UnconvertedStatementHandler(initialASTNode).build();
                }
            } else {
                if (initialASTNode.childNodeList.get(index).type.equals("Block")) {
                    ifStatement.body = new BlockHandler(initialASTNode.childNodeList.get(index)).build();
                } else {
                    return new UnconvertedStatementHandler(initialASTNode).build();
                }
            }
        }
        ifStatement.lineno = initialASTNode.startLine;
        ifStatement.end_lineno = initialASTNode.endLine;
        ifStatement.name = initialASTNode.label;
        return ifStatement;
    }
}
