package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know;

import java.util.Collection;

public interface KnowsPartOf {
	
	public Collection<String> getBearers(String part);
	
	public Collection<String> getParts(String bearer);
	
}