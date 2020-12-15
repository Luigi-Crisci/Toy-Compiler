package visitor;

import java.util.List;
import java.util.stream.Collectors;

import common.*;
import common.exceptions.*;
import intefaces.Visitor;
import nodes.ExpressionNode.*;
import nodes.*;
import nodes.StatementNode.*;
import parser.Symbols;

public class SemanticVisitor implements Visitor {
	
	private SymbolTableStack stack;
	
	@Override
	public Object visit(ProgramNode item) throws SemanticException {
		stack.enterScope();
		return null;
	}
	
	@Override
	public Object visit(ProcedureNode item) throws SemanticException {
		stack.enterScope();
		return null;
	}

	@Override
	public Object visit(VariableDeclarationNode item) throws SemanticException {
		
		//TODO: Collapse IdInitializerNode into VariableDeclarationNode
		for (IdInitializerNode id : item.IdInitializerList){
			id.typeList = item.typeList;
			id.accept(this);
		}

		return null;
	}

	@Override
	public Object visit(IdInitializerNode item) throws SemanticException {
		if ( stack.probe(item.id.value) )
			throw new MultipleDeclarationException();
		
		stack.addId(new Symbol(item.id.value, SymbolTypes.VAR, item.typeList.get(0)));
		
		if (item.expression != null){
			item.expression.accept(this);
			
			if(item.expression.typeList.size() > 1)
				throw new InvalidAssignmentException("Too many values returned from expression");

			if( item.expression.typeList.get(0) == Symbols.BOOL && item.id.typeList.get(0) != Symbols.BOOL)
				throw new TypeMismatch("Invalid assignment of BOOLEAN to" + item.id.typeList.get(0) );

		}

		return null;
	}


	@Override
	public Object visit(ParameterDeclarationNode item) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ProcedureBodyNode item) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ReadStatement item) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(WriteStatement item) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(AssignStatement item) throws SemanticException {
		
		for (IdentifierExpression identifierExpression : item.idList)
			identifierExpression.accept(this);
		for (ExpressionNode e: item.expressionList)
			e.accept(this);
		
		List<Integer> expressionTypes = item.expressionList.stream()
											.map(expression->expression.typeList)
											.flatMap(list->list.stream())
											.collect(Collectors.toList());
		
		if (expressionTypes.size() != item.idList.size())
			throw new InvalidAssignmentException("Id list size is different from expression list size");
		
		for(int i = 0; i < expressionTypes.size(); i++)
			if( TypeCheck.checkType(Symbols.ASSIGN, item.idList.get(i).typeList.get(0),expressionTypes.get(i)) == -1)
				throw new TypeMismatch("Trying to assign " + item.idList.get(i).typeList.get(0) + " to " + expressionTypes.get(i));
	
		return null;
	}

	@Override
	public Object visit(CallProcedureStatement item) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(WhileStatement item) throws SemanticException {
		
		for (StatementNode statement : item.conditionStatementList)
			statement.accept(this);
		for (StatementNode statement : item.conditionStatementList)
			statement.accept(this);
		item.conditionExpression.accept(this);
		
		if ( item.conditionExpression.typeList.size() != 1 && item.conditionExpression.typeList.get(0) != Symbols.BOOL )
			throw new InvalidConditionException("Condition error: type is " + item.conditionExpression.typeList.get(0)+ ", expected BOOLEAN");
		return null;
	}

	@Override
	public Object visit(IfStatement item) throws SemanticException {
		
		item.conditionExpression.accept(this);
		
		if(item.conditionExpression.typeList.get(0) == Symbols.BOOL )
			throw new InvalidConditionException("Condition error: type is " + item.conditionExpression.typeList.get(0)+ ", expected BOOLEAN");

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

		if(item.expression.typeList.get(0) == Symbols.BOOL )
			throw new InvalidConditionException("Condition error: type is " + Symbols.terminalNames[item.expression.typeList.get(0)]+ ", expected BOOLEAN");

		for (StatementNode elif : item.elifBodyStatementList)
			elif.accept(this);
		
		return null;
	}

	@Override
	public Object visit(BinaryExpression item) throws SemanticException {
		
		item.leftExpression.accept(this);
		item.rightExpression.accept(this);

		if(item.leftExpression.typeList.size() > 1 || item.rightExpression.typeList.size() > 1)
			throw new InvalidExpressionException("Cannot apply " + Symbols.terminalNames[item.operation] + " to more than one element");

		item.typeList.add(TypeCheck.checkType(item.operation, item.leftExpression.typeList.get(0) , item.rightExpression.typeList.get(0)));
		if ( item.typeList.get(0) == -1 )
			throw new InvalidExpressionException("Operation " + Symbols.terminalNames[item.operation] + 
												" not applicable to " + Symbols.terminalNames[item.rightExpression.typeList.get(0)] + 
												" and " + Symbols.terminalNames[item.leftExpression.typeList.get(0)]);
		
		return null;
	}

	@Override
	public Object visit(UnaryExpression item) throws SemanticException {
		
		item.rightExpression.accept(this);

		if(item.rightExpression.typeList.size() > 1)
			throw new InvalidExpressionException("Cannot apply " + Symbols.terminalNames[item.operation] + " to more than one element");
		
		item.typeList.add(TypeCheck.checkType(item.operation, item.rightExpression.typeList.get(0), null));
		if ( item.typeList.get(0) == -1 )
			throw new InvalidExpressionException("Operation " + Symbols.terminalNames[item.operation] + " not applicable to " + Symbols.terminalNames[item.rightExpression.typeList.get(0)]);

		return null;
	}

	@Override
	public Object visit(IntegerConstant item) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(FloatConstant item) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StringConstant item) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(BooleanConstant item) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NullConstant item) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(IdentifierExpression item) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(CallProcedureExpression item) throws SemanticException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
