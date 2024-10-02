package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class CatchClause extends ASTNode {
    public String _type = "Catch";
    public List<Statement> body = new ArrayList<>();
    public ASTNode exception;
    public int end_lineno;
    public int lineno;
    public String name;
}
