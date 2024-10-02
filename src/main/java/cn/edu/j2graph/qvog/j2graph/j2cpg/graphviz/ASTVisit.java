package cn.edu.j2graph.qvog.j2graph.j2cpg.graphviz;

import org.eclipse.jdt.core.dom.*;
import cn.edu.j2graph.qvog.j2graph.j2cpg.pe.ClassFieldInfo;
import cn.edu.j2graph.qvog.j2graph.j2cpg.pe.FieldInfo;
import cn.edu.j2graph.qvog.j2graph.j2cpg.pe.FldPkgImpInfo;

public class ASTVisit extends ASTVisitor {
    public boolean ifLevelFlag = false;
    private final CompilationUnit compilationUnit;
    private final FldPkgImpInfo fldPkgImpInfo;

    public ASTVisit(CompilationUnit compilationUnit, FldPkgImpInfo fldPkgImpInfo) {
        this.compilationUnit = compilationUnit;
        this.fldPkgImpInfo = fldPkgImpInfo;
    }

    public FldPkgImpInfo getFldPkgImpInfo() {
        return this.fldPkgImpInfo;
    }

    @Override
    public boolean visit(FieldDeclaration node) {
        for (Object obj : node.fragments()) {
            VariableDeclarationFragment v = (VariableDeclarationFragment) obj;
            this.fldPkgImpInfo.classFieldInfos.get(this.fldPkgImpInfo.classFieldInfos.size() - 1)
                    .field.add(new FieldInfo(node.getType() + " " + v.toString().replace("=", " = ")
                            .replace("\"", "\\\"") + ";", String.valueOf(compilationUnit.getLineNumber(node.getStartPosition()))));
            //System.out.println("Line:\t"+this.compilationUnit.getLineNumber(node.getStartPosition()));
        }

        return true;
    }

    @Override
    public boolean visit(ImportDeclaration node) {
        this.fldPkgImpInfo.importName.add(node.toString().replace("import ", "").replace("\n", "").replace(";", ""));
        return true;
    }

    @Override
    public boolean visit(PackageDeclaration node) {
        this.fldPkgImpInfo.packageName = String.valueOf(node.getName());
        return true;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        //System.out.println("Class: " + node.getName());
        this.fldPkgImpInfo.classFieldInfos.add(new ClassFieldInfo(String.valueOf(node.getName())));
        //System.out.println("haha:\t" + node.getAST());
        //System.out.println("Line:\t"+this.compilationUnit.getLineNumber(node.getStartPosition()));
        return true;
    }
}
