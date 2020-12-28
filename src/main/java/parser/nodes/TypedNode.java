package parser.nodes;

import java.util.LinkedList;
import java.util.List;

import java_cup.runtime.ComplexSymbolFactory.Location;

public abstract class TypedNode {
	public List<Integer> typeList;
	public Location left, right;

	public TypedNode(Location left, Location right){
		typeList = new LinkedList<>();
		this.left = left;
		this.right = right;
	}

	public TypedNode(){
		typeList = new LinkedList<>();
		left = right = null;
	}

	public Integer getType(){
		return typeList.get(0);
	}


}
