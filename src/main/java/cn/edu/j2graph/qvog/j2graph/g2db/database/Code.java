package cn.edu.j2graph.qvog.j2graph.g2db.database;

public class Code {
    public String label;
    public String json;
    public String lineno;
    public String functionDefName;
    public String belongFile;
    public String codeLabel;
    public boolean inserted = false;
    public String nodeId;

    public Code(String json, String lineno, String functionDefName, String belongFile, String codeLabel, String nodeId) {
        this.label = "Code";
        this.json = json;
        this.lineno = lineno;
        this.functionDefName = functionDefName;
        this.belongFile = belongFile;
        this.codeLabel = codeLabel;
        this.nodeId = nodeId;
    }

}
