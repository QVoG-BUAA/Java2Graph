package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.Assign;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.Constant;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.Name;

public class AssignmentHandler extends Handler {
    InitialASTNode initialASTNode;

    public AssignmentHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public ASTNode build() {
        Assign assign = new Assign();
        Name name = new Name();
        name.name = new SimpleNameHandler(initialASTNode.childNodeList.get(0)).build();
        name.id = name.name;
        name.end_lineno = initialASTNode.endLine;
        name.lineno = initialASTNode.startLine;
        assign.targets.add(name);
        String valueType = initialASTNode.childNodeList.get(1).type;
        if (valueType.equals("NullLiteral")) {
            Constant constant = new Constant();
            constant.value = new NullLiteralHandler(initialASTNode.childNodeList.get(1)).build();
            constant.isNull = true;
            constant.lineno = initialASTNode.childNodeList.get(1).startLine;
            constant.end_lineno = initialASTNode.childNodeList.get(1).endLine;
            constant.name = initialASTNode.childNodeList.get(1).label;
            assign.value = constant;
        } else if (valueType.equals("NumberLiteral")) {
            Constant constant = new Constant();
            constant.value = new NumberLiteralHandler(initialASTNode.childNodeList.get(1)).build();
            constant.isNull = false;
            constant.lineno = initialASTNode.childNodeList.get(1).startLine;
            constant.end_lineno = initialASTNode.childNodeList.get(1).endLine;
            constant.name = initialASTNode.childNodeList.get(1).label;
            assign.value = constant;
        } else if (valueType.equals("StringLiteral")) {
            Constant constant = new Constant();
            constant.value = new StringLiteralHandler(initialASTNode.childNodeList.get(1)).build();
            constant.isNull = false;
            constant.lineno = initialASTNode.childNodeList.get(1).startLine;
            constant.end_lineno = initialASTNode.childNodeList.get(1).endLine;
            constant.name = initialASTNode.childNodeList.get(1).label;
            assign.value = constant;
        } else if (valueType.equals("SimpleName")) {
            Name name1 = new Name();
            name1.id = new SimpleNameHandler(initialASTNode.childNodeList.get(1)).build();
            name1.lineno = initialASTNode.childNodeList.get(1).startLine;
            name1.end_lineno = initialASTNode.childNodeList.get(1).endLine;
            name1.name = initialASTNode.childNodeList.get(1).label;
            assign.value = name1;
        } else if (valueType.equals("InfixExpression")) {
            assign.value = new InfixExpressionHandler(initialASTNode.childNodeList.get(1)).build();
        } else {
            assign.value = new UnconvertedASTNodeHandler().build();
        }
        assign.name = initialASTNode.label;
        assign.lineno = initialASTNode.startLine;
        assign.end_lineno = initialASTNode.endLine;
        return assign;
    }
}
