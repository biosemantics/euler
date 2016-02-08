package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison;

import java.util.Collection;

import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.ArticulationProposal;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.RelationProposal;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.Taxonomy;

public interface ArticulationsGenerator {
	
	public Collection<ArticulationProposal> generate(Taxonomy taxonomyA, Taxonomy taxonomyB);
	
}
