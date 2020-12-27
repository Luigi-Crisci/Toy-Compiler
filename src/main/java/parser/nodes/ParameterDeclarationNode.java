package parser.nodes;

import java.util.List;

import common.exceptions.SemanticException;
import common.interfaces.Visitable;
import common.interfaces.Visitor;

import parser.nodes.ExpressionNode.IdentifierExpression;

public class ParameterDeclarationNode extends TypedNode implements Visitable {

	public List<IdentifierExpression> idList;

	public ParameterDeclarationNode(Integer type, List<IdentifierExpression> idList) {
		super();
		this.typeList.add(type);
		this.idList = idList;
	}

	@Override
	public Object accept(Visitor visitor) throws SemanticException {
		return visitor.visit(this);
	}

}
