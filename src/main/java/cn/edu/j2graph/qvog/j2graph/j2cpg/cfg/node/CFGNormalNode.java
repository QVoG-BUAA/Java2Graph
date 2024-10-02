package cn.edu.j2graph.qvog.j2graph.j2cpg.cfg.node;

import cn.edu.j2graph.qvog.j2graph.j2cpg.pe.ProgramElementInfo;

public class CFGNormalNode<T extends ProgramElementInfo> extends CFGNode<T> {

    public CFGNormalNode(final T element) {
        super(element);
    }
}
