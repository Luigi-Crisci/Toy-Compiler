package visitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import common.*;
import common.exceptions.*;
import common.interfaces.Visitor;
import parser.nodes.ExpressionNode.*;
import parser.nodes.*;
import parser.nodes.StatementNode.*;

public class ToyToCVisitor implements Visitor {

	PrintWriter writer;
	String currentFunctionName;
	Deque<String> functionStructNames;

	public ToyToCVisitor(String filename) {
		currentFunctionName = "";
		functionStructNames = new ArrayDeque<>();
		try {
			writer = new PrintWriter(filename + ".c");
			writeCommonInclude();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	private void writeCommonInclude() {
		writer.println("#include<stdlib.h>");
		writer.println("#include<stdio.h>");
		writer.println("#include<string.h>");
		writer.println("#include<toy_functions.h>");
		// writer.println("#include\"src/main/native/emsctipten_functions.h\""); //This enable emscripten input
	}

	public void flush() {
		writer.flush();
		writer.close();
	}

	private void addSpace() {
		writer.print(" ");
	}

	private void addPrefix(){
		writer.print(ToyToCUtils.IDENTIFIER_PREFIX);
	}

	private void openRoundBracket() {
		writer.print("(");
	}

	private void closeRoundBracket() {
		writer.print(")");
	}

	private void openCurlyBracket() {
		writer.print("{");
	}

	private void closeCurlyBracket() {
		writer.print("}");
	}

	private void addNewline() {
		writer.print("\n");
	}

	private void addAssign() {
		writer.print("=");
	}

	private void addSemicolonAndNewline() {
		writer.print(";\n");
	}

	private void addComma() {
		writer.print(", ");
	}

	// DONE
	public Object visit(ProgramNode item) throws SemanticException {
		for (VariableDeclarationNode v : item.vars)
			v.accept(this);
		for (ProcedureNode n : item.procs)
			n.accept(this);
		return null;
	}

	// DONE
	public Object visit(VariableDeclarationNode item) throws SemanticException {
		
		for (int i = 0; i < item.IdInitializerList.size(); i++) {
			writer.print(ToyToCUtils.typeConverter(item.getType()));
			item.IdInitializerList.get(i).accept(this);
			addSemicolonAndNewline();
		}

		return null;
	}

	// DONE
	public Object visit(IdInitializerNode item) throws SemanticException {
		item.id.accept(this);
		if (item.expression != null) {
			addAssign();
			item.expression.accept(this);
		}
		return null;
	}

	public Object visit(ProcedureNode item) throws SemanticException {
		boolean hasStruct = false;
		if (item.returnTypes.size() > 1) {
			writeFunctionStructDefinition(item);
			hasStruct = true;
		}
		currentFunctionName = ToyToCUtils.FUNCTION_STRUCT_PREFIX + ToyToCUtils.IDENTIFIER_PREFIX + item.id.value;
		if (hasStruct)
			writer.print(currentFunctionName);
		else
			writer.print(ToyToCUtils.typeConverter(item.getType()));

		addSpace();
		item.id.accept(this);

		// Handle parameters
		openRoundBracket();
		for (int i = 0; i < item.paramList.size(); i++) {
			item.paramList.get(i).accept(this);
			if (i < item.paramList.size() - 1)
				addComma();
		}
		closeRoundBracket();

		// handle body
		openCurlyBracket();
		item.procBody.accept(this);
		closeCurlyBracket();
		return null;
	}

	public Object visit(ParameterDeclarationNode item) throws SemanticException {
		for (int i = 0; i < item.idList.size(); i++) {
			writer.print(ToyToCUtils.typeConverter(item.getType()));
			item.idList.get(i).accept(this);
			if (i < item.idList.size() - 1)
				addComma();
		}
		return null;
	}

	public Object visit(ProcedureBodyNode item) throws SemanticException {
		for (VariableDeclarationNode v : item.varList)
			v.accept(this);
		for (StatementNode s : item.statementList)
			s.accept(this);

		handleFunctionCalls(item.exprList);
		List<ExpressionNode> returnList = item.exprList;

		// Handle multiple return types
		int current_index = 0;
		if (returnList.size() > 1 || returnList.size() == 1 && Utils.isFunctionWithMultipleReturns(returnList.get(0)) ) {
			String variableName = ToyToCUtils.getUniqueFunctionVariabletName(currentFunctionName);
			writer.print(currentFunctionName + " " + variableName);
			addSemicolonAndNewline();
			//Initialize the funciton struct fields
			for (int i = 0; i < returnList.size(); i++) { 
				//If one of the return expressions is a function call with multiple return values, the return struct values must be 
				//copied into the current function return struct 
				if(Utils.isFunctionWithMultipleReturns(returnList.get(i))){
					String structName = functionStructNames.pop();
					for (int j = 0; j < returnList.get(i).size(); j++) {
						writer.print(variableName + ".p_" + current_index++ + " = " + structName + ".p_" + j);
						addSemicolonAndNewline();
					}
				}
				else{
					//The expression generate one value and can be printed as it is
					writer.print(variableName + ".p_" + current_index++ + " = ");
					returnList.get(i).accept(this);
					addSemicolonAndNewline();
				}
			}
			writer.print("return " + variableName);
			addSemicolonAndNewline();
		} else if (returnList.size() == 1) {
			writer.print("return ");
			returnList.get(0).accept(this);
			addSemicolonAndNewline();
		}
		return null;
	}
	
	
	public Object visit(ReadStatement item) throws SemanticException {
		for(IdentifierExpression id : item.idList){
			id.accept(this);
			addAssign();
			writer.print(ToyToCUtils.getInputString(id.getType()));
			addSemicolonAndNewline();
		}
		return null;
	}

	/**
	 * The writeStatement conversion does not maintain the original sequence of operations in the .toy source file:
	 * this is possibile because the Toy language doesn't allow to modify an element inside of an expression, so the order
	 * in which they are executed doesn't matter
	 * For example: it's possible to execute functions originally placed inside the write statement before calling it
	 */
	public Object visit(WriteStatement item) throws SemanticException {
		handleFunctionCalls(item.expressionList);

		writer.print("printf");
		openRoundBracket();

		writer.print("\"");
		for (ExpressionNode currentExpressionNode : item.expressionList) {
			if (currentExpressionNode.size() > 1) {
				for (Integer type : currentExpressionNode.typeList)
					writer.print(ToyToCUtils.getPlaceholder(type));
			} else
				writer.print(ToyToCUtils.getPlaceholder(currentExpressionNode.getType()));
		}
		writer.print("\"");
		addComma();

		// Handle variable list
		for (int i = 0; i < item.expressionList.size(); i++) {
			ExpressionNode currentExpressionNode = item.expressionList.get(i);
			if (currentExpressionNode.size() > 1) {
				CallProcedureExpression callProcedureExpression = (CallProcedureExpression) currentExpressionNode;
				// String structName = variableNames[count++]; // Get the name from list
				String structName = functionStructNames.pop(); // Get the name from list

				for (int j = 0; j < callProcedureExpression.size(); j++) {
					writer.print(structName + ".p_" + j);
					if (j < callProcedureExpression.size() - 1)
						addComma();
				}
			} 
			else{
				currentExpressionNode.accept(this);
				//Convert c bool to "true" or "false	
				if(currentExpressionNode.getType() == Symbols.BOOL)
					writer.print("== 1 ? \"true\" : \"false\"");
			}
			if (i < item.expressionList.size() - 1)
				addComma();
		}

		closeRoundBracket();
		addSemicolonAndNewline();
		return null;
	}

	public Object visit(AssignStatement item) throws SemanticException {
		handleFunctionCalls(item.expressionList);

		int i = 0;
		for (ExpressionNode e : item.expressionList) {
			if (e.size() > 1) {
				String variableName = functionStructNames.pop();
				for (int j = 0; j < e.size(); j++) {
					item.idList.get(i).accept(this);
					addAssign();
					writer.print(variableName + ".p_" + j);
					addSemicolonAndNewline();
					i++;
				}
			} else {
				item.idList.get(i).accept(this);
				addAssign();
				e.accept(this);
				i++;
				addSemicolonAndNewline();
			}
		}

		return null;
	}

	public Object visit(WhileStatement item) throws SemanticException {
		for (StatementNode st : item.conditionStatementList)
			st.accept(this);

		writer.print("while");
		openRoundBracket();
		item.conditionExpression.accept(this);
		closeRoundBracket();
		openCurlyBracket();
		addNewline();

		for (StatementNode e : item.bodyStatementList)
			e.accept(this);
		// Print conditional statements again
		for (StatementNode st : item.conditionStatementList)
			st.accept(this);

		closeCurlyBracket();
		addNewline();

		return null;
	}

	public Object visit(IfStatement item) throws SemanticException {
		writer.print("if");
		
		openRoundBracket();
		item.conditionExpression.accept(this);
		closeRoundBracket();
		openCurlyBracket();
		addNewline();

		for (StatementNode e : item.ifBodyStatatementList)
			e.accept(this);
		closeCurlyBracket();
		addNewline();

		for (ElifStatement elif : item.elifStatementList)
			elif.accept(this);

		if (item.elseStatementList.size() > 0) {
			writer.print("else");
			openCurlyBracket();
			for (StatementNode st : item.elseStatementList)
				st.accept(this);
			closeCurlyBracket();
			addNewline();
		}
		return null;
	}

	public Object visit(ElifStatement item) throws SemanticException {
		writer.print("else if");
		openRoundBracket();
		item.expression.accept(this);
		closeRoundBracket();

		openCurlyBracket();
		addNewline();

		for (StatementNode st : item.elifBodyStatementList)
			st.accept(this);

		closeCurlyBracket();
		addNewline();

		return null;
	}

	public Object visit(CallProcedureStatement item) throws SemanticException {
		handleFunctionCalls(item.expressionList);

		
		String variableName = null;
		if(Utils.isFunctionWithMultipleReturns(item)){
			variableName = ToyToCUtils.getUniqueFunctionVariabletName(item.id.value);
			writer.print(ToyToCUtils.getFunctionStructName(item.id.value) + " " + variableName + " = ");
		}
				
		item.id.accept(this);

		openRoundBracket();
		for (int i = 0; i < item.expressionList.size(); i++) {
			ExpressionNode expressionNode = item.expressionList.get(i);
				if (expressionNode.size() > 1) {
					CallProcedureExpression callProcedureExpression = (CallProcedureExpression) expressionNode;
					String structName = functionStructNames.pop(); // Get the name from list
	
					for (int j = 0; j < callProcedureExpression.size(); j++) {
						writer.print(structName + ".p_" + j);
						if (j < callProcedureExpression.size() - 1)
							addComma();
					}
				}
			 	else
					expressionNode.accept(this);
			if (i < item.expressionList.size() - 1)
				addComma();
		}

		closeRoundBracket();
		addSemicolonAndNewline();
		return variableName;
	}

	public Object visit(CallProcedureExpression item) throws SemanticException {
		String variableName = null;
		if(Utils.isFunctionWithMultipleReturns(item)){
			variableName = ToyToCUtils.getUniqueFunctionVariabletName(item.id.value);
			writer.print(ToyToCUtils.getFunctionStructName(item.id.value) + " " + variableName + " = ");
		}
				
		item.id.accept(this);

		openRoundBracket();
		for (int i = 0; i < item.expressionList.size(); i++) {
			ExpressionNode expressionNode = item.expressionList.get(i);
				if (expressionNode.size() > 1) {
					CallProcedureExpression callProcedureExpression = (CallProcedureExpression) expressionNode;
					String structName = functionStructNames.pop(); // Get the name from list
	
					for (int j = 0; j < callProcedureExpression.size(); j++) {
						writer.print(structName + ".p_" + j);
						if (j < callProcedureExpression.size() - 1)
							addComma();
					}
				}
			 	else
					expressionNode.accept(this);
			if (i < item.expressionList.size() - 1)
				addComma();
		}

		closeRoundBracket();
		return variableName;
	}

	public Object visit(BinaryExpression item) throws SemanticException {
		item.leftExpression.accept(this);
		writer.print(ToyToCUtils.opConverter(item.operation));
		item.rightExpression.accept(this);
		return null;
	}

	public Object visit(UnaryExpression item) throws SemanticException {
		writer.print(ToyToCUtils.opConverter(item.operation));
		item.rightExpression.accept(this);
		return null;
	}

	public Object visit(IntegerConstant item) throws SemanticException {
		writer.print(item.value);
		return null;
	}

	public Object visit(FloatConstant item) throws SemanticException {
		writer.print(item.value);
		return null;
	}

	public Object visit(StringConstant item) throws SemanticException {
		writer.print("\"" + item.value.replace("\n", "\\n") + "\"");
		return null;
	}

	public Object visit(BooleanConstant item) throws SemanticException {
		writer.print(item.value == true ? 1 : 0);
		return null;
	}

	public Object visit(NullConstant item) throws SemanticException {
		writer.print("NULL");
		return null;
	}

	public Object visit(IdentifierExpression item) throws SemanticException {
		if(item.value.compareToIgnoreCase(Symbols.terminalNames[Symbols.MAIN]) != 0)
			addPrefix();
		writer.print(item.value);
		return null;
	}

	/**
	 * Utily to call WriteFunctionStruct, reverting the stack before to return
	 * @param expressionNodes
	 * @throws SemanticException
	 */
	public void handleFunctionCalls(List<ExpressionNode> expressionNodes) throws SemanticException{
		clearStack();
		writeFunctionStruct(expressionNodes);
		reverseStack();
	}

	/**
	 * Recursively explore an expressionList looking for call procedures with multiple returns and make a bottom-up visit
	 * to each callProcedureNode found, initializing a stack with the struct variable names for each call procedure
	 * This function handle the case where function calls are nested
	 * @param expressionList
	 * @return the generated names for all function calls
	 * @return the functionStructNames stack is also initialized with the names of the struct variable used to capture the returns of the found functions, with the 
	 * last one
	 * @throws SemanticException
	 */
	public void writeFunctionStruct(List<ExpressionNode> expressionList) throws SemanticException {
		for (ExpressionNode e : expressionList){
			if (e instanceof CallProcedureExpression){
				CallProcedureExpression callProcedureExpression = (CallProcedureExpression) e;
				writeFunctionStruct(callProcedureExpression.expressionList);
				if (e.size() > 1 ) {
					functionStructNames.push((String)callProcedureExpression.accept(this));
					addSemicolonAndNewline();
				}
			}
		}
	}

	private void writeFunctionStructDefinition(ProcedureNode item) {
		String functionStructName = ToyToCUtils.FUNCTION_STRUCT_PREFIX + ToyToCUtils.IDENTIFIER_PREFIX  + item.id.value;
		writer.print("typedef struct " + functionStructName + "{\n");
		for (int i = 0; i < item.returnTypes.size(); i++) {
			writer.println(ToyToCUtils.typeConverter(item.returnTypes.get(i)) + " p_" + i + ";");
		}
		writer.print("}" + functionStructName);
		addSemicolonAndNewline();
	}

	private void reverseStack(){
		Deque<String> reverseStack = new ArrayDeque<>();
		for(String s : functionStructNames)
			reverseStack.push(s);
		functionStructNames = reverseStack;
	}

	private void clearStack(){
		functionStructNames = new ArrayDeque<>();
	}

}
