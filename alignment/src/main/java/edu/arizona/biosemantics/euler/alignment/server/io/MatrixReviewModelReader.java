package edu.arizona.biosemantics.euler.alignment.server.io;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.arizona.biosemantics.common.log.LogLevel;
import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.common.taxonomy.RankData;
import edu.arizona.biosemantics.common.taxonomy.TaxonIdentification;
import edu.arizona.biosemantics.euler.alignment.shared.model.Taxonomy;
import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterState;
import edu.arizona.biosemantics.matrixreview.shared.model.Model;
import edu.arizona.biosemantics.matrixreview.shared.model.core.Character;
import edu.arizona.biosemantics.matrixreview.shared.model.core.Organ;
import edu.arizona.biosemantics.matrixreview.shared.model.core.Taxon;
import edu.arizona.biosemantics.matrixreview.shared.model.core.TaxonMatrix;
import edu.arizona.biosemantics.matrixreview.shared.model.core.Value;

public class MatrixReviewModelReader {
	
	public Taxonomy getTaxonomy(Model model) throws Exception {
		TaxonMatrix matrix = model.getTaxonMatrix();
		
		/*Taxon reviewRootTaxon = null;
		if(matrix.getHierarchyRootTaxa().size() == 0) {
			throw new Exception("Empty taxonomy cannot be used as input.");
		}
		if(matrix.getHierarchyRootTaxa().size() == 1) {
			reviewRootTaxon = matrix.getHierarchyRootTaxa().get(0);
		}
		if(matrix.getHierarchyRootTaxa().size() > 1) {
			reviewRootTaxon = new Taxon();
			reviewRootTaxon.setDescription("");
			LinkedList<RankData> rankData = new LinkedList<RankData>();
			rankData.add(new RankData(Rank.LIFE, "Root", null, "NA", String.valueOf(Calendar.getInstance().get(Calendar.YEAR))));
			TaxonIdentification taxonIdentification = new TaxonIdentification(rankData, "NA", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
			reviewRootTaxon.setTaxonIdentification(taxonIdentification);
			reviewRootTaxon.setChildren(matrix.getHierarchyRootTaxa());
		}*/	
		
		//edu.arizona.biosemantics.euler.alignment.shared.model.Taxon rootTaxon = createCharacterizedTaxon(reviewRootTaxon, model);
		List<edu.arizona.biosemantics.euler.alignment.shared.model.Taxon> rootTaxa = new LinkedList<edu.arizona.biosemantics.euler.alignment.shared.model.Taxon>();
		
		for(Taxon taxon : matrix.getHierarchyRootTaxa()) {
			edu.arizona.biosemantics.euler.alignment.shared.model.Taxon alignmentTaxon = createCharacterizedTaxon(taxon, model);
			rootTaxa.add(alignmentTaxon);
			addChildren(alignmentTaxon, taxon, model);
		}
				
		return new Taxonomy("", "", "", rootTaxa);
	}

	private void addChildren(edu.arizona.biosemantics.euler.alignment.shared.model.Taxon parentTaxon, Taxon taxon, Model model) throws Exception {
		for(Taxon child : taxon.getChildren()) {
			edu.arizona.biosemantics.euler.alignment.shared.model.Taxon childTaxon = createCharacterizedTaxon(child, model);
			parentTaxon.addChild(childTaxon);
			addChildren(childTaxon, child, model);
		}
	}

	private edu.arizona.biosemantics.euler.alignment.shared.model.Taxon createCharacterizedTaxon(Taxon taxon, Model model) throws Exception {		
		String author = taxon.getTaxonIdentification().getRankData().getLast().getAuthor();
		String year = taxon.getTaxonIdentification().getRankData().getLast().getDate();
		if(author == null || author.isEmpty() || year == null || year.isEmpty()) {
			throw new Exception("Taxon concept required as input. All taxa are required to have author and year");
		}
		
		edu.arizona.biosemantics.euler.alignment.shared.model.Taxon newTaxon = new edu.arizona.biosemantics.euler.alignment.shared.model.Taxon(taxon.getTaxonIdentification(), taxon.getDescription());
		newTaxon.setCharacterStates(createCharacters(taxon, model));
		return newTaxon;
	}

	private List<CharacterState> createCharacters(Taxon taxon, Model model) {
		List<CharacterState> characterStates = new LinkedList<CharacterState>();
		List<Organ> charactersByOrgan = model.getTaxonMatrix().getHierarchyCharacters();
		for(Organ organ : charactersByOrgan) {
			Set<Character> characters = organ.getCharacters();//[shape of calyx]
			for(Character character : characters){
				Value state = model.getTaxonMatrix().getValue(taxon, character);
				if(state != null && !state.getValue().isEmpty()){ //5(3-7)-parted
					CharacterState characterState = new CharacterState(organ.getName(), character.getName(), state.getValue());
					characterStates.add(characterState);
				} else {
					log(LogLevel.DEBUG, "Empty state.");
				}
			}
		}
		Collections.sort(characterStates);
		return characterStates;
	}

}
