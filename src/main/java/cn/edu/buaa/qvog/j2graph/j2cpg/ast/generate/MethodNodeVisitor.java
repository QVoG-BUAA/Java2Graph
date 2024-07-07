package cn.edu.buaa.qvog.j2graph.j2cpg.ast.generate;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;
import java.util.List;


public class MethodNodeVisitor extends ASTVisitor {

    List<MethodDeclaration> methodNodeList = new ArrayList<>();

    public List<MethodDeclaration> getMethodDecs() {
        return methodNodeList;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        methodNodeList.add(node);
        return true;
    }
}
