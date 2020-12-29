package parser.nodes;

import java.util.Collections;
import java.util.List;

import common.exceptions.SemanticException;
import common.interfaces.Visitable;
import common.interfaces.Visitor;
import parser.nodes.ExpressionNode.IdentifierExpression;

public class ProcedureNode extends TypedNode implements Visitable {

	public IdentifierExpression id;
	public List<ParameterDeclarationNode> paramList;
	public List<Integer> returnTypes;
	public ProcedureBodyNode procBody;

	public ProcedureNode(IdentifierExpression id, List<ParameterDeclarationNode> paramList, List<Integer> returnTypes, ProcedureBodyNode procBody) {
		super();
		this.id = id;
		this.procBody = procBody;
		this.paramList = paramList;
		this.returnTypes = returnTypes;
	}

	public ProcedureNode(IdentifierExpression id, List<Integer> returnTypes, ProcedureBodyNode procBody) {
		super();
		this.id = id;
		this.procBody = procBody;
		this.paramList = Collections.emptyList();
		this.returnTypes = returnTypes;
	}

	@Override
	public Object accept(Visitor visitor) throws SemanticException {
		return visitor.visit(this);
	}

	

}
