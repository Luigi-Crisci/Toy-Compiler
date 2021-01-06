package common;

import parser.nodes.TypedNode;

public class Utils {
	public static boolean isFunctionWithMultipleReturns(TypedNode node){
		return hasMultipleReturns(node);
	}

	public static boolean hasMultipleReturns(TypedNode node){
		return node.typeList.size() > 1;
	}
}
