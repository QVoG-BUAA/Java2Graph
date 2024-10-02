package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.statementHandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.BlockHandler;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.CatchClauseHandler;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.Handler;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement.Statement;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement.TryStatement;

public class TryStatementHandler extends Handler {
    InitialASTNode initialASTNode;

    public TryStatementHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public Statement build() {
        TryStatement tryStatement = new TryStatement(initialASTNode.endLine, initialASTNode.startLine, initialASTNode.label);
        for (int i = 0; i < initialASTNode.childNodeList.size(); i++) {
            if (initialASTNode.childNodeList.get(i).type.equals("Block")) {
                if (i == 0) {
                    tryStatement.tryBody = new BlockHandler(initialASTNode.childNodeList.get(i)).build();
                    tryStatement.try_lineno = initialASTNode.childNodeList.get(i).startLine;
                    tryStatement.try_end_lineno = initialASTNode.childNodeList.get(i).endLine;
                } else {
                    tryStatement.finallyBody = new BlockHandler(initialASTNode.childNodeList.get(i)).build();
                    tryStatement.finally_lineno = initialASTNode.childNodeList.get(i).startLine;
                    tryStatement.finally_end_lineno = initialASTNode.childNodeList.get(i).endLine;
                }
            } else if (initialASTNode.childNodeList.get(i).type.equals("CatchClause")) {
                tryStatement.catchClauses.add(new CatchClauseHandler(initialASTNode.childNodeList.get(i)).build());
            }
        }


        tryStatement.lineno = initialASTNode.startLine;
        tryStatement.end_lineno = initialASTNode.endLine;
        tryStatement.name = initialASTNode.label;
        return tryStatement;
    }
}
