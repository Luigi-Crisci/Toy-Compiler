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

	public ToyToCVisitor(String filename) {
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

	//DONE
	public Object visit(ProgramNode item) throws SemanticException {
		for (VariableDeclarationNode v : item.vars)
			v.accept(this);
		for (ProcedureNode n : item.procs)
			n.accept(this);
		return null;
	}

	//DONE
	public Object visit(VariableDeclarationNode item) throws SemanticException {

		writer.print(ToyToCUtils.typeConverter(item.getType()));

		for(int i = 0; i < item.IdInitializerList.size(); i++){
			item.IdInitializerList.get(i).accept(this);
			if(i < item.IdInitializerList.size() - 1)
				addComma();
		}

		addSemicolonAndNewline();
		return null;
	}

	//DONE
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

		if(item.returnTypes.size() )

		item.id.accept(this);
		for (ParameterDeclarationNode parameterDeclarationNode : item.paramList)
			parameterDeclarationNode.accept(this);
		writer.println("<ResultTypes>");
		for (Integer i : item.returnTypes)
			writer.println("<Type>" + Symbols.terminalNames[i] + "</Type>");
		writer.println("</ResultTypes>");
		item.procBody.accept(this);
		writer.println("</ProcedureNode>");
		return null;
	}

	public Object visit(ParameterDeclarationNode item) throws SemanticException {
		writer.println("<ParameterDeclarations>");
		writer.println("<Type>" + Symbols.terminalNames[item.typeList.get(0)] + "</Type>");
		for (IdentifierExpression i : item.idList)
			i.accept(this);
		writer.println("</ParameterDeclarations>");
		return null;
	}

	public Object visit(ProcedureBodyNode item) throws SemanticException {
		writer.println("<ProcedureBody>");
		for (VariableDeclarationNode v : item.varList)
			v.accept(this);
		for (StatementNode s : item.statementList)
			s.accept(this);
		for (ExpressionNode e : item.exprList)
			e.accept(this);
		writer.println("</ProcedureBody>");
		return null;
	}

	//DONE
	public Object visit(ReadStatement item) throws SemanticException {
		writer.print("readln(");
		writer.print(ToyToCUtils.createPlaceholderString(item.idList));
		writer.print(")");
		addSemicolonAndNewline();
		return null;
	}

	//TODO: Function call with multiple returns to handle
	public Object visit(WriteStatement item) throws SemanticException {
		writer.print("write(");

		for (ExpressionNode e : item.expressionList)
			e.accept(this);

		writer.println("</WriteStatement>");

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

}
