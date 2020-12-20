package common;

import java.util.List;

import nodes.ExpressionNode.IdentifierExpression;
import parser.Symbols;

public class ToyToCUtils {
	
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

	private static String getPlaceholder(Integer type) {
		if(type == Symbols.STRING)
			return "%s";
		if(type == Symbols.BOOL || type == Symbols.INT)
			return "%d";
		if(type == Symbols.FLOAT)
			return "%f";
		return "";
	}

}
