package parser.nodes;

import java.util.Collections;
import java.util.List;

import common.exceptions.SemanticException;
import common.interfaces.Visitable;
import common.interfaces.Visitor;
import java_cup.runtime.ComplexSymbolFactory.Location;

public abstract class ExpressionNode extends TypedNode implements Visitable {


	public ExpressionNode(Location left,Location rigth){
		super(left, rigth);
	}

	public ExpressionNode(){
		super();
	}

	public static class BinaryExpression extends ExpressionNode {

		public ExpressionNode leftExpression;
		public int operation;
		public ExpressionNode rightExpression;

		public BinaryExpression(ExpressionNode leftExpression, int operation, ExpressionNode rightExpression,Location left,Location right) {
			super(left,right);
			this.leftExpression = leftExpression;
			this.operation = operation;
			this.rightExpression = rightExpression;
		}

		@Override
		public Object accept(Visitor visitor) throws SemanticException {
			return visitor.visit(this);
		}
	}

	public static class UnaryExpression extends ExpressionNode {

		public int operation;
		public ExpressionNode rightExpression;

		public UnaryExpression(int operation, ExpressionNode rightExpression,Location left,Location right) {
			super(left,right);
			this.operation = operation;
			this.rightExpression = rightExpression;
		}

		@Override
		public Object accept(Visitor visitor) throws SemanticException {
			return visitor.visit(this);
		}

	}

	public static class IntegerConstant extends ExpressionNode {

		public int value;

		public IntegerConstant(int value,Location left, Location rigth) {
			super(left,rigth);
			this.value = value;
		}

		@Override
		public Object accept(Visitor visitor) throws SemanticException {
			return visitor.visit(this);
		}
	}

	public static class FloatConstant extends ExpressionNode {

		public float value;

		public FloatConstant(float value,Location left, Location rigth) {
			super(left,rigth);
			this.value = value;
		}

		@Override
		public Object accept(Visitor visitor) throws SemanticException {
			return visitor.visit(this);
		}
	}

	public static class StringConstant extends ExpressionNode {

		public String value;

		public StringConstant(String value,Location left, Location rigth) {
			super(left,rigth);
			this.value = value;
		}

		@Override
		public Object accept(Visitor visitor) throws SemanticException {
			return visitor.visit(this);
		}
	}

	public static class BooleanConstant extends ExpressionNode {

		public boolean value;

		public BooleanConstant(boolean value,Location left, Location rigth) {
			super(left,rigth);
			this.value = value;
		}

		@Override
		public Object accept(Visitor visitor) throws SemanticException {
			return visitor.visit(this);
		}
	}

	public static class NullConstant extends ExpressionNode {

		public NullConstant(Location left, Location rigth) {
			super(left,rigth);
		}
		
		@Override
		public Object accept(Visitor visitor) throws SemanticException {
			return visitor.visit(this);
		}

	}

	public static class IdentifierExpression extends ExpressionNode {

		public String value;

		public IdentifierExpression(String value,Location left, Location rigth) {
			super(left,rigth);
			this.value = value;
		}

		@Override
		public Object accept(Visitor visitor) throws SemanticException {
			return visitor.visit(this);
		}
	}

	public static class CallProcedureExpression extends ExpressionNode {

		public IdentifierExpression id;
		public List<ExpressionNode> expressionList;

		public CallProcedureExpression(IdentifierExpression id, List<ExpressionNode> expressionList,Location left,Location right) {
			super(left,right);
			this.id = id;
			this.expressionList = expressionList;
		}

		public CallProcedureExpression(IdentifierExpression id,Location left,Location right) {
			super(left,right);
			this.id = id;
			this.expressionList = Collections.emptyList();
		}

		@Override
		public Object accept(Visitor visitor) throws SemanticException {
			return visitor.visit(this);
		}
	}

}
