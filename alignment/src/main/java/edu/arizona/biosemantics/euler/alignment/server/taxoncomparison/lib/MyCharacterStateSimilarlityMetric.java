package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.lib;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import edu.arizona.biosemantics.common.ling.know.IGlossary;
import edu.arizona.biosemantics.euler.alignment.server.Configuration;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.CharacterStateSimilarityMetric;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.KnowsPartOf;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.AsymmetricSimilarity;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterState;

public class MyCharacterStateSimilarlityMetric implements CharacterStateSimilarityMetric {

	private KnowsPartOf knowsPartOf;
	private IGlossary glossary;
	private double stateWeight;
	
	public MyCharacterStateSimilarlityMetric(KnowsPartOf knowsPartOf, IGlossary glossary, double stateWeight) { 
		this.knowsPartOf = knowsPartOf;
		this.glossary = glossary;
		this.stateWeight = stateWeight;
	}

	@Override
	public AsymmetricSimilarity<CharacterState> get(CharacterState characterStateA, CharacterState characterStateB) {
		String organA = characterStateA.getOrgan();
		String characterA = characterStateA.getCharacter();
		String stateA = characterStateA.getState();
		String organB = characterStateB.getOrgan();
		String characterB = characterStateB.getCharacter();
		String stateB = characterStateB.getState();
		if(equals(characterA, characterB)) {
			if(organA.equals(organB)) {
				AsymmetricSimilarity<String> similarity = getStateSimilarity(stateA, stateB);
				return new AsymmetricSimilarity<CharacterState>(characterStateA, characterStateB, similarity.getSimilarity(), 
						similarity.getOppositeSimilarity());
			} else {
				int partOfHops = getPartOfHops(organA, organB);
				if(partOfHops == -1) {
					return new AsymmetricSimilarity<CharacterState>(characterStateA, characterStateB, 0, 0);
				} else {
					double discount = 1/(1+partOfHops);
					AsymmetricSimilarity<String> similarity = getStateSimilarity(stateA, stateB);
					return new AsymmetricSimilarity<CharacterState>(characterStateA, characterStateB, discount * similarity.getSimilarity(), 
							discount * similarity.getOppositeSimilarity());
				}
			}
		}
		return new AsymmetricSimilarity<CharacterState>(characterStateA, characterStateB, 0, 0);
	}
	
	private boolean equals(String characterA, String characterB) {
		Set<String> a = new HashSet<String>(Arrays.asList(characterA.split("_or_")));
		a.retainAll(Arrays.asList(characterB.split("_or_")));
		return !a.isEmpty();
	}

	//has to guarantee there is no circular part-of relationships
	private int getPartOfHops(String organA, String organB) {
		int partOfParentHops = getPartOfParentHops(organA, organB);
		int partOfChildHops = getPartOfChildHops(organA, organB);
		if(partOfParentHops == -1)
			return partOfChildHops;
		if(partOfChildHops == -1)
			return partOfParentHops;
		return Math.min(partOfParentHops, partOfChildHops);
	}

	private int getPartOfParentHops(String organA, String organB) {
		if(organA.equals(organB))
			return 0;
		
		Collection<String> parents = knowsPartOf.getBearers(organA);
		if(parents.isEmpty())
			return -1;
		
		int minimalHops = Integer.MAX_VALUE;
		for(String parent : parents) {
			int hops = this.getPartOfParentHops(parent, organB);
			if(hops != -1 && hops < Integer.MAX_VALUE) {
				minimalHops = hops;
			}
		}
		
		if(minimalHops == Integer.MAX_VALUE) {
			return -1;
		} else {
			return minimalHops;
		}
	}
	
	private int getPartOfChildHops(String organA, String organB) {
		if(organA.equals(organB))
			return 0;
		
		Collection<String> parts = knowsPartOf.getParts(organA);
		if(parts.isEmpty())
			return -1;
		
		int minimalHops = Integer.MAX_VALUE;
		for(String part : parts) {
			int hops = this.getPartOfChildHops(part, organB);
			if(hops != -1 && hops < Integer.MAX_VALUE) {
				minimalHops = hops;
			}
		}
		
		if(minimalHops == Integer.MAX_VALUE) {
			return -1;
		} else {
			return minimalHops;
		}
	}

	private AsymmetricSimilarity<String> getStateSimilarity(String stateA, String stateB) {
		Set<String> aTokens = new HashSet<String>();
		aTokens.addAll(Arrays.asList(stateA.split("\\s+"))); 
		Set<String> bTokens = new HashSet<String>();
		bTokens.addAll(Arrays.asList(stateB.split("\\s+")));
		
		double similarity = getSimilarity(aTokens, bTokens);
		double oppositeSimilarity = getSimilarity(bTokens, aTokens);
		return new AsymmetricSimilarity<String>(stateA, stateB, similarity, oppositeSimilarity);
	}

	private double getSimilarity(Set<String> aTokens, Set<String> bTokens) {
		int availableOverlap = 0;
		int overlap = 0;
		boolean foundState = false;
		for(String aToken : aTokens) {
			if(isState(aToken)) {
				foundState = true;
				availableOverlap += stateWeight;
			
				if(bTokens.contains(aToken)) 
					overlap += stateWeight;
			} else {
				availableOverlap++;
				
				if(bTokens.contains(aToken)) 
					overlap++;
			}
		}
		return (double)overlap/availableOverlap;
	}

	private boolean isState(String token) {
		Set<String> categories = glossary.getCategories(token);
		categories.remove("substance");
		categories.remove("structure_subtype");
		categories.remove("structure_in_adjective_form");
		categories.remove("structure");
		categories.remove("taxon_name");
		categories.remove("season");
		return !categories.isEmpty();
	}
}


