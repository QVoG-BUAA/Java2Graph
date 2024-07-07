package cn.edu.buaa.qvog.j2graph.j2cpg.cfg.node;

import cn.edu.buaa.qvog.j2graph.j2cpg.cfg.node.CFGPseudoNode.PseudoElement;
import cn.edu.buaa.qvog.j2graph.j2cpg.pe.ProgramElementInfo;

public class CFGPseudoNode extends CFGNode<PseudoElement> {

    public CFGPseudoNode() {
        super(new PseudoElement());
    }

    public static class PseudoElement extends ProgramElementInfo {
        PseudoElement() {
            super(0, 0);
        }
    }
}
