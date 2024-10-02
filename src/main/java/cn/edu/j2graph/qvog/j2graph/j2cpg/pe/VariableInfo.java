package cn.edu.j2graph.qvog.j2graph.j2cpg.pe;

public class VariableInfo extends ProgramElementInfo {

    final public TypeInfo type;
    final public String name;
    private CATEGORY category;

    public VariableInfo(final CATEGORY category, final TypeInfo type,
                        final String name, final int startLine, final int endLine) {
        super(startLine, endLine);
        this.category = category;
        this.type = type;
        this.name = name;
    }

    public CATEGORY getCategory() {
        return this.category;
    }

    public void setCategory(final CATEGORY category) {
        assert null != category : "\"category\" is null.";
        this.category = category;
    }

    public enum CATEGORY {
        FIELD, LOCAL, PARAMETER
    }
}
