package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison;

import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.AsymmetricSimilarity;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterState;

public interface CharacterStateSimilarityMetric {

	public AsymmetricSimilarity<CharacterState> get(CharacterState characterStateA, CharacterState characterStateB);
	
}
