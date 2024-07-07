package cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.BinaryOperator;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.Constant;
import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.Name;

public class BinaryOperatorHandler {
    public InitialASTNode initialASTNode;

    public BinaryOperatorHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public ASTNode build() {
        BinaryOperator binaryOperator = new BinaryOperator();
        if (initialASTNode.childNodeList.get(0).type.equals("NumberLiteral")) {
            Constant left = new Constant();
            left.value = new NumberLiteralHandler(initialASTNode.childNodeList.get(0)).build();
            left.isNull = false;
            left.lineno = initialASTNode.childNodeList.get(0).startLine;
            left.end_lineno = initialASTNode.childNodeList.get(0).endLine;
            left.name = initialASTNode.childNodeList.get(0).label;
            binaryOperator.left = left;
        } else {
            binaryOperator.left = new UnconvertedASTNodeHandler().build();
        }
        if (initialASTNode.childNodeList.get(1).type.equals("NumberLiteral")) {
            Constant right = new Constant();
            right.value = new NumberLiteralHandler(initialASTNode.childNodeList.get(1)).build();
            right.isNull = false;
            right.lineno = initialASTNode.childNodeList.get(1).startLine;
            right.end_lineno = initialASTNode.childNodeList.get(1).endLine;
            right.name = initialASTNode.childNodeList.get(1).label;
            binaryOperator.right = right;
        } else if (initialASTNode.childNodeList.get(1).type.equals("SimpleName")) {
            Name name = new Name();
            name.name = new SimpleNameHandler(initialASTNode.childNodeList.get(1)).build();
            name.id = name.name;
            name.end_lineno = initialASTNode.endLine;
            name.lineno = initialASTNode.startLine;
            binaryOperator.right = name;
        } else {
            binaryOperator.right = new UnconvertedASTNodeHandler().build();
        }
        int space1 = initialASTNode.label.indexOf(" ");
        int space2 = initialASTNode.label.lastIndexOf(" ");
        binaryOperator.op = initialASTNode.label.substring(space1 + 1, space2);
        binaryOperator.name = initialASTNode.label;
        binaryOperator.lineno = initialASTNode.startLine;
        binaryOperator.end_lineno = initialASTNode.endLine;
        return binaryOperator;
    }
}
