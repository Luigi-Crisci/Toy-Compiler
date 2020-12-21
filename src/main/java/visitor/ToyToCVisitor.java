package visitor;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import common.*;
import common.exceptions.*;
import intefaces.Visitor;
import nodes.ExpressionNode.*;
import nodes.*;
import nodes.StatementNode.*;
import parser.Symbols;
import common.ToyToCUtils.*;

public class ToyToCVisitor implements Visitor {

	PrintWriter writer;
	String currentFunctionName;

	public ToyToCVisitor(String filename) {
		currentFunctionName = "";
		try {
			writer = new PrintWriter(filename + ".c");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
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

	private void addSemicolon() {
		writer.print(";");
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
		writer.print(item.id.value);
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

	// DONE
	public Object visit(ReadStatement item) throws SemanticException {
		writer.print("scanf");
		openRoundBracket();

		writer.print(ToyToCUtils.createPlaceholderString(item.idList));
		addComma();
		for (int i = 0; i < item.idList.size(); i++) {
			item.idList.get(i).accept(this);
			if (i < item.idList.size() - 1)
				addComma();
		}

		closeRoundBracket();
		addSemicolonAndNewline();
		return null;
	}

	// TODO: add support to variableNames
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
		for (int i = 0; i < item.expressionList.size(); i++) {
			ExpressionNode currentExpressionNode = item.expressionList.get(i);
			if (currentExpressionNode.typeList.size() > 1) {
				CallProcedureExpression callProcedureExpression = (CallProcedureExpression) currentExpressionNode;
				String structName = ToyToCUtils.FUNCTION_STRUCT_VARIABLE_PREFIX + callProcedureExpression.id.value;

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
		return null;
	}

	public Object visit(AssignStatement item) throws SemanticException {
		writer.println("<AssignStatement>");

		for (IdentifierExpression i : item.idList)
			i.accept(this);
		for (ExpressionNode e : item.expressionList)
			e.accept(this);

		writer.println("</AssignStatement>");

		return null;
	}

	public Object visit(CallProcedureStatement item) throws SemanticException {
		writer.println("<CallProcedureStatement>");

		for (ExpressionNode e : item.expressionList)
			e.accept(this);

		writer.println("</CallProcedureStatement>");

		return null;
	}

	public Object visit(WhileStatement item) throws SemanticException {
		writer.println("<WhileStatement>");

		for (StatementNode st : item.conditionStatementList)
			st.accept(this);
		item.conditionExpression.accept(this);
		for (StatementNode e : item.bodyStatementList)
			e.accept(this);

		writer.println("</WhileStatement>");

		return null;
	}

	public Object visit(IfStatement item) throws SemanticException {
		writer.println("<IfStatement>");

		item.conditionExpression.accept(this);
		for (StatementNode e : item.ifBodyStatatementList)
			e.accept(this);
		for (ElifStatement elif : item.elifStatementList)
			elif.accept(this);
		for (StatementNode st : item.elseStatementList)
			st.accept(this);

		writer.println("</IfStatement>");

		return null;
	}

	public Object visit(ElifStatement item) throws SemanticException {

		writer.println("<ElifStatement>");
		item.expression.accept(this);
		for (StatementNode elif : item.elifBodyStatementList)
			elif.accept(this);

		writer.println("</ElifStatement>");

		return null;

	}

	public Object visit(BinaryExpression item) throws SemanticException {
		writer.println("<BinaryExpression>");
		item.leftExpression.accept(this);
		writer.println("<Operation> " + Symbols.terminalNames[item.operation] + "</Operation>");
		item.rightExpression.accept(this);
		writer.println("</BinaryExpression>");
		return null;
	}

	public Object visit(UnaryExpression item) throws SemanticException {
		writer.println("<UnaryExpression>");
		writer.println("<Operation> " + Symbols.terminalNames[item.operation] + "</Operation>");
		item.rightExpression.accept(this);
		writer.println("</UnaryExpression>");
		return null;
	}

	public Object visit(IntegerConstant item) throws SemanticException {
		writer.println("<IntegerConstant> " + item.value + "</IntegerConstant>");
		return null;
	}

	public Object visit(FloatConstant item) throws SemanticException {
		writer.println("<FloatConstant> " + item.value + "</FloatConstant>");
		return null;
	}

	public Object visit(StringConstant item) throws SemanticException {
		writer.println("<StringConstant> " + item.value + "</StringConstant>");
		return null;
	}

	public Object visit(BooleanConstant item) throws SemanticException {
		writer.println("<BooleanConstant> " + item.value + "</BooleanConstant>");
		return null;
	}

	public Object visit(NullConstant item) throws SemanticException {
		writer.println("<NullConstant> null </NullConstant>");
		return null;
	}

	public Object visit(IdentifierExpression item) throws SemanticException {
		writer.println("<IdentifierExpression>");

		writer.println("<xleft> " + item.xleft + "</xleft>");
		writer.println("<value> " + item.value + "</value>");
		writer.println("<xright> " + item.xright + "</xright>");

		writer.println("</IdentifierExpression>");
		return null;
	}

	public Object visit(CallProcedureExpression item) throws SemanticException {
		writer.println("<CallProcedureExpression>");
		item.id.accept(this);
		for (ExpressionNode e : item.expressionList) {
			e.accept(this);
		}
		writer.println("</CallProcedureExpression>");
		return null;
	}

	// TODO: Now the name are generated randomly, and writeFunctionStruct must
	// return the generated names
	// TODO: Now the CallProcedurExpression only print his name so this method must
	// write also the type
	public String[] writeFunctionStruct(List<ExpressionNode> expressionList) throws SemanticException {
		for (ExpressionNode e : expressionList)
			if (e.typeList.size() > 1)
				e.accept(this);
		return null;
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
