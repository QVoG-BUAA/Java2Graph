package cn.edu.buaa.qvog.j2graph.g2db.database;

public class CG {
    public String label;
    public String fromCodeLabel;
    public String fromCodeBelongFile;
    public String toCodeLabel;
    public String fromCodeAST;
    public String toCodeAST;
    public String toCodeBelongFile;
    public String fromCodeLineNo;

    public CG(String fromCodeLabel, String fromCodeBelongFile, String toCodeLabel, String fromCodeAST, String toCodeAST, String toCodeBelongFile, String fromCodeLineNo) {
        this.label = "cg";
        this.fromCodeLabel = fromCodeLabel;
        this.fromCodeBelongFile = fromCodeBelongFile;
        this.toCodeLabel = toCodeLabel;
        this.fromCodeAST = fromCodeAST;
        this.toCodeAST = toCodeAST;
        this.toCodeBelongFile = toCodeBelongFile;
        this.fromCodeLineNo = fromCodeLineNo;
    }

}

