package nodes;

import java.util.LinkedList;
import java.util.List;

public abstract class TypedNode {
	public List<Integer> typeList;

	public TypedNode(){
		typeList = new LinkedList<>();
	}
}
