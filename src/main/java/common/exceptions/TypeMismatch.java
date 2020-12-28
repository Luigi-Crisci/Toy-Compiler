package common.exceptions;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class TypeMismatch extends SemanticException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public TypeMismatch(String string,Location l) {
		super(string,l);
	}
	
	public TypeMismatch(String string,int l) {
		super(string,l);
	}
}
