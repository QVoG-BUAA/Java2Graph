package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class IfStatement extends Statement {
    public String _type = "If";
    public List<Statement> body = new ArrayList<>();
    public ASTNode test;
    public int end_lineno;
    public int lineno;
    public String name;

    public IfStatement(int end_lineno, int lineno, String name) {
        super(end_lineno, lineno, name);
        this.end_lineno = end_lineno;
        this.lineno = lineno;
        this.name = name;
    }


}
