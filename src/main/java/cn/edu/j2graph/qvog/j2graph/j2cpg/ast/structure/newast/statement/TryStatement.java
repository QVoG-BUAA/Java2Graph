package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class TryStatement extends Statement {
    public String _type = "Try";
    public List<Statement> tryBody = new ArrayList<>();
    public List<ASTNode> catchClauses = new ArrayList<>();
    public List<Statement> finallyBody = new ArrayList<>();
    public int end_lineno;
    public int lineno;
    public String name;
    public int try_end_lineno;
    public int try_lineno;
    public int finally_end_lineno;
    public int finally_lineno;

    public TryStatement(int end_lineno, int lineno, String name) {
        super(end_lineno, lineno, name);
        this.end_lineno = end_lineno;
        this.lineno = lineno;
        this.name = name;
    }
}
