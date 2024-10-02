package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.statement;

public class DefaultStatement extends Statement {
    public String _type = "null";
    public int end_lineno;
    public int lineno;
    public String name;

    public DefaultStatement(int end_lineno, int lineno, String name) {
        super(end_lineno, lineno, name);
        this.end_lineno = end_lineno;
        this.lineno = lineno;
        this.name = name;
    }
}
