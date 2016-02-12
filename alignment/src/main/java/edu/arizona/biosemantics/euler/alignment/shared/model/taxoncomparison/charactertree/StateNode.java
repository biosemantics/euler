package edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.charactertree;

import edu.arizona.biosemantics.euler.alignment.shared.model.taxoncomparison.CharacterState;

public class StateNode extends Node {

	public CharacterState characterState;

	public StateNode() {  }
	
	public StateNode(String id, String name, CharacterState characterState) {
		super(id, name);
		this.characterState = characterState;
	}
}