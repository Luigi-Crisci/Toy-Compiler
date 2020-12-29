package common.exceptions;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class InvalidReturnTypeException extends SemanticException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public InvalidReturnTypeException(String string, Location l) {
		super(string,l);
	}

	public InvalidReturnTypeException(String string,int l) {
		super(string,l);
	}
	
}
