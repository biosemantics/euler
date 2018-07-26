package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

import edu.arizona.biosemantics.common.ling.know.IGlossary;
import edu.arizona.biosemantics.common.ling.know.lib.InMemoryGlossary;
import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.euler.alignment.server.Configuration;
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

/**
 * new untested code to compute character similarity based on  James/Nico cases.
 * @author hongcui
 *
 */
public class MyCharacterStateSimilarityMetricAndrew extends MyCharacterStateSimilarlityMetric {

	    private KnowsPartOf knowsPartOf;
		private IGlossary glossary;
		private double stateWeight;
		private LikelyStateReader likelyStateReader;
		private StateSimilarity stateSimilarity;
		
		public MyCharacterStateSimilarityMetricAndrew(KnowsPartOf knowsPartOf, IGlossary glossary, 
				LikelyStateReader likelyStateReader, 
				double stateWeight, StateSimilarity stateSimilarity) { 
			super(knowsPartOf, glossary, likelyStateReader, stateWeight, stateSimilarity);
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
			
			CharacterState characterStateA = new CharacterState("leaf", "architecture", "long-petiolate | 3-foliate | stipulate");
			CharacterState characterStateB = new CharacterState("leaf", "architecture", "3-9-foliolate");

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
					if(characterA.equals("quantity")) {
						AsymmetricSimilarity<String> similarity = getStateSimilarityNumeric(stateA, stateB);
						return new AsymmetricSimilarity<CharacterState>(characterStateA, characterStateB, similarity.getSimilarity(), 
								similarity.getOppositeSimilarity());
					}
					else if(characterA.equals("length") || characterA.equals("width") || characterA.equals("depth")) {
						AsymmetricSimilarity<String> similarity = getStateSimilarityMeasurement(stateA, stateB);
						return new AsymmetricSimilarity<CharacterState>(characterStateA, characterStateB, similarity.getSimilarity(), 
								similarity.getOppositeSimilarity());
					}
					else {
						AsymmetricSimilarity<String> similarity = getStateSimilarity(stateA, stateB);
						return new AsymmetricSimilarity<CharacterState>(characterStateA, characterStateB, similarity.getSimilarity(), 
								similarity.getOppositeSimilarity());
					}
					
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
		private AsymmetricSimilarity<String> getStateSimilarityNumeric(String stateA, String stateB) {
			double similarity = getSimilarityNumeric(stateA, stateB);
			double oppositeSimilarity = getSimilarityNumeric(stateB, stateA);
			return new AsymmetricSimilarity<String>(stateA, stateB, similarity, oppositeSimilarity);
		}
		private AsymmetricSimilarity<String> getStateSimilarityMeasurement(String stateA, String stateB) {
			double similarity = getSimilarityMeasurement(stateA, stateB);
			double oppositeSimilarity = getSimilarityMeasurement(stateB, stateA);
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
			String[] stateAs = stateAString.split("\\|-");
			String[] stateBs = stateBString.split("\\|-");
			
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
			
			for(int i = 0; i < stateBs.length; i++) {
				stateBs[i] = stateBs[i].trim();
			}
			
			// trimming states
			for(int i = 0; i < stateAs.length; i++) {
				stateAs[i] = stateAs[i].trim();
			}
			for(int i = 0; i < stateBs.length; i++) {
				stateBs[i] = stateBs[i].trim();
			}
			
			// converting synonyms of "many"
			for(int i = 0; i < stateAs.length; i++) {
				if(stateAs[i].equals("numerous") || stateAs[i].equals("copious") ||  stateAs[i].equals("abundant") ||  stateAs[i].equals("rich") ||  stateAs[i].equals("superfluous") ||  stateAs[i].equals("present")) {
					stateAs[i] = "many";
				}
			}
			for(int i = 0; i < stateBs.length; i++) {
				if(stateBs[i].equals("numerous") || stateBs[i].equals("copious") ||  stateBs[i].equals("abundant") ||  stateBs[i].equals("rich") ||  stateBs[i].equals("superfluous") ||  stateBs[i].equals("present")) {
					stateBs[i] = "many";
				}
			}
			
			// converting synonyms of "few"
			for(int i = 0; i < stateAs.length; i++) {
				if(stateAs[i].equals("fewer") || stateAs[i].equals("lacking") ||  stateAs[i].equals("scanty") ||  stateAs[i].equals("scant") ||  stateAs[i].equals("wanting") ||  stateAs[i].equals("sparse") ||  stateAs[i].equals("scarce") ||  stateAs[i].equals("several") ||  stateAs[i].equals("multiple") ||  stateAs[i].equals("multiples") ||  stateAs[i].equals("present")) {
					stateAs[i] = "few";
				}
			}
			for(int i = 0; i < stateBs.length; i++) {
				if(stateBs[i].equals("fewer") || stateBs[i].equals("lacking") ||  stateBs[i].equals("scanty") ||  stateBs[i].equals("scant") ||  stateBs[i].equals("wanting") ||  stateBs[i].equals("sparse") ||  stateBs[i].equals("scarce") ||  stateBs[i].equals("several") ||  stateBs[i].equals("multiple") ||  stateBs[i].equals("multiples") ||  stateBs[i].equals("present")) {
					stateBs[i] = "few";
				}
			}
			
			// converting single and twin to numbers
			for(int i = 0; i < stateAs.length; i++) {
				if(stateAs[i].equals("single")) {
					stateAs[i] = "1";
				}
			}
			for(int i = 0; i < stateBs.length; i++) {
				if(stateBs[i].equals("single")) {
					stateBs[i] = "1";
				}
			}
			for(int i = 0; i < stateAs.length; i++) {
				if(stateAs[i].equals("twin")) {
					stateAs[i] = "2";
				}
			}
			for(int i = 0; i < stateBs.length; i++) {
				if(stateBs[i].equals("twin")) {
					stateBs[i] = "2";
				}
			}
			
			int equivalents = 0;
			for(int i = 0; i < stateAs.length; i++) {
				for(int j = 0; j < stateBs.length; j++) {
					if(stateAs[i].equals(stateBs[i])) {
						equivalents++;
					}
				}
			}
			if(equivalents == 0) {
				return 0;
			}
			if(Math.max(stateAs.length, stateBs.length) == 1 && equivalents == 1) {
				return 1;
			}
			if(Math.max(stateAs.length, stateBs.length) > 1 && equivalents > 1) {
				return 1;
			}
			if(Math.max(stateAs.length, stateBs.length) > 1 && equivalents == 1) {
				return 0.5;
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
		
		private double getSimilarityNumeric(String stateAString, String stateBString) {
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
			
			// trimming states
			for(int i = 0; i < stateAs.length; i++) {
				stateAs[i] = stateAs[i].trim();
			}
			for(int i = 0; i < stateBs.length; i++) {
				stateBs[i] = stateBs[i].trim();
			}
			
			
			
			boolean stateAIsANumber = true;
			
			for(int i = 0; i < stateAs.length; i++) {
				for(int j = 0; j < stateAs[i].length(); j++) {
					if(!Character.isDigit(stateAs[i].charAt(j)) && stateAs[i].charAt(j) != '-') {
						stateAIsANumber = false;
					}
				}
			}
			
			boolean stateBIsANumber = true;
			
			for(int i = 0; i < stateBs.length; i++) {
				for(int j = 0; j < stateBs[i].length(); j++) {
					if(!Character.isDigit(stateBs[i].charAt(j)) && stateBs[i].charAt(j) != '-') {
						stateBIsANumber = false;
					}
				}
			}
			
			
			if(stateAIsANumber) {
				// converts numbers to "many" or "few"
				for(int i = 0; i < stateAs.length; i++) {
					boolean isJustANumber = true;
					for(int j = 0; j < stateAs[i].length(); j++) {
						if(!Character.isDigit(stateAs[i].charAt(j))) {
							isJustANumber = false;
						}
					}
					if(isJustANumber) {
						int number = Integer.parseInt(stateAs[i]);
						if(number > 10) {
							stateAs[i] = "many";
						}
						if(number < 10) {
							stateAs[i] = "few";
						}
					}
					else {
						int number1 = Integer.parseInt(stateAs[i].substring(0, stateAs[i].indexOf('-')));
						int number2 = Integer.parseInt(stateAs[i].substring(stateAs[i].indexOf('-') + 1, stateAs[i].length()));
						int trueNumber = (number1 + number2) / 2;
						if(trueNumber > 10) {
							stateAs[i] = "many";
						}
						if(trueNumber < 10) {
							stateAs[i] = "few";
						}
					}
				}
			}
			
			if(stateBIsANumber) {
				for(int i = 0; i < stateBs.length; i++) {
					boolean isJustANumber = true;
					for(int j = 0; j < stateBs[i].length(); j++) {
						if(!Character.isDigit(stateBs[i].charAt(j))) {
							isJustANumber = false;
						}
					}
					if(isJustANumber) {
						int number = Integer.parseInt(stateBs[i]);
						if(number > 10) {
							stateBs[i] = "many";
						}
						if(number < 10) {
							stateBs[i] = "few";
						}
					}
					else {
						int number1 = Integer.parseInt(stateBs[i].substring(0, stateBs[i].indexOf('-')));
						int number2 = Integer.parseInt(stateBs[i].substring(stateBs[i].indexOf('-') + 1, stateBs[i].length()));
						int trueNumber = (number1 + number2) / 2;
						if(trueNumber > 10) {
							stateBs[i] = "many";
						}
						if(trueNumber < 10) {
							stateBs[i] = "few";
						}
					}
				}
			}
			
			
			
			// converting synonyms of "many"
			for(int i = 0; i < stateAs.length; i++) {
				if(stateAs[i].equals("numerous") || stateAs[i].equals("copious") ||  stateAs[i].equals("abundant") ||  stateAs[i].equals("rich") ||  stateAs[i].equals("superfluous") ||  stateAs[i].equals("present")) {
					stateAs[i] = "many";
				}
			}
			for(int i = 0; i < stateBs.length; i++) {
				if(stateBs[i].equals("numerous") || stateBs[i].equals("copious") ||  stateBs[i].equals("abundant") ||  stateBs[i].equals("rich") ||  stateBs[i].equals("superfluous") ||  stateBs[i].equals("present")) {
					stateBs[i] = "many";
				}
			}
			
			// converting synonyms of "few"
			for(int i = 0; i < stateAs.length; i++) {
				if(stateAs[i].equals("fewer") || stateAs[i].equals("lacking") ||  stateAs[i].equals("scanty") ||  stateAs[i].equals("scant") ||  stateAs[i].equals("wanting") ||  stateAs[i].equals("sparse") ||  stateAs[i].equals("scarce") ||  stateAs[i].equals("several") ||  stateAs[i].equals("multiple") ||  stateAs[i].equals("multiples") ||  stateAs[i].equals("present")) {
					stateAs[i] = "few";
				}
			}
			for(int i = 0; i < stateBs.length; i++) {
				if(stateBs[i].equals("fewer") || stateBs[i].equals("lacking") ||  stateBs[i].equals("scanty") ||  stateBs[i].equals("scant") ||  stateBs[i].equals("wanting") ||  stateBs[i].equals("sparse") ||  stateBs[i].equals("scarce") ||  stateBs[i].equals("several") ||  stateBs[i].equals("multiple") ||  stateBs[i].equals("multiples") ||  stateBs[i].equals("present")) {
					stateBs[i] = "few";
				}
			}
			
			// converting single and twin to numbers
			for(int i = 0; i < stateAs.length; i++) {
				if(stateAs[i].equals("single")) {
					stateAs[i] = "1";
				}
			}
			for(int i = 0; i < stateBs.length; i++) {
				if(stateBs[i].equals("single")) {
					stateBs[i] = "1";
				}
			}
			for(int i = 0; i < stateAs.length; i++) {
				if(stateAs[i].equals("twin")) {
					stateAs[i] = "2";
				}
			}
			for(int i = 0; i < stateBs.length; i++) {
				if(stateBs[i].equals("twin")) {
					stateBs[i] = "2";
				}
			}
			
			
			
			for(int i = 0; i < stateAs.length; i++) {
				if(stateAs[i].length() > 4 && stateAs[i].indexOf('-') != -1 && stateAs[i].substring(0, stateAs[i].indexOf('-')).equals("few")) {
					stateAs[i] = "few";
				}
				if(stateAs[i].length() > 5 && stateAs[i].indexOf('-') != -1 && stateAs[i].substring(stateAs[i].indexOf('-') + 1, stateAs[i].length()).equals("many")) {
					stateAs[i] = "many";
				}
			}
			for(int i = 0; i < stateBs.length; i++) {
				if(stateBs[i].length() > 4 && stateBs[i].indexOf('-') != -1 && stateBs[i].substring(0, stateBs[i].indexOf('-')).equals("few")) {
					stateBs[i] = "few";
				}
				if(stateBs[i].length() > 5 && stateBs[i].indexOf('-') != -1 && stateBs[i].substring(stateBs[i].indexOf('-') + 1, stateBs[i].length()).equals("many")) {
					stateBs[i] = "many";
				}
			}
			
			
			boolean aHasFrequencyMod = false;
			boolean bHasFrequencyMod = false;
			int aFrequency = 100;
			int bFrequency = 100;
			
			for(int i = 0; i < stateAs.length; i++) {
				if(stateAs[i].length() > 5 && stateAs[i].substring(0, 5).equals("never")) {
					aHasFrequencyMod = true;
					aFrequency = 0;
				}
				else if(stateAs[i].length() > 12 && stateAs[i].substring(0, 12).equals("infrequently") || stateAs[i].length() > 12 && stateAs[i].substring(0, 12).equals("occasionally") || stateAs[i].length() > 6 && stateAs[i].substring(0, 6).equals("seldom") || stateAs[i].length() > 10 && stateAs[i].substring(0, 10).equals("uncommonly") || stateAs[i].length() > 6 && stateAs[i].substring(0, 6).equals("rarely")) {
					aHasFrequencyMod = true;
					aFrequency = 25;
				}
				else if(stateAs[i].length() > 9 && stateAs[i].substring(0, 9).equals("sometimes")) {
					aHasFrequencyMod = true;
					aFrequency = 50;
				}
				else if(stateAs[i].length() > 10 && stateAs[i].substring(0, 10).equals("frequently") || stateAs[i].length() > 5 && stateAs[i].substring(0, 5).equals("often") || stateAs[i].length() > 9 && stateAs[i].substring(0, 9).equals("regularly") || stateAs[i].length() > 7 && stateAs[i].substring(0, 7).equals("usually")) {
					aHasFrequencyMod = true;
					aFrequency = 75;
				}
				else if(stateAs[i].length() > 6 && stateAs[i].substring(0, 6).equals("always") || stateAs[i].length() > 12 && stateAs[i].substring(0, 12).equals("consistently")) {
					aHasFrequencyMod = true;
					aFrequency = 100;
				}
			}
			
			if(aHasFrequencyMod || bHasFrequencyMod) {
				if(aFrequency == bFrequency) {
					return 1.0;
				}
				else if(Math.max(aFrequency, bFrequency) - Math.min(aFrequency, bFrequency) <= 50) {
					return 0.5;
				}
				else {
					return 0.0;
				}
			}
			
			
			// checks for states that are the exact same
			int equivalents = 0;
			for(int i = 0; i < stateAs.length; i++) {
				for(int j = 0; j < stateBs.length; j++) {
					if(stateAs[i].equals(stateBs[j])) {
						equivalents++;
					}
				}
			}
			if(equivalents == 0) {
				return 0;
			}
			if(Math.max(stateAs.length, stateBs.length) == 1 && equivalents == 1) {
				return 1;
			}
			if(Math.max(stateAs.length, stateBs.length) > 1 && equivalents > 1) {
				return 1;
			}
			if(Math.max(stateAs.length, stateBs.length) > 1 && equivalents == 1) {
				return 0.5;
			}
			
			for(int i = 0; i < stateAs.length; i++) {
				for(int j = 0; j < stateBs.length; j++) {
					// check if one is many and the other is few
					if(stateAs[i].equals("many") && stateAs[i].equals("few") || stateAs[i].equals("few") && stateAs[i].equals("many")) {
						return 0.5;
					}
					// check if one is many/few and the other is present (present should be a synonym for both
					if(stateAs[i].equals("many") && stateAs[i].equals("present") || stateAs[i].equals("few") && stateAs[i].equals("present")) {
						return 1;
					}
				}
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
		
		private double getSimilarityMeasurement(String stateAString, String stateBString) {
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
			
			for(int i = 0; i < stateBs.length; i++) {
				stateBs[i] = stateBs[i].trim();
			}
			
			// trimming states
			for(int i = 0; i < stateAs.length; i++) {
				stateAs[i] = stateAs[i].trim();
			}
			for(int i = 0; i < stateBs.length; i++) {
				stateBs[i] = stateBs[i].trim();
			}
			
			
			// compares measurements
			
			double partA1Number = -1.0;
			double partA2Number = -1.0;
			boolean partA1Defined = true;
			boolean partA2Defined = true;
			String partA1Operator = "";
			String partA2Operator = "";
			
			boolean partAHasASecondPart = false;
			
			for(int i = 0; i < stateAs.length; i++) {
				if(stateAs[i].indexOf('-') != -1) {
					partAHasASecondPart = true;
					if(Character.isDigit(stateAs[i].charAt(0))) {
						partA1Defined = true;
						if(stateAs[i].charAt((stateAs[i].indexOf('-')-1)) == 'm') {
							partA1Number = Integer.parseInt(stateAs[i].substring(0, stateAs[i].indexOf('-') - 2));
						}
						else {
							partA1Number = Integer.parseInt(stateAs[i].substring(0, stateAs[i].indexOf('-') - 3));
						}
					}
					else if(stateAs[i].substring(0, 2).equals("<=") || stateAs[i].substring(0, 2).equals(">=")) {
						partA1Defined = false;
						if(stateAs[i].charAt((stateAs[i].indexOf('-')-1)) == 'm') {
							partA1Number = Integer.parseInt(stateAs[i].substring(2, stateAs[i].indexOf('-') - 2));
						}
						else {
							partA1Number = Integer.parseInt(stateAs[i].substring(2, stateAs[i].indexOf('-') - 3));
						}
						partA1Operator = stateAs[i].substring(0, 2);
					}
					else if(stateAs[i].substring(0, 1).equals("<") || stateAs[i].substring(0, 1).equals(">")) {
						partA1Defined = false;
						if(stateAs[i].charAt((stateAs[i].indexOf('-')-1)) == 'm') {
							partA1Number = Integer.parseInt(stateAs[i].substring(1, stateAs[i].indexOf('-') - 2));
						}
						else {
							partA1Number = Integer.parseInt(stateAs[i].substring(1, stateAs[i].indexOf('-') - 3));
						}
						partA1Operator = stateAs[i].substring(0, 1);
					}
				}
				else {
					if(Character.isDigit(stateAs[i].charAt(0))) {
						partA1Defined = true;
						if(stateAs[i].charAt(stateAs[i].length()-1) == 'm') {
							partA1Number = Integer.parseInt(stateAs[i].substring(0, stateAs[i].length() - 2));
						}
						else {
							partA1Number = Integer.parseInt(stateAs[i].substring(0, stateAs[i].length() - 3));
						}
					}
					else if(stateAs[i].substring(0, 2).equals("<=") || stateAs[i].substring(0, 2).equals(">=")) {
						partA1Defined = false;
						if(stateAs[i].charAt(stateAs[i].length()-1) == 'm') {
							partA1Number = Integer.parseInt(stateAs[i].substring(2, stateAs[i].length() - 2));
						}
						else {
							partA1Number = Integer.parseInt(stateAs[i].substring(2, stateAs[i].length() - 3));
						}
						partA1Operator = stateAs[i].substring(0, 2);
					}
					else if(stateAs[i].substring(0, 1).equals("<") || stateAs[i].substring(0, 1).equals(">")) {
						partA1Defined = false;
						if(stateAs[i].charAt(stateAs[i].length()-1) == 'm') {
							partA1Number = Integer.parseInt(stateAs[i].substring(1, stateAs[i].length() - 2));
						}
						else {
							partA1Number = Integer.parseInt(stateAs[i].substring(1, stateAs[i].length() - 3));
						}
						partA1Operator = stateAs[i].substring(0, 1);
					}
				}
			}
			
			if(partAHasASecondPart) {
				for(int i = 0; i < stateAs.length; i++) {
					String temp = stateAs[i].substring(stateAs[i].indexOf('-') + 2, stateAs[i].length());
					if(Character.isDigit(temp.charAt(0))) {
						partA2Defined = true;
						if(temp.charAt(temp.length()-1) == 'm' && Character.isDigit(temp.charAt(temp.length()-2))) {
							partA2Number = Integer.parseInt(temp.substring(0, temp.length()-1));
						}
						else {
							partA2Number = Integer.parseInt(temp.substring(0, temp.length()-2));
						}
					}
				}
			}
			else {
				partA2Defined = false;
			}
			
			
			
			double partARangeLow = -1.0;
			double partARangeHigh = -1.0;
			boolean partARangeDefined = true;
			
			if(partA1Defined && partA2Defined) {
				partARangeLow = partA1Number;
				partARangeHigh = partA2Number;
			}
			else if(partA1Defined) {
				partARangeLow = partA1Number;
				if(partA2Operator.equals("<")) {
					partARangeHigh = partA2Number;
				}
				else if(partA2Operator.equals(">")) {
					partARangeHigh = -1.0;
				}
				else if(partA2Operator.equals(">=")) {
					partARangeHigh = -1.0;
				}
				else if(partA2Operator.equals("<=")) {
					partARangeHigh = partA2Number;
				}
			}
			else if(partA2Defined) {
				partARangeHigh = partA2Number;
				if(partA1Operator.equals("<")) {
					partARangeLow = 0;
				}
				else if(partA1Operator.equals(">")) {
					partARangeLow = partA1Number;
				}
				else if(partA1Operator.equals(">=")) {
					partARangeLow = partA1Number;
				}
				else if(partA1Operator.equals("<=")) {
					partARangeLow = 0;
				}
			}
			else {
				if(partA1Operator.equals("<")) {
					partARangeLow = 0;
				}
				else if(partA1Operator.equals(">")) {
					partARangeLow = partA1Number;
				}
				else if(partA1Operator.equals(">=")) {
					partARangeLow = partA1Number;
				}
				else if(partA1Operator.equals("<=")) {
					partARangeLow = 0;
				}
				
				if(partA2Operator.equals("<")) {
					partARangeHigh = partA2Number;
				}
				else if(partA2Operator.equals(">")) {
					partARangeHigh = -1.0;
				}
				else if(partA2Operator.equals(">=")) {
					partARangeHigh = -1.0;
				}
				else if(partA2Operator.equals("<=")) {
					partARangeHigh = partA2Number;
				}
			}
			
			if(partARangeHigh == -1.0) {
				partARangeDefined = false;
			}
			
			double partB1Number = -1.0;
			double partB2Number = -1.0;
			boolean partB1Defined = true;
			boolean partB2Defined = true;
			String partB1Operator = "";
			String partB2Operator = "";
			
			boolean partBHasASecondPart = false;
			
			for(int i = 0; i < stateBs.length; i++) {
				if(stateBs[i].indexOf('-') != -1) {
					partBHasASecondPart = true;
					if(Character.isDigit(stateBs[i].charAt(0))) {
						partB1Defined = true;
						if(stateBs[i].charAt((stateBs[i].indexOf('-')-1)) == 'm') {
							partB1Number = Integer.parseInt(stateBs[i].substring(0, stateBs[i].indexOf('-') - 2));
						}
						else {
							partB1Number = Integer.parseInt(stateBs[i].substring(0, stateBs[i].indexOf('-') - 3));
						}
					}
					else if(stateBs[i].substring(0, 2).equals("<=") || stateBs[i].substring(0, 2).equals(">=")) {
						partB1Defined = false;
						if(stateBs[i].charAt((stateBs[i].indexOf('-')-1)) == 'm') {
							partB1Number = Integer.parseInt(stateBs[i].substring(2, stateBs[i].indexOf('-') - 2));
						}
						else {
							partB1Number = Integer.parseInt(stateBs[i].substring(2, stateBs[i].indexOf('-') - 3));
						}
						partB1Operator = stateBs[i].substring(0, 2);
					}
					else if(stateBs[i].substring(0, 1).equals("<") || stateBs[i].substring(0, 1).equals(">")) {
						partB1Defined = false;
						if(stateBs[i].charAt((stateBs[i].indexOf('-')-1)) == 'm') {
							partB1Number = Integer.parseInt(stateBs[i].substring(1, stateBs[i].indexOf('-') - 2));
						}
						else {
							partB1Number = Integer.parseInt(stateBs[i].substring(1, stateBs[i].indexOf('-') - 3));
						}
						partB1Operator = stateBs[i].substring(0, 1);
					}
				}
				else {
					if(Character.isDigit(stateBs[i].charAt(0))) {
						partB1Defined = true;
						if(stateBs[i].charAt(stateBs[i].length()-1) == 'm') {
							partB1Number = Integer.parseInt(stateBs[i].substring(0, stateBs[i].length() - 2));
						}
						else {
							partB1Number = Integer.parseInt(stateBs[i].substring(0, stateBs[i].length() - 3));
						}
					}
					else if(stateBs[i].substring(0, 2).equals("<=") || stateBs[i].substring(0, 2).equals(">=")) {
						partB1Defined = false;
						if(stateBs[i].charAt(stateBs[i].length()-1) == 'm') {
							partB1Number = Integer.parseInt(stateBs[i].substring(2, stateBs[i].length() - 2));
						}
						else {
							partB1Number = Integer.parseInt(stateBs[i].substring(2, stateBs[i].length() - 3));
						}
						partB1Operator = stateBs[i].substring(0, 2);
					}
					else if(stateBs[i].substring(0, 1).equals("<") || stateBs[i].substring(0, 1).equals(">")) {
						partB1Defined = false;
						if(stateBs[i].charAt(stateBs[i].length()-1) == 'm') {
							partB1Number = Integer.parseInt(stateBs[i].substring(1, stateBs[i].length() - 2));
						}
						else {
							partB1Number = Integer.parseInt(stateBs[i].substring(1, stateBs[i].length() - 3));
						}
						partB1Operator = stateBs[i].substring(0, 1);
					}
				}
			}
			
			if(partBHasASecondPart) {
				for(int i = 0; i < stateBs.length; i++) {
					String temp = stateBs[i].substring(stateBs[i].indexOf('-') + 2, stateBs[i].length());
					if(Character.isDigit(temp.charAt(0))) {
						partB2Defined = true;
						if(temp.charAt(temp.length()-1) == 'm' && Character.isDigit(temp.charAt(temp.length()-2))) {
							partB2Number = Integer.parseInt(temp.substring(0, temp.length()-1));
						}
						else {
							partB2Number = Integer.parseInt(temp.substring(0, temp.length()-2));
						}
					}
				}
			}
			else {
				partB2Defined = false;
			}
			
			System.out.println("partA1: " + partA1Number);
			System.out.println("partA1defined " + partA1Defined);
			System.out.println("partA2: " + partA2Number);
			System.out.println("partA2defined " + partA2Defined);
			System.out.println("partB1: " + partB1Number);
			System.out.println("partB1defined " + partB1Defined);
			System.out.println("partB2: " + partB2Number);
			System.out.println("partB2defined " + partB2Defined);
			
			double partBRangeLow = -1.0;
			double partBRangeHigh = -1.0;
			boolean partBRangeDefined = true;
			
			if(partB1Defined && partB2Defined) {
				partBRangeLow = partB1Number;
				partBRangeHigh = partB2Number;
			}
			else if(partB1Defined) {
				partBRangeLow = partB1Number;
				if(partB2Operator.equals("<")) {
					partBRangeHigh = partB2Number;
				}
				else if(partB2Operator.equals(">")) {
					partBRangeHigh = -1.0;
				}
				else if(partB2Operator.equals(">=")) {
					partBRangeHigh = -1.0;
				}
				else if(partB2Operator.equals("<=")) {
					partBRangeHigh = partB2Number;
				}
			}
			else if(partB2Defined) {
				partBRangeHigh = partB2Number;
				if(partB1Operator.equals("<")) {
					partBRangeLow = 0;
				}
				else if(partB1Operator.equals(">")) {
					partBRangeLow = partB1Number;
				}
				else if(partB1Operator.equals(">=")) {
					partBRangeLow = partB1Number;
				}
				else if(partB1Operator.equals("<=")) {
					partBRangeLow = 0;
				}
			}
			else {
				if(partB1Operator.equals("<")) {
					partBRangeLow = 0;
				}
				else if(partB1Operator.equals(">")) {
					partBRangeLow = partB1Number;
				}
				else if(partB1Operator.equals(">=")) {
					partBRangeLow = partB1Number;
				}
				else if(partB1Operator.equals("<=")) {
					partBRangeLow = 0;
				}
				
				if(partB2Operator.equals("<")) {
					partBRangeHigh = partB2Number;
				}
				else if(partB2Operator.equals(">")) {
					partBRangeHigh = -1.0;
				}
				else if(partB2Operator.equals(">=")) {
					partBRangeHigh = -1.0;
				}
				else if(partB2Operator.equals("<=")) {
					partBRangeHigh = partB2Number;
				}
			}
			
			if(partBRangeHigh == -1.0) {
				partBRangeDefined = false;
			}
			
			System.out.println("partARangeLow: " + partARangeLow);
			System.out.println("partARangeHigh: " + partARangeHigh);
			System.out.println("partBRangeLow: " + partBRangeLow);
			System.out.println("partBRangeHigh: " + partBRangeHigh);
			
			if(partARangeDefined && partBRangeDefined) {
				if(partARangeHigh >= partBRangeHigh && partARangeLow <= partBRangeLow || partBRangeHigh >= partARangeHigh && partBRangeLow <= partARangeLow) {
					return 1.0;
				}
				else if(partARangeHigh < partBRangeLow || partBRangeHigh < partARangeLow) {
					return 0.0;
				}
				else if(partARangeHigh > partBRangeHigh) {
					double totalRange = partARangeHigh - partBRangeLow;
					double rangeDiff = (partARangeHigh - partARangeLow) - (partBRangeHigh - partARangeLow);
					if(rangeDiff <= totalRange/2.0) {
						return 1.0;
					}
					else {
						return 0.5;
					}
				}
				else if(partARangeHigh < partBRangeHigh) {
					double totalRange = partBRangeHigh - partARangeLow;
					double rangeDiff = (partBRangeHigh - partBRangeLow) - (partARangeHigh - partBRangeLow);
					if(rangeDiff <= totalRange/2.0) {
						return 1.0;
					}
					else {
						return 0.5;
					}
				}
			}
			else if(partARangeDefined) {
				if(partBRangeLow <= partARangeLow) {
					return 1.0;
				}
				else if(partARangeHigh < partBRangeHigh) {
					return 0.0;
				}
				else if(partARangeHigh > partBRangeHigh) {
					double midRange = partARangeHigh - partARangeLow;
					if(partBRangeLow <= midRange) {
						return 1.0;
					}
					else {
						return 0.5;
					}
				}
			}
			else if(partBRangeDefined) {
				if(partARangeLow <= partBRangeLow) {
					return 1.0;
				}
				else if(partBRangeHigh < partARangeLow) {
					return 0.0;
				}
				else if(partBRangeHigh > partARangeLow) {
					double midRange = partBRangeHigh - partBRangeLow;
					if(partARangeLow <= midRange) {
						return 1.0;
					}
					else {
						return 0.5;
					}
				}
			}	
			else {
				if(partARangeLow == -1.0 && partBRangeLow == -1.0)
					return 1.0;
				if(partARangeHigh == -1.0 && partBRangeHigh == -1.0)
					return 1.0;
				else {
					return 0.5;
				}
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
//		//for now assume units are always the same
//		private double getNumericalStateSimilarity(String stateA, String stateB) {
//			Pattern numericalPart = Pattern.compile(".*?(\\d+)\\s*"+ units + "?");
	//
//			double[] aMinMax = new double[2];
//			if(stateA.contains("-")) {
//				String[] parts = stateA.split("-");
//				if(parts.length == 2) {
//					for(int i=0; i<2; i++) {
//						String part = parts[i].trim();
//						Matcher matcher = numericalPart.matcher(part);
//						if(matcher.matches()) {
//							aMinMax[i] = Double.valueOf(matcher.group(1));
//						} else {
//							return 0;
//						}
//					}
//				} else {
//					return 0;
//				}
//			} 
	//
//			double[] bMinMax = new double[2];
//			if(stateB.contains("-")) {
//				String[] parts = stateB.split("-");
//				if(parts.length == 2) {
//					for(int i=0; i<2; i++) {
//						String part = parts[i].trim();
//						Matcher matcher = numericalPart.matcher(part);
//						if(matcher.matches()) {
//							bMinMax[i] = Double.valueOf(matcher.group(1));
//						} else {
//							return 0;
//						}
//					}
//				} else {
//					return 0;
//				}
//			} 
//			
//			
//			double commonStart = Math.max(aMinMax[0], bMinMax[0]);
//			double commonEnd = Math.min(aMinMax[1], bMinMax[1]);
//			if (commonEnd < commonStart)
//				return 0;
//			return (commonEnd - commonStart) / (aMinMax[1]-aMinMax[0]);
//		}
	//
//		private boolean isNumeric(String state) {
//			Pattern pattern = Pattern.compile(".*?\\d+\\s*"+ units + "?");
//			boolean numeric = true;
//			if(state.contains("-")) {
//				String[] parts = state.split("-");
//				for(String part : parts) {
//					part = part.trim();
//					numeric &= pattern.matcher(part).matches();
//				}
//			} else {
//				numeric &= state.matches(".*?\\d+\\s*"+ units + "?");
//			}
//			return numeric;
//		}
	//
//		private boolean isState(String token) {
//			Set<String> categories = glossary.getCategories(token);
//			categories.remove("substance");
//			categories.remove("structure_subtype");
//			categories.remove("structure_in_adjective_form");
//			categories.remove("structure");
//			categories.remove("taxon_name");
//			categories.remove("season");
//			return !categories.isEmpty();
//		}
	}

