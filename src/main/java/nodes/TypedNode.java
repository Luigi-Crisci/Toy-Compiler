package nodes;

import java.util.LinkedList;
import java.util.List;

public abstract class TypedNode {
	public List<Integer> type;

	public TypedNode(){
		type = new LinkedList<>();
	}
}
