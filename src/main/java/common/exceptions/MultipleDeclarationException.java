package common.exceptions;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class MultipleDeclarationException extends SemanticException {

	private static final long serialVersionUID = 1L;

	public MultipleDeclarationException(String string,Location l) {
		super(string,l);
	}

	public MultipleDeclarationException(String string,int l) {
		super(string,l);
	}
	
}
