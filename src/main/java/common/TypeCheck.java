package common;

public class TypeCheck {

	public static int checkType(Integer op, Integer type1, Integer type2) {
		// Binary arithmetics
		if (isArithmeticOperator(op)) {
			if (!isArithmeticalOperationAllowed(type1) || !isArithmeticalOperationAllowed(type2))
				return -1;

			if (type1 == type2)
				return type1;
			else
				return Symbols.FLOAT;
		}

		// Binary logical
		if (isLogicalOperator(op))
			if (type1 == type2 && type1 == Symbols.BOOL)
				return Symbols.BOOL;

		// Comparators
		if (isRelationalOperator(op))
			if (type1 != Symbols.STRING && type2 != Symbols.STRING)
				return Symbols.BOOL;

		// Assign
		if (op == Symbols.ASSIGN) {
			if (type1 == type2)
				return type1;

			// if(type1 == Symbols.INT && type2 == Symbols.FLOAT || type1 == Symbols.FLOAT
			// && type2 == Symbols.INT)
			// return type1;
		}

		// Call proc
		if (op == Symbols.CORP)
			if (type1 == type2)
				return type1;

		return -1;

	}

	public static int checkType(Integer op, Integer type) {
		if (!isUnaryOperator(op))
			return -1;
		if (op == Symbols.MINUS && isArithmeticalOperationAllowed(type))
			return type;
		if (op == Symbols.NOT && type == Symbols.BOOL)
			return type;
		return -1;
	}

	public static boolean isRelationalOperator(Integer op) {
		return op == Symbols.LE || op == Symbols.LT || op == Symbols.EQ || op == Symbols.NE || op == Symbols.GT || op == Symbols.GE;
	}

	public static boolean isLogicalOperator(Integer op) {
		return op == Symbols.AND || op == Symbols.OR;
	}

	public static boolean isUnaryOperator(Integer op) {
		return op == Symbols.MINUS || op == Symbols.NOT;
	}

	public static boolean isArithmeticalOperationAllowed(Integer type) {
		return type == Symbols.INT || type == Symbols.FLOAT;
	}

	public static boolean isArithmeticOperator(Integer op) {
		return op == Symbols.PLUS || op == Symbols.MINUS || op == Symbols.TIMES || op == Symbols.DIV;
	}

}