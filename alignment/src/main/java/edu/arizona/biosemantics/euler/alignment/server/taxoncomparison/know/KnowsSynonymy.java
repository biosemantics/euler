package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know;

import java.util.Set;

public interface KnowsSynonymy {

	public Set<String> getSynonyms(String term);
	
	public boolean isPreferredTerm(String term);
	
	public String getPreferredTerm(String term);
	
}
