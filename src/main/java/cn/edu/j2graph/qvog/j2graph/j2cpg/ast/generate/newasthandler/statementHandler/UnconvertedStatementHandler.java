package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.statementHandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.Handler;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement.DefaultStatement;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement.Statement;

public class UnconvertedStatementHandler extends Handler {
    InitialASTNode initialASTNode;

    public UnconvertedStatementHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public Statement build() {
        return new DefaultStatement(initialASTNode.endLine, initialASTNode.startLine, initialASTNode.label);
    }
}
