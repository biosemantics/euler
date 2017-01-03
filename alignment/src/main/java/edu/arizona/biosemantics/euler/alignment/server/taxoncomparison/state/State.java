package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state;

public interface State {
	
	public enum Type {
		CATEGORICAL, DISCRETE_NUMERICAL_RANGE, NUMERICAL_RANGE
	}
	
	public Type getType();

}
