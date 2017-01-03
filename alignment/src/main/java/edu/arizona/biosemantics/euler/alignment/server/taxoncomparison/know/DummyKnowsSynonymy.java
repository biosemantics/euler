package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DummyKnowsSynonymy implements KnowsSynonymy {

	@Override
	public Set<String> getSynonyms(String term) {
		return new HashSet<String>();
	}

	@Override
	public boolean isPreferredTerm(String term) {
		return true;
	}

	@Override
	public String getPreferredTerm(String term) {
		return term;
	}

}
