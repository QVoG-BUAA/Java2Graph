package cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.statement;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class DeclineStatement extends Statement {
    public String _type = "Decl";
    public List<ASTNode> targets = new ArrayList<>();
    public ASTNode value;
    public int end_lineno;
    public int lineno;
    public String name;
    public String type;

    public DeclineStatement(int end_lineno, int lineno, String name) {
        super(end_lineno, lineno, name);
        this.end_lineno = end_lineno;
        this.lineno = lineno;
        this.name = name;
    }
}
