package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dev.util.collect.Lists;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.CharacterOverlapCalculator;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.CharacterStateSimilarityMetric;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.TaxonSimilarityCalculator;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.AsymmetricSimilarity;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterOverlap;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterState;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.Overlap;

public class MyOverlapCalculator implements CharacterOverlapCalculator, TaxonSimilarityCalculator {

	private CharacterStateSimilarityMetric characterStateSimilarityMetric;

	public MyOverlapCalculator(CharacterStateSimilarityMetric characterStateSimilarityMetric) {
		this.characterStateSimilarityMetric = characterStateSimilarityMetric;
	}
	
	@Override
	public AsymmetricSimilarity<Taxon> getTaxonSimilarity(Taxon taxonA, Taxon taxonB) {
		Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>> betweenTaxaMap = 
				this.getCharacterSimilarity(taxonA, taxonB);
		Collection<CharacterState> characterStatesA = taxonA.getCharacterStates();
		Collection<CharacterState> characterStatesB = taxonB.getCharacterStates();
		
		//get the aggregated score
		//1. simplest: 
		double sumSimilarity = 0;
		double sumOppositeSimilarity = 0;
		//in each row, find the max sim
		//sum up all max sim from all rows
		//div the sum by row number = sim
		for(CharacterState characterStateA : characterStatesA) {
			double maxSimilarity = 0;
			for(CharacterState characterStateB : characterStatesB) {
				double similarity = betweenTaxaMap.get(characterStateA).get(characterStateB).getSimilarity();
				if(similarity > maxSimilarity) 
					maxSimilarity = similarity;
			}
			sumSimilarity += maxSimilarity;
		}
		double similarity = sumSimilarity / characterStatesA.size();
		
		//in each column, find the max sim
		//sum up all max sim from all columns
		//div the sum by column number = oppSim
		for(CharacterState characterStateB : characterStatesB) {
			double maxOppositeSimilarity = 0;
			for(CharacterState characterStateA : characterStatesA) {
				double oppositeSimilarity = betweenTaxaMap.get(characterStateA).get(characterStateB).getOppositeSimilarity();
				if(oppositeSimilarity > maxOppositeSimilarity) 
					maxOppositeSimilarity = oppositeSimilarity;
			}
			sumOppositeSimilarity += maxOppositeSimilarity;
		}
		double oppositeSimilarity = sumOppositeSimilarity / characterStatesB.size();
		return new AsymmetricSimilarity<Taxon>(taxonA, taxonB, similarity, oppositeSimilarity);
	}

	@Override
	public CharacterOverlap getCharacterOverlap(Taxon taxonA, Taxon taxonB, double threshold) {
		Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>> betweenTaxaMap = this.getCharacterSimilarity(taxonA, taxonB);
		
		List<CharacterState> characterStatesA = new ArrayList<CharacterState>(taxonA.getCharacterStates());
		List<CharacterState> characterStatesB = new ArrayList<CharacterState>(taxonB.getCharacterStates());
		Set<CharacterState> remainingTaxonACharacterStates = new HashSet<CharacterState>(taxonA.getCharacterStates());
		Set<CharacterState> remainingTaxonBCharacterStates = new HashSet<CharacterState>(taxonB.getCharacterStates());
		List<Overlap> overlap = new LinkedList<Overlap>();

		if(!characterStatesA.isEmpty() && !characterStatesB.isEmpty()) {
			double[][] costMatrix = new double[characterStatesA.size()][characterStatesB.size()];
			for(int i=0; i<characterStatesA.size(); i++) {
				CharacterState characterStateA = characterStatesA.get(i);
				for(int j=0; j<characterStatesB.size(); j++) {
					CharacterState characterStateB = characterStatesB.get(j);
					AsymmetricSimilarity<CharacterState> asymmetricSimilarity = betweenTaxaMap.get(characterStateA).get(characterStateB);
					costMatrix[i][j] = 1 - asymmetricSimilarity.getAverageSimilarity();
				}
			}
			HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm(costMatrix);
			int[] assignments = hungarianAlgorithm.execute();

			List<AsymmetricSimilarity<CharacterState>> maximumSimilarityPairs = new LinkedList<AsymmetricSimilarity<CharacterState>>();
			for(int i=0; i < assignments.length; i++) {
				CharacterState characterStateA = characterStatesA.get(i);
				if(assignments[i] != -1) {
					CharacterState characterStateB = characterStatesB.get(assignments[i]);
					maximumSimilarityPairs.add(betweenTaxaMap.get(characterStateA).get(characterStateB));
				}
			}
			
			for(AsymmetricSimilarity<CharacterState> similarity : maximumSimilarityPairs) {
				if(similarity.getAverageSimilarity() >= threshold) {
					overlap.add(new Overlap(similarity.getItemA(), similarity.getItemB(), similarity.getAverageSimilarity(), 
							DiagnosticValue.MEDIUM));
					remainingTaxonACharacterStates.remove(similarity.getItemA());;
					remainingTaxonBCharacterStates.remove(similarity.getItemB());
				}
			}
		}

		CharacterOverlap characterOverlap = new CharacterOverlap();
		characterOverlap.setOverlap(overlap);
		characterStatesA = new ArrayList<CharacterState>(remainingTaxonACharacterStates);
		characterStatesB = new ArrayList<CharacterState>(remainingTaxonBCharacterStates);
		//fails on server due to diff. JVM version?
		//Lists.sort(characterStatesA);
		//Lists.sort(characterStatesB);
		characterOverlap.setCharacterStatesA(characterStatesA);
		characterOverlap.setCharacterStatesB(characterStatesB);
		characterOverlap.setTaxonA(taxonA);
		characterOverlap.setTaxonB(taxonB);
		return characterOverlap;
	}
	
	private Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>> getCharacterSimilarity(Taxon taxonA, Taxon taxonB) {
		Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>> betweenTaxaMap = 
				new HashMap<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>>();
		List<CharacterState> characterStatesA = new ArrayList<CharacterState>(taxonA.getCharacterStates());
		List<CharacterState> characterStatesB = new ArrayList<CharacterState>(taxonB.getCharacterStates());
		
		for(int i=0; i<characterStatesA.size(); i++) {
			CharacterState characterStateA = characterStatesA.get(i);
			betweenTaxaMap.put(characterStateA, new HashMap<CharacterState, AsymmetricSimilarity<CharacterState>>());
			for(int j=0; j<characterStatesB.size(); j++) {
				CharacterState characterStateB = characterStatesB.get(j);
				AsymmetricSimilarity<CharacterState> asymmetricSimilarity = characterStateSimilarityMetric.get(characterStateA, characterStateB);
				betweenTaxaMap.get(characterStateA).put(characterStateB, asymmetricSimilarity);
				log(LogLevel.DEBUG, characterStateA + " vs " + characterStateB + " = " + 
						asymmetricSimilarity.getSimilarity() + " " + asymmetricSimilarity.getOppositeSimilarity());
			}
		}
		return betweenTaxaMap;
	}

}
