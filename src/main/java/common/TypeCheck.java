package common;

public class TypeCheck {

	public static int checkType(Integer op, Integer type1, Integer type2){
		if ( type1 == type2)
			return type1;
		return -1;
	}
	
}
