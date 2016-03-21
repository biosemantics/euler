package edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;

public class CharacterOverlap implements Serializable {

	private Taxon taxonA;
	private Taxon taxonB;
	private List<CharacterState> characterStatesA;
	private List<CharacterState> characterStatesB;
	private List<Overlap> overlap;
		
	public CharacterOverlap() { }
	
	public CharacterOverlap(
			Taxon taxonA,
			Taxon taxonB,
			List<CharacterState> characterStatesA,
			List<CharacterState> characterStatesB,
			List<Overlap> overlap) {
		super();
		this.taxonA = taxonA;
		this.taxonB = taxonB;
		this.characterStatesA = characterStatesA;
		this.characterStatesB = characterStatesB;
		this.overlap = overlap;
	}
	
	
	
	public Taxon getTaxonA() {
		return taxonA;
	}
	public void setTaxonA(Taxon taxonA) {
		this.taxonA = taxonA;
	}
	public Taxon getTaxonB() {
		return taxonB;
	}
	public void setTaxonB(Taxon taxonB) {
		this.taxonB = taxonB;
	}
	public List<CharacterState> getCharacterStatesA() {
		return characterStatesA;
	}
	public void setCharacterStatesA(List<CharacterState> characterStatesA) {
		this.characterStatesA = characterStatesA;
	}
	public List<CharacterState> getCharacterStatesB() {
		return characterStatesB;
	}
	public void setCharacterStatesB(List<CharacterState> characterStatesB) {
		this.characterStatesB = characterStatesB;
	}
	public List<Overlap> getOverlap() {
		return overlap;
	}
	public void setOverlap(List<Overlap> overlap) {
		this.overlap = overlap;
	}	
	
	
}
