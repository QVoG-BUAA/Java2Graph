package cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.initialast;

import java.util.ArrayList;
import java.util.List;

public class InitialASTNode {
    public String type;
    public String label;
    public int startLine;
    public int endLine;
    public List<InitialASTNode> childNodeList = new ArrayList<>();
}
