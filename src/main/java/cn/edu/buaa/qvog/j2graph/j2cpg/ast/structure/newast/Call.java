package cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast;

import java.util.ArrayList;
import java.util.List;

public class Call extends Value {
    public String _type = "Call";
    public List<ASTNode> args = new ArrayList<>();
    public ASTNode func;
    public int end_lineno;
    public int lineno;
    public String name;
}
