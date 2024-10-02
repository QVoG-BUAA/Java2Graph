package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement.Statement;

import java.util.ArrayList;
import java.util.List;

public class FunctionDef extends ASTNode {
    public String _type = "FunctionDef";
    public Args args = new Args();
    public List<String> modifier = new ArrayList<>();
    public String primitiveType;
    public List<Statement> body = new ArrayList<>();
    public int end_lineno;
    public int lineno;
    public String name;

}
