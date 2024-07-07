package cn.edu.buaa.qvog.j2graph.j2cpg.pdg.node;

import cn.edu.buaa.qvog.j2graph.j2cpg.pe.ProgramElementInfo;

public abstract class PDGNormalNode<T extends ProgramElementInfo> extends
        PDGNode<T> {

    protected PDGNormalNode(final T element) {
        super(element);
    }
}
