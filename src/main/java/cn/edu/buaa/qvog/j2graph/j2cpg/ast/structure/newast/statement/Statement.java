package cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.statement;

import cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast.ASTNode;

public class Statement extends ASTNode {
    public transient int end_lineno;
    public transient int lineno;
    public transient String name;

    public Statement(int end_lineno, int lineno, String name) {
        this.end_lineno = end_lineno;
        this.lineno = lineno;
        this.name = name;
    }
}
