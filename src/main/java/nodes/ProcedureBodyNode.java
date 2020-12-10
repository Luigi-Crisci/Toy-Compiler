package nodes;

import intefaces.Visitable;
import intefaces.Visitor;


import java.util.Collections;
import java.util.List;

public class ProcedureBodyNode implements Visitable {

	public List<VariableDeclarationNode> varList;
	public List<StatementNode> statementList;
	public List<ExpressionNode> exprList;

	public ProcedureBodyNode(List<VariableDeclarationNode> varList, List<StatementNode> statementList,
			List<ExpressionNode> exprList) {
		this.varList = varList;
		this.statementList = statementList;
		this.exprList = exprList;
	}

	public ProcedureBodyNode(List<VariableDeclarationNode> varList, List<ExpressionNode> exprList) {
		this.varList = varList;
		this.statementList = Collections.emptyList();
		this.exprList = exprList;
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}

}
