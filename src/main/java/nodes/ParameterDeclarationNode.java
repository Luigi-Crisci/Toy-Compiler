package nodes;

import java.util.List;

import intefaces.Visitable;
import intefaces.Visitor;

import nodes.ExpressionNode.IdentifierExpression;

public class ParameterDeclarationNode implements Visitable {

	public Integer type;
	public List<IdentifierExpression> idList;

	public ParameterDeclarationNode(Integer type, List<IdentifierExpression> idList) {
		this.type = type;
		this.idList = idList;
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}

}
