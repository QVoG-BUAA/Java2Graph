package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;

public class ExpressionStatement extends Statement {
    public String _type = "Expr";
    public ASTNode value;
    public int end_lineno;
    public int lineno;
    public String name;

    public ExpressionStatement(int end_lineno, int lineno, String name) {
        super(end_lineno, lineno, name);
        this.end_lineno = end_lineno;
        this.lineno = lineno;
        this.name = name;
    }
}
