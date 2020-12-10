package intefaces;

import nodes.*;
import nodes.ExpressionNode.*;
import nodes.StatementNode.*;

public interface Visitor {

	public Object visit(ProgramNode item);

	public Object visit(VariableDeclarationNode item);

	public Object visit(IdInitializerNode item);

	public Object visit(ProcedureNode item);

	public Object visit(ParameterDeclarationNode item);

	public Object visit(ProcedureBodyNode item);

	public Object visit(ReadStatement item);

	public Object visit(WriteStatement item);

	public Object visit(AssignStatement item);

	public Object visit(CallProcedureStatement item);

	public Object visit(WhileStatement item);

	public Object visit(IfStatement item);

	public Object visit(ElifStatement item);

	public Object visit(BinaryExpression item);

	public Object visit(UnaryExpression item);

	public Object visit(IntegerConstant item);

	public Object visit(FloatConstant item);

	public Object visit(StringConstant item);

	public Object visit(BooleanConstant item);

	public Object visit(NullConstant item);

	public Object visit(IdentifierExpression item);

	public Object visit(CallProcedureExpression item);


}
