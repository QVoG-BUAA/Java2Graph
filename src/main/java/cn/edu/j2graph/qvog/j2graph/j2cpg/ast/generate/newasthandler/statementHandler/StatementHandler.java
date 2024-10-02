package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.statementHandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.Handler;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement.DefaultStatement;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement.Statement;

public class StatementHandler extends Handler {
    InitialASTNode initialASTNode;

    public StatementHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public Statement build() {
        Statement statement = null;
        String type = initialASTNode.type;
        if (type.equals("VariableDeclarationStatement")) {
            statement = new VariableDeclarationStatementHandler(initialASTNode).build();
        } else if (type.equals("ExpressionStatement")) {
            statement = new ExpressionStatementHandler(initialASTNode).build();
        } else if (type.equals("IfStatement")) {
            statement = new IfStatementHandler(initialASTNode).build();
        } else if (type.equals("TryStatement")) {
            statement = new TryStatementHandler(initialASTNode).build();
        } else {
            statement = new UnconvertedStatementHandler(initialASTNode).build();
        }
        if (statement instanceof DefaultStatement) {
            return new UnconvertedStatementHandler(initialASTNode).build();
        }
        return statement;
    }
}
