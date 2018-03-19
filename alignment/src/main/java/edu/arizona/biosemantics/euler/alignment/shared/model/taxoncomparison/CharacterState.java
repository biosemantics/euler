package edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison;

import java.io.Serializable;
import java.util.Comparator;

import edu.arizona.biosemantics.common.taxonomy.Rank;
import edu.arizona.biosemantics.euler.alignment.shared.model.DiagnosticValue;
import edu.arizona.biosemantics.matrixreview.shared.model.core.Value;

public class CharacterState implements Serializable, Comparable<CharacterState>, Comparator<CharacterState> {

	private String organ;
	//character attributes
	private String character; //"name" attribute
	private Value state; //"value" attribute


	public CharacterState() {
		
	}
	
	//based on schema at https://github.com/biosemantics/schemas/blob/master/semanticMarkupOutput.xsd
	public CharacterState(String organ, String character, Value state) {
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

	public void setState(Value state) {
		this.state = state;
	}

	public String getOrgan() {
		return organ;
	}

	public String getCharacter() {
		return character;
	}

	public Value getState() {
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
	
	@Override
	public int compare(CharacterState a, CharacterState b) {
		return a.toString().compareTo(b.toString());
	}	
}
