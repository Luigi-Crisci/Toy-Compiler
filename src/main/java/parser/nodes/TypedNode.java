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

	public Integer getType(int i){
		return typeList.get(i);
	}

	/**
	 * Return number of types for this node
	 * @return	the size of the typeList
	 */
	public Integer size(){
		return typeList.size();
	}

	public boolean containsType(int type){
		return typeList.contains(type);
	}

	public void addType(int type){
		typeList.add(type);
	}
}
