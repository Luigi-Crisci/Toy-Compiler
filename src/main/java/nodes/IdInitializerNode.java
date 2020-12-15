package nodes;

import common.exceptions.SemanticException;
import intefaces.Visitable;
import intefaces.Visitor;
import nodes.ExpressionNode.IdentifierExpression;

public class IdInitializerNode extends TypedNode implements Visitable {

	public IdentifierExpression id;
	public ExpressionNode expression;

	public IdInitializerNode(IdentifierExpression id) {
		super();
		this.id = id;
		expression = null;
	}

	public IdInitializerNode(IdentifierExpression id, ExpressionNode expression) {
		super();
		this.id = id;
		this.expression = expression;
	}

	@Override
	public Object accept(Visitor visitor) throws SemanticException {
		return visitor.visit(this);
	}

}
