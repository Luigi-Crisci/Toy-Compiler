package nodes;

import java.util.List;

import common.exceptions.SemanticException;
import intefaces.Visitable;
import intefaces.Visitor;


public class VariableDeclarationNode extends TypedNode implements Visitable {

	public List<IdInitializerNode> IdInitializerList;

	public VariableDeclarationNode(Integer type, List<IdInitializerNode> idInitializerList) {
		super();
		this.type.add(type);
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
