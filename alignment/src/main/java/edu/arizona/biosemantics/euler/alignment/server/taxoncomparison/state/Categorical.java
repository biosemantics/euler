package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state;

import edu.arizona.biosemantics.common.log.LogLevel;

public class Categorical implements State {

	private String value;

	public Categorical(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public Type getType() {
		return Type.CATEGORICAL;
	}
	
}
