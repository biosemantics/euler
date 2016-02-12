package edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;

public class CharacterOverlap implements Serializable {

	private Taxon taxonA;
	private Taxon taxonB;
	private Collection<CharacterState> characterStatesA;
	private Collection<CharacterState> characterStatesB;
	private List<Overlap> overlap;
		
	public CharacterOverlap() { }
	
	public CharacterOverlap(
			Taxon taxonA,
			Taxon taxonB,
			Collection<CharacterState> characterStatesA,
			Collection<CharacterState> characterStatesB,
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
	public Collection<CharacterState> getCharacterStatesA() {
		return characterStatesA;
	}
	public void setCharacterStatesA(Collection<CharacterState> characterStatesA) {
		this.characterStatesA = characterStatesA;
	}
	public Collection<CharacterState> getCharacterStatesB() {
		return characterStatesB;
	}
	public void setCharacterStatesB(Collection<CharacterState> characterStatesB) {
		this.characterStatesB = characterStatesB;
	}
	public List<Overlap> getOverlap() {
		return overlap;
	}
	public void setOverlap(List<Overlap> overlap) {
		this.overlap = overlap;
	}	
	
	
}
