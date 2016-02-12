package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison;

import java.util.Collection;

import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.ArticulationProposal;

public interface ArticulationsGenerator {
	
	public Collection<ArticulationProposal> generate(Taxonomy taxonomyA, Taxonomy taxonomyB);
	
}
