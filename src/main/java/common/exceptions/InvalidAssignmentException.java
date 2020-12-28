package common.exceptions;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class InvalidAssignmentException extends SemanticException {

	public InvalidAssignmentException(String string,Location l) {
		super(string,l);
	}

	public InvalidAssignmentException(String string,int l) {
		super(string,l);
	}
	
}
