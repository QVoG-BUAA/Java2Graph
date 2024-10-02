package cn.edu.j2graph.qvog.j2graph.j2cpg.cfg.edge;

import cn.edu.j2graph.qvog.j2graph.j2cpg.cfg.node.CFGNode;

public class CFGNormalEdge extends CFGEdge {

    CFGNormalEdge(CFGNode<?> fromNode, final CFGNode<?> toNode) {
        super(fromNode, toNode);
    }

    @Override
    public String getDependenceString() {
        return "";
    }

    @Override
    public String getDependenceTypeString() {
        return "normal";
    }
}
