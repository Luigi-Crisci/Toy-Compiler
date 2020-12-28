package common.exceptions;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class SemanticException extends Exception{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public SemanticException(String message,Location location) {
		super("Error at line " + location.getLine() + ", column " + location.getColumn()+": " + message);
	}

	public SemanticException(String message,int line) {
		super("Error at line " + line + ": " + message);
	}

	public SemanticException() {
		super();
	}
	
	


}
