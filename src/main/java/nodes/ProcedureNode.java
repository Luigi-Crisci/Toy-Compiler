package nodes;

import java.util.Collections;
import java.util.List;

import intefaces.Visitable;
import intefaces.Visitor;
import nodes.ExpressionNode.IdentifierExpression;

public class ProcedureNode implements Visitable {

	public IdentifierExpression id;
	public List<ParameterDeclarationNode> paramList;
	public List<Integer> returnTypes;
	public ProcedureBodyNode procBody;

	public ProcedureNode(IdentifierExpression id, List<ParameterDeclarationNode> paramList, List<Integer> returnTypes, ProcedureBodyNode procBody) {
		this.id = id;
		this.procBody = procBody;
		this.paramList = paramList;
		this.returnTypes = returnTypes;
	}

	public ProcedureNode(IdentifierExpression id, List<Integer> returnTypes, ProcedureBodyNode procBody) {
		this.id = id;
		this.procBody = procBody;
		this.paramList = Collections.emptyList();
		this.returnTypes = returnTypes;
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}

	

}
