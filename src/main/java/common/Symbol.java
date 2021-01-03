package common;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Symbol {

	public static final int SEPARATOR = -1;
	
	public String identifier;
	public Integer entryType;
	public LinkedList<Integer> typeList;

	public Symbol(String id,Integer entryType, Integer type) {
		identifier = id;
		this.entryType = entryType;
		typeList = new LinkedList<>();
		typeList.add(type);
	}

	public Symbol(String id, List<Integer> paramList, List<Integer> returnList) {
		identifier = id;
		this.entryType = SymbolTypes.METHOD;
		typeList = new LinkedList<>();	
		
		if (paramList != null)
			typeList.addAll(paramList);
		typeList.add(SEPARATOR);
		typeList.addAll(returnList);

	}

	public ArrayList<List<Integer>> getParamAndReturnTypes(){
		if (entryType != SymbolTypes.METHOD)
			return null;

		ArrayList<List<Integer>> lists = new ArrayList<>();

		int sep = typeList.indexOf(-1);
		lists.add(typeList.subList(0, sep));
		lists.add(typeList.subList(sep + 1, typeList.size()));

		return lists;
	}

	public Integer getType(){
		if (entryType != SymbolTypes.VAR)
			return null;

		return typeList.get(0);
	}
	
}
