package cn.edu.buaa.qvog.j2graph.j2cpg.cfg.node;

import cn.edu.buaa.qvog.j2graph.j2cpg.pe.VariableInfo;

public class CFGParameterNode extends CFGNode<VariableInfo> {

    private CFGParameterNode(final VariableInfo variable) {
        super(variable);
    }
}
