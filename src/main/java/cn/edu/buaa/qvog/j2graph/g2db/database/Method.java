package cn.edu.buaa.qvog.j2graph.g2db.database;


public class Method {
    public String methodName;
    public String methodAST;
    public String methodLineNo;
    public String belongFile;
    public String methodCode;
    public String nodeId;

    public Method(String methodName, String methodAST, String methodLineNo, String belongFile, String methodCode, String nodeId) {
        this.methodName = methodName;
        this.methodAST = methodAST;
        this.methodLineNo = methodLineNo;
        this.belongFile = belongFile;
        this.methodCode = methodCode;
        this.nodeId = nodeId;
    }
}
