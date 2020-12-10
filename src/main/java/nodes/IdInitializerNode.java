package nodes;

import intefaces.Visitable;
import intefaces.Visitor;
import nodes.ExpressionNode.IdentifierExpression;

public class IdInitializerNode implements Visitable {

	public IdentifierExpression id;
	public ExpressionNode expression;

	public IdInitializerNode(IdentifierExpression id) {
		this.id = id;
		expression = null;
	}

	public IdInitializerNode(IdentifierExpression id, ExpressionNode expression) {
		this.id = id;
		this.expression = expression;
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}

}
