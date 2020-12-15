package nodes;

import java.util.List;

import common.exceptions.SemanticException;
import intefaces.Visitable;
import intefaces.Visitor;

import nodes.ExpressionNode.IdentifierExpression;

public class ParameterDeclarationNode extends TypedNode implements Visitable {

	public List<IdentifierExpression> idList;

	public ParameterDeclarationNode(Integer type, List<IdentifierExpression> idList) {
		super();
		this.type.add(type);
		this.idList = idList;
	}

	@Override
	public Object accept(Visitor visitor) throws SemanticException {
		return visitor.visit(this);
	}

}
