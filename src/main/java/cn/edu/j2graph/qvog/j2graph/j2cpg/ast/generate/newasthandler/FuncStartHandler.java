package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialMethod;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.FuncStart;

public class FuncStartHandler extends Handler {
    public InitialMethod initialMethod;

    public FuncStartHandler(InitialMethod initialMethod) {
        this.initialMethod = initialMethod;
    }

    public FuncStart build() {
        FuncStart funcStart = new FuncStart();
        funcStart.name = initialMethod.label;
        funcStart.FunctionDef = new MethodDeclarationHandler(initialMethod.initialASTNode).build();
        funcStart.FunctionDef.name = funcStart.name;
        funcStart.lineno = initialMethod.startLine;
        funcStart.end_lineno = initialMethod.endLine;
        funcStart.name = initialMethod.label;
        return funcStart;
    }
}
