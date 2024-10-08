package cn.edu.j2graph.qvog.j2graph.j2cpg.pdg.edge;

import cn.edu.j2graph.qvog.j2graph.j2cpg.pdg.node.PDGNode;

public class PDGExecutionDependenceEdge extends PDGEdge {

    public PDGExecutionDependenceEdge(final PDGNode<?> fromNode,
                                      final PDGNode<?> toNode) {
        super(PDGEdge.TYPE.EXECUTION, fromNode, toNode);
    }

    @Override
    public PDGEdge replaceFromNode(final PDGNode<?> fromNode) {
        assert null != fromNode : "\"fromNode\" is null.";
        return new PDGExecutionDependenceEdge(fromNode, this.toNode);
    }

    @Override
    public PDGEdge replaceToNode(final PDGNode<?> toNode) {
        assert null != fromNode : "\"toNode\" is null.";
        return new PDGExecutionDependenceEdge(this.fromNode, toNode);
    }

    @Override
    public String getDependenceString() {
        return "";
    }
}
