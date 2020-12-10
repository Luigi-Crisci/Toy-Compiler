package nodes;

import java.util.List;

import intefaces.Visitable;
import intefaces.Visitor;


public class ProgramNode implements Visitable {

	public List<VariableDeclarationNode> vars;
	public List<ProcedureNode> procs;
	
	public ProgramNode(List<VariableDeclarationNode> vdl, List<ProcedureNode> pl){
		vars = vdl;
		procs = pl;
	}

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}
	
}
