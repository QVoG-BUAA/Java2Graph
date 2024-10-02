package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.BinaryOperator;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.Constant;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.Name;

public class InfixExpressionHandler extends Handler {
    public InitialASTNode initialASTNode;

    public InfixExpressionHandler(InitialASTNode initialASTNode) {
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
        } else if (initialASTNode.childNodeList.get(0).type.equals("SimpleName")) {
            Name name = new Name();
            name.name = new SimpleNameHandler(initialASTNode.childNodeList.get(0)).build();
            name.id = name.name;
            name.end_lineno = initialASTNode.endLine;
            name.lineno = initialASTNode.startLine;
            binaryOperator.left = name;
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

/*public class InfixExpressionHandler extends Handler{

    InitialASTNode initialASTNode;

    public InfixExpressionHandler(InitialASTNode initialASTNode) {
        this.initialASTNode = initialASTNode;
    }

    public ASTNode build(){
        Compare compare = new Compare();
        String ops = initialASTNode.label;
        if(ops.contains("!=")){
            compare.ops.add("NotEq()");
        }
        else if(ops.contains("==")){
            compare.ops.add("Eq()");
        }
        else {
            System.out.println("InfixExpressionHandlerError1");
            return new UnconvertedASTNodeHandler().build();
        }
        for(int index=0; index<initialASTNode.childNodeList.size(); index++){
            if(initialASTNode.childNodeList.get(index).type.equals("SimpleName")){
                Name name = new Name();
                name.id = new SimpleNameHandler(initialASTNode.childNodeList.get(index)).build();
                name.lineno = initialASTNode.childNodeList.get(index).startLine;
                name.end_lineno = initialASTNode.childNodeList.get(index).endLine;
                name.name = initialASTNode.childNodeList.get(index).label;
                if(index==0){
                    compare.left = name;
                }
            }
            else if(initialASTNode.childNodeList.get(index).type.equals("NullLiteral")){
                Constant constant = new Constant();
                constant.value = new NullLiteralHandler(initialASTNode.childNodeList.get(index)).build();
                constant.isNull = true;
                constant.lineno = initialASTNode.childNodeList.get(index).startLine;
                constant.end_lineno = initialASTNode.childNodeList.get(index).endLine;
                constant.name = initialASTNode.childNodeList.get(index).label;
                if(index!=0){
                    compare.comparators.add(constant);
                }
            }
            else {
                System.out.println("InfixExpressionHandlerError2");
                return new UnconvertedASTNodeHandler().build();
            }
        }
        compare.name=initialASTNode.label;
        compare.end_lineno=initialASTNode.endLine;
        compare.lineno=initialASTNode.startLine;
        return compare;
    }

}*/
