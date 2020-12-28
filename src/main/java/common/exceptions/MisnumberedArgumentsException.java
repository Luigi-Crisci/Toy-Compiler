package common.exceptions;

import java_cup.runtime.ComplexSymbolFactory.Location;

public class MisnumberedArgumentsException extends SemanticException {

	public MisnumberedArgumentsException(String string,Location l) {
		super(string,l);
	}

	public MisnumberedArgumentsException(String string,int l) {
		super(string,l);
	}
	
}
