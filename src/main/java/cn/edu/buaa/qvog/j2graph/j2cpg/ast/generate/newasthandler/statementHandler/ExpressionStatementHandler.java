package cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler.statementHandler;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler.AssignmentHandler;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler.Handler;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler.MethodInvocationHandler;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.DefaultASTNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.statement.ExpressionStatement;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.statement.Statement;

public class ExpressionStatementHandler extends Handler {
    InitialASTNode initialASTNode;

    public ExpressionStatementHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public Statement build() {
        ExpressionStatement expressionStatement = new ExpressionStatement(initialASTNode.endLine, initialASTNode.startLine, initialASTNode.label);
        for (InitialASTNode initialASTNode : initialASTNode.childNodeList) {
            if (initialASTNode.type.equals("MethodInvocation")) {
                expressionStatement.value = new MethodInvocationHandler(initialASTNode).build();
            } else if (initialASTNode.type.equals("Assignment")) {
                expressionStatement.value = new AssignmentHandler(initialASTNode).build();
            } else {
                return new UnconvertedStatementHandler(initialASTNode).build();
            }
            if (expressionStatement.value instanceof DefaultASTNode) {
                return new UnconvertedStatementHandler(initialASTNode).build();
            }
        }
        return expressionStatement;
    }
}
