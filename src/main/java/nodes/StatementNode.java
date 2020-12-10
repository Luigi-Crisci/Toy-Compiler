package nodes;

import java.util.Collections;
import java.util.List;

import intefaces.Visitable;
import intefaces.Visitor;

import nodes.ExpressionNode.IdentifierExpression;

public abstract class StatementNode implements Visitable {

	public static class ReadStatement extends StatementNode {

		public List<IdentifierExpression> idList;

		public ReadStatement(List<IdentifierExpression> idList) {
			this.idList = idList;
		}

		@Override
		public Object accept(Visitor visitor) {
			return visitor.visit(this);
		}
	}

	public static class WriteStatement extends StatementNode {

		public List<ExpressionNode> expressionList;

		public WriteStatement(List<ExpressionNode> expressionList) {
			this.expressionList = expressionList;
		}

		@Override
		public Object accept(Visitor visitor) {
			return visitor.visit(this);
		}
	}

	public static class AssignStatement extends StatementNode {

		public List<IdentifierExpression> idList;
		public List<ExpressionNode> expressionList;

		public AssignStatement(List<IdentifierExpression> idList, List<ExpressionNode> expressionList) {
			this.idList = idList;
			this.expressionList = expressionList;
		}

		@Override
		public Object accept(Visitor visitor) {
			return visitor.visit(this);
		}
	}

	public static class CallProcedureStatement extends StatementNode {

		public IdentifierExpression id;
		public List<ExpressionNode> expressionList;

		public CallProcedureStatement(IdentifierExpression id, List<ExpressionNode> expressionList) {
			this.id = id;
			this.expressionList = expressionList;
		}

		public CallProcedureStatement(IdentifierExpression id) {
			this.id = id;
			this.expressionList = Collections.emptyList();
		}

		@Override
		public Object accept(Visitor visitor) {
		
			return visitor.visit(this);

		}
	}

	public static class WhileStatement extends StatementNode {

		public List<StatementNode> conditionStatementList;
		public ExpressionNode conditionExpression;
		public List<StatementNode> bodyStatementList;

		public WhileStatement(List<StatementNode> conditionStatementList, ExpressionNode conditionExpression,
				List<StatementNode> bodyStatementList) {
			this.conditionStatementList = conditionStatementList;
			this.conditionExpression = conditionExpression;
			this.bodyStatementList = bodyStatementList;
		}

		public WhileStatement(ExpressionNode conditionExpression, List<StatementNode> bodyStatementList) {
			this.conditionStatementList = Collections.emptyList();
			this.conditionExpression = conditionExpression;
			this.bodyStatementList = bodyStatementList;
		}

		@Override
		public Object accept(Visitor visitor) {
			return visitor.visit(this);

		}
	}

	public static class IfStatement extends StatementNode {

		public ExpressionNode conditionExpression;
		public List<ElifStatement> elifStatementList;
		public List<StatementNode> ifBodyStatatementList;
		public List<StatementNode> elseStatementList;

		public IfStatement(ExpressionNode conditionExpression, List<StatementNode> ifBodyStatatementList,
				List<ElifStatement> elifStatementList, List<StatementNode> elseStatementList) {
			this.conditionExpression = conditionExpression;
			this.elifStatementList = elifStatementList;
			this.ifBodyStatatementList = ifBodyStatatementList;
			this.elseStatementList = elseStatementList;
		}

		@Override
		public Object accept(Visitor visitor) {
			return visitor.visit(this);

		}

	}

	public static class ElifStatement extends StatementNode {

		public ExpressionNode expression;
		public List<StatementNode> elifBodyStatementList;

		public ElifStatement(ExpressionNode expression, List<StatementNode> elifBodyStatementList) {
			this.expression = expression;
			this.elifBodyStatementList = elifBodyStatementList;
		}

		@Override
		public Object accept(Visitor visitor) {
			return visitor.visit(this);
		}
	}

}
