package cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.Attribute;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.Call;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.Name;

public class MethodInvocationHandler extends Handler {
    InitialASTNode initialASTNode;

    public MethodInvocationHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public ASTNode build() {
        Call call = new Call();
        Attribute attribute = new Attribute();
        Name name = new Name();
        for (int i = 0; i < initialASTNode.childNodeList.size(); i++) {
            if (i == 0 && initialASTNode.childNodeList.get(i).type.equals("SimpleName")) {
                name.id = new SimpleNameHandler(initialASTNode.childNodeList.get(i)).build();
                attribute.value = name;

            } else if (initialASTNode.childNodeList.get(i).type.equals("SimpleName")) {
                attribute.attr = new SimpleNameHandler(initialASTNode.childNodeList.get(i)).build();
                call.func = attribute;
            } else {
                return new UnconvertedASTNodeHandler().build();
            }
        }
        attribute.name = initialASTNode.label;
        attribute.lineno = initialASTNode.startLine;
        attribute.end_lineno = initialASTNode.endLine;
        name.name = name.id;
        name.end_lineno = initialASTNode.endLine;
        name.lineno = initialASTNode.startLine;
        call.lineno = initialASTNode.startLine;
        call.end_lineno = initialASTNode.endLine;
        call.name = initialASTNode.label;
        return call;
    }
}
