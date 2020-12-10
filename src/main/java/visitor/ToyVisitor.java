package visitor;

import java.io.IOException;
import java.io.PrintWriter;

import intefaces.Visitor;
import nodes.ExpressionNode;
import nodes.IdInitializerNode;
import nodes.ParameterDeclarationNode;
import nodes.ProcedureBodyNode;
import nodes.ProcedureNode;
import nodes.ProgramNode;
import nodes.StatementNode;
import nodes.VariableDeclarationNode;
import parser.Symbols;
import nodes.ExpressionNode.*;
import nodes.StatementNode.*;

public class ToyVisitor implements Visitor {

	PrintWriter writer;

	public ToyVisitor(String filename) {
		try {
			writer = new PrintWriter(filename + ".xml");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void flush() {
		writer.flush();
		writer.close();
	}

	public Object visit(ProgramNode item) {
		writer.println("<ProgramNode>");
		for (VariableDeclarationNode v : item.vars)
			v.accept(this);
		for (ProcedureNode n : item.procs)
			n.accept(this);
		writer.println("</ProgramNode>");
		return null;
	}

	public Object visit(VariableDeclarationNode item) {
		writer.println("<VariableDeclaration>");
		writer.println("<Type>" + Symbols.terminalNames[item.type] + "</Type>");

		for (IdInitializerNode node : item.IdInitializerList)
			node.accept(this);

		writer.println("</VariableDeclaration>");

		return null;
	}

	public Object visit(IdInitializerNode item) {
		writer.println("<IdInitialization>");
		item.id.accept(this);
		if (item.expression != null)
			item.expression.accept(this);
		writer.println("</IdInitialization>");
		return null;
	}

	public Object visit(ProcedureNode item) {
		writer.println("<ProcedureNode>");
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

	public Object visit(ParameterDeclarationNode item) {
		writer.println("<ParameterDeclarations>");
		writer.println("<Type>" + Symbols.terminalNames[item.type] + "</Type>");
		for (IdentifierExpression i : item.idList)
			i.accept(this);
		writer.println("</ParameterDeclarations>");
		return null;
	}

	public Object visit(ProcedureBodyNode item) {
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

	public Object visit(ReadStatement item) {
		writer.println("<ReadStatement>");

		for (IdentifierExpression e : item.idList)
			e.accept(this);

		writer.println("</ReadStatement>");
		return null;
	}

	public Object visit(WriteStatement item) {
		writer.print("<WriteStatement>");

		for (ExpressionNode e : item.expressionList)
			e.accept(this);

		writer.println("</WriteStatement>");

		return null;
	}

	public Object visit(AssignStatement item) {
		writer.println("<AssignStatement>");

		for (IdentifierExpression i : item.idList)
			i.accept(this);
		for (ExpressionNode e : item.expressionList)
			e.accept(this);

		writer.println("</AssignStatement>");

		return null;
	}

	public Object visit(CallProcedureStatement item) {
		writer.println("<CallProcedureStatement>");

		for (ExpressionNode e : item.expressionList)
			e.accept(this);

		writer.println("</CallProcedureStatement>");

		return null;
	}

	public Object visit(WhileStatement item) {
		writer.println("<WhileStatement>");

		for (StatementNode st : item.conditionStatementList)
			st.accept(this);
		item.conditionExpression.accept(this);
		for (StatementNode e : item.bodyStatementList)
			e.accept(this);

		writer.println("</WhileStatement>");

		return null;
	}

	public Object visit(IfStatement item) {
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

	public Object visit(ElifStatement item) {

		writer.println("<ElifStatement>");
		item.expression.accept(this);
		for (StatementNode elif : item.elifBodyStatementList)
			elif.accept(this);

		writer.println("</ElifStatement>");

		return null;

	}

	public Object visit(BinaryExpression item) {
		writer.println("<BinaryExpression>");
		item.leftExpression.accept(this);
		writer.println("<Operation> " + Symbols.terminalNames[item.operation] + "</Operation>");
		item.rightExpresion.accept(this);
		writer.println("</BinaryExpression>");
		return null;
	}

	public Object visit(UnaryExpression item) {
		writer.println("<UnaryExpression>");
		writer.println("<Operation> " + Symbols.terminalNames[item.operation] + "</Operation>");
		item.rightExpression.accept(this);
		writer.println("</UnaryExpression>");
		return null;
	}

	public Object visit(IntegerConstant item) {
		writer.println("<IntegerConstant> " + item.value + "</IntegerConstant>");
		return null;
	}

	public Object visit(FloatConstant item) {
		writer.println("<FloatConstant> " + item.value + "</FloatConstant>");
		return null;
	}

	public Object visit(StringConstant item) {
		writer.println("<StringConstant> " + item.value + "</StringConstant>");
		return null;
	}

	public Object visit(BooleanConstant item) {
		writer.println("<BooleanConstant> " + item.value + "</BooleanConstant>");
		return null;
	}

	public Object visit(NullConstant item) {
		writer.println("<NullConstant> null </NullConstant>");
		return null;
	}

	public Object visit(IdentifierExpression item) {
		writer.println("<IdentifierExpression>");

		writer.println("<xleft> " + item.xleft + "</xleft>");
		writer.println("<value> " + item.value + "</value>");
		writer.println("<xright> " + item.xright + "</xright>");

		writer.println("</IdentifierExpression>");
		return null;
	}

	public Object visit(CallProcedureExpression item) {
		writer.println("<CallProcedureExpression>");
		item.id.accept(this);
		for (ExpressionNode e : item.expressionList) {
			e.accept(this);
		}
		writer.println("</CallProcedureExpression>");
		return null;
	}
}
