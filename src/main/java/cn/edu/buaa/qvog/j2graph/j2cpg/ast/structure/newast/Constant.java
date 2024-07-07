package cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast;

public class Constant extends ASTNode {
    public String _type = "Constant";
    public Object value;
    public String type;
    public int end_lineno;
    public int lineno;
    public String name;
    public boolean isNull = false;
}
