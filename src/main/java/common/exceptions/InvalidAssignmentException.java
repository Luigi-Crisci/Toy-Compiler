package common.exceptions;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class InvalidAssignmentException extends SemanticException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public InvalidAssignmentException(String string, Location l) {
		super(string,l);
	}

	public InvalidAssignmentException(String string,int l) {
		super(string,l);
	}
	
}
