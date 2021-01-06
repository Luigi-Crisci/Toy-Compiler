package visitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import common.*;
import common.exceptions.*;
import common.interfaces.Visitor;
import parser.nodes.ExpressionNode.*;
import parser.nodes.*;
import parser.nodes.StatementNode.*;

public class SemanticVisitor implements Visitor {

	private SymbolTableStack stack;

	public SemanticVisitor() {
		stack = new SymbolTableStack();
	}

	@Override
	public Object visit(ProgramNode item) throws SemanticException {
		stack.enterScope();
		for (VariableDeclarationNode vd : item.vars)
			vd.accept(this);
		for (ProcedureNode p : item.procs)
			p.accept(this);
		return null;
	}

	@Override
	public Object visit(VariableDeclarationNode item) throws SemanticException {

		for (int i = 0; i < item.IdInitializerList.size(); i++) {
			IdInitializerNode id = item.IdInitializerList.get(i);
			id.typeList = item.typeList;
			id.accept(this);
		}

		return null;
	}

	@Override
	public Object visit(IdInitializerNode item) throws SemanticException {
		if (stack.probe(item.id.value) || stack.lookup(item.id.value, SymbolTypes.METHOD) != null)
			throw new MultipleDeclarationException("Symbol " + item.id.value + " has been previously declared",item.id.left);

		if (item.expression != null) {
			item.expression.accept(this);

			if (Utils.hasMultipleReturns(item.expression))
				throw new InvalidAssignmentException("Too many values returned from expression",item.expression.left);

			if (TypeCheck.checkType(Symbols.ASSIGN, item.getType(), item.expression.getType()) == -1)
				throw new TypeMismatch("Invalid assignment of " + Symbols.terminalNames[item.expression.getType()]
						+ " to " + Symbols.terminalNames[item.getType()],item.id.left);

		}
		stack.addId(new Symbol(item.id.value, SymbolTypes.VAR, item.getType()));

		return null;
	}

	@Override
	public Object visit(ProcedureNode item) throws SemanticException {
		Symbol functionSymbol;
		if ((functionSymbol = stack.lookup(item.id.value)) != null)
			throw new MultipleDeclarationException("Procedure " + item.id.value + " has been already declared",item.id.left.getLine());

		item.typeList = item.returnTypes;
		if(item.size() > 1 && item.containsType(Symbols.VOID))
			throw new InvalidReturnTypeException("Cannot have VOID with multiple return types",item.id.left.getLine());
			
		List<Integer> paramTypeList = item.paramList.stream()
				.map(p -> p.idList.stream().map(id -> p.getType()).collect(Collectors.toList()))
				.flatMap(list -> list.stream()).collect(Collectors.toList());
		functionSymbol = new Symbol(item.id.value, paramTypeList, item.returnTypes);
		stack.addId(functionSymbol);
			
		stack.enterScope();
		for (ParameterDeclarationNode e : item.paramList)
			e.accept(this);
		
		item.procBody.accept(this);
		if (item.procBody.size() != item.returnTypes.size())
			throw new MisnumberedArgumentsException(
					"Attempting to make the procedure " + item.id.value + " return " + item.procBody.size()
							+ " arguments." + " Expecting " + item.returnTypes.size() + " elements to be returned",item.id.left.getLine());
		for (int i = 0; i < item.returnTypes.size(); i++)
			if (TypeCheck.checkType(Symbols.CORP, item.procBody.getType(i), item.returnTypes.get(i)) == -1)
				throw new TypeMismatch("Error under " + item.id.value + " on returned element number " + (i + 1)
						+ ": type " + Symbols.terminalNames[item.procBody.getType(i)] + " incompatible with "
						+ Symbols.terminalNames[item.returnTypes.get(i)],item.procBody.exprList.get(i).left);

		stack.exitScope();
		return null;
	}

	@Override
	public Object visit(ParameterDeclarationNode item) throws SemanticException {
		for (IdentifierExpression id : item.idList){
			if (stack.probe(id.value))
				throw new MultipleDeclarationException("Redefinition of parameter " + id.value,id.left);
			id.addType(item.getType());
			stack.addId(new Symbol(id.value, SymbolTypes.VAR,id.getType()));
		}

		return null;
	}

	@Override
	public Object visit(ProcedureBodyNode item) throws SemanticException {
		for (VariableDeclarationNode vd : item.varList)
			vd.accept(this);
		for (StatementNode st : item.statementList)
			st.accept(this);

		List<Integer> returnTypes = new LinkedList<>();
		for (ExpressionNode e : item.exprList) {
			e.accept(this);
			returnTypes.addAll(e.typeList);
		}
		if (returnTypes.size() == 0)
			item.addType(Symbols.VOID);
		else
			item.typeList = returnTypes;

		return null;
	}

	@Override
	public Object visit(ReadStatement item) throws SemanticException {
		for (IdentifierExpression id : item.idList)
			id.accept(this);

		return null;
	}

	@Override
	public Object visit(WriteStatement item) throws SemanticException {
		for (ExpressionNode e : item.expressionList)
			e.accept(this);

		return null;
	}

	@Override
	public Object visit(AssignStatement item) throws SemanticException {

		for (IdentifierExpression identifierExpression : item.idList)
			identifierExpression.accept(this);
		for (ExpressionNode e : item.expressionList)
			e.accept(this);

		List<Integer> expressionTypes = item.expressionList.stream().map(expression -> expression.typeList)
				.flatMap(list -> list.stream()).collect(Collectors.toList());

		if (expressionTypes.size() != item.idList.size())
			throw new InvalidAssignmentException(
					"Attempting to assign " + item.idList.size() + " arguments to " + expressionTypes.size(),item.idList.get(0).left);

		for (int i = 0; i < expressionTypes.size(); i++)
			if (TypeCheck.checkType(Symbols.ASSIGN, item.idList.get(i).getType(), expressionTypes.get(i)) == -1)
				throw new TypeMismatch("Trying to assign " + Symbols.terminalNames[item.idList.get(i).getType()]
						+ " to " + Symbols.terminalNames[expressionTypes.get(i)],item.idList.get(i).left);

		return null;
	}

	@Override
	public Object visit(WhileStatement item) throws SemanticException {

		for (StatementNode statement : item.conditionStatementList)
			statement.accept(this);
		item.conditionExpression.accept(this);
		for (StatementNode statement : item.bodyStatementList)
			statement.accept(this);

		if (item.conditionExpression.size() != 1 || item.conditionExpression.getType() != Symbols.BOOL)
			throw new InvalidConditionException("Condition error: type is "
					+ Symbols.terminalNames[item.conditionExpression.getType()] + ", expected BOOLEAN", item.conditionExpression.left);
		return null;
	}

	@Override
	public Object visit(IfStatement item) throws SemanticException {

		item.conditionExpression.accept(this);

		if (item.conditionExpression.getType() != Symbols.BOOL)
			throw new InvalidConditionException("Condition error: type is "
					+ Symbols.terminalNames[item.conditionExpression.getType()] + ", expected BOOLEAN",item.conditionExpression.left);

		for (StatementNode e : item.ifBodyStatatementList)
			e.accept(this);
		for (ElifStatement elif : item.elifStatementList)
			elif.accept(this);
		for (StatementNode st : item.elseStatementList)
			st.accept(this);

		return null;
	}

	@Override
	public Object visit(ElifStatement item) throws SemanticException {

		item.expression.accept(this);

		if (item.expression.getType() != Symbols.BOOL)
			throw new InvalidConditionException("Condition error: type is "
					+ Symbols.terminalNames[item.expression.getType()] + ", expected BOOLEAN",item.expression.left);

		for (StatementNode elif : item.elifBodyStatementList)
			elif.accept(this);

		return null;
	}

	@Override
	public Object visit(BinaryExpression item) throws SemanticException {

		item.leftExpression.accept(this);
		item.rightExpression.accept(this);

		if (item.leftExpression.size() > 1 || item.rightExpression.size() > 1)
			throw new InvalidExpressionException(
					"Cannot apply " + Symbols.terminalNames[item.operation] + " to more than one element",item.left);

		Integer type = TypeCheck.checkType(item.operation, item.leftExpression.getType(), item.rightExpression.getType());
		if (type == -1)
			throw new TypeMismatch("Cannot use the operation " + Symbols.terminalNames[item.operation] + " on "
					+ Symbols.terminalNames[item.leftExpression.getType()] + " and "
					+ Symbols.terminalNames[item.rightExpression.getType()],item.left);
		item.addType(type);

		return null;
	}

	@Override
	public Object visit(UnaryExpression item) throws SemanticException {

		item.rightExpression.accept(this);

		if (item.rightExpression.size() > 1)
			throw new InvalidExpressionException(
					"Cannot apply " + Symbols.terminalNames[item.operation] + " to more than one element",item.left);

		Integer type = TypeCheck.checkType(item.operation, item.rightExpression.getType());
		if (type == -1)
			throw new TypeMismatch("Cannot apply the unary operator " + Symbols.terminalNames[item.operation] + " to "
					+ Symbols.terminalNames[item.rightExpression.getType()],item.left);
		item.addType(type);

		return null;
	}

	@Override
	public Object visit(IntegerConstant item) throws SemanticException {
		item.addType(Symbols.INT);
		return null;
	}

	@Override
	public Object visit(FloatConstant item) throws SemanticException {
		item.addType(Symbols.FLOAT);
		return null;
	}

	@Override
	public Object visit(StringConstant item) throws SemanticException {
		item.addType(Symbols.STRING);
		return null;
	}

	@Override
	public Object visit(BooleanConstant item) throws SemanticException {
		item.addType(Symbols.BOOL);
		return null;
	}

	@Override
	public Object visit(NullConstant item) throws SemanticException {
		item.addType(Symbols.NULL);
		return null;
	}

	@Override
	public Object visit(IdentifierExpression item) throws SemanticException {
		Symbol s;
		if ((s = stack.lookup(item.value,SymbolTypes.VAR)) == null )
			throw new IdNotFoundException("Identifier " + item.value + " not declared",item.left);

		item.typeList = s.typeList;

		return null;
	}

	@Override
	public Object visit(CallProcedureStatement item) throws SemanticException {
		Symbol rootFunction;
		if ((rootFunction = stack.lookup(item.id.value,SymbolTypes.METHOD)) == null)
			throw new IdNotFoundException("Function " + item.id.value + " not found",item.id.left.getLine());

		List<Integer> callTypes = new LinkedList<>();
		for (ExpressionNode e : item.expressionList) {
			e.accept(this);
			callTypes.addAll(e.typeList);
		}

		ArrayList<List<Integer>> parameterAndReturnTypes = rootFunction.getParamAndReturnTypes();
		List<Integer> functionParameterTypes = parameterAndReturnTypes.get(0);
		List<Integer> functionReturnTypes = parameterAndReturnTypes.get(1);

		if (callTypes.size() != functionParameterTypes.size())
			throw new MisnumberedArgumentsException("Attempting to call " + item.id.value + " using " + callTypes.size()
					+ " arguments. Expecting " + functionParameterTypes.size(),item.id.left.getLine());

		for (int i = 0; i < callTypes.size(); i++)
			if (TypeCheck.checkType(Symbols.CORP, callTypes.get(i), functionParameterTypes.get(i)) == -1)
				throw new TypeMismatch("Incompatible type " + Symbols.terminalNames[callTypes.get(i)] + ": expected "
						+ Symbols.terminalNames[functionParameterTypes.get(i)],item.expressionList.get(i).left);

		item.typeList = functionReturnTypes;

		return null;
	}

	@Override
	public Object visit(CallProcedureExpression item) throws SemanticException {
		Symbol rootFunction;
		if ((rootFunction = stack.lookup(item.id.value,SymbolTypes.METHOD)) == null)
			throw new IdNotFoundException("Function " + item.id.value + " not found",item.id.left);

		List<Integer> callTypes = new LinkedList<>();
		for (ExpressionNode e : item.expressionList) {
			e.accept(this);
			callTypes.addAll(e.typeList);
		}

		ArrayList<List<Integer>> parameterAndReturnTypes = rootFunction.getParamAndReturnTypes();
		List<Integer> functionParameterTypes = parameterAndReturnTypes.get(0);
		List<Integer> functionReturnTypes = parameterAndReturnTypes.get(1);

		if (callTypes.size() != functionParameterTypes.size())
			throw new MisnumberedArgumentsException("Attempting to call " + item.id.value + " using " + callTypes.size()
					+ " arguments. Expecting " + functionParameterTypes.size(),item.id.left);

		for (int i = 0; i < callTypes.size(); i++)
			if (TypeCheck.checkType(Symbols.CORP, callTypes.get(i), functionParameterTypes.get(i)) == -1)
				throw new TypeMismatch("Incompatible type " + Symbols.terminalNames[callTypes.get(i)] + ": expected "
						+ Symbols.terminalNames[functionParameterTypes.get(i)],item.expressionList.get(i).left);

		item.typeList = functionReturnTypes;

		return null;
	}

}
