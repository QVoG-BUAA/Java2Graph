package cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.newast;

import java.util.ArrayList;
import java.util.List;

public class FileStart extends ASTNode {
    public String _type = "FileStart";
    public List<FuncStart> funcStartList = new ArrayList<>();
    public String hashCode;
}
