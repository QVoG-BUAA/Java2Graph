package cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast;

public class Arg extends ASTNode {
    public String _type = "arg";
    public boolean isArray = false;
    public String argClass;
    public String arg;
    public int end_lineno;
    public int lineno;
    public String name;
}
