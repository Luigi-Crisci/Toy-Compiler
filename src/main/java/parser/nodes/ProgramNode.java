package parser.nodes;

import java.util.List;

import common.exceptions.SemanticException;
import common.interfaces.Visitable;
import common.interfaces.Visitor;


public class ProgramNode  extends TypedNode implements Visitable {

	public List<VariableDeclarationNode> vars;
	public List<ProcedureNode> procs;
	
	public ProgramNode(List<VariableDeclarationNode> vdl, List<ProcedureNode> pl){
		super();
		vars = vdl;
		procs = pl;
	}

	@Override
	public Object accept(Visitor visitor) throws SemanticException {
		return visitor.visit(this);
	}
	
}
