package cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.Call;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.Name;

public class CastExpressionHandler {
    public InitialASTNode initialASTNode;

    public CastExpressionHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public ASTNode build() {
        Call call = new Call();
        if (initialASTNode.childNodeList.get(0).type.equals("PrimitiveType")) {
            Name name = new Name();
            name.id = initialASTNode.childNodeList.get(0).label;
            name.lineno = initialASTNode.childNodeList.get(0).startLine;
            name.end_lineno = initialASTNode.childNodeList.get(0).endLine;
            name.name = initialASTNode.childNodeList.get(0).label;
            call.func = name;
        } else {
            call.func = new UnconvertedASTNodeHandler().build();
        }
        if (initialASTNode.childNodeList.get(1).type.equals("ParenthesizedExpression")) {
            call.args.add(new ParenthesizedExpressionHandler(initialASTNode.childNodeList.get(1)).build());
        } else {
            call.args.add(new UnconvertedASTNodeHandler().build());
        }
        call.name = initialASTNode.label;
        call.lineno = initialASTNode.startLine;
        call.end_lineno = initialASTNode.endLine;
        return call;
    }
}
