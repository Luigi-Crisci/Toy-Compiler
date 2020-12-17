package common;

import java.util.LinkedList;
import java.util.List;

public class Symbol {

	public static final int SEPARATOR = -1;
	
	public String identifier;
	public Integer entryType;
	public List<Integer> typeList;

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

	public List<Integer>[] getParamAndReturnTypes(){
		if (entryType != SymbolTypes.METHOD)
			return null;

		List<Integer>[] lists = new LinkedList[2];

		int sep = typeList.indexOf(-1);
		lists[0] = typeList.subList(0, sep);
		lists[1] = typeList.subList(sep + 1, typeList.size());

		return lists;
	}

	public Integer getType(){
		if (entryType != SymbolTypes.VAR)
			return null;

		return typeList.get(0);
	}

}
