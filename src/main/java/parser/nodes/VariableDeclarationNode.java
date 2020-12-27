package parser.nodes;

import java.util.List;

import common.exceptions.SemanticException;
import common.interfaces.Visitable;
import common.interfaces.Visitor;


public class VariableDeclarationNode extends TypedNode implements Visitable {

	public List<IdInitializerNode> IdInitializerList;

	public VariableDeclarationNode(Integer type, List<IdInitializerNode> idInitializerList) {
		super();
		this.typeList.add(type);
		IdInitializerList = idInitializerList;
	}

	public VariableDeclarationNode() {
		super();
	}

	@Override
	public Object accept(Visitor visitor) throws SemanticException {
		return visitor.visit(this);
	}
}
