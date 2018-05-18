package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.arizona.biosemantics.common.ling.know.IGlossary;
import edu.arizona.biosemantics.common.ling.know.lib.InMemoryGlossary;
import edu.arizona.biosemantics.euler.alignment.server.db.CollectionDAO;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.CharacterStateSimilarityMetric;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.DummyKnowsModifier;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.DummyKnowsSynonymy;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.KnowsModifier;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.KnowsPartOf;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.KnowsSynonymy;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.know.PartOfModel;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state.CategoricalReader;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state.DiscreteNumericalRangeReader;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state.LikelyStateReader;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state.ModifiedStateReader;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state.NumericalRangeReader;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state.State;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state.StateSimilarity;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.AsymmetricSimilarity;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterState;
import edu.arizona.biosemantics.matrixreview.shared.model.core.Value;

public class MyCharacterStateSimilarlityMetric implements CharacterStateSimilarityMetric {

	private KnowsPartOf knowsPartOf;
	private IGlossary glossary;
	private double stateWeight;
	private LikelyStateReader likelyStateReader;
	private StateSimilarity stateSimilarity;
	
	public MyCharacterStateSimilarlityMetric(KnowsPartOf knowsPartOf, IGlossary glossary, 
			LikelyStateReader likelyStateReader, 
			double stateWeight, StateSimilarity stateSimilarity) { 
		this.knowsPartOf = knowsPartOf;
		this.likelyStateReader = likelyStateReader;
		this.glossary = glossary;
		this.stateWeight = stateWeight;
		this.stateSimilarity = stateSimilarity;
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
		
		KnowsModifier knowsModifier = new DummyKnowsModifier();
		KnowsSynonymy knowsSynonymy = new DummyKnowsSynonymy();
		CategoricalReader categoricalReader = new CategoricalReader();
		DiscreteNumericalRangeReader discreteNumericalRangeReader = new DiscreteNumericalRangeReader();
		ModifiedStateReader modifiedStateReader = new ModifiedStateReader(knowsModifier);
		NumericalRangeReader numericalRangeReader = new NumericalRangeReader();
		LikelyStateReader likelyStateReader = new LikelyStateReader(categoricalReader, 
				discreteNumericalRangeReader, modifiedStateReader, numericalRangeReader);
		modifiedStateReader.setLikelyStateReader(likelyStateReader);
		StateSimilarity stateSimilarity = new StateSimilarity(knowsModifier, knowsSynonymy);
		MyCharacterStateSimilarlityMetric similarityMetric = 
				new MyCharacterStateSimilarlityMetric(partOfModel, glossary, likelyStateReader, 3.0, stateSimilarity);
		//CharacterState characterStateA = new CharacterState("stem", "architecture", "unarmed | with a few weak bristles");
		//CharacterState characterStateB = new CharacterState("stem", "architecture", "unarmed");
		
		Value stateA = new Value("horizontal | upright | of near the base or along the length the horizontal ones; arising");
		Value stateB = new Value("upright");
		
		CharacterState characterStateA = new CharacterState("stem", "orientation", stateA);
		CharacterState characterStateB = new CharacterState("stem", "orientation", stateB);
		
		//CharacterState characterStateA = new CharacterState("ovary", "position", "inferior | superior");
		//CharacterState characterStateB = new CharacterState("ovary", "position", "inferior | semi-inferior | superior");
		AsymmetricSimilarity<CharacterState> similarity = similarityMetric.get(characterStateA, characterStateB);
		System.out.println(similarity.getAverageSimilarity());
	}
	
	@Override
	public AsymmetricSimilarity<CharacterState> get(CharacterState characterStateA, CharacterState characterStateB) {
		String organA = characterStateA.getOrgan();
		String characterA = characterStateA.getCharacter();
		String stateA = characterStateA.getState().getValue();
		String organB = characterStateB.getOrgan();
		String characterB = characterStateB.getCharacter();
		String stateB = characterStateB.getState().getValue();
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
	
	private class StatePair {
		
		public State stateA;
		public State stateB;
		public double similarity = 0.0;
		
		public StatePair(State stateA, State stateB, double similarity) {
			this.stateA = stateA;
			this.stateB = stateB;
			this.similarity = similarity;
		}
		
	}

	private double getSimilarity(String stateAString, String stateBString) {
		String[] stateAs = stateAString.split("\\|");
		String[] stateBs = stateBString.split("\\|");
		
		List<State> sAs = new ArrayList<State>(stateAs.length);
		List<State> sBs = new ArrayList<State>(stateBs.length);
		for(String stateA : stateAs) {
			stateA = stateA.trim();
			sAs.add(likelyStateReader.read(stateA));
		}
		for(String stateB : stateBs) {
			stateB = stateB.trim();
			sBs.add(likelyStateReader.read(stateB));
		}
		List<StatePair> pairs = new ArrayList<StatePair>();
		for(State sA : sAs) 
			for(State sB : sBs) 
				pairs.add(new StatePair(sA, sB, stateSimilarity.getSimilarity(sA, sB)));
		
		Collections.sort(pairs, new Comparator<StatePair>() {
			@Override
			public int compare(StatePair o1, StatePair o2) {
				return Double.compare(o1.similarity, o2.similarity);
			}
		});
		
		Set<State> usedStatesA = new HashSet<State>();
		Set<State> usedStatesB = new HashSet<State>();
		Set<StatePair> selectedPairs = new HashSet<StatePair>();
		for(StatePair p : pairs) {
			if(!usedStatesA.contains(p.stateA) && !usedStatesB.contains(p.stateB) && p.similarity > 0.0) {
				usedStatesA.add(p.stateA);
				usedStatesB.add(p.stateB);
				selectedPairs.add(p);
			}
		}
		
		double maxSimilarity = Math.max(stateAs.length, stateBs.length);
		double accumulatedSimilarity = 0.0;
		for(StatePair selected : selectedPairs) {
			accumulatedSimilarity += selected.similarity;
		}
		
		return accumulatedSimilarity / maxSimilarity;
	}

	//
//	//for now assume units are always the same
//	private double getNumericalStateSimilarity(String stateA, String stateB) {
//		Pattern numericalPart = Pattern.compile(".*?(\\d+)\\s*"+ units + "?");
//
//		double[] aMinMax = new double[2];
//		if(stateA.contains("-")) {
//			String[] parts = stateA.split("-");
//			if(parts.length == 2) {
//				for(int i=0; i<2; i++) {
//					String part = parts[i].trim();
//					Matcher matcher = numericalPart.matcher(part);
//					if(matcher.matches()) {
//						aMinMax[i] = Double.valueOf(matcher.group(1));
//					} else {
//						return 0;
//					}
//				}
//			} else {
//				return 0;
//			}
//		} 
//
//		double[] bMinMax = new double[2];
//		if(stateB.contains("-")) {
//			String[] parts = stateB.split("-");
//			if(parts.length == 2) {
//				for(int i=0; i<2; i++) {
//					String part = parts[i].trim();
//					Matcher matcher = numericalPart.matcher(part);
//					if(matcher.matches()) {
//						bMinMax[i] = Double.valueOf(matcher.group(1));
//					} else {
//						return 0;
//					}
//				}
//			} else {
//				return 0;
//			}
//		} 
//		
//		
//		double commonStart = Math.max(aMinMax[0], bMinMax[0]);
//		double commonEnd = Math.min(aMinMax[1], bMinMax[1]);
//		if (commonEnd < commonStart)
//			return 0;
//		return (commonEnd - commonStart) / (aMinMax[1]-aMinMax[0]);
//	}
//
//	private boolean isNumeric(String state) {
//		Pattern pattern = Pattern.compile(".*?\\d+\\s*"+ units + "?");
//		boolean numeric = true;
//		if(state.contains("-")) {
//			String[] parts = state.split("-");
//			for(String part : parts) {
//				part = part.trim();
//				numeric &= pattern.matcher(part).matches();
//			}
//		} else {
//			numeric &= state.matches(".*?\\d+\\s*"+ units + "?");
//		}
//		return numeric;
//	}
//
//	private boolean isState(String token) {
//		Set<String> categories = glossary.getCategories(token);
//		categories.remove("substance");
//		categories.remove("structure_subtype");
//		categories.remove("structure_in_adjective_form");
//		categories.remove("structure");
//		categories.remove("taxon_name");
//		categories.remove("season");
//		return !categories.isEmpty();
//	}
}