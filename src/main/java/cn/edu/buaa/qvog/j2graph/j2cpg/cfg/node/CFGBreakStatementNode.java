package cn.edu.buaa.qvog.j2graph.j2cpg.cfg.node;

import cn.edu.buaa.qvog.j2graph.j2cpg.pe.StatementInfo;

public class CFGBreakStatementNode extends CFGJumpStatementNode {

    public CFGBreakStatementNode(final StatementInfo breakStatement) {
        super(breakStatement);
    }
}
