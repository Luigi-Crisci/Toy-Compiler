package visitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

	public ToyToCVisitor(String filename) {
		currentFunctionName = "";
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
		writer.println("#include\"src/main/native/functions.h\"");
		// writer.println("#include\"src/main/native/emsctipten_functions.h\""); //This enable emscripten input
	}

	public void flush() {
		writer.flush();
		writer.close();
	}

	private void addSpace() {
		writer.print(" ");
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

		writer.print(ToyToCUtils.typeConverter(item.getType()));

		for (int i = 0; i < item.IdInitializerList.size(); i++) {
			item.IdInitializerList.get(i).accept(this);
			if (i < item.IdInitializerList.size() - 1)
				addComma();
		}

		addSemicolonAndNewline();
		return null;
	}

	// DONE
	public Object visit(IdInitializerNode item) throws SemanticException {
		item.id.accept(this);
		if (item.expression != null) {
			writer.print("=");
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
		currentFunctionName = item.id.value;
		if (hasStruct)
			writer.print(ToyToCUtils.FUNCTION_STRUCT_PREFIX + currentFunctionName);
		else
			writer.print(ToyToCUtils.typeConverter(item.getType()));

		addSpace();
		writer.print(item.id.value);

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

		// Handle multiple return types
		if (item.exprList.size() > 1) {
			String variableName = ToyToCUtils.getUniqueFunctionVariabletName(currentFunctionName);
			writer.print(ToyToCUtils.FUNCTION_STRUCT_PREFIX + currentFunctionName + " " + variableName);
			addSemicolonAndNewline();
			for (int i = 0; i < item.exprList.size(); i++) {
				writer.print(variableName + ".p_" + i + " = ");
				item.exprList.get(i).accept(this);
				addSemicolonAndNewline();
			}
			writer.print("return " + variableName);
			addSemicolonAndNewline();
		} else if (item.exprList.size() == 1) {
			writer.print("return ");
			item.exprList.get(0).accept(this);
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
	 * TODO: Add support to multiline string 
	 */
	public Object visit(WriteStatement item) throws SemanticException {
		String[] variableNames = writeFunctionStruct(item.expressionList);

		writer.print("printf");
		openRoundBracket();

		writer.print("\"");
		for (ExpressionNode currentExpressionNode : item.expressionList) {
			if (currentExpressionNode.typeList.size() > 1) {
				for (Integer type : currentExpressionNode.typeList)
					writer.print(ToyToCUtils.getPlaceholder(type));
			} else
				writer.print(ToyToCUtils.getPlaceholder(currentExpressionNode.getType()));
		}
		writer.print("\"");
		addComma();

		// Handle variable list
		int count = 0;
		for (int i = 0; i < item.expressionList.size(); i++) {
			ExpressionNode currentExpressionNode = item.expressionList.get(i);
			if (currentExpressionNode.typeList.size() > 1) {
				CallProcedureExpression callProcedureExpression = (CallProcedureExpression) currentExpressionNode;
				String structName = variableNames[count++]; // Get the name from list

				for (int j = 0; j < callProcedureExpression.typeList.size(); j++) {
					writer.print(structName + ".p_" + j);
					if (j < callProcedureExpression.typeList.size() - 1)
						addComma();
				}
			} else
				currentExpressionNode.accept(this);
			if (i < item.expressionList.size() - 1)
				addComma();
		}

		closeRoundBracket();
		addSemicolonAndNewline();
		return null;
	}

	public Object visit(AssignStatement item) throws SemanticException {
		String[] variableNames = writeFunctionStruct(item.expressionList);

		int i = 0, currentName = 0;
		for (ExpressionNode e : item.expressionList) {
			if (e.typeList.size() > 1) {
				for (int j = 0; j < e.typeList.size(); j++) {
					item.idList.get(i).accept(this);
					addAssign();
					writer.print(variableNames[currentName] + ".p_" + j);
					addSemicolonAndNewline();
					i++;
				}
				currentName++;
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
		String[] variableNames = writeFunctionStruct(item.expressionList);

		writer.print(item.id.value);
		openRoundBracket();
		int count = 0;
		for (int i = 0; i < item.expressionList.size(); i++) {
			ExpressionNode expressionNode = item.expressionList.get(i);
			if (expressionNode.typeList.size() > 1) {
				CallProcedureExpression callProcedureExpression = (CallProcedureExpression) expressionNode;
				String structName = variableNames[count++]; // Get the name from list

				for (int j = 0; j < callProcedureExpression.typeList.size(); j++) {
					writer.print(structName + ".p_" + j);
					if (j < callProcedureExpression.typeList.size() - 1)
						addComma();
				}
			} else
				expressionNode.accept(this);
			if (i < item.expressionList.size() - 1)
				addComma();
		}

		closeRoundBracket();
		addSemicolonAndNewline();
		return null;
	}

	public Object visit(CallProcedureExpression item) throws SemanticException {
		String[] variableNames = writeFunctionStruct(item.expressionList);

		writer.print(item.id.value);
		openRoundBracket();
		int count = 0;
		for (int i = 0; i < item.expressionList.size(); i++) {
			ExpressionNode expressionNode = item.expressionList.get(i);
			if (expressionNode.typeList.size() > 1) {
				CallProcedureExpression callProcedureExpression = (CallProcedureExpression) expressionNode;
				String structName = variableNames[count++]; // Get the name from list

				for (int j = 0; j < callProcedureExpression.typeList.size(); j++) {
					writer.print(structName + ".p_" + j);
					if (j < callProcedureExpression.typeList.size() - 1)
						addComma();
				}
			} else
				expressionNode.accept(this);
			if (i < item.expressionList.size() - 1)
				addComma();
		}

		closeRoundBracket();
		return null;
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
		writer.print("\"" + item.value + "\"");
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
		writer.print(item.value);
		return null;
	}

	public String[] writeFunctionStruct(List<ExpressionNode> expressionList) throws SemanticException {
		ArrayList<String> structNames = new ArrayList<>();
		for (ExpressionNode e : expressionList)
			if (e.typeList.size() > 1) {
				CallProcedureExpression callProcedureExpression = (CallProcedureExpression) e;
				String variableName = ToyToCUtils.getUniqueFunctionVariabletName(callProcedureExpression.id.value);
				writer.print(ToyToCUtils.getFunctionStructName(callProcedureExpression.id.value) + " " + variableName
						+ " = ");
				structNames.add(variableName);
				e.accept(this);
				addSemicolonAndNewline();
			}
		return (String[]) structNames.toArray(new String[0]);
	}

	private void writeFunctionStructDefinition(ProcedureNode item) {
		String functionStructName = ToyToCUtils.FUNCTION_STRUCT_PREFIX + item.id.value;
		writer.print("typedef struct " + functionStructName + "{\n");
		for (int i = 0; i < item.returnTypes.size(); i++) {
			writer.println(ToyToCUtils.typeConverter(item.returnTypes.get(i)) + " p_" + i + ";");
		}
		writer.print("}" + functionStructName);
		addSemicolonAndNewline();
	}

}
