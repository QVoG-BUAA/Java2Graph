package cn.edu.buaa.qvog.j2graph.j2cpg.graphviz;

import org.eclipse.jdt.core.dom.CompilationUnit;
import cn.edu.buaa.qvog.j2graph.j2cpg.pe.FldPkgImpInfo;

public class GetFldPkgImp {

    public static FldPkgImpInfo getFldPkgImp(String path) {
        CompilationUnit comp = JdtAstUtil.getCompilationUnit(path);
        ASTVisit visitor = new ASTVisit(comp, new FldPkgImpInfo());
        comp.accept(visitor);
        return visitor.getFldPkgImpInfo();
    }
}
