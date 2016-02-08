package edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import edu.arizona.biosemantics.euler.alignment.shared.model.Taxon;

public class Similarities implements Serializable {

	private Taxon taxonA;
	private Taxon taxonB;
	private Collection<CharacterState> characterStatesA;
	private Collection<CharacterState> characterStatesB;
	private Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>> equalCharacters;
		
	public Similarities() { }
	
	public Similarities(
			Taxon taxonA,
			Taxon taxonB,
			Collection<CharacterState> characterStatesA,
			Collection<CharacterState> characterStatesB,
			Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>> equalCharacters) {
		super();
		this.taxonA = taxonA;
		this.taxonB = taxonB;
		this.characterStatesA = characterStatesA;
		this.characterStatesB = characterStatesB;
		this.equalCharacters = equalCharacters;
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
	public Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>> getEqualCharacters() {
		return equalCharacters;
	}
	public void setEqualCharacters(
			Map<CharacterState, Map<CharacterState, AsymmetricSimilarity<CharacterState>>> equalCharacters) {
		this.equalCharacters = equalCharacters;
	}
	
	
	
}
