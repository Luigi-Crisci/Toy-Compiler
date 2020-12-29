package common.exceptions;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class IdNotFoundException extends SemanticException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public IdNotFoundException(String string, Location l) {
		super(string,l);
	}

	public IdNotFoundException(String string,int l) {
		super(string,l);
	}


	
}
