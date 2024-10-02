package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.statementHandler.UnconvertedStatementHandler;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.CatchClause;

public class CatchClauseHandler {
    public InitialASTNode initialASTNode;

    public CatchClauseHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public ASTNode build() {
        CatchClause catchClause = new CatchClause();
        if (initialASTNode.childNodeList.get(0).type.equals("SingleVariableDeclaration")) {
            catchClause.exception = new SingleVariableDeclarationHandler(initialASTNode.childNodeList.get(0)).build();
        } else {
            catchClause.exception = new UnconvertedASTNodeHandler().build();
        }
        if (initialASTNode.childNodeList.get(1).type.equals("Block")) {
            catchClause.body = new BlockHandler(initialASTNode.childNodeList.get(1)).build();
        } else {
            catchClause.body.add(new UnconvertedStatementHandler(initialASTNode.childNodeList.get(1)).build());
        }
        catchClause.name = initialASTNode.label;
        catchClause.lineno = initialASTNode.startLine;
        catchClause.end_lineno = initialASTNode.endLine;
        return catchClause;
    }
}
