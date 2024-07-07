package cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast;

import java.util.ArrayList;
import java.util.List;

public class Compare extends ASTNode {
    public String _type = "Compare";
    public List<String> ops = new ArrayList<>();
    public List<ASTNode> comparators = new ArrayList<>();
    public ASTNode left;
    public int end_lineno;
    public int lineno;
    public String name;
}
