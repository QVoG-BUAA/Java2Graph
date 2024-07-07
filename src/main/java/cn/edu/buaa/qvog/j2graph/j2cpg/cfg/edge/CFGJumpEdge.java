package cn.edu.buaa.qvog.j2graph.j2cpg.cfg.edge;

import cn.edu.buaa.qvog.j2graph.j2cpg.cfg.node.CFGNode;

public class CFGJumpEdge extends CFGEdge {

    CFGJumpEdge(final CFGNode<?> fromNode, final CFGNode<?> toNode) {
        super(fromNode, toNode);
    }

    @Override
    public String getDependenceString() {
        return "jump";
    }

    @Override
    public String getDependenceTypeString() {
        return "jump";
    }
}
