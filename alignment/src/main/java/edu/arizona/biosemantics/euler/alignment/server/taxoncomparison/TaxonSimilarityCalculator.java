package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison;

import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.AsymmetricSimilarity;

public interface TaxonSimilarityCalculator {
	
	public AsymmetricSimilarity<Taxon> getTaxonSimilarity(Taxon taxonA, Taxon taxonB);

}
