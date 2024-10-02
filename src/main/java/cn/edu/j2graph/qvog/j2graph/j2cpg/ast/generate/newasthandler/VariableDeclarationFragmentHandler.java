package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler.statementHandler.UnconvertedStatementHandler;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialASTNode;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.Constant;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.Name;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement.DeclineStatement;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement.Statement;

public class VariableDeclarationFragmentHandler extends Handler {
    InitialASTNode initialASTNode;
    String type;
    String name;

    public VariableDeclarationFragmentHandler(InitialASTNode initialASTNode, String type, String name) {
        this.initialASTNode = initialASTNode;
        this.type = type;
        this.name = name;
    }

    public Statement build() {
        DeclineStatement declineStatement = new DeclineStatement(initialASTNode.endLine, initialASTNode.startLine, name);
        if (type.equals("SimpleType")) {
            Name name = new Name();
            name.name = new SimpleNameHandler(initialASTNode.childNodeList.get(0)).build();
            name.id = name.name;
            name.end_lineno = initialASTNode.endLine;
            name.lineno = initialASTNode.startLine;
            declineStatement.targets.add(name);
            if (initialASTNode.childNodeList.size() > 1) {
                String valueType = initialASTNode.childNodeList.get(1).type;
                if (valueType.equals("NullLiteral")) {
                    Constant constant = new Constant();
                    constant.value = new NullLiteralHandler(initialASTNode.childNodeList.get(1)).build();
                    constant.isNull = true;
                    constant.lineno = initialASTNode.childNodeList.get(1).startLine;
                    constant.end_lineno = initialASTNode.childNodeList.get(1).endLine;
                    constant.name = initialASTNode.childNodeList.get(1).label;
                    declineStatement.value = constant;
                } else if (valueType.equals("ClassInstanceCreation")) {
                    declineStatement.value = new ClassInstanceCreationHandler(initialASTNode.childNodeList.get(1)).build();
                } else if (valueType.equals("StringLiteral")) {
                    Constant constant = new Constant();
                    constant.value = new StringLiteralHandler(initialASTNode.childNodeList.get(1)).build();
                    constant.isNull = false;
                    constant.lineno = initialASTNode.childNodeList.get(1).startLine;
                    constant.end_lineno = initialASTNode.childNodeList.get(1).endLine;
                    constant.name = initialASTNode.childNodeList.get(1).label;
                    declineStatement.value = constant;
                } else {
                    return new UnconvertedStatementHandler(initialASTNode).build();
                }
            } else {
                return new UnconvertedStatementHandler(initialASTNode).build();
            }
            /*else {
                Constant constant = new Constant();
                constant.value = "null";
                constant.isNull = true;
                constant.lineno = initialASTNode.childNodeList.get(0).startLine;
                constant.end_lineno = initialASTNode.childNodeList.get(0).endLine;
                constant.name = initialASTNode.childNodeList.get(0).label;
                declineStatement.value = constant;
            }*/
        } else if (type.equals("PrimitiveType")) {
            Name name = new Name();
            name.name = new SimpleNameHandler(initialASTNode.childNodeList.get(0)).build();
            name.id = name.name;
            name.end_lineno = initialASTNode.endLine;
            name.lineno = initialASTNode.startLine;
            declineStatement.targets.add(name);
            if (initialASTNode.childNodeList.size() > 1) {
                String valueType = initialASTNode.childNodeList.get(1).type;
                if (valueType.equals("SimpleName")) {
                    Name name1 = new Name();
                    name1.id = new SimpleNameHandler(initialASTNode.childNodeList.get(1)).build();
                    name1.lineno = initialASTNode.childNodeList.get(1).startLine;
                    name1.end_lineno = initialASTNode.childNodeList.get(1).endLine;
                    name1.name = initialASTNode.childNodeList.get(1).label;
                    declineStatement.value = name1;
                } else if (valueType.equals("InfixExpression")) {
                    declineStatement.value = new InfixExpressionHandler(initialASTNode.childNodeList.get(1)).build();
                } else if (valueType.equals("MethodInvocation")) {
                    declineStatement.value = new MethodInvocationHandler(initialASTNode.childNodeList.get(1)).build();
                } else if (valueType.equals("NumberLiteral")) {
                    Constant constant = new Constant();
                    constant.value = new NumberLiteralHandler(initialASTNode.childNodeList.get(1)).build();
                    constant.isNull = false;
                    constant.lineno = initialASTNode.childNodeList.get(1).startLine;
                    constant.end_lineno = initialASTNode.childNodeList.get(1).endLine;
                    constant.name = initialASTNode.childNodeList.get(1).label;
                    declineStatement.value = constant;
                } else if (valueType.equals("CastExpression")) {
                    declineStatement.value = new CastExpressionHandler(initialASTNode.childNodeList.get(1)).build();
                } else {
                    return new UnconvertedStatementHandler(initialASTNode).build();
                }
            } else {
                return new UnconvertedStatementHandler(initialASTNode).build();
            }
        } else {
            return new UnconvertedStatementHandler(initialASTNode).build();
        }
        return declineStatement;
    }

}
