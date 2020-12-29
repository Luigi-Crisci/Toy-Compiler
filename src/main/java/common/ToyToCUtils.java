package common;

import java.util.UUID;

public class ToyToCUtils {

	public static final String FUNCTION_STRUCT_PREFIX = "function_struct_";
	public static final String FUNCTION_STRUCT_VARIABLE_PREFIX = "returnFunctionParameters_";
	public static final int STRING_DIMENSION = 256;

	public static String typeConverter(int type) {
		if (type == Symbols.STRING)
			return "char* ";
		if (type == Symbols.BOOL || type == Symbols.INT)
			return "int ";
		if (type == Symbols.FLOAT)
			return "float ";
		if (type == Symbols.NULL)
			return "NULL ";
		if (type == Symbols.VOID)
			return "void ";
		return "HORRIBLE_ERROR "; //Just an Esse3 reference
	}

	public static String opConverter(int op) {
		if (op == Symbols.AND)
			return "&&";
		if (op == Symbols.OR)
			return "||";
		if (op == Symbols.NOT)
			return "!";
		if (op == Symbols.ASSIGN)
			return "=";
		if (op == Symbols.DIV)
			return "/";
		if (op == Symbols.EQ)
			return "==";
		if (op == Symbols.GE)
			return ">=";
		if (op == Symbols.GT)
			return ">";
		if (op == Symbols.LE)
			return "<=";
		if (op == Symbols.LT)
			return "<";
		if (op == Symbols.NE)
			return "!=";
		if (op == Symbols.MINUS)
			return "-";
		if (op == Symbols.PLUS)
			return "+";
		if (op == Symbols.TIMES)
			return "*";
		return "error";
	}

	public static String getPlaceholder(Integer type) {
		if (type == Symbols.STRING)
			return "%s";
		if (type == Symbols.BOOL || type == Symbols.INT)
			return "%d";
		if (type == Symbols.FLOAT)
			return "%f";
		return "";
	}

	public static String getUniqueFunctionVariabletName(String functionStructName) {
		return functionStructName + UUID.randomUUID().toString().replace("-", "");
	}

	public static String getFunctionStructName(String functionName) {
		return FUNCTION_STRUCT_PREFIX + functionName;
	}

	public static String getInputString(int type){
		if (type == Symbols.STRING)
			return "readln()";
		if (type == Symbols.BOOL || type == Symbols.INT)
			return "strtol(readln(),NULL,10)";
		if (type == Symbols.FLOAT)
			return "strtof(readln(),NULL)";
		return "";
	}
}
