package cn.edu.j2graph.qvog.j2graph.j2cpg.cfg.node;

import cn.edu.j2graph.qvog.j2graph.j2cpg.pe.StatementInfo;

abstract public class CFGJumpStatementNode extends CFGStatementNode {

    CFGJumpStatementNode(final StatementInfo jumpStatement) {
        super(jumpStatement);
    }
}
