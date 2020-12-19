package common;


import parser.Symbols;

public class TypeCheck {
	
	public static int checkType(Integer op, Integer type1, Integer type2){
				
		//Binary arithmetics
		if(op == Symbols.PLUS || op == Symbols.MINUS || op == Symbols.TIMES || op == Symbols.DIV){
			
			if(type1 == type2)
				return type1;


			else if((type1 == Symbols.INT && type2 == Symbols.FLOAT) || (type1 == Symbols.FLOAT && type2 == Symbols.INT))
						return Symbols.FLOAT;

			else 
				return -1;
		}

		//Unary minus
		else if(op == Symbols.MINUS && type2 == null){
			if(type1 == Symbols.INT || type1 == Symbols.FLOAT)
				return type1;
			else 
				return -1;
		}

		//Binary logical
		else if(op == Symbols.AND || op == Symbols.OR){
			if (type1 == type2 && type1 == Symbols.BOOL)
				return Symbols.BOOL;
			else
				return -1;
		}
		
		//Not
		else if(op == Symbols.NOT && type2 == null){
			if(type1 == Symbols.BOOL)
				return Symbols.BOOL;
			else
				return -1;
		}

		//Comparators
		else if(op == Symbols.LE || op == Symbols.LT || op == Symbols.EQ || op == Symbols.GT || op == Symbols.GE){

			if(type1 != Symbols.STRING && type2 != Symbols.STRING)
				return Symbols.BOOL;

			else 
				return -1;

		}
		
		//Assign
		else if(op == Symbols.ASSIGN){

			if(type1==type2)
				return type1;

			// if(type1 == Symbols.INT && type2 == Symbols.FLOAT || type1 == Symbols.FLOAT && type2 == Symbols.INT)
			// 				return type1;
		}

		//Call proc
		else if(op== Symbols.CORP){
			if(type1==type2)
				return type1;
		}		

		return -1;

	}
	
}
