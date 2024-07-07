package cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler.statementHandler.StatementHandler;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class BlockHandler extends Handler {
    public InitialASTNode initialASTNode;

    public BlockHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public List<Statement> build() {
        List<Statement> statementList = new ArrayList<>();
        for (InitialASTNode initialASTNode : initialASTNode.childNodeList) {
            statementList.add(new StatementHandler(initialASTNode).build());
        }
        return statementList;
    }
}
