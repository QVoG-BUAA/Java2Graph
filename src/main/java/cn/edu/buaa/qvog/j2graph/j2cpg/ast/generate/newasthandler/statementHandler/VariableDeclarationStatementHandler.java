package cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler.statementHandler;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler.Handler;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler.PrimitiveTypeHandler;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler.SimpleTypeHandler;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler.VariableDeclarationFragmentHandler;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.Name;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.statement.DeclineStatement;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.statement.DefaultStatement;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.statement.Statement;

public class VariableDeclarationStatementHandler extends Handler {
    InitialASTNode initialASTNode;

    public VariableDeclarationStatementHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public Statement build() {
        Statement declineStatement = null;
        String type = "";
        String kind = "";
        String name = initialASTNode.label;
        for (InitialASTNode initialASTNode : initialASTNode.childNodeList) {
            if (initialASTNode.type.equals("SimpleType")) {
                type = "SimpleType";
                kind = new SimpleTypeHandler(initialASTNode).build();
            } else if (initialASTNode.type.equals("PrimitiveType")) {
                type = "PrimitiveType";
                kind = new PrimitiveTypeHandler(initialASTNode).build();
            } else if (initialASTNode.type.equals("VariableDeclarationFragment")) {
                declineStatement = new VariableDeclarationFragmentHandler(initialASTNode, type, name).build();
                if (declineStatement instanceof DefaultStatement) {
                    return new UnconvertedStatementHandler(initialASTNode).build();
                } else {
                    ASTNode astNode = ((DeclineStatement) declineStatement).targets.get(((DeclineStatement) declineStatement).targets.size() - 1);
                    if (astNode instanceof Name) {
                        ((Name) astNode).type = kind;
                    }
                }
            } else {
                return new UnconvertedStatementHandler(initialASTNode).build();
            }
        }
        assert declineStatement != null;
        ((DeclineStatement) declineStatement).type = kind;
        return declineStatement;
    }
}
