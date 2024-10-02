package cn.edu.j2graph.qvog.j2graph.j2cpg.ast.generate.newasthandler;

import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialAST;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.initialast.InitialMethod;
import cn.edu.j2graph.qvog.j2graph.j2cpg.ast.structure.newast.FileStart;

public class FileStartHandler extends Handler {
    public InitialAST initialAST;

    public FileStartHandler(InitialAST initialAST) {
        this.initialAST = initialAST;
    }

    public FileStart build() {
        FileStart fileStart = new FileStart();
        for (InitialMethod initialMethod : initialAST.initialMethodList) {
            fileStart.funcStartList.add(new FuncStartHandler(initialMethod).build());
        }
        return fileStart;
    }
}
