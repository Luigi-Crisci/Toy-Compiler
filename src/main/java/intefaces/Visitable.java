package intefaces;

import common.exceptions.SemanticException;

public interface Visitable {
	public Object accept(Visitor visitor) throws SemanticException;

}
