package cn.edu.j2graph.qvog.j2graph.j2cpg.pe;

import java.util.*;

public class StatementInfo extends ProgramElementInfo implements BlockInfo {

    final private List<ProgramElementInfo> initializers;
    final private List<ProgramElementInfo> updaters;
    final private List<StatementInfo> statements;
    final private List<StatementInfo> catchStatements;
    private ProgramElementInfo ownerBlock;
    private CATEGORY category;
    private final List<ProgramElementInfo> expressions;
    private ProgramElementInfo condition;
    private final List<StatementInfo> elseStatements;
    private StatementInfo finallyStatement;

    private String label;

    public StatementInfo(final ProgramElementInfo ownerBlock,
                         final CATEGORY category, final int startLine, final int endLine) {

        super(startLine, endLine);

        this.ownerBlock = ownerBlock;
        this.category = category;
        this.expressions = new ArrayList<ProgramElementInfo>();

        this.initializers = new ArrayList<ProgramElementInfo>();
        this.condition = null;
        this.updaters = new ArrayList<ProgramElementInfo>();

        this.statements = new ArrayList<StatementInfo>();
        this.elseStatements = new ArrayList<StatementInfo>();
        this.catchStatements = new ArrayList<StatementInfo>();
        this.finallyStatement = null;

        this.label = null;
    }

    public ProgramElementInfo getOwnerBlock() {
        return this.ownerBlock;
    }

    public void setOwnerBlock(final ProgramElementInfo ownerBlock) {
        assert null != "\"ownerBlock\" is null.";
        this.ownerBlock = ownerBlock;
    }

    public void addInitializer(final ProgramElementInfo initializer) {
        assert null != initializer : "\"initializer\" is null.";
        this.initializers.add(initializer);
    }

    public void addUpdater(final ProgramElementInfo updater) {
        assert null != updater : "\"updater\" is null.";
        this.updaters.add(updater);
    }

    public List<ProgramElementInfo> getInitializers() {
        final List<ProgramElementInfo> initializers = new ArrayList<ProgramElementInfo>();
        initializers.addAll(this.initializers);
        return initializers;
    }

    public ProgramElementInfo getCondition() {
        return this.condition;
    }

    public void setCondition(final ProgramElementInfo condition) {
        assert null != condition : "\"condition\" is null.";
        this.condition = condition;
    }

    public List<ProgramElementInfo> getUpdaters() {
        final List<ProgramElementInfo> updaters = new ArrayList<ProgramElementInfo>();
        updaters.addAll(this.updaters);
        return updaters;
    }

    @Override
    public void setStatement(final StatementInfo statement) {
        assert null != statement : "\"statement\" is null.";
        this.statements.clear();
        if (CATEGORY.SimpleBlock == statement.getCategory()) {
            if (statement.getStatements().isEmpty()) {
                this.statements.add(statement);
            } else {
                this.statements.addAll(statement.getStatements());
            }
        } else {
            this.statements.add(statement);
        }
    }

    public CATEGORY getCategory() {
        return this.category;
    }

    public void setCategory(final CATEGORY category) {
        assert null != "\"category\" is null.";
        this.category = category;
    }

    @Override
    public void addStatement(final StatementInfo statement) {
        assert null != statement : "\"statement\" is null.";
        this.statements.add(statement);
    }

    @Override
    public void addStatements(final Collection<StatementInfo> statements) {
        assert null != statements : "\"statements\" is null.";
        this.statements.addAll(statements);
    }

    @Override
    public List<StatementInfo> getStatements() {
        return Collections.unmodifiableList(this.statements);
    }

    public void setElseStatement(final StatementInfo elseBody) {
        assert null != elseBody : "\"elseStatement\" is null.";
        this.elseStatements.clear();
        if (CATEGORY.SimpleBlock == elseBody.getCategory()) {
            this.elseStatements.addAll(elseBody.getStatements());
        } else {
            this.elseStatements.add(elseBody);
        }
    }

    public List<StatementInfo> getElseStatements() {
        return Collections.unmodifiableList(this.elseStatements);
    }

    public void addCatchStatement(final StatementInfo catchStatement) {
        assert null != catchStatement : "\"catchStatement\" is null.";
        this.catchStatements.add(catchStatement);
    }

    public List<StatementInfo> getCatchStatements() {
        return Collections.unmodifiableList(this.catchStatements);
    }

    public StatementInfo getFinallyStatement() {
        return this.finallyStatement;
    }

    public void setFinallyStatement(final StatementInfo finallyStatement) {
        assert null != finallyStatement : "\"finallyStatement\" is null.";
        this.finallyStatement = finallyStatement;
    }

    public void addExpression(final ProgramElementInfo element) {
        assert null != element : "\"element\" is null.";
        this.expressions.add(element);
    }

    public List<ProgramElementInfo> getExpressions() {
        final List<ProgramElementInfo> list = new ArrayList<ProgramElementInfo>();
        list.addAll(this.expressions);
        return list;
    }

    @Override
    public SortedSet<String> getAssignedVariables() {

        final SortedSet<String> variables = new TreeSet<String>();

        for (final ProgramElementInfo expression : this.expressions) {
            variables.addAll(expression.getAssignedVariables());
        }

        for (final ProgramElementInfo initializer : this.initializers) {
            variables.addAll(initializer.getAssignedVariables());
        }

        if (null != this.condition) {
            variables.addAll(this.condition.getAssignedVariables());
        }

        for (final ProgramElementInfo updater : this.updaters) {
            variables.addAll(updater.getAssignedVariables());
        }

        for (final StatementInfo statement : this.statements) {
            variables.addAll(statement.getAssignedVariables());
        }

        for (final StatementInfo statement : this.elseStatements) {
            variables.addAll(statement.getAssignedVariables());
        }

        for (final StatementInfo catchStatement : this.catchStatements) {
            variables.addAll(catchStatement.getAssignedVariables());
        }

        if (null != this.finallyStatement) {
            variables.addAll(this.finallyStatement.getAssignedVariables());
        }

        return variables;
    }

    @Override
    public SortedSet<String> getReferencedVariables() {

        final SortedSet<String> variables = new TreeSet<String>();

        for (final ProgramElementInfo expression : this.expressions) {
            variables.addAll(expression.getReferencedVariables());
        }

        for (final ProgramElementInfo initializer : this.initializers) {
            variables.addAll(initializer.getReferencedVariables());
        }

        if (null != this.condition) {
            variables.addAll(this.condition.getReferencedVariables());
        }

        for (final ProgramElementInfo updater : this.updaters) {
            variables.addAll(updater.getReferencedVariables());
        }

        for (final StatementInfo statement : this.statements) {
            variables.addAll(statement.getReferencedVariables());
        }

        for (final StatementInfo statement : this.elseStatements) {
            variables.addAll(statement.getReferencedVariables());
        }

        for (final StatementInfo catchStatement : this.catchStatements) {
            variables.addAll(catchStatement.getReferencedVariables());
        }

        if (null != this.finallyStatement) {
            variables.addAll(this.finallyStatement.getReferencedVariables());
        }

        return variables;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public String getJumpToLabel() {
        if (0 == this.expressions.size()) {
            return null;
        } else {
            return this.expressions.get(0).getText();
        }
    }

    public enum CATEGORY {

        Assert("ASSERT"), Break("BREAK"), Case("CASE"), Catch("CATCH"), Continue(
                "CONTINUE"), Do("DO"), Empty("Empty"), Expression("EXPRESSION"), If(
                "IF"), For("FOR"), Foreach("FOREACH"), Return("RETURN"), SimpleBlock(
                "SimpleBlock"), Synchronized("SYNCHRONIZED"), Switch("SWITCH"), Throw(
                "SWITCH"), Try("TRY"), TypeDeclaration("TYPEDECLARATION"), VariableDeclaration(
                "VARIABLEDECLARATION"), While("WHILE");

        final public String id;

        CATEGORY(final String id) {
            this.id = id;
        }
    }
}
