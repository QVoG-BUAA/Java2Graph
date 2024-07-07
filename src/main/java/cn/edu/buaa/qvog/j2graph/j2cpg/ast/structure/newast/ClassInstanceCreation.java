package cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast;

public class ClassInstanceCreation extends Value {
    public String _type = "ClassInstanceCreation";
    public String className;
    public Args args = new Args();
    public int end_lineno;
    public int lineno;
    public String name;
}
