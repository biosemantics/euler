package edu.arizona.biosemantics.euler.alignment.server.taxoncomparison.state;

import edu.arizona.biosemantics.common.log.LogLevel;

public class DiscreteNumericalRange implements State {

	private int to;
	private int from;
	private String additional;
	
	public DiscreteNumericalRange(int from, int to, String additional) {
		this.from = from;
		this.to = to;
		this.additional = additional;
	}

	public int getTo() {
		return to;
	}

	public void setTo(int to) {
		this.to = to;
	}

	public int getFrom() {
		return from;
	}

	public void setFrom(int from) {
		this.from = from;
	}

	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}
	
	@Override
	public Type getType() {
		return Type.DISCRETE_NUMERICAL_RANGE;
	}
	
}
