package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.arizona.biosemantics.common.log.LogLevel;

public class PartOfModel implements Serializable, KnowsPartOf {
	
	private static final long serialVersionUID = 1L;
	private Map<String, Set<String>> bearers = new HashMap<String, Set<String>>();
	private Map<String, Set<String>> parts = new HashMap<String, Set<String>>();
	
	public PartOfModel() { }

	public void addPartOfRelation(String part, String bearer) {
		if(!bearers.containsKey(part))
			bearers.put(part, new HashSet<String>());
		bearers.get(part).add(bearer);
		
		if(!parts.containsKey(bearer))
			parts.put(bearer, new HashSet<String>());
		parts.get(bearer).add(part);
	}
	
	public void removePartOfRelation(String part, String bearer) {
		if(bearers.containsKey(part)) {
			bearers.get(part).remove(bearer);
		}
		if(parts.containsKey(bearer)) {
			parts.get(bearer).remove(part);
		}
	}

	public Set<String> getBearers(String part) {
		if(bearers.containsKey(part)) 
			return bearers.get(part);
		return new HashSet<String>();
	}

	public Set<String> getParts(String bearer) {
		if(parts.containsKey(bearer)) 
			return parts.get(bearer);
		return new HashSet<String>();
	}
}
