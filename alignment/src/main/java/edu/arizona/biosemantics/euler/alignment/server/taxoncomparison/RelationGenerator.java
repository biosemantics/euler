package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.AsymmetricSimilarity;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.RelationProposal;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.Similarities;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.Taxonomy;

public interface RelationGenerator {

	public Set<RelationProposal> generate(Taxon taxonA, Taxon taxonB);

	public List<Evidence> getEvidence(Taxon taxonA, Taxon taxonB);

	public AsymmetricSimilarity<Taxon> getAsymmetricSimilarity(Taxon taxonA, Taxon taxonB);

	public Similarities getSimilarities(Taxon taxonA, Taxon taxonB, double threshold);
	
}
