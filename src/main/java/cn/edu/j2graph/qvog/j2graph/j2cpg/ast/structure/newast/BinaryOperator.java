package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast;

public class BinaryOperator extends ASTNode {
    public String _type = "BinOp";
    public ASTNode left;
    public String op;
    public ASTNode right;
    public int end_lineno;
    public int lineno;
    public String name;
}
