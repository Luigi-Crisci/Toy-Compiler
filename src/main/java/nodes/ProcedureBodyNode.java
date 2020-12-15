package nodes;

import intefaces.Visitable;
import intefaces.Visitor;

import java.util.Collections;
import java.util.List;

import common.exceptions.SemanticException;

public class ProcedureBodyNode  extends TypedNode implements Visitable {

	public List<VariableDeclarationNode> varList;
	public List<StatementNode> statementList;
	public List<ExpressionNode> exprList;

	public ProcedureBodyNode(List<VariableDeclarationNode> varList, List<StatementNode> statementList,
			List<ExpressionNode> exprList) {
			super();
		this.varList = varList;
		this.statementList = statementList;
		this.exprList = exprList;
	}

	public ProcedureBodyNode(List<VariableDeclarationNode> varList, List<ExpressionNode> exprList) {
		super();
		this.varList = varList;
		this.statementList = Collections.emptyList();
		this.exprList = exprList;
	}

	@Override
	public Object accept(Visitor visitor) throws SemanticException {
		return visitor.visit(this);
	}

}
