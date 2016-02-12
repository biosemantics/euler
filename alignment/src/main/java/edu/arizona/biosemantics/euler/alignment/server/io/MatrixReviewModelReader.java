package edu.arizona.biosemantics.euler.alignment.server.io;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.arizona.biosemantics.common.log.LogLevel;
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

	public Taxonomy getTaxonomy(Model model) {
		List<edu.arizona.biosemantics.euler.alignment.shared.model.Taxon> rootTaxa = new LinkedList<edu.arizona.biosemantics.euler.alignment.shared.model.Taxon>();
		TaxonMatrix matrix = model.getTaxonMatrix();
		
		for(Taxon taxon : matrix.getHierarchyRootTaxa()) {
			edu.arizona.biosemantics.euler.alignment.shared.model.Taxon rootTaxon = createCharacterizedTaxon(taxon, model);
			rootTaxa.add(rootTaxon);
			addChildren(rootTaxon, taxon, model);
		}		
		return new Taxonomy(rootTaxa.get(0).getYear(), rootTaxa.get(0).getName(), rootTaxa);
	}

	private void addChildren(edu.arizona.biosemantics.euler.alignment.shared.model.Taxon rootTaxon, Taxon taxon, Model model) {
		for(Taxon child : taxon.getChildren()) {
			edu.arizona.biosemantics.euler.alignment.shared.model.Taxon childTaxon = createCharacterizedTaxon(child, model);
			rootTaxon.addChild(childTaxon);
			addChildren(childTaxon, child, model);
		}
	}

	private edu.arizona.biosemantics.euler.alignment.shared.model.Taxon createCharacterizedTaxon(Taxon taxon, Model model) {		
		edu.arizona.biosemantics.euler.alignment.shared.model.Taxon newTaxon = new edu.arizona.biosemantics.euler.alignment.shared.model.Taxon(taxon.getTaxonIdentification(), taxon.getDescription());
		newTaxon.setCharacterStates(createCharacters(taxon, model));
		return newTaxon;
	}

	private Collection<CharacterState> createCharacters(Taxon taxon, Model model) {
		Collection<CharacterState> characterStates = new LinkedList<CharacterState>();
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
		return characterStates;
	}

}
