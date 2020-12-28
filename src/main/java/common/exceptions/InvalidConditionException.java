package common.exceptions;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class InvalidConditionException extends SemanticException {

	public InvalidConditionException(String string,Location l) {
		super(string,l);
	}

	public InvalidConditionException(String string,int l) {
		super(string,l);
	}
	
}
