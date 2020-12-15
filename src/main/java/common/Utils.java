package common;

import parser.Symbols;

public class Utils {
	
	public static boolean isRelational(Integer type){
		if (type == Symbols.GT || type == Symbols.GE || type == Symbols.LT || type == Symbols.LE || type == Symbols.EQ || type == Symbols.NE)
		  return true;
		return false;
	}

}
