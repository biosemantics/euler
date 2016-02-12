package edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison;

import java.io.Serializable;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;

public class CharacterState implements Serializable, Comparable<CharacterState> {

	private String organ;
	private String character;
	private String state;
		
	public CharacterState() {
		
	}
	
	public CharacterState(String organ, String character, String state) {
		super();
		this.organ = organ;
		this.character = character;
		this.state = state;
	}

	public void setOrgan(String organ) {
		this.organ = organ;
	}

	public void setCharacter(String character) {
		this.character = character;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOrgan() {
		return organ;
	}

	public String getCharacter() {
		return character;
	}

	public String getState() {
		return state;
	}

	@Override
	public String toString() {
		return organ + " " + character + " " + state;
	}

	@Override
	public int compareTo(CharacterState o) {
		return this.toString().compareTo(o.toString());
	}	
}
