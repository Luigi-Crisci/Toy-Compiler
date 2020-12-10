package nodes;

import java.util.List;

import intefaces.Visitable;
import intefaces.Visitor;


public class VariableDeclarationNode implements Visitable {

	public Integer type;
	public List<IdInitializerNode> IdInitializerList;

	public VariableDeclarationNode(Integer type, List<IdInitializerNode> idInitializerList) {
		this.type = type;
		IdInitializerList = idInitializerList;
	}

	public VariableDeclarationNode() {
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
}
