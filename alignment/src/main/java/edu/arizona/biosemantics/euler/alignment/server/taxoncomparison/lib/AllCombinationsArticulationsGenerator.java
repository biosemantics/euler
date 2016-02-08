package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.lib;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.ArticulationsGenerator;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.RelationGenerator;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.ArticulationProposal;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.Taxonomy;

public class AllCombinationsArticulationsGenerator implements ArticulationsGenerator {

	private RelationGenerator relationGenerator;

	public AllCombinationsArticulationsGenerator(RelationGenerator articulationGenerator) {
		this.relationGenerator = articulationGenerator;
	}
	
	@Override
	public Collection<ArticulationProposal> generate(Taxonomy taxonomyA, Taxonomy taxonomyB) {
		Set<ArticulationProposal> result = new HashSet<ArticulationProposal>();
		for(Taxon taxonA : taxonomyA.getTaxaDFS()) {
			for(Taxon taxonB : taxonomyB.getTaxaDFS()) {
				result.add(new ArticulationProposal(taxonA, taxonB, 
						relationGenerator.getEvidence(taxonA, taxonB),
						relationGenerator.generate(taxonA, taxonB)));
			}
		}
		return result;
	}

}
