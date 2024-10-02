package cn.edu.j2graph.qvog.j2graph.j2cpg.cfg.edge;

import cn.edu.j2graph.qvog.j2graph.j2cpg.cfg.node.CFGNode;

public class CFGControlEdge extends CFGEdge {

    final public boolean control;

    CFGControlEdge(CFGNode<?> fromNode, final CFGNode<?> toNode,
                   final boolean control) {
        super(fromNode, toNode);
        this.control = control;
    }

    @Override
    public String getDependenceString() {
        return Boolean.toString(this.control);
    }

    @Override
    public String getDependenceTypeString() {
        return "control";
    }
}
