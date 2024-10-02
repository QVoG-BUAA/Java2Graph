package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast;

import java.util.ArrayList;
import java.util.List;

public class Args extends ASTNode {
    //@SerializedName("_type1")
    public String _type = "arguments";

    public List<Arg> args = new ArrayList<>();
    public String name;
    public int end_lineno;
    public int lineno;
}
