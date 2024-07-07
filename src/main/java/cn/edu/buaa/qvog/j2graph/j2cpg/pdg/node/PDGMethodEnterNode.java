package cn.edu.buaa.qvog.j2graph.j2cpg.pdg.node;

import cn.edu.buaa.qvog.j2graph.j2cpg.pe.ExpressionInfo;
import cn.edu.buaa.qvog.j2graph.j2cpg.pe.MethodInfo;
import cn.edu.buaa.qvog.j2graph.j2cpg.pe.ProgramElementInfo;

public class PDGMethodEnterNode extends PDGControlNode {

    private PDGMethodEnterNode(final ProgramElementInfo methodEnterExpression) {
        super(methodEnterExpression);
    }

    static public PDGMethodEnterNode getInstance(final MethodInfo method) {
        assert null != method : "\"method\" is null.";
        final ProgramElementInfo methodEnterExpression = new ExpressionInfo(
                ExpressionInfo.CATEGORY.MethodEnter, method.startLine,
                method.endLine);
        methodEnterExpression.setText("Enter");
        return new PDGMethodEnterNode(methodEnterExpression);
    }
}
