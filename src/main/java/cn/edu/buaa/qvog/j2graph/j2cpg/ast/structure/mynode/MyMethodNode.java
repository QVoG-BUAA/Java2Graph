package cn.edu.buaa.qvog.j2graph.j2cpg.ast.structure.mynode;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class MyMethodNode {

    public MethodDeclaration methodNode = null;
    public List<MyASTNode> nodeList = null;

    public List<String[]> mapping = null;

    public TypeDeclaration classNode = null;

    public int startLine;

    public int endLine;

    public MyMethodNode() {
        this.classNode = null;
        this.methodNode = null;
        this.nodeList = new ArrayList<MyASTNode>();
        this.mapping = new ArrayList<String[]>();
    }

}
