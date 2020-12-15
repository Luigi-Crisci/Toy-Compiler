package intefaces;

import common.exceptions.MultipleDeclarationException;
import common.exceptions.SemanticException;
import nodes.*;
import nodes.ExpressionNode.*;
import nodes.StatementNode.*;

public interface Visitor {

	public Object visit(ProgramNode item) throws SemanticException;

	public Object visit(VariableDeclarationNode item) throws SemanticException;

	public Object visit(IdInitializerNode item) throws SemanticException;

	public Object visit(ProcedureNode item) throws SemanticException;

	public Object visit(ParameterDeclarationNode item) throws SemanticException;

	public Object visit(ProcedureBodyNode item) throws SemanticException;

	public Object visit(ReadStatement item) throws SemanticException;

	public Object visit(WriteStatement item) throws SemanticException;

	public Object visit(AssignStatement item) throws SemanticException;

	public Object visit(CallProcedureStatement item) throws SemanticException;

	public Object visit(WhileStatement item) throws SemanticException;

	public Object visit(IfStatement item) throws SemanticException;

	public Object visit(ElifStatement item) throws SemanticException;

	public Object visit(BinaryExpression item) throws SemanticException;

	public Object visit(UnaryExpression item) throws SemanticException;

	public Object visit(IntegerConstant item) throws SemanticException;

	public Object visit(FloatConstant item) throws SemanticException;

	public Object visit(StringConstant item) throws SemanticException;

	public Object visit(BooleanConstant item) throws SemanticException;

	public Object visit(NullConstant item) throws SemanticException;

	public Object visit(IdentifierExpression item) throws SemanticException;

	public Object visit(CallProcedureExpression item) throws SemanticException;


}
