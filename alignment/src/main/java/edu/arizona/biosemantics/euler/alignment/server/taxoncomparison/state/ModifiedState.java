package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state;

public class ModifiedState implements State {

	private String modifier;
	private State state;
	
	public ModifiedState(String modifier, State state) {
		this.modifier = modifier;
		this.state = state;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	@Override
	public Type getType() {
		return state.getType();
	}

	public State getRawState() {
		if (!(state instanceof ModifiedState))
			return state;
		else 
			return ((ModifiedState)state).getRawState();
	}
}
