package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison;

import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterOverlap;

public interface CharacterOverlapCalculator {

	public CharacterOverlap getCharacterOverlap(Taxon taxonA, Taxon taxonB, double threshold);
}
