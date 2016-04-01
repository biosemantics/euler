package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.lib;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.arizona.biosemantics.common.ling.know.IGlossary;
import edu.arizona.biosemantics.common.ling.know.lib.InMemoryGlossary;
import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.euler.alignment.server.Configuration;
import edu.arizona.biosemantics.euler.alignment.server.db.CollectionDAO;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.CharacterStateSimilarityMetric;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.KnowsPartOf;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.PartOfModel;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.AsymmetricSimilarity;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterState;

public class MyCharacterStateSimilarlityMetric implements CharacterStateSimilarityMetric {

	private KnowsPartOf knowsPartOf;
	private IGlossary glossary;
	private double stateWeight;
	private Map<String, Double> modifierWeights = new HashMap<String, Double>();
	private static String units = "(?:(?:pm|cm|mm|dm|ft|m|meters|meter|micro_m|micro-m|microns|micron|unes|µm|μm|um|centimeters|"
			+ "centimeter|millimeters|millimeter|transdiameters|transdiameter)[23]?)"; //squared or cubed
	
	public MyCharacterStateSimilarlityMetric(KnowsPartOf knowsPartOf, IGlossary glossary, double stateWeight) { 
		this.knowsPartOf = knowsPartOf;
		this.glossary = glossary;
		this.stateWeight = stateWeight;
		
		initModifierWeights();
	}

	private void initModifierWeights() {
		modifierWeights.put("always", 1.0);
		modifierWeights.put("usually", 1.0);
		modifierWeights.put("generally", 1.0);
		modifierWeights.put("often", 1.0);
		modifierWeights.put("sometimes", 1.0);
		modifierWeights.put("less frequently", 1.0);
		modifierWeights.put("not", 1.0);
		modifierWeights.put("never", 1.0);
	}

	public static void main(String[] args) {
		CollectionDAO collectionDAO = new CollectionDAO();
		InMemoryGlossary glossary = null;
		try {
			glossary = collectionDAO.unserializeGlossary(25);
		} catch(Exception e) {
			e.printStackTrace();
			glossary = new InMemoryGlossary();
		}
		PartOfModel partOfModel = null;
		try {
			partOfModel = collectionDAO.unserializePartOfModel(25);
		} catch(Exception e) {
			e.printStackTrace();
			partOfModel = new PartOfModel();
		}
		MyCharacterStateSimilarlityMetric similarityMetric = new MyCharacterStateSimilarlityMetric(partOfModel, glossary, 3.0);
		CharacterState characterStateA = new CharacterState("ovary", "position", "inferior | superior");
		CharacterState characterStateB = new CharacterState("ovary", "position", "inferior | semi-inferior | superior");
		AsymmetricSimilarity<CharacterState> similarity = similarityMetric.get(characterStateA, characterStateB);
		System.out.println(similarity.getAverageSimilarity());
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
		double similarity = getSimilarity(stateA, stateB);
		double oppositeSimilarity = getSimilarity(stateB, stateA);
		return new AsymmetricSimilarity<String>(stateA, stateB, similarity, oppositeSimilarity);
	}

	private double getSimilarity(String stateA, String stateB) {
		if(isNumeric(stateA) && isNumeric(stateB)) {
			return getNumericalStateSimilarity(stateA, stateB);
		} else if((isNumeric(stateA) && !isNumeric(stateB)) || 
				!isNumeric(stateA) && isNumeric(stateB)) {
			return 0.0;
		} else {
			return getOtherStateSimiliarty(stateA, stateB);			
		}
	}

	private double getOtherStateSimiliarty(String stateAString, String stateBString) {
		/*
		Set<String> aTokens = new HashSet<String>();
		aTokens.addAll(Arrays.asList(stateA.split("\\s+"))); 
		Set<String> bTokens = new HashSet<String>();
		bTokens.addAll(Arrays.asList(stateB.split("\\s+")));
		*/
		String[] stateAs = stateAString.split("\\|");
		String[] stateBs = stateBString.split("\\|");
		
		Map<String, Double> aStateWeights = new HashMap<String, Double>();
		for(String stateA : stateAs) {
			stateA = stateA.trim();
			double weight = 1;
			for(String token : stateA.split("\\s+")) {
				token = token.trim();
				if(isModifier(token)) {
					weight = weight * modifierWeights.get(token);
				} else if(isState(token)) {
					aStateWeights.put(token, weight);
				} else {
					// nothing todo
				}
			}
		}
		Map<String, Double> bStateWeights = new HashMap<String, Double>();
		for(String stateB : stateBs) {
			stateB = stateB.trim();
			double weight = 1;
			for(String token : stateB.split("\\s+")) {
				token = token.trim();
				if(isModifier(token)) {
					weight = weight * modifierWeights.get(token);
				} else if(isState(token)) {
					bStateWeights.put(token, weight);
				} else {
					// nothing todo
				}
			}
		}
		
		double overlap = 0;
		double maxOverlap = 0;
		for(String aState : aStateWeights.keySet()) {
			maxOverlap += aStateWeights.get(aState);
			if(bStateWeights.containsKey(aState)) {
				overlap += bStateWeights.get(aState) / aStateWeights.get(aState);
			}
		}
		return overlap / maxOverlap;
	}

	private boolean isModifier(String token) {
		return modifierWeights.containsKey(token);
	}

	//for now assume units are always the same
	private double getNumericalStateSimilarity(String stateA, String stateB) {
		Pattern numericalPart = Pattern.compile(".*?(\\d+)\\s*"+ units + "?");

		double[] aMinMax = new double[2];
		if(stateA.contains("-")) {
			String[] parts = stateA.split("-");
			if(parts.length == 2) {
				for(int i=0; i<2; i++) {
					String part = parts[i].trim();
					Matcher matcher = numericalPart.matcher(part);
					if(matcher.matches()) {
						aMinMax[i] = Double.valueOf(matcher.group(1));
					} else {
						return 0;
					}
				}
			} else {
				return 0;
			}
		} 

		double[] bMinMax = new double[2];
		if(stateB.contains("-")) {
			String[] parts = stateB.split("-");
			if(parts.length == 2) {
				for(int i=0; i<2; i++) {
					String part = parts[i].trim();
					Matcher matcher = numericalPart.matcher(part);
					if(matcher.matches()) {
						bMinMax[i] = Double.valueOf(matcher.group(1));
					} else {
						return 0;
					}
				}
			} else {
				return 0;
			}
		} 
		
		
		double commonStart = Math.max(aMinMax[0], bMinMax[0]);
		double commonEnd = Math.min(aMinMax[1], bMinMax[1]);
		if (commonEnd < commonStart)
			return 0;
		return (commonEnd - commonStart) / (aMinMax[1]-aMinMax[0]);
	}

	private boolean isNumeric(String state) {
		Pattern pattern = Pattern.compile(".*?\\d+\\s*"+ units + "?");
		boolean numeric = true;
		if(state.contains("-")) {
			String[] parts = state.split("-");
			for(String part : parts) {
				part = part.trim();
				numeric &= pattern.matcher(part).matches();
			}
		} else {
			numeric &= state.matches(".*?\\d+\\s*"+ units + "?");
		}
		return numeric;
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


