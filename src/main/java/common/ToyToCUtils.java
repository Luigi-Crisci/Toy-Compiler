package common;

import java.util.List;
import java.util.UUID;

import nodes.ExpressionNode;
import nodes.ExpressionNode.CallProcedureExpression;
import nodes.ExpressionNode.IdentifierExpression;
import parser.Symbols;

public class ToyToCUtils {

	public static final String FUNCTION_STRUCT_PREFIX = "function_struct_";
	public static final String FUNCTION_STRUCT_VARIABLE_PREFIX = "returnFunctionParameters_";
	
	public static String typeConverter(int type){
		if(type == Symbols.STRING)
			return "char* ";
		if(type == Symbols.BOOL || type == Symbols.INT)
			return "int ";
		if(type == Symbols.FLOAT)
			return "float ";
		if(type == Symbols.NULL)
			return "NULL ";
		return "HORRIBLE_ERROR ";
	}

	public static String createPlaceholderString(List<IdentifierExpression> idList){
		String placeholderString = "";
		for(IdentifierExpression id : idList){
			String placeholder = getPlaceholder(id.getType());
			placeholderString += placeholder;
		}
		return placeholderString;
	}

	public static String getPlaceholder(Integer type) {
		if(type == Symbols.STRING)
			return "%s";
		if(type == Symbols.BOOL || type == Symbols.INT)
			return "%d";
		if(type == Symbols.FLOAT)
			return "%f";
		return "";
	}

	public static String getUniqueFunctionVariabletName(String functionStructName){
		return functionStructName + UUID.randomUUID().toString().replace("-", "");
	}

}
