package cn.edu.j2graph.qvog.j2graph.g2db.database;

public class PDG {
    public String label;
    public String fromCodeLabel;
    public String fromCodeLabelLine;
    public String toCodeLabel;
    public String toCodeLabelLine;
    public String fromCodeAST;
    public String toCodeAST;

    public PDG(String fromCodeLabel, String fromCodeLabelLine, String toCodeLabel, String toCodeLabelLine, String fromCodeAST, String toCodeAST) {
        this.label = "pdg";
        this.fromCodeLabel = fromCodeLabel;
        this.fromCodeLabelLine = fromCodeLabelLine;
        this.toCodeLabel = toCodeLabel;
        this.toCodeLabelLine = toCodeLabelLine;
        this.fromCodeAST = fromCodeAST;
        this.toCodeAST = toCodeAST;
    }

    /*public String label;
    public Code fromCode;
    public Code toCode;
    public PDG(String fromCodeLabel, String fromCodeLabelLine, String toCodeLabel, String toCodeLabelLine, String fromCodeAST, String toCodeAST, List<Code> codeList, List<Method> methodList) {
        this.label = "pdg";
        this.fromCode = getCode(fromCodeLabel,fromCodeLabelLine,fromCodeAST,codeList,methodList);
        this.toCode = getCode(toCodeLabel,toCodeLabelLine,toCodeAST,codeList,methodList);
    }
    public static Code getCode(String codeLabel, String codeLabelLine, String codeAST, List<Code> codeList, List<Method> methodList){
        Code code = new Code("","","","","");
        boolean flag = false;
        for(Code code1 : codeList){
            if(code1.lineno.equals(codeLabelLine)&&code1.codeLabel.equals(codeLabel)){
                code=code1;
                flag = true;
                break;
            }
        }
        if(!flag){
            for(Method method:methodList){
                if(method.methodLineNo.equals(codeLabelLine)&&method.methodCode.equals(codeLabel+"()")){
                    code=new Code(method.methodAST,method.methodLineNo,method.methodName,method.belongFile,method.methodCode);
                    codeList.add(code);
                    break;
                }
            }
        }
        return code;
    }*/
}
