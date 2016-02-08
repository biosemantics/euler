package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.lib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.stat.inference.TestUtils;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.CharacterStateSimilarityMetric;
import edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.RelationGenerator;
import edu.arizona.biosemantics.euler.alignment.shared.model.Articulation;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;
import edu.arizona.biosemantics.euler.alignment.shared.model.Evidence;
import edu.arizona.biosemantics.euler.alignment.shared.model.Relation;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.AsymmetricSimilarity;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterState;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.RelationProposal;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.Similarities;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.Taxonomy;

public class TTestBasedRelationGenerator implements RelationGenerator {
	
	private double disjointSimilarityMax;
	private double symmmetricDifferenceMax;
	private double congruenceSimilarityMin;
	private double inclusionSimilarityMin;
	private double asymmetricDifferenceMin;
	
	private CharacterStateSimilarityMetric characterStateSimilarityMetric;
	private Map<Taxon, Map<Taxon, AsymmetricSimilarity<Taxon>>> betweenTaxonomyScores;
	private Map<Taxon, Map<Taxon, Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>>>> betweenCharacterScores;
	private Taxonomy taxonomyA;
	private Taxonomy taxonomyB;
	private double[] similarities;
	private double[] oppositeSimilarities;
	private double[] differencesSimilarities;
	
	public TTestBasedRelationGenerator(@Named("DisjointSimMax") double disjointSimilarityMax,
			@Named("SymmmetricDiffMax") double symmmetricDifferenceMax,
			@Named("CongruenceSimilarityMin") double congruenceSimilarityMin,
			@Named("InclusionSimilarityMin") double inclusionSimilarityMin,
			@Named("AsymmetricDifferenceMin") double asymmetricDifferenceMin,
			CharacterStateSimilarityMetric characterStateSimilarityMetric, 
			Taxonomy taxonomyA, Taxonomy taxonomyB) {
		this.disjointSimilarityMax = disjointSimilarityMax;
		this.symmmetricDifferenceMax = symmmetricDifferenceMax;
		this.congruenceSimilarityMin = congruenceSimilarityMin;
		this.inclusionSimilarityMin = inclusionSimilarityMin;
		this.asymmetricDifferenceMin = asymmetricDifferenceMin;
		
		this.characterStateSimilarityMetric = characterStateSimilarityMetric;
		this.taxonomyA = taxonomyA;
		this.taxonomyB = taxonomyB;
		initializeBetweenTaxonomyScores(taxonomyA, taxonomyB);
	}

	/*wrong idea, making all taxon in one taxonomy the same
	//union the characters of the concept and all its lower concepts
	HashSet<OrganCharacterState> refCharacters = new HashSet<OrganCharacterState>();
	unionCharacters(refCharacters, reference);
	
	HashSet<OrganCharacterState> compCharacters = new HashSet<OrganCharacterState>();
	unionCharacters(compCharacters, comparison);
	*/
	private void initializeBetweenTaxonomyScores(Taxonomy taxonomyA, Taxonomy taxonomyB) {
		log(LogLevel.DEBUG, "initialize between taxonomy scores");
		betweenTaxonomyScores = new HashMap<Taxon, Map<Taxon, AsymmetricSimilarity<Taxon>>>();
		betweenCharacterScores = new HashMap<Taxon, Map<Taxon, Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>>>>();
		
		for(Taxon taxonA : taxonomyA.getTaxaDFS()) {
			betweenTaxonomyScores.put(taxonA, new HashMap<Taxon, AsymmetricSimilarity<Taxon>>());
			betweenCharacterScores.put(taxonA, new HashMap<Taxon, Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>>>());
			for(Taxon taxonB : taxonomyB.getTaxaDFS()) {
				log(LogLevel.DEBUG, "comparing "+ taxonA.getTaxonIdentification().getDisplayName() + " to "+ taxonB.getTaxonIdentification().getDisplayName());
				
				Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>> betweenTaxaMap = 
						new HashMap<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>>();
				Collection<CharacterState> characterStatesA = taxonA.getCharacterStates();
				Collection<CharacterState> characterStatesB = taxonB.getCharacterStates();
				
				//compare two sets of characters
				//create a m*n matrix to save the character-wise comparison
				for(CharacterState characterStateA : characterStatesA) {
					betweenTaxaMap.put(characterStateA, new HashMap<CharacterState, AsymmetricSimilarity<CharacterState>>());
					for(CharacterState characterStateB : characterStatesB) {
						AsymmetricSimilarity<CharacterState> asymmetricSimilarity = characterStateSimilarityMetric.get(characterStateA, characterStateB);
						betweenTaxaMap.get(characterStateA).put(characterStateB, asymmetricSimilarity);
						log(LogLevel.DEBUG, characterStateA + " vs " + characterStateB + " = " + 
								asymmetricSimilarity.getSimilarity() + " " + asymmetricSimilarity.getOppositeSimilarity());
					}
				}
				
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
				
				betweenTaxonomyScores.get(taxonA).put(taxonB, new AsymmetricSimilarity<Taxon>(taxonA, taxonB, 
						similarity, oppositeSimilarity));
				betweenCharacterScores.get(taxonA).put(taxonB, betweenTaxaMap);
			}
		}
		
		int comparisonSize = taxonomyA.getTaxaDFS().size() * taxonomyB.getTaxaDFS().size();
		similarities = new double[comparisonSize];
		oppositeSimilarities = new double[comparisonSize];
		differencesSimilarities = new double[comparisonSize];
		int i = 0;
		for(Taxon TaxonA : taxonomyA.getTaxaDFS()) {
			for(Taxon TaxonB : taxonomyB.getTaxaDFS()) {
				similarities[i] = betweenTaxonomyScores.get(TaxonA).get(TaxonB).getSimilarity();
				oppositeSimilarities[i] = betweenTaxonomyScores.get(TaxonA).get(TaxonB).getOppositeSimilarity();
				differencesSimilarities[i] = Math.abs(similarities[i] -  oppositeSimilarities[i]);
				i++;
			}
		}
	}
	
	@Override
	public Similarities getSimilarities(Taxon taxonA, Taxon taxonB, double threshold) {
		Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>> betweenTaxaScores = betweenCharacterScores.get(taxonA).get(taxonB);
		Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>> equalCharacters = new HashMap<CharacterState, 
				Map<CharacterState, AsymmetricSimilarity<CharacterState>>>();
		Set<CharacterState> remainingTaxonACharacterStates = new HashSet<CharacterState>(taxonA.getCharacterStates());
		Set<CharacterState> remainingTaxonBCharacterStates = new HashSet<CharacterState>(taxonB.getCharacterStates());
		
		for(CharacterState characterStateA : betweenTaxaScores.keySet()) {
			for(CharacterState characterStateB : betweenTaxaScores.get(characterStateA).keySet()) {
				AsymmetricSimilarity<CharacterState> similiarty = betweenTaxaScores.get(characterStateA).get(characterStateB);
				if((similiarty.getSimilarity() + similiarty.getOppositeSimilarity())/2 > threshold) {
					if(!equalCharacters.containsKey(characterStateA))
						equalCharacters.put(characterStateA, new HashMap<CharacterState, AsymmetricSimilarity<CharacterState>>());
					equalCharacters.get(characterStateA).put(characterStateB, similiarty);
					
					remainingTaxonACharacterStates.remove(characterStateA);
					remainingTaxonBCharacterStates.remove(characterStateB);
				}
			}
		}
		Similarities similarities = new Similarities();
		similarities.setEqualCharacters(equalCharacters);
		similarities.setCharacterStatesA(remainingTaxonACharacterStates);
		similarities.setCharacterStatesB(remainingTaxonBCharacterStates);
		similarities.setTaxonA(taxonA);
		similarities.setTaxonB(taxonB);
		return similarities;
	}
	
	@Override
	public AsymmetricSimilarity<Taxon> getAsymmetricSimilarity(Taxon taxonA, Taxon taxonB) {
		return betweenTaxonomyScores.get(taxonA).get(taxonB);
	}

	@Override
	public Set<RelationProposal> generate(Taxon taxonA, Taxon taxonB) {		
		log(LogLevel.DEBUG, "Generate relation proposals for " + taxonA.getTaxonIdentification().getDisplayName() + " " + 
				taxonB.getTaxonIdentification().getDisplayName());
		Set<RelationProposal> result = new HashSet<RelationProposal>();
		
		AsymmetricSimilarity<Taxon> asymmetricSimilarity = getAsymmetricSimilarity(taxonA, taxonB);
		double similarity = asymmetricSimilarity.getSimilarity();
		double oppositeSimilarity = asymmetricSimilarity.getOppositeSimilarity();
		double similarityDifference = Math.abs(similarity - oppositeSimilarity);		
		double simP=Double.NaN, oppSimP=Double.NaN, diffSimP=Double.NaN;
		try{
			simP = TestUtils.tTest(similarity, similarities); 
			oppSimP = TestUtils.tTest(oppositeSimilarity, oppositeSimilarities);
			diffSimP = TestUtils.tTest(similarityDifference, differencesSimilarities);
		}catch(NumberIsTooSmallException e){
			e.printStackTrace();
		}catch(MaxCountExceededException e){
			e.printStackTrace();
		}catch(NullArgumentException e){
			e.printStackTrace();
		}
		
		if(isOverlap(similarity, oppositeSimilarity, similarityDifference)) 
			result.add(new RelationProposal(Relation.OVERLAP, (1 - (simP + diffSimP) / 2), similarity, oppositeSimilarity));
		if(isDisjoint(similarity, oppositeSimilarity, similarityDifference)) 
			result.add(new RelationProposal(Relation.DISJOINT, (1 - (simP + diffSimP) / 2), similarity, oppositeSimilarity));
		if(isCongruent(similarity, oppositeSimilarity, similarityDifference)) 
			result.add(new RelationProposal(Relation.CONGRUENT, (1 - (simP + diffSimP) / 2), similarity, oppositeSimilarity));
		if(isInclusion(similarity, oppositeSimilarity, similarityDifference)) 
			result.add(new RelationProposal(Relation.A_INCLUDES_B, (1 - (simP + diffSimP) / 2), similarity, oppositeSimilarity));

		log(LogLevel.DEBUG, "Similarity = " + similarity);
		log(LogLevel.DEBUG, "OppSimilarity = " + oppositeSimilarity);
		log(LogLevel.DEBUG, "Proposed relations: " + result);
		return result;
	}

	//inclusion: comp included in ref (all characters of ref are characters of comp): lower taxon have bigger character space.
	private boolean isInclusion(double similarity, double oppositeSimilarity, double similarityDifference) {
		return (similarity > inclusionSimilarityMin /*&& oppSim > 0d*/ && similarityDifference > asymmetricDifferenceMin) ||
				(similarity > congruenceSimilarityMin && similarityDifference > symmmetricDifferenceMax);
	}

	private boolean isCongruent(double similarity, double oppositeSimilarity, double similarityDifference) {
		return similarity > congruenceSimilarityMin /*&& oppSim > congruenceSimMin*/ && similarityDifference < symmmetricDifferenceMax;
	}

	private boolean isDisjoint(double similarity, double oppositeSimilarity, double similarityDifference) {
		return similarity < disjointSimilarityMax /*&& oppSim < disjointSimMax*/ && similarityDifference < symmmetricDifferenceMax;
	}
	
	private boolean isOverlap(double similarity, double oppositeSimilarity,	double similarityDifference) {
		return similarity > disjointSimilarityMax  && similarity < congruenceSimilarityMin /*&& oppSim >disjointSimMax*/  && similarityDifference < symmmetricDifferenceMax;
	}
	
	@Override
	public List<Evidence> getEvidence(Taxon taxonA, Taxon taxonB) {
		List<Evidence> evidence = new LinkedList<Evidence>();
		for(CharacterState characterStateA : betweenCharacterScores.get(taxonA).get(taxonB).keySet()) {
			for(CharacterState characterStateB : betweenCharacterScores.get(taxonA).get(taxonB).get(characterStateA).keySet()) {
				AsymmetricSimilarity<CharacterState> similarity = betweenCharacterScores.get(taxonA).get(taxonB).get(characterStateA).get(characterStateB);
				evidence.add(new Evidence(characterStateA.getOrgan() + " " + characterStateA.getCharacter() + " = " + characterStateA.getState(), 
						characterStateB.getOrgan() + " " +  characterStateB.getCharacter() + " = " + characterStateB.getState(), 
						Double.valueOf(similarity.getSimilarity()), -1, DiagnosticValue.MEDIUM, Rank.UNRANKED));
			}
		}
		return evidence;
	}
}
