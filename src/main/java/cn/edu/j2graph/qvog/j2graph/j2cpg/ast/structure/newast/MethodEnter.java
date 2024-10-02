package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast;

import java.util.List;

public class MethodEnter extends ASTNode {
    public String _type = "FunctionDef";
    public List<Arg> args;
    public int end_lineno;
    public int lineno;

    public MethodEnter(List<Arg> args, String lineno, String end_lineno) {
        this.args = args;
        this.lineno = Integer.parseInt(lineno);
        this.end_lineno = Integer.parseInt(end_lineno);
    }
}
