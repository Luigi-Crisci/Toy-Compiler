package common.exceptions;

public class MultipleDeclarationException extends SemanticException{

	private static final long serialVersionUID = 1L;

	public MultipleDeclarationException(String message) {
		super(message);
	}

	public MultipleDeclarationException(){
		super();
	}
	
}
