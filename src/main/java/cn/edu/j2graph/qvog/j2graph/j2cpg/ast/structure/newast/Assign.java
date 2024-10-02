package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast;

import java.util.ArrayList;
import java.util.List;

public class Assign extends Value {
    public String _type = "Assign";
    public List<ASTNode> targets = new ArrayList<>();
    public ASTNode value;
    public int end_lineno;
    public int lineno;
    public String name;
}
