package common.exceptions;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class InvalidExpressionException extends SemanticException {

	public InvalidExpressionException(String string,Location l) {
		super(string,l);
	}

	public InvalidExpressionException(String string,int l) {
		super(string,l);
	}
	
}
